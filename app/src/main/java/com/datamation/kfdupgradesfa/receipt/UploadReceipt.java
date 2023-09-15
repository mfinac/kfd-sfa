package com.datamation.kfdupgradesfa.receipt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.api.TaskTypeUpload;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.UploadTaskListener;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.RecHed;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.model.apimodel.Result;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadReceipt extends AsyncTask<ArrayList<RecHed>, Integer, ArrayList<RecHed>> {

    Context context;
    ProgressDialog dialog;
    private Handler mHandler;
    UploadTaskListener taskListener;
    NetworkFunctions networkFunctions;
    List<String> resultListPreSale;
    int totalRecords;
    TaskTypeUpload taskType;

    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;

    public UploadReceipt(Context context, UploadTaskListener taskListener, TaskTypeUpload taskType) {
        resultListPreSale = new ArrayList<>();
        this.context = context;
        this.taskListener = taskListener;
        mHandler = new Handler(Looper.getMainLooper());
        localSP = context.getSharedPreferences(SETTINGS, 0);
        this.taskType = taskType;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.show();
    }

    @Override
    protected ArrayList<RecHed> doInBackground(ArrayList<RecHed>... params) {

        int recordCount = 0;
        publishProgress(recordCount);

        final ArrayList<RecHed> RCSList = params[0];
        totalRecords = RCSList.size();
        networkFunctions = new NetworkFunctions(context);
        final String sp_url = localSP.getString("URL", "").toString();
        String URL = "http://" + sp_url;

        for (final RecHed c : RCSList) {

            try {
                String content_type = "application/json";
                ApiInterface apiInterface = ApiCllient.getClient(context).create(ApiInterface.class);
                JsonParser jsonParser = new JsonParser();
                String recJson = new Gson().toJson(c);
                JsonObject objectFromString = jsonParser.parse(recJson).getAsJsonObject();
                Log.d(">>>receipt object", ">>>receipt " + objectFromString.toString());
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(objectFromString);


                try{

                    FileWriter writer=new FileWriter(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/"+ "KFDUPD_Receipt_Json.txt");
                    writer.write(jsonArray.toString());
                    writer.close();
                }catch(Exception e){
                    e.printStackTrace();
                }


                Call<Result> resultCall = apiInterface.uploadReceipt(objectFromString, content_type);
                resultCall.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        int status = response.code();

                        if(response.isSuccessful()){
                            response.body(); // have your all data
                            boolean result =response.body().isResponse();
                            Log.d( ">>response"+status,result+">>"+c.getRefNo() );
                            if(result){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        c.setIsSync("1");
                                    addRefNoResults(c.getRefNo() +" --> Success\n",RCSList.size());
                                    new ReceiptController(context).updateIsSyncedReceipt(c.getRefNo(), "0","1","SYNCED");
                                    }
                                });
                            }else{
                                c.setIsSync("0");
                            addRefNoResults(c.getRefNo() +" --> Failed\n",RCSList.size());
                            new ReceiptController(context).updateIsSyncedReceipt(c.getRefNo(), "0","0","NOT SYNCED");
                            }
                        }else {
                            Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            Log.d( ">>error response"+status,response.errorBody().toString()+">>"+c.getRefNo() );
                        }// this will tell you why your api doesnt work most of time

                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(context, "Error response "+t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

//                        Log.d(">>>response code", ">>>res " + status);
//                        Log.d(">>>response message", ">>>res " + response.message());
//
//                        String resmsg = "" + response.body().toString();
//                        if (status == 200 && !resmsg.equals("") && !resmsg.equals(null)) {
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    c.setIsSync("1");
//                                    addRefNoResults(c.getRefNo() +" --> Success\n",RCSList.size());
//                                    new ReceiptController(context).updateIsSyncedReceipt(c.getRefNo(), "0","1","SYNCED");
//                                }
//                            });
//                        } else {
//
//                            c.setIsSync("0");
//                            addRefNoResults(c.getRefNo() +" --> Failed\n",RCSList.size());
//                            new ReceiptController(context).updateIsSyncedReceipt(c.getRefNo(), "0","0","NOT SYNCED");
//                        }

            } catch (Exception e) {
                e.printStackTrace();
            }

            ++recordCount;
            publishProgress(recordCount);
        }

        return RCSList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dialog.setMessage("Uploading.. Receipt Record " + values[0] + "/" + totalRecords);
    }

    @Override
    protected void onPostExecute(ArrayList<RecHed> RCSList) {
        super.onPostExecute(RCSList);
        dialog.dismiss();
        taskListener.onTaskCompleted(taskType,resultListPreSale);
    }

    private void addRefNoResults(String ref, int count) {
        resultListPreSale.add(ref);
        if(count == resultListPreSale.size()) {
            mUploadResult(resultListPreSale);
        }
    }

    public void mUploadResult(List<String> messages) {
        String msg = "";
        for (String s : messages) {
            msg += s;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle("Upload Receipt Summary");

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();


            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

}



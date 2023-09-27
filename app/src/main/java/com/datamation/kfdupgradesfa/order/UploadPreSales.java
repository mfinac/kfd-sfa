package com.datamation.kfdupgradesfa.order;

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
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.UploadTaskListener;
import com.datamation.kfdupgradesfa.model.OrderHed;
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

public class UploadPreSales extends AsyncTask<ArrayList<OrderHed>, Integer, ArrayList<OrderHed>> {

    // Shared Preferences variables
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private Handler mHandler;
    ProgressDialog dialog;
    UploadTaskListener taskListener;
    List<String> resultListPreSale;
    TaskTypeUpload taskType;
    NetworkFunctions networkFunctions;
    int totalRecords;

    public UploadPreSales(Context context, UploadTaskListener taskListener, TaskTypeUpload taskType) {
        resultListPreSale = new ArrayList<>();
        this.context = context;
        mHandler = new Handler(Looper.getMainLooper());
        this.taskListener = taskListener;
        this.taskType = taskType;
        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setTitle("Uploading order records");
        dialog.show();
    }

    @Override
    protected ArrayList<OrderHed> doInBackground(ArrayList<OrderHed>... params) {
        int recordCount = 0;
        publishProgress(recordCount);
        final ArrayList<OrderHed> RCSList = params[0];
        totalRecords = RCSList.size();

        for (final OrderHed c : RCSList)
        {
            try {
                String content_type = "application/json";
                ApiInterface apiInterface = ApiCllient.getClient(context).create(ApiInterface.class);
                JsonParser jsonParser = new JsonParser();
                String orderJson = new Gson().toJson(c);
                JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                Log.d(">>>Orderjson",">>>"+objectFromString);
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(objectFromString);

                try{

                    FileWriter writer=new FileWriter(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/"+ "DBFSFA_OrderJson.txt");
                    writer.write(jsonArray.toString());
                    writer.close();
                }catch(Exception e){
                    e.printStackTrace();
                }

                Call<Result> resultCall = apiInterface.uploadOrder(objectFromString, content_type);
                resultCall.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        int status = response.code();
                        Log.d(">>>response code", ">>>resPre " + status);
                        Log.d(">>>response message", ">>>resPre " + response.message());
                        Log.d(">>>response body", ">>>res body " + response.body());
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
                                        new OrderController(context).updateIsSynced(c.getRefNo(),"0","SYNCED","0");
                                    }
                                });
                            }else{
                                Log.d( ">>response"+status,""+c.getRefNo() );
                                c.setIsSync("0");
                                new OrderController(context).updateIsSynced(c.getRefNo(),"0","NOT SYNCED","1");
                                addRefNoResults(c.getRefNo() +" --> Failed\n",RCSList.size());
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

            } catch (Exception e) {
                e.printStackTrace();
            }
            ++recordCount;
            publishProgress(recordCount);
        }
      //  taskListener.onTaskCompleted(taskType,resultListPreSale);

        return RCSList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dialog.setMessage("Uploading.. PreSale Record " + values[0] + "/" + totalRecords);
    }

    @Override
    protected void onPostExecute(ArrayList<OrderHed> RCSList) {
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
        alertDialogBuilder.setTitle("Upload Order Summary");

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

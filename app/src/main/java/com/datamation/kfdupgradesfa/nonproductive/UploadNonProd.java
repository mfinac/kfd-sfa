package com.datamation.kfdupgradesfa.nonproductive;

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
import com.datamation.kfdupgradesfa.controller.DayNPrdHedController;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.UploadTaskListener;
import com.datamation.kfdupgradesfa.model.NonPrdHed;
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

public class UploadNonProd extends AsyncTask<ArrayList<NonPrdHed>, Integer, ArrayList<NonPrdHed>> {

    // Shared Preferences variables
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    ProgressDialog dialog;
    TaskTypeUpload taskType;
    private Handler mHandler;
    UploadTaskListener taskListener;
    List<String> resultListNonProduct;
    NetworkFunctions networkFunctions;
    int totalRecords;

    public UploadNonProd(Context context, UploadTaskListener taskListener, TaskTypeUpload taskType) {
        resultListNonProduct = new ArrayList<>();
        this.context = context;
        this.taskListener = taskListener;
        this.taskType = taskType;
        mHandler = new Handler(Looper.getMainLooper());
        //localSP = context.getSharedPreferences(SETTINGS, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setTitle("Uploading nonproductive records");
        dialog.show();
    }

    @Override
    protected ArrayList<NonPrdHed> doInBackground(ArrayList<NonPrdHed>... params) {

        int recordCount = 0;
        publishProgress(recordCount);

        final ArrayList<NonPrdHed> RCSList = params[0];
        totalRecords = RCSList.size();


        try
        {
            for (final NonPrdHed c : RCSList)
            {
                try {
                    String content_type = "application/json";
                    ApiInterface apiInterface = ApiCllient.getClient(context).create(ApiInterface.class);
                    JsonParser jsonParser = new JsonParser();
                    String nonPrdHedJson = new Gson().toJson(c);
                    JsonObject objectFromString = jsonParser.parse(nonPrdHedJson).getAsJsonObject();
                    Log.d(">>>nonPrdHedJson",">>>"+objectFromString);
                    JsonArray jsonArray = new JsonArray();
                    jsonArray.add(objectFromString);

                    try{

                        FileWriter writer=new FileWriter(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/"+ "DBFSFA_OrderJson.txt");
                        writer.write(jsonArray.toString());
                        writer.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    Call<Result> resultCall = apiInterface.uploadNonProd(objectFromString, content_type);
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
                                            addRefNoResults_Non(c.getRefNo() +" --> Success\n",RCSList.size());
                                            new DayNPrdHedController(context).updateIsSynced(c.getRefNo(),"1");
                                        }
                                    });
                                }else{
                                    Log.d( ">>response"+status,""+c.getRefNo() );
                                    c.setIsSync("0");
                                    new DayNPrdHedController(context).updateIsSynced(c.getRefNo(),"0");
                                    addRefNoResults_Non(c.getRefNo() +" --> Failed\n",RCSList.size());
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
//
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ++recordCount;
                publishProgress(recordCount);
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return RCSList;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dialog.setMessage("Uploading.. Non Prod Record " + values[0] + "/" + totalRecords);
    }

    @Override
    protected void onPostExecute(ArrayList<NonPrdHed> NonPrdList) {

        super.onPostExecute(NonPrdList);
        dialog.dismiss();
        taskListener.onTaskCompleted(taskType,resultListNonProduct);
    }

    private void addRefNoResults_Non(String ref, int count) {
        resultListNonProduct.add(ref);
        Log.d(">>>msg",">>>"+ref+">>>"+count);
        if(count == resultListNonProduct.size()) {
            mUploadResult(resultListNonProduct);
        }
    }

    public void mUploadResult(List<String> messages) {
        String msg = "";
        for (String s : messages) {
            msg += s;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle("Nonproductive Upload Summary");

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

package com.datamation.kfdupgradesfa.upload;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.RepGPSLocationController;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.UploadTaskListener;
import com.datamation.kfdupgradesfa.model.OrderHed;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.model.RepGPS;
import com.datamation.kfdupgradesfa.model.RepGpsLoc;
import com.datamation.kfdupgradesfa.model.apimodel.Result;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDailyRepLocations extends AsyncTask<ArrayList<RepGpsLoc>, Integer, ArrayList<RepGpsLoc>> {

    Context context;
    ProgressDialog dialog;
    private Handler mHandler;
    UploadTaskListener taskListener;
    NetworkFunctions networkFunctions;
    List<String> resultListPreSale;
    int totalRecords;
    ArrayList<RepGpsLoc> fRepGPSList = new ArrayList<>();

    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;

    public UploadDailyRepLocations(Context context, UploadTaskListener taskListener, ArrayList<RepGpsLoc> recList) {
        resultListPreSale = new ArrayList<>();
        this.context = context;
        this.taskListener = taskListener;
        mHandler = new Handler(Looper.getMainLooper());
        localSP = context.getSharedPreferences(SETTINGS, 0);
        fRepGPSList.addAll(recList);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.show();
    }

    @Override
    protected ArrayList<RepGpsLoc> doInBackground(ArrayList<RepGpsLoc>... params) {

        int recordCount = 0;
        publishProgress(recordCount);

        final  ArrayList<RepGpsLoc> RCSList = fRepGPSList;
        totalRecords = RCSList.size();

        for (final RepGpsLoc c : RCSList) {

            try {
                String content_type = "application/json";
                ApiInterface apiInterface = ApiCllient.getClient(context).create(ApiInterface.class);
                JsonParser jsonParser = new JsonParser();
                String recJson = new Gson().toJson(c);
                JsonObject objectFromString = jsonParser.parse(recJson).getAsJsonObject();
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(objectFromString);
                Log.d(">>>repGps Json", ">>>repGps Json " + objectFromString.toString());

                Call<Result> resultCall = apiInterface.uploadRepLoc(objectFromString, content_type);
                resultCall.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        int status = response.code();
                        Log.d(">>>response code", ">>>res " + status);
                        Log.d(">>>response message", ">>>res " + response.message());

                        String resmsg = "" + response.body().toString();
                        if (status == 200 && !resmsg.equals("") && !resmsg.equals(null)) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    c.setIsSync("1");
                                    new RepGPSLocationController(context).updateIsSyncedRepGPSLoc(c.getGpsdate(),c.getTxnTime(), "1");
                                    addRefNoResults(c.getTxnTime()+ " --> Success\n",RCSList.size());
                                }
                            });
                        } else {

                            c.setIsSync("0");
                            new RepGPSLocationController(context).updateIsSyncedRepGPSLoc(c.getGpsdate(),c.getTxnTime(), "0");
                            addRefNoResults(c.getTxnTime() + " --> Failed\n",RCSList.size());
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(context, "Rep GPS Error response " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
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
        dialog.setMessage("Uploading.. RepGpsLoc Record " + values[0] + "/" + totalRecords);
    }

//    @Override
//    protected void onPostExecute(ArrayList<RepGpsLoc> RCSList) {
//        super.onPostExecute(RCSList);
//        List<String> list = new ArrayList<>();
//
//        if (RCSList.size() > 0) {
//            list.add("\nREP GPS LOCATION");
//            list.add("------------------------------------\n");
//        }
//
//        int i = 1;
//		for (RepGpsLoc c : RCSList) {
//
//			if (c.getIsSync().equals("1")) {
//				list.add(i + ". " + c.getTxnTime()+ " --> Success\n");
//			} else {
//				list.add(i + ". " + c.getTxnTime() + " --> Failed\n");
//			}
//			i++;
//		}
//
//        dialog.dismiss();
//        taskListener.onTaskCompleted(list);
//    }

    @Override
    protected void onPostExecute(ArrayList<RepGpsLoc> RCSList) {
        super.onPostExecute(RCSList);
        dialog.dismiss();
        taskListener.onTaskCompleted(resultListPreSale);

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
        alertDialogBuilder.setTitle("Upload Daily Rep Locations");

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



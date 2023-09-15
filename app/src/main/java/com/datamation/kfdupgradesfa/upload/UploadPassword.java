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
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.model.Debtor;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPassword extends AsyncTask<ArrayList<Debtor>, Integer, ArrayList<Debtor>> {



    // Shared Preferences variables
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private Handler mHandler;
    ProgressDialog dialog;
    List<String> resultListPreSale;
    NetworkFunctions networkFunctions;
    int totalRecords;

    public UploadPassword(Context context) {
        resultListPreSale = new ArrayList<>();
        this.context = context;
        mHandler = new Handler(Looper.getMainLooper());
        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setTitle("Uploading password records");
        dialog.show();
    }

    @Override
    protected ArrayList<Debtor> doInBackground(ArrayList<Debtor>... params) {
        int recordCount = 0;
        publishProgress(recordCount);
        networkFunctions = new NetworkFunctions(context);
        final ArrayList<Debtor> RCSList = params[0];
        totalRecords = RCSList.size();
        for (final Debtor c : RCSList)
        {
            try {
                String content_type = "application/json";
                ApiInterface apiInterface = ApiCllient.getClient(context).create(ApiInterface.class);
                JsonParser jsonParser = new JsonParser();
                String orderJson = new Gson().toJson(c);
                JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(objectFromString);
                Call<String> resultCall = apiInterface.uploadCusNewPassword(jsonArray, content_type);
                resultCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response != null && response.body() != null) {
                            int status = response.code();
                            Log.d(">>>response code", ">>>res " + status);
                            Log.d(">>>response message", ">>>res " + response.message());
                            Log.d(">>>response body", ">>>res " + response.body().toString());
                            int resLength = response.body().toString().trim().length();
                            String resmsg = "" + response.body().toString();
                            if (status == 200 && !resmsg.equals("") && !resmsg.equals(null)) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // resultListNonProduct.add(np.getNONPRDHED_REFNO()+ "--->SUCCESS");
                                        //    addRefNoResults_Non(np.getNONPRDHED_REFNO() + " --> Success\n",RCSList.size());
                                        //  Log.d( ">>response"+status,""+c.getORDER_REFNO() );
                                        c.setFDEBTOR_IS_SYNC("1");
                                        addRefNoResults(c.getFDEBTOR_CODE() + " --> Success\n", RCSList.size());
                                        // new OrderController(context).updateIsSynced(c);
                                        new CustomerController(context).updateIsSynced(c.getFDEBTOR_CODE(), "1");
                                        //  Toast.makeText(context,np.getNONPRDHED_REFNO()+"-Non-productive uploded Successfully" , Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //addRefNoResults(c.getORDER_REFNO() +" --> Success\n",RCSList.size());

                                //  Toast.makeText(context, c.getORDER_REFNO()+" - Order uploded Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(">>response" + status, "" + c.getFDEBTOR_CODE());
                                c.setFDEBTOR_IS_SYNC("0");
                                new CustomerController(context).updateIsSynced(c.getFDEBTOR_CODE(), "0");
                                addRefNoResults(c.getFDEBTOR_CODE() + " --> Failed\n", RCSList.size());
                                //   Toast.makeText(context, c.getORDER_REFNO()+" - Order uplod Failed", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(context, " Invalid response when signup details upload", Toast.LENGTH_SHORT).show();
                        }
                        }

                        @Override
                        public void onFailure (Call < String > call, Throwable t){
                            Toast.makeText(context, "Error response " + t.toString(), Toast.LENGTH_SHORT).show();
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
        dialog.setMessage("Uploading.. PreSale Record " + values[0] + "/" + totalRecords);
    }

    @Override
    protected void onPostExecute(ArrayList<Debtor> RCSList) {

        super.onPostExecute(RCSList);
        dialog.dismiss();

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
        alertDialogBuilder.setTitle("Upload Password Summary");

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

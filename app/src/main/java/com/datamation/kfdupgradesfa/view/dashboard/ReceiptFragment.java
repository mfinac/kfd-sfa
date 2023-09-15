package com.datamation.kfdupgradesfa.view.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.ExpandableReceiptListAdapter;
import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReceiptDetController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.dialog.PreSalePrintPreviewAlertBox;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.model.RecHed;
import com.datamation.kfdupgradesfa.model.ReceiptDet;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.model.apimodel.Result;
import com.datamation.kfdupgradesfa.utils.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.internal.zzagz.runOnUiThread;

public class ReceiptFragment extends Fragment {

    View view;
    ExpandableListView expListView;
    private NumberFormat numberFormat = NumberFormat.getInstance();


    ExpandableReceiptListAdapter listRecAdapter;
    List<ReceiptHed> receiptHedList;
    HashMap<ReceiptHed, List<ReceiptDet>> listReceiptDataChild;

    NetworkFunctions networkFunctions;
    String Order_Status;
    SearchView search;
    ImageView refresh;
    private Handler mHandler;

//    private PrimeThread p;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.responsive_receipt, container, false);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        refresh = (ImageView) view.findViewById(R.id.img_refresh);
        search = (SearchView) view.findViewById(R.id.search);
        networkFunctions = new NetworkFunctions(getActivity());
        mHandler = new Handler(Looper.getMainLooper());

        prepareReceiptData();


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    searchListData(query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    searchListData(newText);
                }

                return true;
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                p = new PrimeThread(new ReceiptController(getActivity()).getAllNotIssuedReceipts());
//                p.start();

                new ReceiptStatusRefreshResponse(new ReceiptController(getActivity()).getAllNotIssuedReceipts()).execute();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(ReceiptFragment.this).attach(ReceiptFragment.this).commit();
            }
        });


        return view;

    }


    private void actionTakenList(String stats, final RecHed rc) {

        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            if (stats.equals("NOT SYNCED")) {
                try {
                    if (new ReceiptController(getActivity()).getAllNotIssuedReceipts().size() > 0) {
                        try {

                            JsonParser jsonParser = new JsonParser();
                            String orderJson = new Gson().toJson(rc);
                            JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                            JsonArray jsonArray = new JsonArray();
                            jsonArray.add(objectFromString);
                            String content_type = "application/json";
                            ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);

                            Call<Result> resultCall = apiInterface.uploadReceipt(objectFromString, content_type);
                            resultCall.enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(Call<Result> call, Response<Result> response) {

                                    int status = response.code();

                                    if(response.isSuccessful()){
                                        response.body(); // have your all data
                                        boolean result =response.body().isResponse();
                                        Log.d( ">>response"+status,result+">>"+rc.getRefNo() );
                                        if(result){
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    new ReceiptController(getActivity()).updateIsSyncedReceipt(rc.getRefNo(), "0","1", "SYNCED");
                                                    listRecAdapter.notifyDataSetChanged();
                                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                    ft.detach(ReceiptFragment.this).attach(ReceiptFragment.this).commit();
                                                }
                                            });
                                        }else{
                                            new ReceiptController(getActivity()).updateIsSyncedReceipt(rc.getRefNo(), "0","0", "NOT SYNCED");
                                            listRecAdapter.notifyDataSetChanged();
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.detach(ReceiptFragment.this).attach(ReceiptFragment.this).commit();
                                        }
                                    }else {
                                        Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                        Log.d( ">>error response"+status,response.errorBody().toString()+">>"+rc.getRefNo() );
                                    }// this will tell you why your api doesnt work most of time


//                                    if (response != null && response.body() != null) {
//                                        int status = response.code();
//                                        Log.d(">>>response code", ">>>res " + status);
//                                        Log.d(">>>response message", ">>>res " + response.message());
//                                        Log.d(">>>response body", ">>>res " + response.body());
//                                        String resmsg = "" + response.body();
//                                        if (status == 200 && !resmsg.equals("") && !resmsg.equals(null)) {
////
//                                            new ReceiptController(getActivity()).updateIsSyncedReceipt(rc.getRefNo(), "0","1", "SYNCED");
//                                            listRecAdapter.notifyDataSetChanged();
//                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                            ft.detach(ReceiptFragment.this).attach(ReceiptFragment.this).commit();
//
//                                        } else {
//                                            new ReceiptController(getActivity()).updateIsSyncedReceipt(rc.getRefNo(), "0","0", "NOT SYNCED");
//                                            listRecAdapter.notifyDataSetChanged();
//                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                            ft.detach(ReceiptFragment.this).attach(ReceiptFragment.this).commit();
//                                        }
//                                    } else {
//                                        Log.d(">>>response ", ">>>res " + response);
//
//                                        Toast.makeText(getActivity(), " Invalid response when receipt upload", Toast.LENGTH_LONG).show();
//                                    }

                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {
                                    Log.d(">>>response ", ">>>res " + t);

                                    Toast.makeText(getActivity(), "Error response " + t.toString(), Toast.LENGTH_LONG).show();

                                }

                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        }

                    } else {
                        receiptActionUpdateTask(rc.getRefNo(), "NOT SYNCED");
                        Toast.makeText(getActivity(), "No Records to upload !", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e(">>>ERROR In EDIT", ">>>" + e.toString());
                    throw e;
                }
            } else if (stats.equals("SYNCED")) {
//                new ReceiptStatusDownload(rc.getFPRECHED_REFNO(), "SYNCED").execute();
                receiptActionUpdateTask(rc.getRefNo(), "SYNCED");

            }else if (stats.equals("RECEIVED")) {

//                new ReceiptStatusDownload(rc.getFPRECHED_REFNO(), "RECEIVED").execute();
                 receiptActionUpdateTask(rc.getRefNo(), "RECEIVED");

            }
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
        }

    }

    private void receiptActionUpdateTask(String reciptNo, String status){
        ReceiptController receiptController = new ReceiptController(getActivity());

        String recStatus = "";
        try {
            recStatus = networkFunctions.getReceiptStatus(reciptNo, status);
            Log.d(">>status",">>status"+recStatus);

        } catch (IOException e) {
            e.printStackTrace();
            try {
                throw e;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        // Processing Receipt Status
        try {
            JSONObject ReceiptStatusJSON = new JSONObject(recStatus);
            JSONArray ReceiptStatusJSONArray = ReceiptStatusJSON.getJSONArray("data");
            ArrayList<ReceiptHed> ReceiptStatusList = new ArrayList<ReceiptHed>();


            for (int i = 0; i < ReceiptStatusJSONArray.length(); i++) {
                Log.d("*******^^^ loop", "doInBackground: " + ReceiptStatusJSONArray);
                ReceiptStatusList.add(ReceiptHed.parseReceiptStatus(ReceiptStatusJSONArray.getJSONObject(i)));
            }
            receiptController.CreateOrUpdateReceiptStatus(ReceiptStatusList);
        } catch (JSONException | NumberFormatException e) {
            try {
                throw e;
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
    }

    // receipt status download - manoj - ---------------- 17/05/2022 -----------------------------------
    private class ReceiptStatusDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String reciptNo;
        private String status;

        public ReceiptStatusDownload(String recNo, String status) {
            this.reciptNo = recNo;
            this.status = status;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Receipt Status...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {

                    ReceiptController receiptController = new ReceiptController(getActivity());

                    /*****************Receipt Status *****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Receipt Status...");
                        }
                    });

                    String recStatus = "";
                    try {
                        recStatus = networkFunctions.getReceiptStatus(reciptNo, status);

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing Receipt Status
                    try {
                        JSONObject ReceiptStatusJSON = new JSONObject(recStatus);
                        JSONArray ReceiptStatusJSONArray = ReceiptStatusJSON.getJSONArray("data");
                        ArrayList<ReceiptHed> ReceiptStatusList = new ArrayList<ReceiptHed>();


                        for (int i = 0; i < ReceiptStatusJSONArray.length(); i++) {
                            Log.d("*******^^^ loop", "doInBackground: " + ReceiptStatusJSONArray);
                            ReceiptStatusList.add(ReceiptHed.parseReceiptStatus(ReceiptStatusJSONArray.getJSONObject(i)));
                        }
                        receiptController.CreateOrUpdateReceiptStatus(ReceiptStatusList);
                    } catch (JSONException | NumberFormatException e) {
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }


        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);
            getFragmentManager().beginTransaction().detach(ReceiptFragment.this).attach(ReceiptFragment.this).commit();
            pdialog.setMessage("Finalizing Receipt Status Data");
            pdialog.setMessage("Download Completed..");
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }
        }
    }






    public void prepareReceiptData() {

        receiptHedList = new ReceiptController(getActivity()).getTodayReceipts();

        if (receiptHedList.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();

        } else {
            listReceiptDataChild = new HashMap<>();

            for (ReceiptHed rHed : receiptHedList) {
                listReceiptDataChild.put(rHed, new ReceiptDetController(getActivity()).getTodayreceiptsDets(rHed.getFPRECHED_REFNO(), "", ""));
            }

            listRecAdapter = new ExpandableReceiptListAdapter(getActivity(), receiptHedList, listReceiptDataChild);
            expListView.setAdapter(listRecAdapter);
            Toast.makeText(getActivity(), "Receipt list refreshed", Toast.LENGTH_LONG).show();

        }
    }

    public void searchListData(String key) {

        receiptHedList = new ReceiptController(getActivity()).getTodayReceiptsBySearch(key);

        if (receiptHedList.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();

        } else {
            listReceiptDataChild = new HashMap<ReceiptHed, List<ReceiptDet>>();

            for (ReceiptHed rHed : receiptHedList) {
                listReceiptDataChild.put(rHed, new ReceiptDetController(getActivity()).getTodayreceiptsDets(rHed.getFPRECHED_REFNO(), "", ""));
            }

            listRecAdapter = new ExpandableReceiptListAdapter(getActivity(), receiptHedList, listReceiptDataChild);
            expListView.setAdapter(listRecAdapter);
        }
    }

    // order status download - MMS - ---------------- 07/06/2022 -----------------------------------
    private class ReceiptStatusRefreshResponse extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        ArrayList<RecHed> receiptList;

        public ReceiptStatusRefreshResponse(ArrayList<RecHed> recList) {
            this.receiptList = recList;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Please while for a status update...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                for (RecHed rec : receiptList) {
                    actionTakenList(rec.getStatus(), rec);

                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }


        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);
            getParentFragmentManager().beginTransaction().detach(ReceiptFragment.this).attach(ReceiptFragment.this).commit();
            pdialog.setMessage("Finalizing Receipt Status Data");
            pdialog.setMessage("Download Completed..");
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }
            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Receipt list refreshed success!.")
                    .setContentText("")
                    .setConfirmText("OK")
                    .showCancelButton(false)

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();

                            prepareReceiptData();
                        }
                    })

                    .show();


        }
    }

}

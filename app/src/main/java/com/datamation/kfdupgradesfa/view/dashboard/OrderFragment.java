package com.datamation.kfdupgradesfa.view.dashboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.downloadListAdapter;
import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.DownloadController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.dialog.PreSalePrintPreviewAlertBox;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.OrderResponseListener;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Control;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.model.OrderHed;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.model.apimodel.Result;
import com.datamation.kfdupgradesfa.utils.NetworkUtil;
import com.datamation.kfdupgradesfa.view.OrderActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.rgb;
import static com.google.android.gms.internal.zzagz.runOnUiThread;

public class OrderFragment extends Fragment {

    View view;
    ExpandableListView expListView;
    private NumberFormat numberFormat = NumberFormat.getInstance();
    ExpandableListAdapter listVanAdapter;
    List<Order> listDataHeader;
    NetworkFunctions networkFunctions;
    HashMap<Order, List<OrderDetail>> listDataChild;
    String Order_Status;
    SearchView search;
    ImageView refresh;
    private Handler mHandler;
//    private PrimeThread p;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.responsive_order, container, false);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        refresh = (ImageView) view.findViewById(R.id.img_refresh);
        search = (SearchView) view.findViewById(R.id.search);
        networkFunctions = new NetworkFunctions(getActivity());
        mHandler = new Handler(Looper.getMainLooper());
        prepareListData();


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

//                p = new PrimeThread(new OrderController(getActivity()).getAllNotIssueOrders());
//                p.start();

                new OrderStatusRefreshResponse(new OrderController(getActivity()).getAllNotIssueOrders()).execute();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
                //prepareListData();
            }
        });


        return view;

    }

    private void showData() {


        listDataHeader = new OrderController(getActivity()).getAllOrders();

        if (listDataHeader.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();

        } else {
            listDataChild = new HashMap<Order, List<OrderDetail>>();

            for (Order free : listDataHeader) {
                listDataChild.put(free, new OrderDetailController(getActivity()).getAllOrderDets(free.getFORDHED_REFNO()));
            }

            listVanAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listVanAdapter);
            Toast.makeText(getActivity(), "List Refreshed", Toast.LENGTH_LONG).show();

        }

    }

//
//    class PrimeThread extends Thread {
//
//        CustomProgressDialog pdialog;
//        ArrayList<Order> orderList;
//
//        PrimeThread(ArrayList<Order> ordList) {
//            this.pdialog = new CustomProgressDialog(getActivity());
//            this.orderList = ordList;
//
//            pdialog = new CustomProgressDialog(getActivity());
//            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            pdialog.setMessage("Please while for a status update...");
//            pdialog.show();
//        }
//
//        public void run() {
//
//            Looper.prepare();
//
//            for (Order odr : orderList) {
//                actionTakenList(odr.getFORDHED_STATUS(), odr);
//                try {
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            showData();
//                        }
//                    });
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (pdialog.isShowing()) {
//                pdialog.dismiss();
//
//                p.stop();
//            }
//        }
//    }
//

    private void actionTakenList(String stats, final OrderHed c) {

     //   if (NetworkUtil.isNetworkAvailable(getActivity())) {
            if (stats.equals("NOT SYNCED")) {
                try {
                    if (new OrderController(getActivity()).getAllNotIssueOrders().size() > 0) {
                        try {

                            JsonParser jsonParser = new JsonParser();
                            String orderJson = new Gson().toJson(c);
                            JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                            JsonArray jsonArray = new JsonArray();
                            jsonArray.add(objectFromString);
                            Log.d(">>>Orderjson",">>>"+objectFromString);
                            String content_type = "application/json";
                            ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);

                            Call<Result> resultCall = apiInterface.uploadOrder(objectFromString, content_type);
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
                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1", "SYNCED","0");

                                            listVanAdapter.notifyDataSetChanged();

                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
                                                }
                                            });
                                        }else{
                                            Log.d(">>>Orderjson",">>>"+objectFromString);
                                            c.setIsSync("0");
                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0", "NOT SYNCED","1");
                                            listVanAdapter.notifyDataSetChanged();
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
                                        }
                                    }else {
                                        Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
                                    }// this will tell you why your api doesnt work most of time


                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {
                                    Toast.makeText(getActivity(), "Error response "+t.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        }

                    } else {
                        orderActionUpdateTask(c.getRefNo(), "NOT SYNCED");
                        Toast.makeText(getActivity(), "No Records to upload !", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e(">>>ERROR In EDIT", ">>>" + e.toString());
                    throw e;
                }
            } else if (stats.equals("SYNCED")) {

//                new OrderStatusDownload(c.getFORDHED_REFNO(), "SYNCED").execute();
                 orderActionUpdateTask(c.getRefNo(), "SYNCED");
            } else if (stats.equals("DISPATCHED")) {
//                new OrderStatusDownload(c.getFORDHED_REFNO(), "DISPATCHED").execute();
                 orderActionUpdateTask(c.getRefNo(), "DISPATCHED");
            } else if (stats.equals("INVOICED")) {
//                new OrderStatusDownload(c.getFORDHED_REFNO(), "INVOICED").execute();
                orderActionUpdateTask(c.getRefNo(), "INVOICED");
            }
//        } else {
//            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
//        }

    }


    private void orderActionUpdateTask(String orderNo, String status){
        OrderController orderController = new OrderController(getActivity());


        String orderStatus = "";
        try {
            orderStatus = networkFunctions.getOrderStatus(orderNo, status);

        } catch (IOException e) {
            e.printStackTrace();
            try {
                throw e;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        // Processing Order Status
        try {
            JSONObject OrderStatusJSON = new JSONObject(orderStatus);
            JSONArray OrderStatusJSONArray = OrderStatusJSON.getJSONArray("data");
            ArrayList<Order> OrderStatusList = new ArrayList<Order>();


            for (int i = 0; i < OrderStatusJSONArray.length(); i++) {
                Log.d("*******^^^ loop", "doInBackground: " + OrderStatusJSONArray);
                OrderStatusList.add(Order.parseOrderStatus(OrderStatusJSONArray.getJSONObject(i)));
            }
            orderController.CreateOrUpdateOrderStatus(OrderStatusList);
        } catch (JSONException | NumberFormatException e) {
            try {
                throw e;
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<Order> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<Order, List<OrderDetail>> _listDataChild;
        private FragmentManager fragmentManager = null;

        public ExpandableListAdapter(Context context, List<Order> listDataHeader,
                                     HashMap<Order, List<OrderDetail>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View grpview, ViewGroup parent) {

            final OrderDetail childText = (OrderDetail) getChild(groupPosition, childPosition);

            if (grpview == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                grpview = infalInflater.inflate(R.layout.list_items, null);
            }

            TextView txtListChild = (TextView) grpview.findViewById(R.id.itemcode);
            TextView txtListChild1 = (TextView) grpview.findViewById(R.id.qty);
            TextView txtListChild2 = (TextView) grpview.findViewById(R.id.amount);

            txtListChild.setText(" " + childText.getFORDDET_ITEMNAME());
            txtListChild1.setText("Qty - " + childText.getFORDDET_QTY());
            txtListChild2.setText("Amount - " + numberFormat.format(Double.parseDouble(childText.getFORDDET_AMT())));
            return grpview;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            final Order headerTitle = (Order) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.refno);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView deb = (TextView) convertView.findViewById(R.id.debcode);
            TextView tot = (TextView) convertView.findViewById(R.id.total);
            final TextView stats = (TextView) convertView.findViewById(R.id.status);
            TextView cnfrmstats = (TextView) convertView.findViewById(R.id.cnfrmstatus);
//            ImageView type = (ImageView) convertView.findViewById(R.id.type);
            TextView type = (TextView) convertView.findViewById(R.id.type);
            type.setTextColor(Color.WHITE);
            type.setBackground(getResources().getDrawable(R.drawable.bg_rejected));

            ImageView orderDet = (ImageView) convertView.findViewById(R.id.showOrder);
            ImageView print = (ImageView) convertView.findViewById(R.id.print);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getFORDHED_REFNO());
            deb.setText(new CustomerController(_context).getCusNameByCode(headerTitle.getFORDHED_DEB_CODE()));


            if (headerTitle.getFORDHED_STATUS() != null && headerTitle.getFORDHED_STATUS().equals("DISPATCHED")) {
                stats.setText("DISPATCHED");
                stats.setTextColor(Color.WHITE);
                cnfrmstats.setText("Click to refersh");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_dispatch));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
            } else if (headerTitle.getFORDHED_STATUS() != null && headerTitle.getFORDHED_STATUS().equals("ISSUED")) {
                stats.setText("ISSUED");
                cnfrmstats.setText("               ");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_issued));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
            } else if (headerTitle.getFORDHED_STATUS() != null && headerTitle.getFORDHED_STATUS().equals("INVOICED")) {
                stats.setText("INVOICED");
                cnfrmstats.setVisibility(View.GONE);
                stats.setTextColor(Color.WHITE);
                stats.setBackground(getResources().getDrawable(R.drawable.bg_synced));//green
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
            }
//            else if (headerTitle.getFORDHED_STATUS() != null && headerTitle.getFORDHED_STATUS().equals("APPROVED")) {
//                stats.setText("APPROVED");
//                cnfrmstats.setText("Click to refersh");
//                stats.setBackground(getResources().getDrawable(R.drawable.bg_approval));
//                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
//            }
//            else if (headerTitle.getFORDHED_IS_SYNCED().equals("1") && headerTitle.getFORDHED_IS_ACTIVE().equals("2")) {
//                listVanAdapter.notifyDataSetChanged();
//                stats.setText("PENDING");
//                cnfrmstats.setText("Click to refersh");
//                stats.setBackground(getResources().getDrawable(R.drawable.bg_sync));
//                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
//            }
            else if (headerTitle.getFORDHED_IS_SYNCED().equals("1") && headerTitle.getFORDHED_IS_ACTIVE().equals("0")) {
                stats.setText("NOT SYNCED");
                cnfrmstats.setText("Click to confirm");
               // stats.setBackground(getResources().getDrawable(R.drawable.bg_positive));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_positive));
            } else if (headerTitle.getFORDHED_IS_SYNCED().equals("0") && headerTitle.getFORDHED_IS_ACTIVE().equals("0")&& headerTitle.getFORDHED_STATUS().equals("SYNCED")) {
                stats.setText("SYNCED");
                cnfrmstats.setText("Click to confirm");
                new OrderController(getActivity()).updateIsSynced(headerTitle.getFORDHED_REFNO(), "0", "SYNCED","0");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_positive));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_positive));
            }
            else {
                stats.setText("NOT SYNCED");
                cnfrmstats.setText("Click to Confirm");
                stats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
                cnfrmstats.setBackground(getResources().getDrawable(R.drawable.bg_negative));
            }

            date.setText(headerTitle.getFORDHED_TXN_DATE());
            tot.setText(headerTitle.getFORDHED_TOTAL_AMT());

            listVanAdapter.notifyDataSetChanged();


//            orderDet.setVisibility(View.VISIBLE);
            orderDet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new PreSalePrintPreviewAlertBox(getActivity(), headerTitle.getFORDHED_REFNO());

                }
            });

            type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (headerTitle.getFORDHED_IS_SYNCED().equals("0")) {
                        int result = new OrderController(getActivity()).restData("" + headerTitle.getFORDHED_REFNO());
                        if (result > 0) {
                            new OrderDetailController(getActivity()).restData("" + headerTitle.getFORDHED_REFNO());
                            //       new SharedPref(getActivity()).setOrderId(0);
                            expListView.setAdapter((BaseExpandableListAdapter) null);
                            prepareListData();
                            Toast.makeText(getActivity(), "Order deleted successfully", Toast.LENGTH_LONG).show();
                            //       new SharedPref(getActivity()).setEditOrderId(0);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Cannot delete synced orders", Toast.LENGTH_LONG).show();
                    }
                }
            });


            stats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new OrderController(getActivity()).updateIsActive("" + headerTitle.getFORDHED_REFNO(), "2");
//                    ArrayList<Order> orders = new OrderController(getActivity()).getAllUnSyncOrdHed();
                    ArrayList<OrderHed> orders = new OrderController(getActivity()).getAllUnSyncOrdHedByRefNo(headerTitle.getFORDHED_REFNO());
                    // Log.d(">>>2", ">>>2 ");
               //     if (NetworkUtil.isNetworkAvailable(getActivity())) {
                        if (stats.getText().toString().equals("NOT SYNCED")) {
                            try {
                                if (new OrderController(getActivity()).getAllUnSyncOrdHedNew().size() > 0) {
                                    //   Log.d(">>>3", ">>>3 ");
                                    for (final OrderHed c : orders) {
                                        try {
                                            //    Log.d(">>>4", ">>>4 ");
                                            //  final Handler mHandler = new Handler(Looper.getMainLooper());
                                            JsonParser jsonParser = new JsonParser();
                                            String orderJson = new Gson().toJson(c);
                                            JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                                            JsonArray jsonArray = new JsonArray();
                                            jsonArray.add(objectFromString);
                                            String content_type = "application/json";
                                            ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
                                            Call<Result> resultCall = apiInterface.uploadOrder(objectFromString, content_type);
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
                                                                    new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1", "SYNCED","0");
                                                                    prepareListData();
                                                                    listVanAdapter.notifyDataSetChanged();

                                                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                    ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
                                                                }
                                                            });
                                                        }else{
//                                                            c.setIsSync("0");
                                                           new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0", "NOT SYNCED","1");
                                                            requestReupload();
                                                            listVanAdapter.notifyDataSetChanged();
//                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
//                                                           listVanAdapter.notifyDataSetChanged();

                                                        }
                                                    }else {
                                                        //Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
                                                        new isCheckUploadedOrder(headerTitle.getFORDHED_REFNO(), "NOT SYNCED").execute();
                                                        Log.d( ">>error response"+status,response.errorBody().toString()+">>"+c.getRefNo() );
                                                    }// this will tell you why your api doesnt work most of time



//                                                    if (response != null && response.body() != null) {
//                                                        int status = response.code();
//                                                        Log.d(">>>response code", ">>>res " + status);
//                                                        Log.d(">>>response message", ">>>res " + response.message());
//                                                        Log.d(">>>response body", ">>>res " + response.body().toString());
//                                                        int resLength = response.body().toString().trim().length();
//                                                        String resmsg = "" + response.body().toString();
//
//                                                        if (status == 200 && !resmsg.equals("") && !resmsg.equals(null)) {
////                                                        mHandler.post(new Runnable() {
////                                                            @Override
////                                                            public void run() {
//                                                            c.setIsSync("1");
//                                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1", "SYNCED");
//                                                            prepareListData();
//                                                            listVanAdapter.notifyDataSetChanged();
//
//                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
//
//                                                            //       }
//                                                            //     });
//                                                        } else {
//                                                            c.setIsSync("0");
//                                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0", "NOT SYNCED");
//                                                            listVanAdapter.notifyDataSetChanged();
//                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
////                                                           listVanAdapter.notifyDataSetChanged();
//                                                            requestReupload();
//                                                        }
//                                                    } else {
//                                                        Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
//                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<Result> call, Throwable t) {
                                                    Toast.makeText(getActivity(), "Error response " + t.toString(), Toast.LENGTH_LONG).show();

                                                }

                                            });

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } else {
                                    Toast.makeText(getActivity(), "No Records to upload !", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e(">>>ERROR In EDIT", ">>>" + e.toString());
                                throw e;
                            }
                        } else if (stats.getText().toString().equals("SYNCED")) {
                            Log.d("********^^^^^^", "onClick: " + stats.getText());
                            new OrderStatusDownload(headerTitle.getFORDHED_REFNO(), "SYNCED").execute();
                        }
//                        else if (stats.getText().toString().equals("APPROVED")) {
//                            new OrderStatusDownload(headerTitle.getOrderId(), "APPROVED").execute();
//                        }
                        else if (stats.getText().toString().equals("DISPATCHED")) {
                            new OrderStatusDownload(headerTitle.getFORDHED_REFNO(), "DISPATCHED").execute();
                        } else if (stats.getText().toString().equals("INVOICED")) {
                            new OrderStatusDownload(headerTitle.getFORDHED_REFNO(), "INVOICED").execute();
                        }
//                    } else {
//                        Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
//                    }
                    prepareListData();
//                    listVanAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Confirmed order successfully", Toast.LENGTH_LONG).show();

                }
            });
            listVanAdapter.notifyDataSetChanged();
            return convertView;
        }

        public void requestReupload() {
            MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                    .content("Order NOT SEND. Do you want to RESEND the order?")
                    .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                    .positiveText("Yes")
                    .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                    .negativeText("No, Exit")
                    .callback(new MaterialDialog.ButtonCallback() {

                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            ArrayList<OrderHed> orders = new OrderController(getActivity()).getAllUnSyncOrdHedNew();
                            try {
                                if (new OrderController(getActivity()).getAllUnSyncOrdHedNew().size() > 0) {
                                    //   Log.d(">>>3", ">>>3 ");
                                    for (final OrderHed c : orders) {
                                        try {
                                            //    Log.d(">>>4", ">>>4 ");
                                            //  final Handler mHandler = new Handler(Looper.getMainLooper());
                                            JsonParser jsonParser = new JsonParser();
                                            String orderJson = new Gson().toJson(c);
                                            JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                                            JsonArray jsonArray = new JsonArray();
                                            jsonArray.add(objectFromString);
                                            String content_type = "application/json";

                                            ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
                                            Call<Result> resultCall = apiInterface.uploadOrder(objectFromString, content_type);
                                            resultCall.enqueue(new Callback<Result>() {
                                                @Override
                                                public void onResponse(Call<Result> call, Response<Result> response) {
                                                    if (response != null && response.body() != null) {

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
                                                                        new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1", "SYNCED","0");
                                                                        prepareListData();
                                                                        listVanAdapter.notifyDataSetChanged();

                                                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                        ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
                                                                    }
                                                                });
                                                            }else{
//                                                            c.setIsSync("0");
                                                                new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0", "NOT SYNCED","1");
                                                                writeJsonToFile(c.getRefNo(),objectFromString.toString());
                                                                requestReupload();
                                                                listVanAdapter.notifyDataSetChanged();


                                                            }
                                                        }else {
                                                            Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
                                                            Log.d( ">>error response"+status,response.errorBody().toString()+">>"+c.getRefNo() );
                                                        }// this will tell you why your api doesnt work most of time




//                                                        if (status == 200 && !resmsg.equals("") && !resmsg.equals(null) && resmsg.substring(0, 3).equals("202")) {
////                                                        mHandler.post(new Runnable() {
////                                                            @Override
////                                                            public void run() {
//                                                            c.setIsSync("1");
//                                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1", "SYNCED","0");
//
//                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
////                                                            listVanAdapter.notifyDataSetChanged();
//                                                            //       }
//                                                            //     });
//                                                        } else {
//                                                            c.setIsSync("0");
//                                                            new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0", "NOT SYNCED","1");
//                                                            writeJsonToFile(c.getRefNo(),objectFromString.toString());
//                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                                            ft.detach(OrderFragment.this).attach(OrderFragment.this).commit();
////                                                           listVanAdapter.notifyDataSetChanged();
////                                                            requestReupload();
//                                                        }
                                                    } else {
                                                        Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<Result> call, Throwable t) {
                                                    Toast.makeText(getActivity(), "Error response " + t.toString(), Toast.LENGTH_LONG).show();

                                                }

                                            });

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } else {
                                    Toast.makeText(getActivity(), "No Records to upload !", android.widget.Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e(">>>ERROR In EDIT", ">>>" + e.toString());
                                throw e;
                            }

                            // Toast.makeText(getActivity(), "Order discarded successfully..!", Toast.LENGTH_SHORT).show();

//                            Intent intnt = new Intent(getActivity(), DebtorDetailsActivity.class);
//                            intnt.putExtra("outlet", outlet);
//                            startActivity(intnt);
//                            getActivity().finish();


                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);

                            dialog.dismiss();


                        }
                    })
                    .build();
            materialDialog.setCanceledOnTouchOutside(false);
            materialDialog.show();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            double grossTotal = 0;


            List<Order> searchingDetails = _listDataHeader;


            for (Order invoice : searchingDetails) {

                if (invoice != null) {
                    try {
                        grossTotal += Double.parseDouble(invoice.getFORDHED_TOTAL_AMT());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                }
            }

            // total.setText(numberFormat.format(grossTotal));
        }
    }

    private void fragmentChange() {
        Fragment currentFragment = getChildFragmentManager().findFragmentByTag("TAG_ORDER");
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }
    private void writeJsonToFile(String fileName,String content) {
        File path = getActivity().getApplicationContext().getFilesDir();
        try{
            FileOutputStream writer = new FileOutputStream(new File(path,fileName));
            writer.write(content.getBytes());
            writer.close();
            Toast.makeText(getActivity(),"wrote to file : "+fileName,Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //    modified by MMS - 2022/02/17 #$#$#$#$#$//
    public void prepareListData() {
        showData();
        listDataHeader = new OrderController(getActivity()).getAllOrders();

        if (listDataHeader.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();

        } else {
            listDataChild = new HashMap<Order, List<OrderDetail>>();

            for (Order free : listDataHeader) {
                listDataChild.put(free, new OrderDetailController(getActivity()).getAllOrderDets(free.getFORDHED_REFNO()));
            }

            listVanAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listVanAdapter);
        }
    }


    public void searchListData(String key) {

        listDataHeader = new OrderController(getActivity()).getAllOrdersBySearch(key);

        if (listDataHeader.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();

        } else {
            listDataChild = new HashMap<Order, List<OrderDetail>>();

            for (Order free : listDataHeader) {
                listDataChild.put(free, new OrderDetailController(getActivity()).getAllOrderDets(free.getFORDHED_REFNO()));
            }

            listVanAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
            expListView.setAdapter(listVanAdapter);
        }
    }

    private boolean IsConfirmed(Order order) {

        boolean orderConfirmed = false;


        if (order.getOrderId() != 0) {

            orderConfirmed = new OrderController(getActivity()).isAnyConfirmOrderHed(order.getOrderId() + "");

        }

        return orderConfirmed;
    }

    private boolean IsSync(Order order) {

        boolean orderSync = false;


        if (order.getOrderId() != 0) {

            orderSync = new OrderController(getActivity()).isAnySyncOrderHed(order.getOrderId() + "");

        }

        return orderSync;
    }


    // order status download - kaveesha - ---------------- 24/11/2021 -----------------------------------
    private class OrderStatusDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String orderNo;
        private String status;

        public OrderStatusDownload(String orderNo, String status) {
            this.orderNo = orderNo;
            this.status = status;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Order Status...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {

                    OrderController orderController = new OrderController(getActivity());

                    /*****************Order Status *****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Order Status...");
                        }
                    });

                    String orderStatus = "";
                    try {
                        orderStatus = networkFunctions.getOrderStatus(orderNo, status);

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing Order Status
                    try {
                        JSONObject OrderStatusJSON = new JSONObject(orderStatus);
                        JSONArray OrderStatusJSONArray = OrderStatusJSON.getJSONArray("data");
                        ArrayList<Order> OrderStatusList = new ArrayList<Order>();


                        for (int i = 0; i < OrderStatusJSONArray.length(); i++) {
                            Log.d("*******^^^ loop", "doInBackground: " + OrderStatusJSONArray);
                            OrderStatusList.add(Order.parseOrderStatus(OrderStatusJSONArray.getJSONObject(i)));
                        }
                        orderController.CreateOrUpdateOrderStatus(OrderStatusList);
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
            getParentFragmentManager().beginTransaction().detach(OrderFragment.this).attach(OrderFragment.this).commit();
            pdialog.setMessage("Finalizing Order Status Data");
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

    private class isCheckUploadedOrder extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String orderNo;
        private String status;
        private boolean isHave;

        public isCheckUploadedOrder(String orderNo, String status) {
            this.orderNo = orderNo;
            this.status = status;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Order Status...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {

                    OrderController orderController = new OrderController(getActivity());

                    /*****************Order Status *****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Order Status...");
                        }
                    });

                    String orderStatus = "";
                    try {
                        orderStatus = networkFunctions.getOrderStatus(orderNo, status);

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing Order Status
                    try {
                        JSONObject OrderStatusJSON = new JSONObject(orderStatus);
                        JSONArray OrderStatusJSONArray = OrderStatusJSON.getJSONArray("data");
                        ArrayList<Order> OrderStatusList = new ArrayList<Order>();


                        for (int i = 0; i < OrderStatusJSONArray.length(); i++) {
                            Log.d("*******^^^ loop", "doInBackground: " + OrderStatusJSONArray);
                            OrderStatusList.add(Order.parseOrderStatus(OrderStatusJSONArray.getJSONObject(i)));


                            if(Order.parseOrderStatus(OrderStatusJSONArray.getJSONObject(i)).getFORDHED_STATUS().equals("SYNCED")){
                                isHave = true;
                            }else if(Order.parseOrderStatus(OrderStatusJSONArray.getJSONObject(i)).getFORDHED_STATUS().equals("NOT SYNCED")){
                                isHave = false;
                            }
                        }
                        orderController.CreateOrUpdateOrderStatus(OrderStatusList);
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
            getParentFragmentManager().beginTransaction().detach(OrderFragment.this).attach(OrderFragment.this).commit();
            pdialog.setMessage("Finalizing Order Status Data");
            pdialog.setMessage("Download Completed..");
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

                if(isHave){
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Order list refreshed success!.")
                            .setContentText("")
                            .setConfirmText("OK")
                            .showCancelButton(false)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();

                                    prepareListData();
                                }
                            })

                            .show();
                }else{
                    Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }
        }
    }


    // order status download - MMS - ---------------- 07/06/2022 -----------------------------------
    private class OrderStatusRefreshResponse extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        ArrayList<OrderHed> orderList;

        public OrderStatusRefreshResponse(ArrayList<OrderHed> ordList) {
            this.orderList = ordList;
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
                for (OrderHed odr : orderList) {
                    actionTakenList(odr.getStatus(), odr);

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
            getParentFragmentManager().beginTransaction().detach(OrderFragment.this).attach(OrderFragment.this).commit();
            pdialog.setMessage("Finalizing Order Status Data");
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
                    .setTitleText("Order list refreshed success!.")
                    .setContentText("")
                    .setConfirmText("OK")
                    .showCancelButton(false)

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();

                            prepareListData();
                        }
                    })

                    .show();


        }
    }


}

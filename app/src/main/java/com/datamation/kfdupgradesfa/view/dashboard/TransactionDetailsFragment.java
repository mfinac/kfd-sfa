package com.datamation.kfdupgradesfa.view.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.ExpandableNonPListAdapter;
import com.datamation.kfdupgradesfa.adapter.ExpandableReceiptListAdapter;
import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.DayNPrdDetController;
import com.datamation.kfdupgradesfa.controller.DayNPrdHedController;
import com.datamation.kfdupgradesfa.controller.InvDetController;
import com.datamation.kfdupgradesfa.controller.InvHedController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.PreProductController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReceiptDetController;
import com.datamation.kfdupgradesfa.controller.SalesReturnController;
import com.datamation.kfdupgradesfa.controller.SalesReturnDetController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.dialog.PreSalePrintPreviewAlertBox;
import com.datamation.kfdupgradesfa.dialog.VanSalePrintPreviewAlertBox;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.DayNPrdDet;
import com.datamation.kfdupgradesfa.model.DayNPrdHed;
import com.datamation.kfdupgradesfa.model.FInvRDet;
import com.datamation.kfdupgradesfa.model.FInvRHed;
import com.datamation.kfdupgradesfa.model.InvDet;
import com.datamation.kfdupgradesfa.model.InvHed;
import com.datamation.kfdupgradesfa.model.NonPrdDet;
import com.datamation.kfdupgradesfa.model.NonPrdHed;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.model.RecHed;
import com.datamation.kfdupgradesfa.model.ReceiptDet;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.model.apimodel.Result;
import com.datamation.kfdupgradesfa.utils.NetworkUtil;
import com.datamation.kfdupgradesfa.utils.UtilityContainer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class TransactionDetailsFragment extends Fragment {

    private View view;
    public ExpandableListView expListView;
    TextView total;
    private NumberFormat numberFormat = NumberFormat.getInstance();
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Spinner spnTrans;
    public Calendar Report_Calender;
    public DatePickerDialog datePickerDialogfrom, datePickerDialogTo;
    ExpandablePreListAdapter listPreAdapter;
    List<Order> listPreDataHeader;
    HashMap<Order, List<OrderDetail>> listPreDataChild;

    ExpandableReceiptListAdapter listRecAdapter;
    List<ReceiptHed> receiptHedList;
    HashMap<ReceiptHed, List<ReceiptDet>> listReceiptDataChild;

    ExpandableNonPListAdapter listNonPAdapter;
    List<DayNPrdHed> nonPrdHedList;
    HashMap<DayNPrdHed, List<DayNPrdDet>> listNonPrdDataChild;

    int year, month, day;
    Button searchBtn;
    ExpandableVanListAdapter listVanAdapter;
    List<InvHed> listVanDataHeader;
    HashMap<InvHed, List<InvDet>> listVanDataChild;
    NetworkFunctions networkFunctions;

    ExpandableRetListAdapter listRetAdapter;
    List<FInvRHed> listRetDataHeader;
    HashMap<FInvRHed, List<FInvRDet>> listRetDataChild;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private long timeInMillis;
    public ImageView btnFromDate, btnToDate;
    public TextView fromDate, toDate;
    ImageView refresh;

    public TransactionDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaction_details_dspl, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);

        refresh = (ImageView) view.findViewById(R.id.img_refresh);
        spnTrans = (Spinner) view.findViewById(R.id.spnMainTrans);
        total = (TextView) view.findViewById(R.id.item_payment_details_tv_outstanding_amount_total);
        Report_Calender = Calendar.getInstance();
        searchBtn = (Button) view.findViewById(R.id.fragment_report_search_btn);
        year = Report_Calender.get(Calendar.YEAR);
        month = Report_Calender.get(Calendar.MONTH);
        day = Report_Calender.get(Calendar.DAY_OF_MONTH);
        timeInMillis = System.currentTimeMillis();
        networkFunctions = new NetworkFunctions(getActivity());
        ArrayList<String> otherList = new ArrayList<String>();
        otherList.add("ORDERS");
        otherList.add("RECEIPTS");
        otherList.add("NON PRODUCTIVE");

        final ArrayAdapter<String> otherAdapter = new ArrayAdapter<String>(getActivity(), R.layout.reason_spinner_item, otherList);
        otherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTrans.setAdapter(otherAdapter);

        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(true);
        btnFromDate = (ImageView) view.findViewById(R.id.image_view_date_select_from);
        btnToDate = (ImageView) view.findViewById(R.id.image_view_date_select_to);
        toDate = (TextView) view.findViewById(R.id.fragment_invoice_details_tv_filter_params_date_to);
        fromDate = (TextView) view.findViewById(R.id.fragment_invoice_details_tv_filter_params_date_from);
        toDate.setText(dateFormat.format(new Date(timeInMillis)));
        fromDate.setText(dateFormat.format(new Date(timeInMillis)));
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        expListView.setAdapter((BaseExpandableListAdapter) null);
        expListView.clearTextFilter();
        preparePreListData("", "");

        btnFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialogfrom = new DatePickerDialog();
                datePickerDialogfrom.setThemeDark(false);
                datePickerDialogfrom.showYearPickerFirst(false);
                datePickerDialogfrom.setAccentColor(Color.parseColor("#0072BA"));
                datePickerDialogfrom.show(getActivity().getFragmentManager(), "DatePickerDialog");
                datePickerDialogfrom.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        // String date =  "Select Date : " + day + " - " + month + " - " + year;
                        String datesaveFrom = "";
                        if (String.valueOf(monthOfYear + 1).length() < 2 && String.valueOf(dayOfMonth).length() < 2) {
                            datesaveFrom = "" + year + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                        } else {
                            if (String.valueOf(monthOfYear + 1).length() < 2) {
                                datesaveFrom = "" + year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth;
                            } else if (String.valueOf(dayOfMonth).length() < 2) {
                                datesaveFrom = "" + year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                            } else {
                                datesaveFrom = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }

                        }
                        fromDate.setText("" + datesaveFrom);
                    }
                });

            }
        });

        btnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialogTo = new DatePickerDialog();
                datePickerDialogTo.setThemeDark(false);
                datePickerDialogTo.showYearPickerFirst(false);
                datePickerDialogTo.setAccentColor(Color.parseColor("#0072BA"));
                datePickerDialogTo.show(getActivity().getFragmentManager(), "DatePickerDialog");
                datePickerDialogTo.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        //String date =  "Select Date : " + day + " - " + month + " - " + year;
                        String datesaveTo = "";
                        if (String.valueOf(monthOfYear + 1).length() < 2 && String.valueOf(dayOfMonth).length() < 2) {
                            datesaveTo = "" + year + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                        } else {
                            if (String.valueOf(monthOfYear + 1).length() < 2) {
                                datesaveTo = "" + year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth;
                            } else if (String.valueOf(dayOfMonth).length() < 2) {
                                datesaveTo = "" + year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                            } else {
                                datesaveTo = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        }
                        toDate.setText("" + datesaveTo);
                    }
                });

            }
        });

        spnTrans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == 0) {
                    expListView.setAdapter((BaseExpandableListAdapter) null);
                    expListView.clearTextFilter();
                    preparePreListData("", "");
                } else if (position == 1) {
                    expListView.setAdapter((BaseExpandableListAdapter) null);
                    expListView.clearTextFilter();
                    prepareReceiptData("", "");
                } else if (position == 2) {
                    expListView.setAdapter((BaseExpandableListAdapter) null);
                    expListView.clearTextFilter();
                    prepareNonPData("", "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnTrans.getSelectedItemPosition() == 0) {//order
                    preparePreListData(fromDate.getText().toString().trim(), toDate.getText().toString().trim());
                } else if (spnTrans.getSelectedItemPosition() == 1) {//receipt
                    prepareReceiptData(fromDate.getText().toString().trim(), toDate.getText().toString().trim());
                } else if (spnTrans.getSelectedItemPosition() == 2) {//nonproductive
                    prepareNonPData(fromDate.getText().toString().trim(), toDate.getText().toString().trim());
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spnTrans.getSelectedItemPosition() == 0) {//order
                    preparePreListData(fromDate.getText().toString().trim(), toDate.getText().toString().trim());
                } else if (spnTrans.getSelectedItemPosition() == 1) {//receipt

                    PrimeThread p = new PrimeThread(new ReceiptController(getActivity()).getAllNotIssuedReceipts());
                    p.start();
                    Toast.makeText(getActivity(), "Receipt list refreshed", Toast.LENGTH_LONG).show();

                }
            }
        });
        return view;
    }


    class PrimeThread extends Thread {

        CustomProgressDialog pdialog;
        ArrayList<RecHed> receiptList;

        PrimeThread(ArrayList<RecHed> recList) {
            this.pdialog = new CustomProgressDialog(getActivity());
            this.receiptList = recList;

            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Please while for a status update...");
            pdialog.show();
        }

        public void run() {

            Looper.prepare();

            for (RecHed rec : receiptList) {
                actionTakenList(rec.getStatus(), rec);
                try {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            prepareReceiptData(fromDate.getText().toString().trim(), toDate.getText().toString().trim());
                        }
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (pdialog.isShowing()) {
                pdialog.dismiss();

            }
        }
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


                                    if (response != null && response.body() != null) {
                                        int status = response.code();
                                        Log.d(">>>response code", ">>>res " + status);
                                        Log.d(">>>response message", ">>>res " + response.message());
                                        Log.d(">>>response body", ">>>res " + response.body());
                                        String resmsg = "" + response.body();
                                        if (status == 200 && !resmsg.equals("") && !resmsg.equals(null)) {
//
                                            new ReceiptController(getActivity()).updateIsSyncedReceipt(rc.getRefNo(), "0","1", "SYNCED");
                                            listRecAdapter.notifyDataSetChanged();
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.detach(TransactionDetailsFragment.this).attach(TransactionDetailsFragment.this).commit();

                                        } else {
                                            new ReceiptController(getActivity()).updateIsSyncedReceipt(rc.getRefNo(), "0","0", "NOT SYNCED");
                                            listRecAdapter.notifyDataSetChanged();
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.detach(TransactionDetailsFragment.this).attach(TransactionDetailsFragment.this).commit();
                                        }
                                    } else {
                                        Log.d(">>>response ", ">>>res " + response);

                                        Toast.makeText(getActivity(), " Invalid response when receipt upload", Toast.LENGTH_LONG).show();
                                    }

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
                        Toast.makeText(getActivity(), "No Records to upload !", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e(">>>ERROR In EDIT", ">>>" + e.toString());
                    throw e;
                }
            } else if (stats.equals("SYNCED")) {

                new ReceiptStatusDownload(rc.getRefNo(), "SYNCED").execute();
            }else if (stats.equals("RECEIVED")) {

                new ReceiptStatusDownload(rc.getRefNo(), "RECEIVED").execute();
            }
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
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
            getFragmentManager().beginTransaction().detach(TransactionDetailsFragment.this).attach(TransactionDetailsFragment.this).commit();
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

    public void preparePreListData(String from, String to) {
        if (from.equals("") || to.equals("")) {
            listPreDataHeader = new OrderController(getActivity()).getTodayOrders();
        } else {
            listPreDataHeader = new OrderController(getActivity()).getOrdersByDate(from, to);
        }

        expListView.setAdapter((BaseExpandableListAdapter) null);

        if (listPreDataHeader.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();

        } else {
            listPreDataChild = new HashMap<Order, List<OrderDetail>>();

            for (Order free : listPreDataHeader) {
                listPreDataChild.put(free, new OrderDetailController(getActivity()).getTodayOrderDets(free.getFORDHED_REFNO(), from, to));
            }

            listPreAdapter = new ExpandablePreListAdapter(getActivity(), listPreDataHeader, listPreDataChild);
            expListView.setAdapter(listPreAdapter);
        }
    }


    public void prepareReceiptData(String from, String to) {
        if (from.equals("") || to.equals("")) {
            receiptHedList = new ReceiptController(getActivity()).getTodayReceipts();
        } else {
            receiptHedList = new ReceiptController(getActivity()).getReceiptsByDate(from, to);
        }

        expListView.setAdapter((BaseExpandableListAdapter) null);
        if (receiptHedList.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();

        } else {
            listReceiptDataChild = new HashMap<ReceiptHed, List<ReceiptDet>>();

            for (ReceiptHed rHed : receiptHedList) {
                listReceiptDataChild.put(rHed, new ReceiptDetController(getActivity()).getTodayreceiptsDets(rHed.getFPRECHED_REFNO(), from, to));
            }

            listRecAdapter = new ExpandableReceiptListAdapter(getActivity(), receiptHedList, listReceiptDataChild);
            expListView.setAdapter(listRecAdapter);
        }
    }

    public void prepareNonPData(String from, String to) {
        if (from.equals("") || to.equals("")) {
            nonPrdHedList = new DayNPrdHedController(getActivity()).getTodayNPHeds();
        } else {
            nonPrdHedList = new DayNPrdHedController(getActivity()).getNPHedsByDate(from, to);
        }
        expListView.setAdapter((BaseExpandableListAdapter) null);

        if (nonPrdHedList.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();

        } else {
            listNonPrdDataChild = new HashMap<DayNPrdHed, List<DayNPrdDet>>();

            for (DayNPrdHed nonPrdHed : nonPrdHedList) {
                listNonPrdDataChild.put(nonPrdHed, new DayNPrdDetController(getActivity()).getTodayNPDets(nonPrdHed.getNONPRDHED_REFNO(), from, to));
            }

            listNonPAdapter = new ExpandableNonPListAdapter(getActivity(), nonPrdHedList, listNonPrdDataChild);
            expListView.setAdapter(listNonPAdapter);
        }
    }

    public void prepareVanListData(String from, String to) {
        if (from.equals("") || to.equals("")) {
            listVanDataHeader = new InvHedController(getActivity()).getTodayOrders();
        } else {
            listVanDataHeader = new InvHedController(getActivity()).getOrdersByDate(from, to);
        }

        if (listVanDataHeader.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();
            listVanDataChild = new HashMap<InvHed, List<InvDet>>();

            for (InvHed free : listVanDataHeader) {
                listVanDataChild.put(free, new InvDetController(getActivity()).getTodayOrderDets(free.getFINVHED_REFNO(), from, to));
            }

            listVanAdapter = new ExpandableVanListAdapter(getActivity(), listVanDataHeader, listVanDataChild);
            expListView.setAdapter(listVanAdapter);
        } else {
            listVanDataChild = new HashMap<InvHed, List<InvDet>>();

            for (InvHed free : listVanDataHeader) {
                listVanDataChild.put(free, new InvDetController(getActivity()).getTodayOrderDets(free.getFINVHED_REFNO(), from, to));
            }

            listVanAdapter = new ExpandableVanListAdapter(getActivity(), listVanDataHeader, listVanDataChild);
            expListView.setAdapter(listVanAdapter);
        }
    }

    public void prepareRetListData(String from, String to) {

        if (from.equals("") || to.equals("")) {
            listRetDataHeader = new SalesReturnController(getActivity()).getTodayReturns();
        } else {
            listRetDataHeader = new SalesReturnController(getActivity()).getReturnsByDate(from, to);
        }


        if (listRetDataHeader.size() == 0) {
            Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();
        } else {
            listRetDataChild = new HashMap<FInvRHed, List<FInvRDet>>();

            for (FInvRHed free : listRetDataHeader) {
                listRetDataChild.put(free, new SalesReturnDetController(getActivity()).getTodayOrderDets(free.getFINVRHED_REFNO()));
            }

            listRetAdapter = new ExpandableRetListAdapter(getActivity(), listRetDataHeader, listRetDataChild);
            expListView.setAdapter(listRetAdapter);
        }
    }

    // adapter for pre sale

    public class ExpandablePreListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<Order> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<Order, List<OrderDetail>> _listDataChild;

        public ExpandablePreListAdapter(Context context, List<Order> listDataHeader,
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

            txtListChild.setText("ItemCode - " + childText.getFORDDET_ITEM_CODE());
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
            TextView deb = (TextView) convertView.findViewById(R.id.debcode);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView tot = (TextView) convertView.findViewById(R.id.total);
            TextView stats = (TextView) convertView.findViewById(R.id.status);
//            ImageView type = (ImageView) convertView.findViewById(R.id.type);
            TextView type = (TextView) convertView.findViewById(R.id.type);

            ImageView print = (ImageView) convertView.findViewById(R.id.print);

            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getFORDHED_REFNO());
            deb.setText(new CustomerController(_context).getCusNameByCode(headerTitle.getFORDHED_DEB_CODE()));
            if (headerTitle.getFORDHED_IS_SYNCED().equals("1")) {
                //    delete.setBackground(null);
                stats.setText("Synced");
                stats.setTextColor(getResources().getColor(R.color.material_alert_positive_button));
            } else {
                //  delete.setBackground(getResources().getDrawable(R.drawable.icon_minus));
                stats.setText("Not Synced");
                stats.setTextColor(getResources().getColor(R.color.material_alert_negative_button));
            }
            //type.setText(headerTitle.getORDER_TXNTYPE());
            date.setText(headerTitle.getFORDHED_TXN_DATE());
            tot.setText(headerTitle.getFORDHED_TOTAL_AMT());

            type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (headerTitle.getFORDHED_IS_SYNCED().equals("0")) {
                        deleteOrder(headerTitle.getFORDHED_REFNO());
                    } else {
                        Toast.makeText(getActivity(), "Cannot delete synced orders", Toast.LENGTH_LONG).show();

                    }

                }
            });
            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    printOrder(headerTitle.getFORDHED_REFNO());
                }
            });
            return convertView;
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
                    grossTotal += Double.parseDouble(invoice.getFORDHED_TOTAL_AMT());

                }
            }

            total.setText(numberFormat.format(grossTotal));

        }
    }

    public void deleteOrder(final String RefNo) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Do you want to delete this order ?")
                .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        int result = new OrderController(getActivity()).restData(RefNo);

                        if (result > 0) {
                            new OrderDetailController(getActivity()).restData(RefNo);
                            new PreProductController(getActivity()).mClearTables();
                            Toast.makeText(getActivity(), "Order deleted successfully..!", Toast.LENGTH_SHORT).show();

                            preparePreListData(fromDate.getText().toString().trim(), toDate.getText().toString().trim());
                        } else {
                            Toast.makeText(getActivity(), "Order delete unsuccess..!", Toast.LENGTH_SHORT).show();
                        }


                        UtilityContainer.ClearReturnSharedPref(getActivity());
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

    public void deleteInvoice(final String RefNo) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Do you want to delete this invoice ?")
                .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        InsertDeleteReason(RefNo);
                        listVanAdapter.notifyDataSetChanged();
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

    public void InsertDeleteReason(final String refno) {
        final Dialog dltReasonDialog = new Dialog(getActivity());
        dltReasonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dltReasonDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dltReasonDialog.setCancelable(false);
        dltReasonDialog.setCanceledOnTouchOutside(false);
        dltReasonDialog.setContentView(R.layout.delete_reason);

        //initializations
        final EditText reason = (EditText) dltReasonDialog.findViewById(R.id.reason);
        //exit
        dltReasonDialog.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dltReasonDialog.dismiss();
                prepareVanListData(fromDate.getText().toString().trim(), toDate.getText().toString().trim());

            }
        });
        //close
        dltReasonDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (reason.length() > 0 && !reason.getText().toString().equals("")) {

                    ArrayList<InvHed> logHedList = new InvHedController(getActivity()).getAllUnsyncedforLog();
                    new InvHedController(getActivity()).createOrUpdateInvHedLog(logHedList);
                    ArrayList<InvDet> logDetList = new InvDetController(getActivity()).getAllInvDet(refno);
                    new InvDetController(getActivity()).createOrUpdateBCInvDetLog(logDetList);
                    new InvHedController(getActivity()).updateDeleteReason(refno, reason.getText().toString());
                    //   new ItemLocController(getActivity()).UpdateVanStock(refno,"+",new SalRepController(getActivity()).getCurrentLoccode().trim());
                    int result = new InvHedController(getActivity()).restDataBC(refno);
                    if (result > 0) {
                        new InvDetController(getActivity()).restData(refno);
                        prepareVanListData(fromDate.getText().toString().trim(), toDate.getText().toString().trim());
                        Toast.makeText(getActivity(), "Invoice deleted successfully..!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Invoice delete unsuccess..!", Toast.LENGTH_SHORT).show();

                    }
                    dltReasonDialog.dismiss();

                } else {
                    Toast.makeText(getActivity(), "Please enter delete reason..!", Toast.LENGTH_SHORT).show();
                    dltReasonDialog.dismiss();
                }
            }
        });
        dltReasonDialog.show();
    }

    public void printInvoice(final String RefNo) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Do you want to print this invoice ?")
                .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        int a = new VanSalePrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Print preview - reprint", RefNo);
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

    public void printOrder(final String RefNo) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Do you want to print this order ?")
                .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        new PreSalePrintPreviewAlertBox(getActivity(), RefNo);
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

    // adapter for van sale


    public class ExpandableVanListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<InvHed> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<InvHed, List<InvDet>> _listDataChild;

        public ExpandableVanListAdapter(Context context, List<InvHed> listDataHeader,
                                        HashMap<InvHed, List<InvDet>> listChildData) {
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

            final InvDet childText = (InvDet) getChild(groupPosition, childPosition);

            if (grpview == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                grpview = infalInflater.inflate(R.layout.list_items, null);
            }

            TextView txtListChild = (TextView) grpview.findViewById(R.id.itemcode);
            TextView txtListChild1 = (TextView) grpview.findViewById(R.id.qty);
            TextView txtListChild2 = (TextView) grpview.findViewById(R.id.amount);

            txtListChild.setText("ItemCode - " + childText.getFINVDET_ITEM_CODE());
            txtListChild1.setText("Qty - " + childText.getFINVDET_QTY());
            txtListChild2.setText("Amount - " + numberFormat.format(Double.parseDouble(childText.getFINVDET_AMT())));
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
            final InvHed headerTitle = (InvHed) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.refno);
            TextView deb = (TextView) convertView.findViewById(R.id.debcode);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView tot = (TextView) convertView.findViewById(R.id.total);
            TextView stats = (TextView) convertView.findViewById(R.id.status);
//            ImageView type = (ImageView) convertView.findViewById(R.id.type);
            TextView type = (TextView) convertView.findViewById(R.id.type);
            ImageView print = (ImageView) convertView.findViewById(R.id.print);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getFINVHED_REFNO());
            deb.setText(new CustomerController(_context).getCusNameByCode(headerTitle.getFINVHED_DEBCODE()));

            if (headerTitle.getFINVHED_IS_SYNCED().equals("1")) {
                stats.setText("Synced");
                stats.setTextColor(getResources().getColor(R.color.material_alert_positive_button));
            } else {
                stats.setText("Not Synced");
                stats.setTextColor(getResources().getColor(R.color.material_alert_negative_button));

            }

            date.setText(headerTitle.getFINVHED_TXNDATE());
            tot.setText(headerTitle.getFINVHED_TOTALAMT());
            type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (headerTitle.getFINVHED_IS_SYNCED().equals("0")) {
                        deleteInvoice(headerTitle.getFINVHED_REFNO());
                    } else {
                        Toast.makeText(getActivity(), "Cannot delete synced invoices", Toast.LENGTH_LONG).show();
                    }
                }
            });
            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    printInvoice(headerTitle.getFINVHED_REFNO());
                }
            });

            return convertView;
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


            List<InvHed> searchingDetails = _listDataHeader;


            for (InvHed invoice : searchingDetails) {

                if (invoice != null) {
                    grossTotal += Double.parseDouble(invoice.getFINVHED_TOTALAMT());

                }
            }
            total.setText(numberFormat.format(grossTotal));
        }
    }

    // adapter for sale return

    public class ExpandableRetListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<FInvRHed> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<FInvRHed, List<FInvRDet>> _listDataChild;

        public ExpandableRetListAdapter(Context context, List<FInvRHed> listDataHeader,
                                        HashMap<FInvRHed, List<FInvRDet>> listChildData) {
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

            final FInvRDet childText = (FInvRDet) getChild(groupPosition, childPosition);

            if (grpview == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                grpview = infalInflater.inflate(R.layout.list_items, null);
            }

            TextView txtListChild = (TextView) grpview.findViewById(R.id.itemcode);
            TextView txtListChild1 = (TextView) grpview.findViewById(R.id.qty);
            TextView txtListChild2 = (TextView) grpview.findViewById(R.id.amount);

            txtListChild.setText("ItemCode - " + childText.getFINVRDET_ITEMCODE());
            txtListChild1.setText("Qty - " + childText.getFINVRDET_QTY());
            txtListChild2.setText("Amount - " + numberFormat.format(Double.parseDouble(childText.getFINVRDET_AMT())));
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
            FInvRHed headerTitle = (FInvRHed) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.refno);
            TextView deb = (TextView) convertView.findViewById(R.id.debcode);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView tot = (TextView) convertView.findViewById(R.id.total);
            TextView stats = (TextView) convertView.findViewById(R.id.status);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getFINVRHED_REFNO());
            deb.setText(new CustomerController(_context).getCusNameByCode(headerTitle.getFINVRHED_DEBCODE()));
            if (headerTitle.getFINVRHED_IS_SYNCED().equals("1")) {
                stats.setText("Synced");
                stats.setTextColor(getResources().getColor(R.color.material_alert_positive_button));
            } else {
                stats.setText("Not Synced");
                stats.setTextColor(getResources().getColor(R.color.material_alert_negative_button));

            }
            date.setText(headerTitle.getFINVRHED_TXN_DATE());
            tot.setText(headerTitle.getFINVRHED_TOTAL_AMT());

            return convertView;
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


            List<FInvRHed> searchingDetails = _listDataHeader;


            for (FInvRHed invoice : searchingDetails) {

                if (invoice != null) {
                    grossTotal += Double.parseDouble(invoice.getFINVRHED_TOTAL_AMT());

                }
            }
            total.setText(numberFormat.format(grossTotal));
        }
    }

}

package com.datamation.kfdupgradesfa.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.OrderDetailsAdapter;
import com.datamation.kfdupgradesfa.adapter.OrderFreeIssueDetailAdapter;
import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.api.TaskTypeUpload;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.ItemLocController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.PreProductController;
import com.datamation.kfdupgradesfa.controller.PreSaleTaxDTController;
import com.datamation.kfdupgradesfa.controller.PreSaleTaxRGController;
import com.datamation.kfdupgradesfa.controller.ReferenceController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.fragment.FragmentTools;
import com.datamation.kfdupgradesfa.helpers.OrderResponseListener;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.UploadTaskListener;
import com.datamation.kfdupgradesfa.model.Customer;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.model.OrderDisc;
import com.datamation.kfdupgradesfa.model.OrderHed;
import com.datamation.kfdupgradesfa.model.apimodel.Result;
import com.datamation.kfdupgradesfa.settings.ReferenceNum;
import com.datamation.kfdupgradesfa.utils.NetworkUtil;
import com.datamation.kfdupgradesfa.utils.UtilityContainer;
import com.datamation.kfdupgradesfa.view.ActivityHome;
import com.datamation.kfdupgradesfa.view.DebtorDetailsActivity;
import com.datamation.kfdupgradesfa.view.OrderActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderSummaryFragment extends Fragment implements UploadTaskListener {

    public static final String SETTINGS = "PreSalesSummary";
    View view;
    TextView lblGross, lblNetVal, lblReplacements, lblQty, lblSummaryHeader;
    SharedPref mSharedPref;
    String RefNo = "", customerName = "";
    ArrayList<OrderDetail> list;
    ArrayList<OrderDisc> discList;
    String locCode;
    ListView lv_order_det, lvFree;
    ArrayList<OrderDetail> orderList;
    FloatingActionButton fabEdit, fabDiscard, fabSave, fabSave_Upload;
    FloatingActionMenu fam;
    MyReceiver r;
    int iTotFreeQty = 0;
    double totalMKReturn = 0;
    OrderActivity mainActivity;
    private Customer outlet;
    OrderResponseListener responseListener;
    Activity mactivity;
    private double currentLatitude, currentLongitude;
    private Customer customer;
    String refNo;
    List<String> resultListPreSale;
    ProgressDialog dialog;
    String EndTime  ;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_order_summary_layout, container, false);

        mSharedPref = new SharedPref(getActivity());
        mainActivity = (OrderActivity) getActivity();

//        fabEdit = (FloatingActionButton) view.findViewById(R.id.fab2);
        fabDiscard = (FloatingActionButton) view.findViewById(R.id.fab3);
        fabSave = (FloatingActionButton) view.findViewById(R.id.fab1);
        fabSave_Upload = (FloatingActionButton) view.findViewById(R.id.fab4);
        fam = (FloatingActionMenu) view.findViewById(R.id.fab_menu);
        lv_order_det = (ListView) view.findViewById(R.id.lvProducts_pre);
        lvFree = (ListView) view.findViewById(R.id.lvFreeIssue_Inv);

        lblNetVal = (TextView) view.findViewById(R.id.lblNetVal_Inv);
        lblSummaryHeader = (TextView) view.findViewById(R.id.summary_header);
        lblReplacements = (TextView) view.findViewById(R.id.lblReplacement);
        lblGross = (TextView) view.findViewById(R.id.lblGross_Inv);
        lblQty = (TextView) view.findViewById(R.id.lblQty_Inv);

        context=getActivity();
        resultListPreSale = new ArrayList<>();

        mactivity = getActivity();
        customerName = new CustomerController(getActivity()).getCusNameByCode(SharedPref.getInstance(getActivity()).getSelectedDebCode());
        refNo = new ReferenceController(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));
        RefNo = "" + refNo;
        lblSummaryHeader.setText("ORDER SUMMARY - (" + customerName + ")");

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });

//        fabEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                responseListener.moveBackToFragment(1);
//            }
//        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAndUploadDialog(false);
            }
        });

        fabSave_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveAndUploadDialog(true);
//                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
//                if (connectionStatus) {
//                        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
//                                .content("Do you want to save and upload order?")
//                                .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
//                                .positiveText("Yes")
//                                .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
//                                .negativeText("No, Exit")
//                                .callback(new MaterialDialog.ButtonCallback() {
//
//                                    @Override
//                                    public void onPositive(MaterialDialog dialog) {
//                                        super.onPositive(dialog);
//                                        if (NetworkUtil.isNetworkAvailable(getActivity()) && (NetworkUtil.isNotPoorConnection(getActivity())==false)) {
//                                            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
//                                            Toast.makeText(getActivity(), "Poor Internet Connection", Toast.LENGTH_LONG).show();
//                                            saveSummaryDialog();
//                                        } else {
//                                            SaveAndUploadDialog();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onNegative(MaterialDialog dialog) {
//                                        super.onNegative(dialog);
//
//                                        dialog.dismiss();
//
//
//                                    }
//                                })
//                                .build();
//                        materialDialog.setCanceledOnTouchOutside(false);
//                        materialDialog.show();
//
//
//                } else {
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//                    alertDialogBuilder.setTitle("Internet connection lost !");
//                    alertDialogBuilder.setMessage("Do you want to proceed this order ?");
//                    alertDialogBuilder.setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                            saveSummaryDialog();
//
//                        }
//                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    AlertDialog alertD = alertDialogBuilder.create();
//                    alertD.show();
//                }

            }
        });

        fabDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoEditingData();
                mSharedPref.setOrdertHeaderNextClicked(false);
            }
        });

        lv_order_det.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new OrderDetailController(getActivity()).restFreeIssueData(RefNo);
                newDeleteOrderDialog(position);
                return true;
            }
        });

        return view;
    }

    public void newDeleteOrderDialog(final int dltPosition) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Confirm Deletion !");
        alertDialogBuilder.setMessage("Do you want to delete this item ?");
        alertDialogBuilder.setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // new PreProductController(getActivity()).updateProductQty(orderList.get(dltPosition).getFORDERDET_ITEMCODE(), "0",orderList.get(dltPosition).getFORDERDET_TYPE());
                // new PreProductController(getActivity()).updateProductCase(orderList.get(dltPosition).getFORDERDET_ITEMCODE(), "0",orderList.get(dltPosition).getFORDERDET_TYPE());
                new OrderDetailController(getActivity()).mDeleteRecords(RefNo, orderList.get(dltPosition).getFORDDET_ITEM_CODE(), orderList.get(dltPosition).getFORDDET_TYPE());
                Toast.makeText(getActivity(), "Deleted successfully!", Toast.LENGTH_SHORT).show();
                showData();
                responseListener.moveBackToFragment(1);

            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    public void undoEditingData() {

        Order hed = new OrderController(getActivity()).getAllActiveOrdHed();
        outlet = new CustomerController(getActivity()).getSelectedCustomerByCode(hed.getFORDHED_DEB_CODE());

        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Do you want to discard the order?")
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
                            mSharedPref.setDiscountClicked("0");
                            mSharedPref.setOrdertHeaderNextClicked(false);
                            mSharedPref.setIsQuantityAdded(false);

                        }

                        Toast.makeText(getActivity(), "Order discarded successfully..!", Toast.LENGTH_SHORT).show();
                        Intent intnt = new Intent(getActivity(), ActivityHome.class);
                        intnt.putExtra("outlet", outlet);
                        startActivity(intnt);
                        getActivity().finish();
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

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Save primary & secondary invoice-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*/


    public void mRefreshData() {
        showData();
        if (mSharedPref.getDiscountClicked().equals("0")) {
            responseListener.moveBackToFragment(1);
            Toast.makeText(getActivity(), "Please tap on Arrow Button", Toast.LENGTH_LONG).show();
        }

        customerName = new CustomerController(getActivity()).getCusNameByCode(SharedPref.getInstance(getActivity()).getSelectedDebCode());

        lblSummaryHeader.setText("ORDER SUMMARY - (" + customerName + ")");
        int ftotQty = 0, fTotFree = 0, returnQty = 0, replacements = 0;
        double ftotAmt = 0, fTotLineDisc = 0, fTotSchDisc = 0, totalReturn = 0;
        String itemCode = "";

//        locCode = new CustomerController(getActivity()).getCurrentLocCode();
        locCode = mSharedPref.getGlobalVal("KeyLoc");
        list = new OrderDetailController(getActivity()).getAllOrderDetails(RefNo);
//        discList = new OrderDiscController(getActivity()).getAllOrderDiscs(RefNo);
//
        for (OrderDetail ordDet : list) {

            ftotAmt += Double.parseDouble(ordDet.getFORDDET_AMT());

            if (ordDet.getFORDDET_TYPE().equals("FI")) {
                fTotFree += Integer.parseInt(ordDet.getFORDDET_QTY());
            } else {
                ftotQty += Integer.parseInt(ordDet.getFORDDET_QTY());
            }

        }


        Double gross = 0.0;
        Double net = 0.0;

        gross = ftotAmt + fTotSchDisc;
        net = gross + totalReturn - fTotSchDisc;

        iTotFreeQty = fTotFree;
        lblQty.setText(String.valueOf(ftotQty));

        lblGross.setText(String.format("%.2f", gross)); // SA type total amt + discount
        lblReplacements.setText(String.valueOf(fTotFree));


    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/
    public void showData() {
        try {
            lv_order_det.setAdapter(null);
            orderList = new OrderDetailController(getActivity()).getAllOrderDetails(RefNo);
            lv_order_det.setAdapter(new OrderDetailsAdapter(getActivity(), orderList, mSharedPref.getSelectedDebCode()));//2019-07-07 till error free


        } catch (NullPointerException e) {
            Log.v("SA Error", e.toString());
        }
    }

//    public void saveSummaryDialog() {
//
//        if (new OrderDetailController(getActivity()).getItemCount(RefNo) > 0) {
//            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//            View promptView = layoutInflater.inflate(R.layout.sales_management_van_sales_summary_dialog, null);
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//            alertDialogBuilder.setTitle("Do you want to save the invoice ?");
//            alertDialogBuilder.setView(promptView);
//
//            final ListView lvProducts_Invoice = (ListView) promptView.findViewById(R.id.lvProducts_Summary_Dialog_Inv);
//            ViewGroup.LayoutParams invItmparams = lvProducts_Invoice.getLayoutParams();
//            ArrayList<OrderDetail> orderItemList = null;
//            orderItemList = new OrderDetailController(getActivity()).getAllItemsAddedInCurrentSale(RefNo, "SA", "");
//            if (orderItemList.size() > 0) {
//                invItmparams.height = 200;
//            } else {
//                invItmparams.height = 0;
//            }
//            lvProducts_Invoice.setLayoutParams(invItmparams);
//
//            lvProducts_Invoice.setAdapter(new OrderDetailsAdapter(getActivity(), orderItemList, mSharedPref.getSelectedDebCode()));
//
//            //MMS - freeissues
//            ListView lvProducts_freeIssue = (ListView) promptView.findViewById(R.id.lvProducts_Summary_freeIssue);
//            ViewGroup.LayoutParams params = lvProducts_freeIssue.getLayoutParams();
//            ArrayList<OrderDetail> orderFreeIssueItemList = null;
//            orderFreeIssueItemList = new OrderDetailController(getActivity()).getAllItemsAddedInCurrentSale(RefNo, "FI", "FD");
//            if (orderFreeIssueItemList.size() > 0) {
//                params.height = 200;
//            } else {
//                params.height = 0;
//            }
//
//            lvProducts_freeIssue.setLayoutParams(params);
//            lvProducts_freeIssue.setAdapter(new OrderFreeIssueDetailAdapter(getActivity(), orderFreeIssueItemList, mSharedPref.getSelectedDebCode()));
//
//            alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                public void onClick(final DialogInterface dialog, int id) {
//
//                    TimeZone tz = TimeZone.getTimeZone("UTC+5:30");
//                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
//                    df.setTimeZone(tz);
//                    EndTime  = df.format(new Date());
//
//                    String AppVersion = "";
//
//                    try {
//                        PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
//                        AppVersion = pInfo.versionName;
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    Order ordHed = new Order();
//                    ArrayList<Order> ordHedList = new ArrayList<Order>();
//                    ordHed.setFORDHED_REFNO(refNo);
//                    ordHed.setOrderId(0);
//                    Order presale = new OrderController(getActivity()).getAllActiveOrdHed();
//
//                    ordHed.setFORDHED_DEB_CODE(presale.getFORDHED_DEB_CODE());
//                    ordHed.setFORDHED_ADD_DATE(presale.getFORDHED_ADD_DATE());
//                    ordHed.setFORDHED_DELV_DATE(presale.getFORDHED_DELV_DATE());
//                    ordHed.setFORDHED_ROUTE_CODE(presale.getFORDHED_ROUTE_CODE());
//                    ordHed.setFORDHED_MANU_REF(presale.getFORDHED_MANU_REF());
//                    ordHed.setFORDHED_REMARKS(presale.getFORDHED_REMARKS());
//                    ordHed.setFORDHED_ADD_MACH(presale.getFORDHED_ADD_MACH());
//                    ordHed.setFORDHED_ADD_USER(presale.getFORDHED_ADD_USER());
//                    ordHed.setFORDHED_APP_DATE(presale.getFORDHED_APP_DATE());
//                    ordHed.setFORDHED_APPSTS(presale.getFORDHED_APPSTS());
//                    ordHed.setFORDHED_RECORD_ID(presale.getFORDHED_RECORD_ID());
//                  //  ordHed.setFORDHED_RECORD_ID(AppVersion);
//                    ordHed.setFORDHED_APP_USER(presale.getFORDHED_APP_USER());
//                    ordHed.setFORDHED_CUR_CODE(presale.getFORDHED_CUR_CODE());
//                    ordHed.setFORDHED_CUR_RATE(presale.getFORDHED_CUR_RATE());
//                    ordHed.setFORDHED_CUSADD1(presale.getFORDHED_CUSADD1());
//                    ordHed.setFORDHED_CUSADD2(presale.getFORDHED_CUSADD2());
//                    ordHed.setFORDHED_CUSADD3(presale.getFORDHED_CUSADD3());
//                    ordHed.setFORDHED_CONTACT(presale.getFORDHED_CONTACT());
//                    ordHed.setFORDHED_CUSTELE(presale.getFORDHED_CUSTELE());
//                    ordHed.setFORDHED_LOC_CODE(locCode);
//                    ordHed.setFORDHED_TAX_REG(new CustomerController(getActivity()).getTaxRegStatus(presale.getFORDHED_DEB_CODE()));
//
////                   ordHed.setFORDHED_BP_TOTAL_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+""); // String.format("%.2f",sHeaderDisPer)+""
//                    ordHed.setFORDHED_B_TOTAL_AMT(lblGross.getText().toString());
////                    ordHed.setFORDHED_B_TOTAL_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
//                    ordHed.setFORDHED_BP_TOTAL_DIS("0.00");
//                    ordHed.setFORDHED_B_TOTAL_DIS("0.00");
//                    ordHed.setFORDHED_B_TOTAL_TAX("0.00");
//                    ordHed.setFORDHED_DIS_PER("0.00");
//                    //ordHed.setFORDHED_DIS_PER(String.format("%.2f",presale.getFORDHED_DIS_PER())+"");
//                    ordHed.setFORDHED_END_TIME_SO(EndTime);
//                    ordHed.setFORDHED_START_TIME_SO(presale.getFORDHED_START_TIME_SO());
//                    ordHed.setFORDHED_ADDRESS("");
//                    ordHed.setFORDHED_COST_CODE(presale.getFORDHED_COST_CODE());
//
//                    ordHed.setFORDHED_TOTAL_TAX("0.00");
//                    ordHed.setFORDHED_TOTALDIS("0.00");
//                    ordHed.setFORDHED_TOTAL_ITM_DIS("0.00");
//                    ordHed.setFORDHED_TOT_MKR_AMT("0.00");
//                    ordHed.setFORDHED_TOTAL_AMT(lblGross.getText().toString());
//                    ordHed.setFORDHED_TXN_DATE(currentDate());
//                    ordHed.setFORDHED_TXN_TYPE("21");
////                    ordHed.setFORDHED_REPCODE(mSharedPref.getGlobalVal("KeySelectedRep"));
//                    ordHed.setFORDHED_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());
//                    ordHed.setFORDHED_IS_ACTIVE("0");
//                    ordHed.setFORDHED_IS_SYNCED("1");
//                    ordHed.setFORDHED_LATITUDE("0.00");
//                    ordHed.setFORDHED_LONGITUDE("0.00");
//                    ordHed.setFORDHED_HED_DIS_VAL(String.format("%.2f", presale.getFORDHED_TOTALDIS()) + "");
//                    ordHed.setFORDHED_HED_DIS_PER_VAL(String.format("%.2f", presale.getFORDHED_TOTALDIS()) + "");//String.format("%,.2f",sHeaderDisPer)
//
//                    ordHed.setFORDHED_LONGITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("Longitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Longitude"));
//                    ordHed.setFORDHED_LATITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("Latitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Latitude"));
//                    ordHed.setFORDHED_PAYMENT_TYPE(presale.getFORDHED_PAYMENT_TYPE());
//                    ordHed.setFORDHED_STATUS("NOT SYNCED");
//                    ordHedList.add(ordHed);
//
//                    if (new OrderController(getActivity()).createOrUpdateOrdHed(ordHedList) > 0) {
//
//
//                        final OrderActivity activity = (OrderActivity) getActivity();
//                        /*-*-*-*-*-*-*-*-*-*-QOH update-*-*-*-*-*-*-*-*-*/
//                        UpdateTaxDetails(RefNo);
//                        new ItemLocController(getActivity()).UpdateOrderQOH(RefNo, "-", locCode);
//                        Toast.makeText(getActivity(), "Order saved successfully..!", Toast.LENGTH_SHORT).show();
//                        mSharedPref.setDiscountClicked("0");
//                        mSharedPref.setHeaderNextClicked("0");
//                        mSharedPref.setGlobalVal("placeAnOrder", "");
//                        mSharedPref.setGlobalVal("KeySelectedRep", "");
//                        mSharedPref.setOrdertHeaderNextClicked(false);
//                        UtilityContainer.ClearReturnSharedPref(getActivity());
//                        //  outlet = new CustomerController(getActivity()).getSelectedCustomerByCode(mSharedPref.getSelectedDebCode());
//                        // new PreProductController(getActivity()).mClearTables();
//                        new OrderController(getActivity()).InactiveStatusUpdate(RefNo);
//                        new OrderDetailController(getActivity()).InactiveStatusUpdate(RefNo);
//                        new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.NumVal));
//                        Intent intnt = new Intent(getActivity(), DebtorDetailsActivity.class);
//                        startActivity(intnt);
//                        getActivity().finish();
//                        mSharedPref.setIsQuantityAdded(false);
//
//
//                    } else {
//                        Toast.makeText(getActivity(), "Order Save Failed..", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                }
//            });
//
//            AlertDialog alertD = alertDialogBuilder.create();
//            alertD.show();
//
//
//        } else
//            Toast.makeText(getActivity(), "Add items before save ...!", Toast.LENGTH_SHORT).show();
//
//
//    }

    public void SaveAndUploadDialog(boolean isUpload)
    {
        if (new OrderDetailController(getActivity()).getItemCount(RefNo) > 0)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View promptView = layoutInflater.inflate(R.layout.sales_management_van_sales_summary_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Do you want to upload this order?");
            alertDialogBuilder.setView(promptView);

            final ListView lvProducts_Invoice = (ListView) promptView.findViewById(R.id.lvProducts_Summary_Dialog_Inv);
            ViewGroup.LayoutParams invItmparams = lvProducts_Invoice.getLayoutParams();
            ArrayList<OrderDetail> orderItemList = null;
            orderItemList = new OrderDetailController(getActivity()).getAllItemsAddedInCurrentSale(RefNo, "SA", "");
            if (orderItemList.size() > 0) {
                invItmparams.height = 200;
            } else {
                invItmparams.height = 0;
            }
            lvProducts_Invoice.setLayoutParams(invItmparams);

            lvProducts_Invoice.setAdapter(new OrderDetailsAdapter(getActivity(), orderItemList, mSharedPref.getSelectedDebCode()));

            //MMS - freeissues
            ListView lvProducts_freeIssue = (ListView) promptView.findViewById(R.id.lvProducts_Summary_freeIssue);
            ViewGroup.LayoutParams params = lvProducts_freeIssue.getLayoutParams();
            ArrayList<OrderDetail> orderFreeIssueItemList = null;
            orderFreeIssueItemList = new OrderDetailController(getActivity()).getAllItemsAddedInCurrentSale(RefNo, "FI", "FD");
            if (orderFreeIssueItemList.size() > 0) {
                params.height = 200;
            } else {
                params.height = 0;
            }

            lvProducts_freeIssue.setLayoutParams(params);
            lvProducts_freeIssue.setAdapter(new OrderFreeIssueDetailAdapter(getActivity(), orderFreeIssueItemList, mSharedPref.getSelectedDebCode()));

            alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(final DialogInterface dialog, int id) {

                    save(isUpload);

                }

            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intnt2 = new Intent(getActivity(), DebtorDetailsActivity.class);
                    startActivity(intnt2);
                    getActivity().finish();
                    dialog.cancel();
                }
            });


            AlertDialog alertD = alertDialogBuilder.create();
            alertD.show();


        } else
            Toast.makeText(getActivity(), "Add items before save ...!", Toast.LENGTH_LONG).show();


    }

    private void navigateToNext()
    {
        Intent intnt = new Intent(getActivity(), DebtorDetailsActivity.class);
        startActivity(intnt);
        getActivity().finish();
    }

    private void save(boolean isUpload)
    {
        TimeZone tz = TimeZone.getTimeZone("UTC+5:30");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        EndTime  = df.format(new Date());

        Order ordHed = new Order();
        ArrayList<Order> ordHedList = new ArrayList<Order>();
        Order presale = new Order();//new OrderController(getActivity()).getAllActiveOrdHed();
        ordHed.setFORDHED_REFNO(RefNo);
        ordHed.setOrderId(0);
        presale = new OrderController(getActivity()).getAllActiveOrdHed();

        ordHed.setFORDHED_DEB_CODE(presale.getFORDHED_DEB_CODE());
        ordHed.setFORDHED_ADD_DATE(presale.getFORDHED_ADD_DATE());
        ordHed.setFORDHED_DELV_DATE(presale.getFORDHED_DELV_DATE());
        ordHed.setFORDHED_ROUTE_CODE(presale.getFORDHED_ROUTE_CODE());
        ordHed.setFORDHED_MANU_REF(presale.getFORDHED_MANU_REF());
        ordHed.setFORDHED_REMARKS(presale.getFORDHED_REMARKS());
        ordHed.setFORDHED_ADD_MACH(presale.getFORDHED_ADD_MACH());
        ordHed.setFORDHED_ADD_USER(presale.getFORDHED_ADD_USER());
        ordHed.setFORDHED_APP_DATE(presale.getFORDHED_APP_DATE());
        ordHed.setFORDHED_APPSTS(presale.getFORDHED_APPSTS());
        ordHed.setFORDHED_APP_USER(presale.getFORDHED_APP_USER());
        ordHed.setFORDHED_CUR_CODE(presale.getFORDHED_CUR_CODE());
        ordHed.setFORDHED_CUR_RATE(presale.getFORDHED_CUR_RATE());
        ordHed.setFORDHED_CUSADD1(presale.getFORDHED_CUSADD1());
        ordHed.setFORDHED_CUSADD2(presale.getFORDHED_CUSADD2());
        ordHed.setFORDHED_CUSADD3(presale.getFORDHED_CUSADD3());
        ordHed.setFORDHED_CONTACT(presale.getFORDHED_CONTACT());
        ordHed.setFORDHED_CUSTELE(presale.getFORDHED_CUSTELE());
        ordHed.setFORDHED_RECORD_ID(presale.getFORDHED_RECORD_ID());
        ordHed.setFORDHED_LOC_CODE(locCode);
        ordHed.setFORDHED_TAX_REG(new CustomerController(getActivity()).getTaxRegStatus(presale.getFORDHED_DEB_CODE()));

//                   ordHed.setFORDHED_BP_TOTAL_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+""); // String.format("%.2f",sHeaderDisPer)+""
        ordHed.setFORDHED_B_TOTAL_AMT(lblGross.getText().toString());
//                    ordHed.setFORDHED_B_TOTAL_DIS(String.format("%.2f",presale.getFORDHED_TOTALDIS())+"");
        ordHed.setFORDHED_BP_TOTAL_DIS("0.00");
        ordHed.setFORDHED_B_TOTAL_DIS("0.00");
        ordHed.setFORDHED_B_TOTAL_TAX("0.00");
        ordHed.setFORDHED_DIS_PER("0.00");
        //ordHed.setFORDHED_DIS_PER(String.format("%.2f",presale.getFORDHED_DIS_PER())+"");
        ordHed.setFORDHED_END_TIME_SO(EndTime);
        ordHed.setFORDHED_START_TIME_SO(presale.getFORDHED_START_TIME_SO());
        ordHed.setFORDHED_ADDRESS("");

        ordHed.setFORDHED_COST_CODE(presale.getFORDHED_COST_CODE());
        ordHed.setFORDHED_TOTAL_TAX("0.00");
        ordHed.setFORDHED_TOTALDIS("0.00");
        ordHed.setFORDHED_TOTAL_ITM_DIS("0.00");
        ordHed.setFORDHED_TOT_MKR_AMT("0.00");

        ordHed.setFORDHED_LONGITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("Longitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Longitude"));
        ordHed.setFORDHED_LATITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("Latitude").equals("") ? "0.00" : mSharedPref.getGlobalVal("Latitude"));
        ordHed.setFORDHED_TOTAL_AMT(lblGross.getText().toString());
        ordHed.setFORDHED_TXN_DATE(currentDate());
        ordHed.setFORDHED_TXN_TYPE("21");
        ordHed.setFORDHED_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());
        ordHed.setFORDHED_IS_ACTIVE("0");
        ordHed.setFORDHED_IS_SYNCED("1");
        // ordHed.setFORDHED_STATUS("NOT SYNCED");
        ordHed.setFORDHED_HED_DIS_VAL(String.format("%.2f", presale.getFORDHED_TOTALDIS()) + "");
        ordHed.setFORDHED_HED_DIS_PER_VAL(String.format("%.2f", presale.getFORDHED_TOTALDIS()) + "");//String.format("%,.2f",sHeaderDisPer)

        ordHed.setFORDHED_PAYMENT_TYPE(presale.getFORDHED_PAYMENT_TYPE());

        ordHedList.add(ordHed);

        if (new OrderController(getActivity()).createOrUpdateOrdHed(ordHedList) > 0)
        {
            final OrderActivity activity = (OrderActivity) getActivity();
            /*-*-*-*-*-*-*-*-*-*-QOH update-*-*-*-*-*-*-*-*-*/
            UpdateTaxDetails(RefNo);
            new ItemLocController(getActivity()).UpdateOrderQOH(RefNo, "-", locCode);
            //Toast.makeText(getActivity(), "Locally Order saved successfully..!", Toast.LENGTH_LONG).show();
            mSharedPref.setDiscountClicked("0");

            mSharedPref.setGlobalVal("placeAnOrder", "");
            mSharedPref.setGlobalVal("KeySelectedRep", "");
            UtilityContainer.ClearReturnSharedPref(getActivity());
            outlet = new CustomerController(getActivity()).getSelectedCustomerByCode(mSharedPref.getSelectedDebCode());

            new PreProductController(getActivity()).mClearTables();
            new OrderController(getActivity()).InactiveStatusUpdate(RefNo);
            new OrderDetailController(getActivity()).InactiveStatusUpdate(RefNo);
            new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.NumVal));

            Toast.makeText(getActivity(), "Locally Order saved successfully..!", Toast.LENGTH_LONG).show();

            if (isUpload)
            {
                if (NetworkUtil.isNetworkAvailable(getActivity()))
                {
                    if (NetworkUtil.isNotPoorConnection(getActivity()))
                    {
                        try {
                            Upload(new OrderController(getActivity()).getAllUnSyncOrdHedNew());
                            mSharedPref.setOrdertHeaderNextClicked(false);
                            mSharedPref.setIsQuantityAdded(false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        networkWarning("Order saved locally. Please use tools upload due to poor network", "Poor network");
                    }
                }
                else
                {
                    networkWarning("Order saved locally. Please use tools upload due to no internet", "No internet");
                }
            }
            else
            {
                navigateToNext();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "Order Save Failed..", Toast.LENGTH_LONG).show();
            navigateToNext();
        }
    }

    @SuppressLint("SuspiciousIndentation")
    public void Upload(final ArrayList<OrderHed> orders) throws InterruptedException {
        // new OrderController(getActivity()).updateIsActive(""+mSharedPref.generateOrderId(),"2");


        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Uploading order records");
        dialog.show();

      //  if (!NetworkUtil.isNetworkAvailable(getActivity()) && NetworkUtil.isNotPoorConnection(getActivity())) {
            if (orders.size() > 0) {

                for (final OrderHed c : orders) {
                    try {
                        String content_type = "application/json";
                        ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
                        final Handler mHandler = new Handler(Looper.getMainLooper());
                        JsonParser jsonParser = new JsonParser();
                        String orderJson = new Gson().toJson(c);
                        JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
                        JsonArray jsonArray = new JsonArray();
                        jsonArray.add(objectFromString);

                        Call<Result> resultCall = apiInterface.uploadOrder(objectFromString, content_type);
                        resultCall.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {

                                int status = response.code();

                                if (response.isSuccessful()) {
                                    response.body(); // have your all data
                                    boolean result = response.body().isResponse();
                                    Log.d(">>response" + status, result + ">>" + c.getRefNo());
                                    if (result) {
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "0", "SYNCED", "0");
                                                //  new OrderController(getActivity()).updateIsActive(c.getRefNo(), "0");
//                                                new OrderController(getActivity()).up(c.getFORDHED_REFNO(), "2");

                                                Toast.makeText(getActivity(), "Order Upload successfully..!", Toast.LENGTH_LONG).show();

                                                addRefNoResults(c.getRefNo() + " --> Success\n", orders.size());
                                            }
                                        });
                                    } else {
                                        c.setIsSync("1");
                                        c.setIsActive("0");
                                        new OrderController(getActivity()).updateIsSynced(c.getRefNo(), "1", "NOT SYNCED", "0");
                                        // new OrderController(getActivity()).updateIsActive(c.getRefNo(), "0");
                                        Toast.makeText(getActivity(), "Order Upload Failed.", Toast.LENGTH_LONG).show();
                                        addRefNoResults(c.getRefNo() + " --> Failed\n", orders.size());
                                    }
                                } else {
                                    Toast.makeText(context, " Invalid response when order upload", Toast.LENGTH_LONG).show();
                                    Intent intnt = new Intent(context, DebtorDetailsActivity.class);
                                    startActivity(intnt);
                                    getActivity().finish();
                                }// this will tell you why your api doesnt work most of time


                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                Toast.makeText(getActivity(), "Error response " + t.toString(), Toast.LENGTH_LONG).show();
                                Intent intnt = new Intent(getActivity(), DebtorDetailsActivity.class);
                                startActivity(intnt);
                                getActivity().finish();

                            }

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                Toast.makeText(getActivity(), "No Records to upload !", android.widget.Toast.LENGTH_LONG).show();
            }
//        } else {
//            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
//            Toast.makeText(getActivity(), "Poor Internet Connection", Toast.LENGTH_LONG).show();
//           // saveSummaryDialog();
//            dialog.dismiss();
//        }
    }


    private void addRefNoResults(String ref, int count) {
        dialog.dismiss();
        resultListPreSale.add(ref);
        if (count == resultListPreSale.size()) {
            mUploadResult(resultListPreSale);
        }
    }

    public void mUploadResult(List<String> messages) {
        String msg = "";
        for (String s : messages) {
            msg += s;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle("Upload Order Summary");

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intnt = new Intent(getActivity(), DebtorDetailsActivity.class);
                startActivity(intnt);
                getActivity().finish();
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(cal.getTime());
    }

    private String currentDate() {
        // TODO Auto-generated method stub
        //current date and time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);

    }
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public void UpdateTaxDetails(String refNo) {

        ArrayList<OrderDetail> list = new OrderDetailController(getActivity()).getAllOrderDetailsForTaxUpdate(RefNo);
        new PreSaleTaxRGController(getActivity()).UpdateSalesTaxRG(list, mSharedPref.getSelectedDebCode());
        new PreSaleTaxDTController(getActivity()).UpdateSalesTaxDT(list);
    }
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public void mPauseinvoice() {

        if (new OrderDetailController(getActivity()).getItemCount(RefNo) > 0) {
            Order hed = new OrderController(getActivity()).getAllActiveOrdHed();
            outlet = new CustomerController(getActivity()).getSelectedCustomerByCode(hed.getFORDHED_DEB_CODE());
            Intent intnt = new Intent(getActivity(), ActivityHome.class);
            intnt.putExtra("outlet", outlet);
            startActivity(intnt);
            getActivity().finish();
        } else
            Toast.makeText(getActivity(), "Add items before pause ...!", Toast.LENGTH_LONG).show();
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_PRE_SUMMARY"));
    }

    @Override
    public void onTaskCompleted(TaskTypeUpload taskType, List<String> list) {

    }

    @Override
    public void onTaskCompleted(List<String> list) {

    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            mRefreshData();
        }
    }

    /*******************************************************************/
    @Override
    public void onAttach(Activity activity) {
        this.mactivity = activity;
        super.onAttach(mactivity);
        try {
            responseListener = (OrderResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }

    public void networkWarning(String message, String title){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                dialog.cancel();
                navigateToNext();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

}

package com.datamation.kfdupgradesfa.order;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.CustomerDebtAdapter;
import com.datamation.kfdupgradesfa.controller.CostController;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.LocationsController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.OutstandingController;
import com.datamation.kfdupgradesfa.controller.PreProductController;
import com.datamation.kfdupgradesfa.controller.ReferenceController;
import com.datamation.kfdupgradesfa.controller.RouteController;
import com.datamation.kfdupgradesfa.controller.RouteDetController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.fragment.debtorlist.AllCustomerFragment;
import com.datamation.kfdupgradesfa.helpers.OrderResponseListener;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Cost;
import com.datamation.kfdupgradesfa.model.Customer;
import com.datamation.kfdupgradesfa.model.FddbNote;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.settings.ReferenceNum;
import com.datamation.kfdupgradesfa.view.ActivityHome;
import com.datamation.kfdupgradesfa.view.CustomerListActivity;
import com.datamation.kfdupgradesfa.view.DebtorDetailsActivity;
import com.datamation.kfdupgradesfa.view.OrderActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//import com.bit.sfa.Settings.SharedPreferencesClass;

public class OrderHeaderFragment extends Fragment {

    View view;
    private FloatingActionButton next;
    //public static EditText ordno, date, mNo, deldate, remarks;
    public String LOG_TAG = "OrderHeaderFragment";
    TextView lblCustomerName, outStandingAmt, lastBillAmt, lblPreRefno, deliveryDate;
    EditText currnentDate, txtManual, txtRemakrs, txtRoute;
    MyReceiver r;
    public SharedPref pref;
    public SharedPref mSharedPref;
    OrderResponseListener preSalesResponseListener;
    OrderActivity activity;
    ArrayList<Cost> costList;
    private Customer outlet;
    ImageButton img_bdate;
    Calendar Scalendar;
    Context context;
    int year, month, day;
    DatePickerDialog datePickerDialog;
    Spinner spCost, spSales_Rep;
    String formattedDate;
    String address = "No Address";
    String repcode, refNo;
    String startTime  ;
    //ArrayList<Order> ordHedList = new ArrayList<Order>();


    public OrderHeaderFragment() {
        // Required empty public constructor
    }

    public static OrderHeaderFragment newInstance() {
        OrderHeaderFragment fragment = new OrderHeaderFragment();
        return fragment;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_promo_sale_header, container, false);
        activity = (OrderActivity) getActivity();
        next = (FloatingActionButton) view.findViewById(R.id.fab);
        pref = SharedPref.getInstance(getActivity());
        mSharedPref = SharedPref.getInstance(getActivity());
        setHasOptionsMenu(true);
        final Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); //change this
        formattedDate = simpleDateFormat.format(d);

        Scalendar = Calendar.getInstance();
        Scalendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = Scalendar.getTime();

        next.setVisibility(View.GONE);
        lblCustomerName = (TextView) view.findViewById(R.id.customerName);
        outStandingAmt = (TextView) view.findViewById(R.id.lbl_Inv_outstanding_amt);
        lastBillAmt = (TextView) view.findViewById(R.id.lbl_inv_lastbill);
        lblPreRefno = (TextView) view.findViewById(R.id.invoice_no);
        currnentDate = (EditText) view.findViewById(R.id.lbl_InvDate);
        txtManual = (EditText) view.findViewById(R.id.txt_InvManual);
        txtRemakrs = (EditText) view.findViewById(R.id.txt_InvRemarks);
        txtRoute = (EditText) view.findViewById(R.id.txt_route);
        img_bdate = (ImageButton) view.findViewById(R.id.imgbtn_DeliveryDate);
        deliveryDate = (TextView) view.findViewById(R.id.txt_deliveryDate);
        spCost = (Spinner) view.findViewById(R.id.cost_spinner);
        spSales_Rep = (Spinner) view.findViewById(R.id.sales_rep_spinner);
        if(new ReferenceController(getActivity()).IsExistRefNoOrder(new ReferenceController(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)))>0){
            new ReferenceNum(getActivity()).NumValueUpdateAvoidDuplicateOrder(getResources().getString(R.string.NumVal));
            refNo = new ReferenceController(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));
        }else{
            refNo = new ReferenceController(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));
        }

        Log.d("*****$$$$$$$$******", "onCreateView: "+refNo);
        // lblCustomerName.setText(pref.getSelectedDebName());
        lblCustomerName.setText(new CustomerController(getActivity()).getCusNameByCode(pref.getSelectedDebCode()));
//         txtRoute.setText(new RouteDetController(getActivity()).getRouteCodeByDebCode(pref.getSelectedDebCode()));
        String routeCode = new RouteDetController(getActivity()).getRouteCodeByDebCode(pref.getSelectedDebCode());

        String routenam = new RouteController(getActivity()).getRouteNameByCode(routeCode);
        txtRoute.setText(routenam.trim());

        TimeZone tz = TimeZone.getTimeZone("UTC+5:30");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        startTime  = df.format(new Date());

        currnentDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        deliveryDate.setText(simpleDateFormat.format(tomorrow));
        outStandingAmt.setText(String.format("%,.2f", new OutstandingController(getActivity()).getDebtorBalance(pref.getSelectedDebCode())));
        txtRemakrs.setEnabled(true);
        txtManual.setEnabled(true);
        //select Delivery Date
        year = Scalendar.get(Calendar.YEAR);
        month = Scalendar.get(Calendar.MONTH);
        day = Scalendar.get(Calendar.DAY_OF_MONTH);
        context = getActivity();

        try {
            lblPreRefno.setText("" + refNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*create payment Type*/
        costList = new CostController(getActivity()).getAllCostCenters();
        List<String> costNames = new ArrayList<String>();
        /* Merge group code with group name to the list */
        for (Cost cost : costList) {
            costNames.add(cost.getFCOST_CODE() + " - " + cost.getFCOST_NAME());
        }

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, costNames);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCost.setAdapter(dataAdapter1);


        spCost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                pref.setCostCode(spCost.getSelectedItem().toString().split("-")[0].trim());
                pref.setLocCode(new LocationsController(getActivity()).getRepLocation(pref.getCostCode()));

                Log.v("Cost center>>", pref.getCostCode());
                Log.v("Loccode>>", pref.getLocCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                pref.setCostCode(spCost.getSelectedItem().toString().split("-")[0].trim());
                pref.setLocCode(new LocationsController(getActivity()).getRepLocation(pref.getCostCode()));
            }
        });

        ArrayList<String> repList = new SalRepController(getActivity()).getAllSalesRep();
        repcode = new CustomerController(getActivity()).getCurrentRepCode();

        if (repList.isEmpty()) {
            new SharedPref(getActivity()).setGlobalVal("KeySelectedRep", repcode);
        } else {
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, repList);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spSales_Rep.setAdapter(dataAdapter2);

            spSales_Rep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    new SharedPref(getActivity()).setGlobalVal("KeySelectedRep", spSales_Rep.getSelectedItem().toString().split("-")[0].trim());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        outStandingAmt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.customer_debtor, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Customer outstanding...");
                alertDialogBuilder.setView(promptView);

                final ListView listView = (ListView) promptView.findViewById(R.id.lvCusDebt);
                ArrayList<FddbNote> list = new OutstandingController(getActivity()).getDebtInfo(SharedPref.getInstance(getActivity()).getSelectedDebCode());
                listView.setAdapter(new CustomerDebtAdapter(getActivity(), list));

                alertDialogBuilder.setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });



       // next.postDelayed(new Runnable() {
          //  public void run() {
              next.setVisibility(View.VISIBLE);
          //  }
      //  }, 68000);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lblCustomerName.getText().toString().equals("")) {
                    Log.d("<<<lblCustomerName<<<<", " " + lblCustomerName.getText().toString());
                    Log.d("<<<txtRoute<<<<", " " + txtRoute.getText().toString());

                    Toast.makeText(getActivity(), "Can not proceed without Customer...", Toast.LENGTH_LONG).show();
                    checkdate();
                } else {
                    SaveSalesHeader();
                 }

            }
        });
        Log.d("Header>>", ">>Headeroncreate");
        //OrderHeaderFragment.this.mRefreshHeader();
        return view;
    }
    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mnu_exit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.close:
                if (new OrderDetailController(getActivity()).isAnyActiveOrders()) {

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

                                    pref.setGlobalVal("placeAnOrder", "");
                                    Toast.makeText(getActivity(), "Order discarded successfully..!", Toast.LENGTH_SHORT).show();
                                    Intent intnt = new Intent(context, DebtorDetailsActivity.class);
                                    intnt.putExtra("outlet", outlet);
                                    startActivity(intnt);

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

                } else {
                    MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                            .content("Do you want to back?")
                            .positiveText("Yes")
                            .positiveColor(getResources().getColor(R.color.material_alert_positive_button))
                            .negativeText("No")
                            .negativeColor(getResources().getColor(R.color.material_alert_negative_button))

                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    // Toast.makeText(getActivity(),"Not apply yet",Toast.LENGTH_LONG).show();
                                    Intent back = new Intent(getActivity(), ActivityHome.class);
                                    back.putExtra("outlet", new CustomerController(getActivity()).getSelectedCustomerByCode(SharedPref.getInstance(getActivity()).getSelectedDebCode()));
                                    startActivity(back);
                                    getActivity().finish();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                }

                                @Override
                                public void onNeutral(MaterialDialog dialog) {
                                    super.onNeutral(dialog);
                                }
                            })
                            .build();
                    materialDialog.setCancelable(false);
                    materialDialog.setCanceledOnTouchOutside(false);
                    materialDialog.show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public void SaveSalesHeader() {

        if (lblPreRefno.getText().length() > 0) {
            Order hed = new Order();

            String AppVersion = "";

            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                AppVersion = pInfo.versionName;

            } catch (Exception e) {
                e.printStackTrace();
            }

            Customer cus = new CustomerController(getActivity()).getSelectedCustomerByCode(pref.getSelectedDebCode());

            hed.setFORDHED_REFNO(lblPreRefno.getText().toString());
//             hed.setOrderId(pref.generateOrderId());

            hed.setFORDHED_DEB_CODE(pref.getSelectedDebCode());
            hed.setFORDHED_ADD_DATE(currnentDate.getText().toString());
            hed.setFORDHED_DELV_DATE(deliveryDate.getText().toString());
            hed.setFORDHED_ROUTE_CODE(new RouteDetController(getActivity()).getRouteCodeByDebCode(pref.getSelectedDebCode()));
            hed.setFORDHED_MANU_REF(txtManual.getText().toString());
            hed.setFORDHED_REMARKS(txtRemakrs.getText().toString());
            //hed.setFORDHED_ADD_MACH(prefs_MACID.getString("MAC_Address", "No name defined").toString());
            hed.setFORDHED_ADD_MACH(pref.getMacAddress());
            hed.setFORDHED_ADD_USER("");
            hed.setFORDHED_APP_DATE("2020-09-30");
            hed.setFORDHED_APPSTS("1");
            hed.setFORDHED_APP_USER(pref.getSelectedDebCode());
            hed.setFORDHED_CUR_CODE("LKR");
            hed.setFORDHED_CUR_RATE("1.00");
            hed.setFORDHED_IS_ACTIVE("1");
            hed.setFORDHED_IS_SYNCED("0");
            hed.setFORDHED_LOC_CODE("");
            hed.setFORDHED_CUSADD1(cus.getCusAdd1());
            hed.setFORDHED_CUSADD2(cus.getCusAdd2());
            hed.setFORDHED_CUSADD3(cus.getCusAdd3());
            hed.setFORDHED_CUSTELE(cus.getCusTele());
            hed.setFORDHED_CONTACT(cus.getCusMob());
            hed.setFORDHED_COST_CODE(spCost.getSelectedItem().toString());

            String str = spCost.getSelectedItem().toString();
            String[] arrOfCostCd = str.split("-", 2);

            hed.setFORDHED_COST_CODE(arrOfCostCd[0]);
            hed.setFORDHED_RECORD_ID(AppVersion);
             hed.setFORDHED_START_TIME_SO(startTime);
            hed.setFORDHED_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());
            hed.setFORDHED_LATITUDE("0.00");
            hed.setFORDHED_LONGITUDE("0.00");

            String cst = hed.getFORDHED_COST_CODE();
            String[] arrOfStr = cst.split("-", 2);
            pref.setCostCode(arrOfStr[0].trim());

            hed.setFORDHED_PAYMENT_TYPE("");

            ArrayList<Order> ordHedList = new ArrayList<Order>();
            OrderController ordHedDS = new OrderController(getActivity());
            ordHedList.add(hed);

            activity.selectedPreHed = hed;
            if (ordHedDS.createOrUpdateOrdHed(ordHedList) >= 0) {
                        mSharedPref.setOrdertHeaderNextClicked(true);
                        Toast.makeText(getActivity(), "Order Header Saved...", Toast.LENGTH_LONG).show();
                        preSalesResponseListener.moveNextToFragment(1);

            }
        }
    }

    public void mRefreshHeader()
    {
        if (SharedPref.getInstance(getActivity()).getGlobalVal("PrekeyCustomer").equals("Y")) {
            ArrayList<OrderDetail> dets = new OrderDetailController(getActivity()).getSAForFreeIssueCalc("" + refNo);
            Log.d("Header>>", ">>detsize" + dets.size());

            if (dets.size() > 0 && mSharedPref.getIsQuantityAdded())
            {
                preSalesResponseListener.moveBackToFragment(1);
            }

            currnentDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            txtRemakrs.setEnabled(true);
            lblCustomerName.setText(new CustomerController(getActivity()).getCusNameByCode(pref.getSelectedDebCode()));
            lblPreRefno.setText("" + refNo);

            new SharedPref(getActivity()).setGlobalVal("KeyCost", spCost.getSelectedItem().toString().split("-")[0].trim());
            new SharedPref(getActivity()).setGlobalVal("KeyLoc", new LocationsController(getActivity()).getRepLocation(spCost.getSelectedItem().toString().split("-")[0].trim()));

            if (activity.selectedPreHed != null) {

            }else {

                //SaveSalesHeader();
            }
        }
        else
        {
            txtRemakrs.setEnabled(false);
            txtManual.setEnabled(false);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
        Log.d("Header>>", ">>Headerpause");
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);

        Log.d("Header>>", ">>Headerpause");
    }

    public void onResume() {
        super.onResume();
        checkdate();
        r = new MyReceiver();
        OrderHeaderFragment.this.mRefreshHeader();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_PRE_HEADER"));

        Log.d("Header>>", ">>Headerresume");

    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            OrderHeaderFragment.this.mRefreshHeader();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            preSalesResponseListener = (OrderResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }

    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public void checkdate() {
        int i = android.provider.Settings.Global.getInt(getActivity().getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0);
        /* If option is selected */
        if (i > 0) {
           /* if not selected */
        } else {
            Toast.makeText(getActivity(), "Date is wrong..Please correct!!!", Toast.LENGTH_LONG).show();
            /* Show Date/time settings dialog */
            startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
        }

    }
}

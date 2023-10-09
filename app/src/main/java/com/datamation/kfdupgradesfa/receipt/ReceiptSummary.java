package com.datamation.kfdupgradesfa.receipt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.OrderDetailsAdapter;
import com.datamation.kfdupgradesfa.adapter.OrderFreeIssueDetailAdapter;
import com.datamation.kfdupgradesfa.adapter.ReceiptAdapter;
import com.datamation.kfdupgradesfa.adapter.ReceiptSummaryAdapter;
import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.api.TaskTypeUpload;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.ItemLocController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.OutstandingController;
import com.datamation.kfdupgradesfa.controller.PreProductController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReceiptDetController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.fragment.FragmentTools;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.UploadTaskListener;
import com.datamation.kfdupgradesfa.model.Customer;
import com.datamation.kfdupgradesfa.model.FddbNote;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.model.RecHed;
import com.datamation.kfdupgradesfa.model.ReceiptDet;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.model.apimodel.Result;
import com.datamation.kfdupgradesfa.settings.ReferenceNum;
import com.datamation.kfdupgradesfa.utils.GPSTracker;
import com.datamation.kfdupgradesfa.utils.NetworkUtil;
import com.datamation.kfdupgradesfa.utils.UtilityContainer;
import com.datamation.kfdupgradesfa.view.DebtorDetailsActivity;
import com.datamation.kfdupgradesfa.view.OrderActivity;
import com.datamation.kfdupgradesfa.view.ReceiptActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptSummary extends Fragment implements UploadTaskListener {

    View view;
    TextView lblRecAmt, lblPayMode, lblCHQNo, lblBank, textNo, textBank;
    SharedPref mSharedPref;
    public static SharedPreferences localSP;
    public static final String SETTINGS = "SETTINGS";
    private Customer outlet;
    public SharedPref pref;
    String RefNo = null;
    ListView lv_fddbnote;
    ReceiptActivity activity;
    ArrayList<FddbNote> fddbnoteList;
    GPSTracker gps;
    FloatingActionMenu fam;
    FloatingActionButton fabPause, fabDiscard, fabSave, fabSave_upload;
    MyReceiver r;
    ReceiptActivity mainActivity;
    List<String> resultListPreSale;
    ProgressDialog dialog;
    String EndTime  ;

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sales_management_receipt_summery, container, false);
        mSharedPref = SharedPref.getInstance(getActivity());
        pref = SharedPref.getInstance(getActivity());
        localSP = getActivity().getSharedPreferences(SETTINGS, 0);
        gps = new GPSTracker(getActivity());
        setHasOptionsMenu(true);
        fam = (FloatingActionMenu) view.findViewById(R.id.fab_menu);
        fabPause = (FloatingActionButton) view.findViewById(R.id.fab2);
        fabDiscard = (FloatingActionButton) view.findViewById(R.id.fab3);
        fabSave_upload = (FloatingActionButton) view.findViewById(R.id.fab4);
        fabSave = (FloatingActionButton) view.findViewById(R.id.fab1);
        lblRecAmt = (TextView) view.findViewById(R.id.lblRecAmt);
        lblPayMode = (TextView) view.findViewById(R.id.lblPayMode);
        lblCHQNo = (TextView) view.findViewById(R.id.lblCHQNo);
        lblBank = (TextView) view.findViewById(R.id.lblBank);
        textNo = (TextView) view.findViewById(R.id.textNo);
        textBank = (TextView) view.findViewById(R.id.textBank);
        lv_fddbnote = (ListView) view.findViewById(R.id.lv_order_det);

        mainActivity = (ReceiptActivity) getActivity();

        resultListPreSale = new ArrayList<>();


        //mithsu//
        RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.RecNumVal));
        //mithsu//
        FetchData();
        outlet = new CustomerController(getActivity()).getSelectedCustomerByCode(mSharedPref.getSelectedDebCode());

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });

        fabPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPauseReceipt();
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fddbnoteList = new OutstandingController(getActivity()).getAllRecords(SharedPref.getInstance(getActivity()).getSelectedDebCode(), true);

                if (fddbnoteList.size() > 0) {
                    if (Double.parseDouble(mSharedPref.getGlobalVal("ReckeyRemnant")) <= 0)
                        saveSummaryDialog(getActivity());
                    else
                        Toast.makeText(getActivity(), "Please allocate remaining amount..!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "No records to Save", Toast.LENGTH_LONG).show();
                }

            }
        });

        fabSave_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fddbnoteList = new OutstandingController(getActivity()).getAllRecords(SharedPref.getInstance(getActivity()).getSelectedDebCode(), true);

                if (fddbnoteList.size() > 0) {
                    if (Double.parseDouble(mSharedPref.getGlobalVal("ReckeyRemnant")) <= 0)
                        SaveAndUploadDialog(getActivity());
                    else
                        Toast.makeText(getActivity(), "Please allocate remaining amount..!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "No records to Save", Toast.LENGTH_LONG).show();
                }
            }
        });

        fabDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoEditingData(getActivity());
            }
        });

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        return view;
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        for (int i = 0; i < menu.size(); ++i) {
            menu.removeItem(menu.getItem(i).getItemId());
        }

        inflater.inflate(R.menu.frag_per_sales_summary, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_pre_sales_save) {

            if (Double.parseDouble(mSharedPref.getGlobalVal("ReckeyRemnant")) <= 0)
                saveSummaryDialog(getActivity());
            else
                Toast.makeText(getActivity(), "Please allocate remaining amount..!", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.action_pre_sale_undo) {
            undoEditingData(getActivity());
        }
        return super.onOptionsItemSelected(item);

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*Cancel order*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    private void undoEditingData(final Context context) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Do you want to discard the receipt ?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //mithsu//
                ReceiptHeader.b = true;
                //mithsu//

                ReceiptActivity activity = (ReceiptActivity) getActivity();
                new OutstandingController(getActivity()).ClearFddbNoteData();
                new ReceiptController(getActivity()).CancelReceiptS(RefNo);
                new ReceiptDetController(getActivity()).restDataForMR(RefNo);
                //activity.cusPosition = 0;
                activity.selectedDebtor = null;
                activity.selectedRecHed = null;
                Toast.makeText(getActivity(), "Receipt discarded successfully..!", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
                //    intent.putExtra("outlet", outlet);
                startActivity(intent);
                getActivity().finish();

                //************************************************************************
                //  UtilityContainer.ClearReceiptSharedPref(getActivity());
                //  UtilityContainer.mLoadFragment(new ReceiptInvoice(), getActivity());

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**/

    public void mPauseReceipt() {
//
        if (!mSharedPref.getSelectedDebCode().equals("0") && mSharedPref.getGlobalVal("ReckeyHeader").equals("1")) {

            //***************************************************************** need to undo comment after adding debtor list - 2021/12/02

//            Intent intnt = new Intent(getActivity(), DebtorDetailsActivity.class);
//            intnt.putExtra("outlet", outlet);
//            startActivity(intnt);
//            getActivity().finish();

            //**************************************************************************
        } else
            Toast.makeText(activity, "Select Customer/Fill in header details before Pause", Toast.LENGTH_SHORT).show();
//        if (new ReceiptDetController(getActivity()).getItemCount(RefNo) > 0) {
//            Intent intnt = new Intent(getActivity(),DebtorDetailsActivity.class);
//            startActivity(intnt);
//            getActivity().finish();
//        } else
//            Toast.makeText(activity, "Add details before pause ...!", Toast.LENGTH_SHORT).show();

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*Clear Shared preference-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    /* Clear shared preference */
    public void ClearSharedPref() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("ReckeyPayModePos");
        editor.remove("ReckeyPayMode");
        editor.remove("ReckeyRemnant");
        editor.remove("ReckeyRecAmt");
        editor.remove("ReckeyCHQNo");
        editor.remove("ReckeyCustomer");
        editor.remove("ReckeyHeader");
        editor.remove("ReckeyCusCode");
        editor.remove("receipAmtChanged");
        editor.commit();

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**/

    public void FetchData() {
        // if (((ReceiptActivity) getActivity()).selectedDebtor != null) {
        lv_fddbnote.setAdapter(null);
        fddbnoteList = new OutstandingController(getActivity()).getAllRecords(SharedPref.getInstance(getActivity()).getSelectedDebCode(), true);
        lv_fddbnote.setAdapter(new ReceiptSummaryAdapter(getActivity(), fddbnoteList, true, RefNo));
        // }
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**/

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Save primary & secondary invoice-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*/

    private void saveSummaryDialog(final Context context) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Do you want to save the receipt ?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(final DialogInterface dialog, int id) {

                //mithsu//
                ReceiptHeader.b = true;
                //mithsu/

                //TimeZone tz = TimeZone.getTimeZone("UTC +5:30");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
                //df.setTimeZone(tz);
                EndTime  = df.format(new Date());

                ReceiptHed recHed = new ReceiptHed();
                recHed.setFPRECHED_LATITUDE(gps.getLatitude() + "");
                recHed.setFPRECHED_LONGITUDE(gps.getLongitude() + "");
//                recHed.setFPRECHED_START_TIME(localSP.getString("Rec_Start_Time", ""));
                recHed.setFPRECHED_START_TIME(new ReceiptController(context).getActiveRecHed().getFPRECHED_START_TIME());
                recHed.setFPRECHED_END_TIME(EndTime);
                recHed.setFPRECHED_ADDRESS("None");
                recHed.setFPRECHED_COSTCODE(mSharedPref.getGlobalVal("PrekeyCost"));
                recHed.setFPRECHED_STATUS("NOT SYNCED");
                new ReceiptController(getActivity()).UpdateRecHed(recHed, RefNo);
                final ReceiptActivity activity = (ReceiptActivity) getActivity();

                ArrayList<ReceiptDet> RecList = new ArrayList<>();

                for (FddbNote fddb : fddbnoteList) {

                    ReceiptDet recDet = new ReceiptDet();
                    recDet.setFPRECDET_REFNO(RefNo);
                    recDet.setFPRECDET_BAMT(String.valueOf(Double.parseDouble(fddb.getFDDBNOTE_TOT_BAL()) - Double.parseDouble(fddb.getFDDBNOTE_ENTER_AMT())));
                    recDet.setFPRECDET_AMT(String.valueOf(Double.parseDouble(fddb.getFDDBNOTE_TOT_BAL()) - Double.parseDouble(fddb.getFDDBNOTE_ENTER_AMT())));
                    //    recDet.setFPRECDET_BAMT(fddb.getFDDBNOTE_ENTER_AMT());
                    //  recDet.setFPRECDET_AMT(fddb.getFDDBNOTE_ENTER_AMT());
                    recDet.setFPRECDET_ALOAMT(fddb.getFDDBNOTE_ENTER_AMT());
                    recDet.setFPRECDET_SALEREFNO(fddb.getFDDBNOTE_REFNO());
                    recDet.setFPRECDET_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());
                    recDet.setFPRECDET_DCURCODE("LKR");
                    recDet.setFPRECDET_DCURRATE("1.0");
                    recDet.setFPRECDET_DTXNDATE(fddb.getFDDBNOTE_TXN_DATE());
                    recDet.setFPRECDET_DTXNTYPE(fddb.getFDDBNOTE_TXN_TYPE());
                    recDet.setFPRECDET_TXNDATE(currentDate());
                    recDet.setFPRECDET_TXNTYPE("21");
                    recDet.setFPRECDET_REFNO1(fddb.getFDDBNOTE_REFNO());
                    recDet.setFPRECDET_MANUREF("");
                    recDet.setFPRECDET_OCURRATE("1.00");
                    recDet.setFPRECDET_OVPAYAMT("0.00");
                    recDet.setFPRECDET_OVPAYBAL("0.00");
                    //   recDet.setFPRECDET_OVPAYAMT(String.valueOf(Double.parseDouble(fddb.getFDDBNOTE_TOT_BAL()) - Double.parseDouble(fddb.getFDDBNOTE_ENTER_AMT())));
                    //   recDet.setFPRECDET_OVPAYBAL(String.valueOf(Double.parseDouble(fddb.getFDDBNOTE_TOT_BAL()) - Double.parseDouble(fddb.getFDDBNOTE_ENTER_AMT())));
                    recDet.setFPRECDET_RECORDID("");
                    recDet.setFPRECDET_TIMESTAMP("");
                    recDet.setFPRECDET_ISDELETE("0");
                    recDet.setFPRECDET_REMARK(fddb.getFDDBNOTE_ENT_REMARK());
                    recDet.setFPRECDET_DEBCODE(SharedPref.getInstance(getActivity()).getSelectedDebCode());
                    RecList.add(recDet);
                }

                new ReceiptDetController(getActivity()).createOrUpdateRecDetS(RecList);
                new OutstandingController(getActivity()).UpdateFddbNoteBalance(fddbnoteList);
                new ReceiptController(getActivity()).InactiveStatusUpdate(RefNo);

                //new ReceiptPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Print preview", RefNo);

                //  activity.cusPosition = 0;
                activity.selectedDebtor = null;
                activity.selectedRecHed = null;
                activity.ReceivedAmt = 0.00;
                new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.RecNumVal));

                /*-*-*-*-*-*-*-*-*-*-*-Check if deadline passed-*-*-*-*-*-*-*-*-*-*-*/

                Toast.makeText(getActivity(), "Receipt saved successfully..!", Toast.LENGTH_SHORT).show();
                //    UtilityContainer.mLoadFragment(new ReceiptInvoice(), getActivity());


                dialog.dismiss();
                ClearSharedPref();/* Clear shared preference */

                Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
                //  intent.putExtra("outlet", outlet);
                startActivity(intent);
                getActivity().finish();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    private String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*/

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        return true;
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_SUMMARY"));
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void mRefreshHeader() {

        ReceiptHed RCHed = new ReceiptController(getActivity()).getReceiptByRefno(RefNo);
        lblPayMode.setText(mSharedPref.getGlobalVal("ReckeyPayMode").toString());
        ReceiptActivity activity = (ReceiptActivity) getActivity();

        if (mSharedPref.getGlobalVal("ReckeyPayModePos").equals("0")) {
            lblBank.setText("N/A");
            lblCHQNo.setText("N/A");
        } else if (mSharedPref.getGlobalVal("ReckeyPayModePos").equals("1")) {
            lblBank.setText("N/A");
            lblCHQNo.setText("N/A");
        } else if (mSharedPref.getGlobalVal("ReckeyPayModePos").equals("2")) {
            textBank.setText("BANK - BRANCH : ");
            ///  if(RCHed.getFPRECHED_CUSBANK() != null)
            // {
            lblBank.setText(mSharedPref.getGlobalVal("KeyBankCode") + " - " + mSharedPref.getGlobalVal("KeyBranchCode"));
            // lblBank.setText(RCHed.getFPRECHED_CUSBANK().toString());
            lblCHQNo.setText(mSharedPref.getGlobalVal("ReckeyCHQNo"));
            //  }
        } else if (mSharedPref.getGlobalVal("ReckeyPayModePos").equals("3")) {
            textBank.setText("CARD TYPE : ");
            textNo.setText("CREDIT CARD NO : ");
            if (RCHed.getFPRECHED_CUSBANK() != null) {
                lblBank.setText(RCHed.getFPRECHED_CUSBANK().toString());
                lblCHQNo.setText(mSharedPref.getGlobalVal("ReckeyCHQNo"));
            }
        } else if (mSharedPref.getGlobalVal("ReckeyPayModePos").equals("4")) {
            textBank.setVisibility(View.GONE);
            lblBank.setVisibility(View.GONE);

            textNo.setText("DEPOSIT NO : ");
            lblCHQNo.setText(mSharedPref.getGlobalVal("ReckeyCHQNo"));
        } else {
            textBank.setVisibility(View.GONE);
            lblBank.setVisibility(View.GONE);

            textNo.setText("DRAFT NO : ");
            lblCHQNo.setText(mSharedPref.getGlobalVal("ReckeyCHQNo"));
        }

        if (!mSharedPref.getGlobalVal("ReckeyRecAmt").equals("") || !mSharedPref.getGlobalVal("ReckeyRecAmt").equals("0") || !mSharedPref.getGlobalVal("ReckeyRecAmt").equals("***"))
            lblRecAmt.setText(String.format("%,.2f", Double.parseDouble(mSharedPref.getGlobalVal("ReckeyRecAmt").replaceAll(",", ""))));

        FetchData();
    }

    @Override
    public void onTaskCompleted(TaskTypeUpload taskType, List<String> list) {

    }

    @Override
    public void onTaskCompleted(List<String> list) {

    }




    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ReceiptSummary.this.mRefreshHeader();
        }
    }



    public void SaveAndUploadDialog(final Context context) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Do you want to save the receipt ?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(final DialogInterface dialog, int id) {


                //mithsu//
                ReceiptHeader.b = true;
                //mithsu//

                //TimeZone tz = TimeZone.getTimeZone("UTC +5:30");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
                //df.setTimeZone(tz);
                EndTime  = df.format(new Date());

                ReceiptHed recHed = new ReceiptHed();
                recHed.setFPRECHED_LATITUDE(gps.getLatitude() + "");
                recHed.setFPRECHED_LONGITUDE(gps.getLongitude() + "");
//                recHed.setFPRECHED_START_TIME(localSP.getString("Rec_Start_Time", ""));
                recHed.setFPRECHED_START_TIME(new ReceiptController(context).getActiveRecHed().getFPRECHED_START_TIME());
                recHed.setFPRECHED_END_TIME(EndTime);
                recHed.setFPRECHED_ADDRESS("None");
                recHed.setFPRECHED_COSTCODE(mSharedPref.getGlobalVal("PrekeyCost"));
                recHed.setFPRECHED_STATUS("NOT SYNCED");

                new ReceiptController(getActivity()).UpdateRecHed(recHed, RefNo);
                final ReceiptActivity activity = (ReceiptActivity) getActivity();

                ArrayList<ReceiptDet> RecList = new ArrayList<>();

                for (FddbNote fddb : fddbnoteList) {

                    ReceiptDet recDet = new ReceiptDet();
                    recDet.setFPRECDET_REFNO(RefNo);
                    recDet.setFPRECDET_BAMT(String.valueOf(Double.parseDouble(fddb.getFDDBNOTE_TOT_BAL()) - Double.parseDouble(fddb.getFDDBNOTE_ENTER_AMT())));
                    recDet.setFPRECDET_AMT(String.valueOf(Double.parseDouble(fddb.getFDDBNOTE_TOT_BAL()) - Double.parseDouble(fddb.getFDDBNOTE_ENTER_AMT())));
                    //    recDet.setFPRECDET_BAMT(fddb.getFDDBNOTE_ENTER_AMT());
                    //  recDet.setFPRECDET_AMT(fddb.getFDDBNOTE_ENTER_AMT());
                    recDet.setFPRECDET_ALOAMT(fddb.getFDDBNOTE_ENTER_AMT());
                    recDet.setFPRECDET_SALEREFNO(fddb.getFDDBNOTE_REFNO());
                    recDet.setFPRECDET_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());
                    recDet.setFPRECDET_DCURCODE("LKR");
                    recDet.setFPRECDET_DCURRATE("1.0");
                    recDet.setFPRECDET_DTXNDATE(fddb.getFDDBNOTE_TXN_DATE());
                    recDet.setFPRECDET_DTXNTYPE(fddb.getFDDBNOTE_TXN_TYPE());
                    recDet.setFPRECDET_TXNDATE(currentDate());
                    recDet.setFPRECDET_TXNTYPE("21");
                    recDet.setFPRECDET_REFNO1(fddb.getFDDBNOTE_REFNO());
                    recDet.setFPRECDET_MANUREF("");
                    recDet.setFPRECDET_OCURRATE("1.00");
                    recDet.setFPRECDET_OVPAYAMT("0.00");
                    recDet.setFPRECDET_OVPAYBAL("0.00");
                    //   recDet.setFPRECDET_OVPAYAMT(String.valueOf(Double.parseDouble(fddb.getFDDBNOTE_TOT_BAL()) - Double.parseDouble(fddb.getFDDBNOTE_ENTER_AMT())));
                    //   recDet.setFPRECDET_OVPAYBAL(String.valueOf(Double.parseDouble(fddb.getFDDBNOTE_TOT_BAL()) - Double.parseDouble(fddb.getFDDBNOTE_ENTER_AMT())));
                    recDet.setFPRECDET_RECORDID("");
                    recDet.setFPRECDET_TIMESTAMP("");
                    recDet.setFPRECDET_ISDELETE("0");
                    recDet.setFPRECDET_REMARK(fddb.getFDDBNOTE_ENT_REMARK());
                    recDet.setFPRECDET_DEBCODE(SharedPref.getInstance(getActivity()).getSelectedDebCode());
                    RecList.add(recDet);
                }

                new ReceiptDetController(getActivity()).createOrUpdateRecDetS(RecList);
                new OutstandingController(getActivity()).UpdateFddbNoteBalance(fddbnoteList);
                new ReceiptController(getActivity()).InactiveStatusUpdate(RefNo);
                new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.RecNumVal));

                activity.selectedDebtor = null;
                activity.selectedRecHed = null;
                activity.ReceivedAmt = 0.00;


                /*-*-*-*-*-*-*-*-*-*-*-Check if deadline passed-*-*-*-*-*-*-*-*-*-*-*/

                Toast.makeText(getActivity(), "Receipt saved successfully..!", Toast.LENGTH_SHORT).show();


                Upload(new ReceiptController(getActivity()).getAllUnsyncedReceiptHed());

//                    dialog.dismiss();
                ClearSharedPref();/* Clear shared preference */



            }

        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();


    }

    public void Upload(final ArrayList<RecHed> Receipt) {

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Uploading Receipt records");
        dialog.show();


        // new OrderController(getActivity()).updateIsActive(""+mSharedPref.generateOrderId(),"2");
        if (NetworkUtil.isNetworkAvailable(getActivity()) && NetworkUtil.isNotPoorConnection(getActivity())) {
            if (Receipt.size() > 0) {

                for (final RecHed rec : Receipt) {
                    try {
                        String content_type = "application/json";
                        ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
                        final Handler mHandler = new Handler(Looper.getMainLooper());
                        JsonParser jsonParser = new JsonParser();
                        String recJson = new Gson().toJson(rec);
                        JsonObject objectFromString = jsonParser.parse(recJson).getAsJsonObject();
                        JsonArray jsonArray = new JsonArray();
                        jsonArray.add(objectFromString);
// menaka
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
                                    Log.d( ">>response"+status,result+">>"+rec.getRefNo() );
                                    if(result){
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                rec.setIsSync("1");
                                                rec.setIsActive("0");
                                                new ReceiptController(getActivity()).updateIsSyncedReceipt(rec.getRefNo(), "0","1","SYNCED");

                                                Toast.makeText(getActivity(), "Receipt Upload successfully..!", Toast.LENGTH_LONG).show();

                                                addRefNoResults(rec.getRefNo() + " --> Success\n", Receipt.size());
                                            }
                                        });
                                    }else{
                                        rec.setIsSync("0");
                                        rec.setIsActive("1");
                                        new ReceiptController(getActivity()).updateIsSyncedReceipt(rec.getRefNo(), "1","0","NOT SYNCED");

                                        Toast.makeText(getActivity(), "Receipt Upload Failed.", Toast.LENGTH_LONG).show();
                                        addRefNoResults(rec.getRefNo() + " --> Failed\n", Receipt.size());
                                    }
                                }else {
                                    Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
                                    Intent intnt = new Intent(getActivity(), DebtorDetailsActivity.class);
                                    startActivity(intnt);
                                    getActivity().finish();
                                }// this will tell you why your api doesnt work most of time


//                                if (response != null && response.body() != null) {
//                                    int status = response.code();
//                                    Log.d(">>>response code", ">>>res " + status);
//                                    Log.d(">>>response message", ">>>res " + response.message());
//                                    Log.d(">>>response body", ">>>res " + response.body());
//                              //      int resLength = response.body().trim().length();
//                                    String resmsg = "" + response.body();
//                                    if (status == 200 && !resmsg.equals("") && !resmsg.equals(null) ) {
//                                        mHandler.post(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                rec.setIsSync("1");
//                                                rec.setIsActive("0");
//                                                new ReceiptController(getActivity()).updateIsSyncedReceipt(rec.getRefNo(), "0","1","SYNCED");
//
//                                                Toast.makeText(getActivity(), "Receipt Upload successfully..!", Toast.LENGTH_LONG).show();
//
//                                                addRefNoResults(rec.getRefNo() + " --> Success\n", Receipt.size());
//                                            }
//                                        });
//                                    } else {
//                                        rec.setIsSync("0");
//                                        rec.setIsActive("1");
//                                        new ReceiptController(getActivity()).updateIsSyncedReceipt(rec.getRefNo(), "1","0","NOT SYNCED");
//
//                                        Toast.makeText(getActivity(), "Receipt Upload Failed.", Toast.LENGTH_LONG).show();
//                                        addRefNoResults(rec.getRefNo() + " --> Failed\n", Receipt.size());
//                                    }
//                                } else {
//                                    Toast.makeText(getActivity(), " Invalid response when order upload", Toast.LENGTH_LONG).show();
//                                    Intent intnt = new Intent(getActivity(), DebtorDetailsActivity.class);
//                                    startActivity(intnt);
//                                    getActivity().finish();
//                                }

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
        } else
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();

    }


    private void addRefNoResults(String ref, int count) {
        dialog.dismiss();
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle("Upload Receipt Summary");

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


}

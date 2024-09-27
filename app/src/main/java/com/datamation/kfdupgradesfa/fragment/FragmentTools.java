package com.datamation.kfdupgradesfa.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.downloadListAdapter;
import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.api.TaskTypeUpload;
import com.datamation.kfdupgradesfa.controller.AreaController;
import com.datamation.kfdupgradesfa.controller.BankController;
import com.datamation.kfdupgradesfa.controller.BrandController;
import com.datamation.kfdupgradesfa.controller.CompanyDetailsController;
import com.datamation.kfdupgradesfa.controller.CostController;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.DayNPrdDetController;
import com.datamation.kfdupgradesfa.controller.DayNPrdHedController;
import com.datamation.kfdupgradesfa.controller.DownloadController;
import com.datamation.kfdupgradesfa.controller.FInvhedL3Controller;
import com.datamation.kfdupgradesfa.controller.FinvDetL3Controller;
import com.datamation.kfdupgradesfa.controller.FreeDebController;
import com.datamation.kfdupgradesfa.controller.FreeDetController;
import com.datamation.kfdupgradesfa.controller.FreeHedController;
import com.datamation.kfdupgradesfa.controller.FreeItemController;
import com.datamation.kfdupgradesfa.controller.FreeMslabController;
import com.datamation.kfdupgradesfa.controller.GroupController;
import com.datamation.kfdupgradesfa.controller.ItemController;
import com.datamation.kfdupgradesfa.controller.ItemLocController;
import com.datamation.kfdupgradesfa.controller.ItemPriceController;
import com.datamation.kfdupgradesfa.controller.LocationsController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.OutstandingController;
import com.datamation.kfdupgradesfa.controller.PreProductController;
import com.datamation.kfdupgradesfa.controller.ReasonController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReceiptDetController;
import com.datamation.kfdupgradesfa.controller.ReferenceDetailDownloader;
import com.datamation.kfdupgradesfa.controller.ReferenceSettingController;
import com.datamation.kfdupgradesfa.controller.RepDebtorController;
import com.datamation.kfdupgradesfa.controller.RepGPSLocationController;
import com.datamation.kfdupgradesfa.controller.RouteController;
import com.datamation.kfdupgradesfa.controller.RouteDetController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.controller.SupplierController;
import com.datamation.kfdupgradesfa.controller.TownController;
import com.datamation.kfdupgradesfa.controller.TypeController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialogUpdated;
import com.datamation.kfdupgradesfa.dialog.StockInquiryDialog;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.UploadTaskListener;
import com.datamation.kfdupgradesfa.model.Control;
import com.datamation.kfdupgradesfa.model.NonPrdHed;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.OrderHed;
import com.datamation.kfdupgradesfa.model.RecHed;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.model.RepGPS;
import com.datamation.kfdupgradesfa.model.RepGpsLoc;
import com.datamation.kfdupgradesfa.model.SalRep;
import com.datamation.kfdupgradesfa.model.User;
import com.datamation.kfdupgradesfa.model.apimodel.ReadJsonList;
import com.datamation.kfdupgradesfa.nonproductive.UploadNonProd;
import com.datamation.kfdupgradesfa.order.UploadPreSales;
import com.datamation.kfdupgradesfa.receipt.UploadReceipt;
import com.datamation.kfdupgradesfa.settings.TaskTypeDownload;
import com.datamation.kfdupgradesfa.upload.UploadDailyRepLocations;
import com.datamation.kfdupgradesfa.utils.NetworkUtil;
import com.datamation.kfdupgradesfa.utils.UtilityContainer;
import com.datamation.kfdupgradesfa.view.ActivityHome;
import com.datamation.kfdupgradesfa.view.DebtorDetailsActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***@Auther - rashmi**/

public class FragmentTools extends Fragment implements View.OnClickListener, UploadTaskListener {

    private Context context;
    User loggedUser;
    View view;
    int count = 0;
    Animation animScale;
    ImageView imgSync, imgUpload, imgPrinter, imgDatabase, imgStockDown, imgStockInq, imgSalesRep, imgTour, imgDayExp, imgImage, imgVideo;
    NetworkFunctions networkFunctions;
    SharedPref pref;
    List<String> resultList;
    LinearLayout layoutTool;
    private long timeInMillis;
    private Handler mHandler;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    ApiInterface apiInterface;
    ArrayList<Control> downloadList;
    SharedPref mSharedPref;

    boolean isAnyActiveImages = false;
    boolean isAnyActiveVideos = false;
    boolean isImageFitToScreen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_management_tools, container, false);
        pref = SharedPref.getInstance(getActivity());
        context = getActivity();

        animScale = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale);
        layoutTool = (LinearLayout) view.findViewById(R.id.layoutTool);
        imgTour = (ImageView) view.findViewById(R.id.imgTourInfo);
        imgStockInq = (ImageView) view.findViewById(R.id.imgStockInquiry);
        imgSync = (ImageView) view.findViewById(R.id.imgSync);
        imgUpload = (ImageView) view.findViewById(R.id.imgUpload);
        imgStockDown = (ImageView) view.findViewById(R.id.imgDownload);
        imgPrinter = (ImageView) view.findViewById(R.id.imgPrinter);
        imgDatabase = (ImageView) view.findViewById(R.id.imgSqlite);
        imgSalesRep = (ImageView) view.findViewById(R.id.imgSalrep);
        imgDayExp = (ImageView) view.findViewById(R.id.imgDayExp);
        imgImage = (ImageView) view.findViewById(R.id.imgImage);
        imgVideo = (ImageView) view.findViewById(R.id.imgVideo);
        mHandler = new Handler(Looper.getMainLooper());

//        isAnyActiveImages = new InvDetController(getActivity()).isAnyActiveOrders();
//        isAnyActiveVideos = new ReceiptDetController(getActivity()).isAnyActiveReceipt();
//
//        if (isAnyActiveImages) {
//            imgImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_image));
//        } else {
//            imgImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_image));
//        }
//
//        if (isAnyActiveVideos) {
//            imgVideo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_video));
//        } else {
//            imgVideo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_video));
//        }
        networkFunctions = new NetworkFunctions(getActivity());
        imgTour.setOnClickListener(this);
        imgStockInq.setOnClickListener(this);
        imgSync.setOnClickListener(this);
        imgUpload.setOnClickListener(this);
        imgStockDown.setOnClickListener(this);
        imgPrinter.setOnClickListener(this);
        imgDatabase.setOnClickListener(this);
        imgSalesRep.setOnClickListener(this);
        imgDayExp.setOnClickListener(this);
        imgImage.setOnClickListener(this);
        imgVideo.setOnClickListener(this);
        resultList = new ArrayList<>();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        timeInMillis = System.currentTimeMillis();

        Log.d("FRAGMENT_TOOL", "IMAGE_FLAG: " + pref.getImageFlag());


//        if (fmc.getAllMediaforCheckIfIsExist("IMG") > 0) {
//            imgImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_img_notification));
//        } else {
//            imgImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_image));
        //       }

//        if (fmc.getAllMediaforCheckIfIsExist("VDO") > 0) {
//            imgVideo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_video_notification));
//        } else {
//            imgVideo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_video));
//        }

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgTourInfo:
                imgTour.startAnimation(animScale);
                UtilityContainer.mLoadFragment(new FragmentMarkAttendance(), getActivity());
                break;

            case R.id.imgStockInquiry:
                imgStockInq.startAnimation(animScale);//
                new StockInquiryDialog(getActivity());
                break;

            case R.id.imgSync:
                imgSync.startAnimation(animScale);
                Log.d("Validate Secondary Sync", ">>Mac>> " + pref.getMacAddress().trim() + " >>URL>> " + pref.getBaseURL() + " >>DB>> " + pref.getDistDB());
                secondSync();
                break;

            case R.id.imgUpload:
                imgUpload.startAnimation(animScale);
                uploadDialog(getActivity());
                break;

            case R.id.imgDownload:
                imgStockDown.startAnimation(animScale);
                UtilityContainer.mLoadFragment(new FragmentCategoryWiseDownload(), getActivity());
                break;

            case R.id.imgPrinter:
                imgPrinter.startAnimation(animScale);
                UtilityContainer.mPrinterDialogbox(getActivity());
                break;

            case R.id.imgSqlite:
                imgDatabase.startAnimation(animScale);
                UtilityContainer.mSQLiteDatabase(getActivity());
                break;

            case R.id.imgSalrep:
                imgSalesRep.startAnimation(animScale);
                ViewRepProfile();
                break;

            case R.id.imgDayExp:
                imgDayExp.startAnimation(animScale);
                break;

            case R.id.imgImage:
                imgImage.startAnimation(animScale);
                // imgUrlList = fmc.getAllMediafromDb("IMG");
                // ViewImageList();
                break;

            case R.id.imgVideo:
                // imgVideo.startAnimation(animScale);
                // vdoUrlList = fmc.getAllMediafromDb("VDO");
                UtilityContainer.mPrinterDialogbox(getActivity());
                //ViewVideoList();
                break;

        }

    }


    public void ViewRepProfile() {
        final Dialog repDialog = new Dialog(getActivity());
        repDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        repDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        repDialog.setCancelable(false);
        repDialog.setCanceledOnTouchOutside(false);
        repDialog.setContentView(R.layout.rep_profile);

        //initializations
        TextView repname = (TextView) repDialog.findViewById(R.id.repname);
        final TextView repcode = (TextView) repDialog.findViewById(R.id.repcode);
        final TextView repPrefix = (TextView) repDialog.findViewById(R.id.repPrefix);
        // final TextView locCode = (TextView) repDialog.findViewById(R.id.target);
        final Spinner spConnection = (Spinner) repDialog.findViewById(R.id.connection);
        final TextView areaCode = (TextView) repDialog.findViewById(R.id.areaCode);
        final TextView dealCode = (TextView) repDialog.findViewById(R.id.dealclode);
        //  areaCode.setText(loggedUser.getRoute());
        final SalRep rep = new SalRepController(getActivity()).getSaleRepDet(new SalRepController(getActivity()).getCurrentRepCode());
        ArrayList<String> splist = new ArrayList<>();
        splist.add("SLT");

        splist.add("Dialog");
        final ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, splist);
        spConnection.setAdapter(dataAdapter3);
        repname.setText(rep.getFREP_NAME());
        repcode.setText(rep.getFREP_CODE());
        repPrefix.setText(rep.getFREP_PREFIX());
        //  locCode.setText("0");

//        repemail.setText(rep.getEMAIL());
//        dealCode.setText(rep.getDEALCODE());
        spConnection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spConnection.getSelectedItemPosition()==0){
                    pref.setBaseURL("http://124.43.5.227:1030");
                    pref.setConnectionName("Mobitel Connection");
                }else{
                    pref.setBaseURL("http://123.231.15.146:1030");
                    pref.setConnectionName("Dialog Connection");
                }
               // Log.d(">>>Selected Conn",">>>"+pref.getBaseURL());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(spConnection.getSelectedItemPosition()==0){
                    pref.setBaseURL("http://124.43.5.227:1030");
                    pref.setConnectionName("Mobitel Connection");
                }else{
                    pref.setBaseURL("http://123.231.15.146:1030");
                    pref.setConnectionName("Dialog Connection");
                }
               // Log.d(">>>Selected Conn",">>>"+pref.getBaseURL());
            }
        });


        //close
        repDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityHome.class);
                startActivity(intent);
                getActivity().finish();
                repDialog.dismiss();
            }
        });


        repDialog.show();
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    private void uploadDialog(final Context context) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .content("Are you sure, Do you want to Upload Data?")
                .positiveColor(ContextCompat.getColor(context, R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(context, R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        if (NetworkUtil.isNetworkAvailable(context))
                        {
//                            if (NetworkUtil.isNotPoorConnection(context))
//                            {
//                                if (isAnyActiveTransactions())
//                                {
//                                    showActiveTransAlert("You have partially saved transactions. Do you want to discard them?", "Partial Data");
//                                }
//                                else
//                                {
//                                    uploadRecords();
//                                }
//                            }
//                            else
//                            {
//                                networkWarning("Unable to upload due to poor network", "Poor network", context);
//                            }
                            new GetUploadSpeed().execute("https://mobitel.lk");
                        }
                        else
                        {
                            networkWarning("Unable to upload due to no internet", "No internet", context);
                        }
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

    @SuppressLint("LongLogTag")
    private boolean isAnyActiveTransactions()
    {
        Order activeOrder = new OrderController(context).getActiveOrdHed();
        RecHed activeReceipt = new ReceiptController(context).getActiveReceiptHed();
        NonPrdHed activeNP = new DayNPrdHedController(context).getActiveNP();

        if (activeOrder.getFORDHED_REFNO() != null || activeReceipt.getRefNo() != null || activeNP.getRefNo() != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @SuppressLint("LongLogTag")
    private void uploadRecords()
    {
        try
        {
            final ArrayList<OrderHed> ordHedList = new OrderController(context).getAllUnSyncOrdHedNew(new SalRepController(getActivity()).getCurrentRepCode());
            ArrayList<RecHed> receiptlist = new ReceiptController(getActivity()).getAllUnsyncedReceiptHed(new SalRepController(getActivity()).getCurrentRepCode());
            final ArrayList<NonPrdHed> npHedList = new DayNPrdHedController(getActivity()).getUnSyncedData(new SalRepController(getActivity()).getCurrentRepCode());
            ArrayList<RepGpsLoc> repGpsLoc = new RepGPSLocationController(context).getUnSyncedGPSLocData();

            if (receiptlist.size() <= 0 && ordHedList.size() <= 0 && repGpsLoc.size() <= 0 && npHedList.size() <= 0)
            {
                Toast.makeText(getActivity(), "No Records to upload !", Toast.LENGTH_LONG).show();
            } else {

                try {
                    new UploadPreSales(getActivity(), FragmentTools.this, TaskTypeUpload.UPLOAD_ORDER).execute(ordHedList);
                    Log.v(">>8>>", "UploadPreSales execute finish");
                } catch (Exception e) {
                    Log.e("***", "onPositive: ", e);
                }

                try {
                    new UploadReceipt(getActivity(), FragmentTools.this, TaskTypeUpload.UPLOAD_RECEIPT).execute(receiptlist);
                    Log.v(">>8>>", "Upload receipt execute finish");
                } catch (Exception e) {
                    Log.v("Exception in sync receipt", e.toString());
                }
                try {
                    new UploadNonProd(getActivity(), FragmentTools.this, TaskTypeUpload.UPLOAD_NONPROD).execute(npHedList);
                    Log.v(">>8>>", "Upload nonproductive execute finish");
                } catch (Exception e) {
                    Log.v("Exception in sync nonproductive", e.toString());
                    Log.e("***", "onPositive: ", e);
                }

                try {
                    Log.v(">>8>>", "Upload daily rep location execute finish");
                } catch (Exception e) {
                    Log.v("Exception in sync rep location", e.toString());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showActiveTransAlert(String message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.info);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                discardActiveTransactions();
                dialog.cancel();
            }
            })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void discardActiveTransactions()
    {
        Order activeOrder = new OrderController(context).getActiveOrdHed();
        RecHed activeReceipt = new ReceiptController(context).getActiveReceiptHed();
        NonPrdHed activeNP = new DayNPrdHedController(context).getActiveNP();

        if (activeOrder.getFORDHED_REFNO() != null){
//            if(new OrderController(context).deleteActiveOrderHed(activeOrder.getFORDHED_REFNO())>0)
//            {
//                new OrderController(context).deleteActiveOrderDet(activeOrder.getFORDHED_REFNO());
//            }
            discardOrderData(activeOrder.getFORDHED_REFNO());
        }

        if (activeReceipt.getRefNo() != null)
        {
//            if(new ReceiptController(context).deleteActiveReceiptHed(activeReceipt.getRefNo())>0)
//            {
//                new ReceiptController(context).deleteActiveReceiptDet(activeReceipt.getRefNo());
//                Toast.makeText(context, "Active Receipt discard successfully", Toast.LENGTH_LONG).show();
//            }
            discardRecData(activeReceipt.getRefNo());
        }

        if (activeNP.getRefNo() != null)
        {
//            if(new DayNPrdHedController(context).deleteActiveNPHed(activeNP.getRefNo())>0)
//            {
//                new DayNPrdHedController(context).deleteActiveNPDet(activeNP.getRefNo());
//                Toast.makeText(context, "Active NP discard successfully", Toast.LENGTH_LONG).show();
//            }
            discardNPData(activeNP.getRefNo());
        }
    }

    private void syncMasterDataDialog(final Context context) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .content("Are you sure, Do you want to Sync Master Data?")
                .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
                        if (connectionStatus == true) {
                            //  if (isAllUploaded()) {
                            dialog.dismiss();
                            try {
                                new secondarySync(pref.getUserId()).execute();
                                SharedPref.getInstance(getActivity()).setGlobalVal("SyncDate", dateFormat.format(new Date(timeInMillis)));
                            } catch (Exception e) {
                                Log.e("## ErrorIn2ndSync ##", e.toString());
                            }
//                            } else {
//                                Toast.makeText(context, "Please Upload All Transactions", Toast.LENGTH_LONG).show();
//                            }
                        } else {
                            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
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

    private boolean isAllUploaded() {
        Boolean allUpload = false;


        return allUpload;
    }

    @Override
    public void onTaskCompleted(TaskTypeUpload taskType, List<String> list) {
        resultList.addAll(list);
        switch (taskType) {
            case UPLOAD_ORDER: {
                DayNPrdHedController npHed = new DayNPrdHedController(getActivity());
                final ArrayList<NonPrdHed> npHedList = npHed.getUnSyncedData(new SalRepController(getActivity()).getCurrentRepCode());
                if (npHedList.size() > 0) {
                    Toast.makeText(getActivity(), "Nonproductive data upload completed..!", Toast.LENGTH_LONG).show();
                }
//                new UploadNonProd(getActivity(), FragmentTools.this, TaskTypeUpload.UPLOAD_NONPROD).execute(npHedList);

                Log.v(">>upload>>", "Upload non productive execute finish");
            }
            break;
            case UPLOAD_NONPROD: {

                ArrayList<RecHed> receiptlist = new ReceiptController(getActivity()).getAllUnsyncedReceiptHed(new SalRepController(getActivity()).getCurrentRepCode());
                if (receiptlist.size() > 0) {
                    Toast.makeText(getActivity(), "Receipt data upload completed..!", Toast.LENGTH_LONG).show();
                }
//                new UploadReceipt(getActivity(), FragmentTools.this, TaskTypeUpload.UPLOAD_RECEIPT).execute(receiptlist);

                Log.v(">>upload>>", "Upload receipt execute finish");


            }
            break;
////            case UPLOAD_ATTENDANCE: {
//                CustomerAttendanceController attendanceController = new CustomerAttendanceController(getActivity());
//                ArrayList<CustomerAttendance> cusAttendList = attendanceController.getUnsyncedAttendanceData();
//                if (cusAttendList.size() > 0) {
//                    Toast.makeText(getActivity(), "Customer attendance data upload completed..!", Toast.LENGTH_LONG).show();
//                }
//                new UploadCusAttendance(getActivity(), FragmentTools.this, cusAttendList, TaskType.UPLOAD_CUS_ATTENDANCE).execute();
//                Log.v(">>upload>>", "Upload customer attendance execute finish");
//            }
//            break;
//            case UPLOAD_CUS_ATTENDANCE: {
//                SalRepController salRepController = new SalRepController(getActivity());
//                ArrayList<SalRep> saleRep = salRepController.getAllUnsyncSalrep(new SalRepController(getActivity()).getCurrentRepCode());
//                if (saleRep.size() > 0) {
//                    Toast.makeText(getActivity(), "repemail data upload completed..!", Toast.LENGTH_LONG).show();
//                }
//                new UploadSalRef(getActivity(), FragmentTools.this, saleRep, TaskType.UPLOAD_ATTENDANCE).execute(saleRep);
//                Log.v(">>upload>>", "Upload repemail execute finish");
//            }
//            case UPLOAD_SALREP:{
//                DayExpHedController exHed = new DayExpHedController(getActivity());
//                final ArrayList<DayExpHed> exHedList = exHed.getUnSyncedData();//8
//                if (exHedList.size() > 0) {
//                    Toast.makeText(getActivity(), "Expense data upload completed..!", Toast.LENGTH_LONG).show();
//                }
//                new UploadExpenses(getActivity(), FragmentTools.this, TaskType.UPLOAD_RECEIPT).execute(exHedList);
//                Log.v(">>upload>>", "Upload expense execute finish");
//            }
//            break;
//            case UPLOAD_EXPENSE:{
//                CustomerController customerDS = new CustomerController(getActivity());
//                ArrayList<Debtor> debtorlist = customerDS.getAllDebtorsToCordinatesUpdate();//5
//                new UploadDebtorCordinates(getActivity(), FragmentTools.this,debtorlist, TaskType.UPLOAD_RECEIPT).execute(debtorlist);
//                Log.v(">>upload>>", "Upload DebtorCordinates execute finish");
//            }
//            break;
//            case UPLOAD_COORDINATES:{
//                CustomerController customerDS = new CustomerController(getActivity());
//                ArrayList<Debtor> imgDebtorList = customerDS.getAllImagUpdatedDebtors();//7
//                new UploadDebtorImges(getActivity(), FragmentTools.this, imgDebtorList,TaskType.UPLOAD_RECEIPT).execute();
//                Log.v(">>upload>>", "Upload imgDebtor execute finish");
//            }
//            break;
//            case UPLOAD_IMAGES:{
//                NewCustomerController customerDS = new NewCustomerController(getActivity());
//                ArrayList<NewCustomer> newCustomersList = customerDS.getAllNewCustomersForSync();
//                new UploadNewCustomer(getActivity(), FragmentTools.this, newCustomersList,TaskType.UPLOAD_NEWCUS).execute();
//                Log.v(">>upload>>", "Upload NewCustomer execute finish");
//            }
//            break;
//            case UPLOAD_NEWCUS:{
//                CustomerController customerDS = new CustomerController(getActivity());
//                ArrayList<Debtor> updExistingDebtors = customerDS.getAllUpdatedDebtors();
//                new UploadEditedDebtors(getActivity(), FragmentTools.this, updExistingDebtors,TaskType.UPLOAD_EDTCUS).execute();
//                Log.v(">>upload>>", "Upload EditedDebtors execute finish");
//            }
//            break;
//            case UPLOAD_EDTCUS:{
//                ArrayList<SalRep> fblist = new ArrayList<>();
//
//                SalRep salRep = new SalRep();
//                salRep.setCONSOLE_DB(SharedPref.getInstance(context).getConsoleDB().trim());
//                salRep.setDIST_DB(SharedPref.getInstance(context).getDistDB().trim());
//                salRep.setRepCode(SharedPref.getInstance(context).getLoginUser().getCode());
//                salRep.setFirebaseTokenID(SharedPref.getInstance(context).getFirebaseTokenKey());
//                fblist.add(salRep);
//                new UploadFirebaseTokenKey(getActivity(), FragmentTools.this, fblist,TaskType.UPLOAD_TKN).execute();
//                Log.v(">>upload>>", "Upload FirebaseToken execute finish");
//
//            }
//            break;
//            case UPLOAD_TKN:{
//                Log.v(">>upload>>", "all upload finish");
//
//               // resultList.addAll(list);
//                String msg = "";
//                for (String s : resultList) {
//                    msg += s;
//                }
//                resultList.clear();
//                mUploadResult(msg);
//            }
//            break;
//            case UPLOAD_RECEIPT: {
//                ReceiptController rece = new ReceiptController(getActivity());
//                ArrayList<ReceiptHed> collectedOutstanding = rece.getAllUnsyncedRecHed();
//                if (collectedOutstanding.size() > 0) {
//                    Toast.makeText(getActivity(), "Receipt data upload completed..!", Toast.LENGTH_LONG).show();
//                }
//                new UploadReceipt(getActivity(), FragmentTools.this, collectedOutstanding).execute();
//                Log.v(">>upload>>", "Upload Receipt execute finish");
//            }
            default:
                break;
        }
    }

    @Override
    public void onTaskCompleted(List<String> list) {

    }

    //**********************secondary sysnc start***********************************************/
    private class secondarySync extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;
        private String repcode;
        private List<String> errors = new ArrayList<>();
        private Handler mHandler;

        public secondarySync(String repcode) {
            this.repcode = repcode;
            this.pdialog = new CustomProgressDialog(getActivity());
            mHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            mHandler = new Handler(Looper.getMainLooper());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Authenticating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
            mHandler = new Handler(Looper.getMainLooper());
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {
                    new DownloadController(getActivity()).deleteAll();
                    new OrderController(context).AutoDataClearingOrder();
                    new OrderDetailController(context).AutoDataClearingOrderDetail();

                    Log.d(">>>>>***", "isinstaled: "+pref.getIsFirstInstallation());
//                    if (pref.getIsFirstInstallation().equals(false)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pdialog.setMessage("Downloading Outsatnding...");
                            }
                        });

                        // Processing fddbnote
                        try {
                            OutstandingController outstandingController = new OutstandingController(getActivity());
                            outstandingController.deleteAll();
                            UtilityContainer.download(getActivity(), TaskTypeDownload.fddbnote, networkFunctions.getFddbNotes(repcode));
                        } catch (IOException e) {
                            e.printStackTrace();
                            errors.add(e.toString());
                            throw e;
                        }
//                    }

                   /*****************company details**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading data(Company details)...");
                        }
                    });
                    // Processing controls
                    try {
                        new CompanyDetailsController(getActivity()).deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Controllist, networkFunctions.getCompanyDetails(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }


                /*****************SalRep**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading SalRep...");
                        }
                    });

                    try {
                        SalRepController salRepController = new SalRepController(getActivity());
                        salRepController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.SalRep, networkFunctions.getSalRep(repcode.trim(), pref.getUserPwd().trim()));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************outlets**********************************************************************/

               //     if (pref.getIsFirstInstallation().equals(false)) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pdialog.setMessage("Downloading Customers...");
                            }
                        });

                        try {
                            CustomerController customerController = new CustomerController(getActivity());
                            customerController.deleteAll();
                            UtilityContainer.download(getActivity(), TaskTypeDownload.Customers, networkFunctions.getCustomer(repcode));
                        } catch (Exception e) {
                            errors.add(e.toString());
                            throw e;
                        }
                 //   }
                    /*****************Settings*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Settings...");
                        }
                    });
                    // Processing company settings
                    try {
                        ReferenceSettingController settingController = new ReferenceSettingController(getActivity());
                        settingController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Settings, networkFunctions.getReferenceSettings());
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Branches*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading data (reference details)...");
                        }
                    });


                    // Processing Branches
                    try {
                        ReferenceDetailDownloader branchController = new ReferenceDetailDownloader(getActivity());
                        branchController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Reference, networkFunctions.getReferences(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Types - *************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Types....");
                        }
                    });

                    // Processing Types
                    try {
                        TypeController typeController = new TypeController(getActivity());
                        typeController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Type, networkFunctions.getTypes());
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }


                    /*****************Items*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (item details)...");
                        }
                    });

                    // Processing item
                    try {
                        Log.d("mithsu", "ddd" + pref.getUserId());
                        ItemController itemController = new ItemController(getActivity());
                        itemController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Items, networkFunctions.getItems(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************ItemLoc*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (item loc details)...");
                        }
                    });

                    // Processing item loc
                    try {
                        ItemLocController itemLocController = new ItemLocController(getActivity());
                        itemLocController.deleteAllItemLoc();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.ItemLoc, networkFunctions.getItemLocations(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************reasons**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Items downloaded\nDownloading reasons...");
                        }
                    });
                    // Processing reasons

                    try {
                        ReasonController reasonController = new ReasonController(getActivity());
                        reasonController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Reason, networkFunctions.getReasons());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************ItemPri**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Items downloaded\nDownloading Item Pri...");
                        }
                    });
                    // Processing Item Pri

                    try {
                        ItemPriceController itemPriceController = new ItemPriceController(getActivity());
                        itemPriceController.deleteAllItemPri();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.ItemPri, networkFunctions.getItemPrices(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading banks...");
                        }
                    });
                    /*****************banks**********************************************************************/

                    try {
                        BankController bankController = new BankController(getActivity());
                        bankController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Bank, networkFunctions.getBanks());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Route**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading route details...");
                        }
                    });
                    // Processing route

                    try {
                        RouteController routeController = new RouteController(getActivity());
                        routeController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Route, networkFunctions.getRoutes(repcode));
                    } catch (Exception e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Cost**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Cost...");
                        }
                    });
                    // Processing Cost

                    try {
                        CostController costController = new CostController(getActivity());
                        costController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Cost, networkFunctions.getCost(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Supplier**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Supplier...");
                        }
                    });

                    // Processing Supplier

                    try {
                        SupplierController supplierController = new SupplierController(getActivity());
                        supplierController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Supplier, networkFunctions.getSupplier(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }


                    /*****************RepDebtor**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Rep Allocated Debtors...");
                        }
                    });

                    // Processing RepDebtors

                    try {
                        RepDebtorController repDebtorController = new RepDebtorController(getActivity());
                        repDebtorController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.RepDebtor, networkFunctions.getRepDebtor(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Route det**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading route details...");
                        }
                    });

                    // Processing route
                    try {
                        RouteDetController routeDetController = new RouteDetController(getActivity());
                        routeDetController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.RouteDet, networkFunctions.getRouteDets(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Area - kaveesha - 2020/10/05 ****************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Area Details...");
                        }
                    });

                    // Processing area
                    try {
                        AreaController areaController = new AreaController(getActivity());
                        areaController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Area, networkFunctions.getArea());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                   /***************** Locations - kaveesha - 2020/10/05 *****************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Locations Details...");
                        }
                    });

                    // Processing locations
                    try {
                        LocationsController locationsController = new LocationsController(getActivity());
                        locationsController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Locations, networkFunctions.getLocations(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }


                    /*****************Towns - kaveesha - 2020/10/05 *******************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Towns...");
                        }
                    });

                    // Processing towns
                    try {
                        TownController townController = new TownController(getActivity());
                        townController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Towns, networkFunctions.getTowns());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Groups - kaveesha - 2020/10/05 *****************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Groups...");
                        }
                    });

                    // Processing group
                    try {
                        GroupController groupController = new GroupController(getActivity());
                        groupController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Groups, networkFunctions.getGroups());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Brands - kaveesha - 2020/10/06 *****************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Brands...");
                        }
                    });

                    // Processing brands
                    try {
                        BrandController brandController = new BrandController(getActivity());
                        brandController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Brand, networkFunctions.getBrands());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }


//                    /*****************Freeslab**********************************************************************/
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            pdialog.setMessage("Processing downloaded data (free)...");
//                        }
//                    });
//                    // Processing freeslab
//                    try {
//
//                        FreeSlabController freeSlabController = new FreeSlabController(getActivity());
//                        freeSlabController.deleteAll();
//                        UtilityContainer.download(getActivity(),TaskTypeDownload.Freeslab, networkFunctions.getFreeSlab());
//                    } catch (Exception e) {
//                        errors.add(e.toString());
//                        throw e;
//                    }

                    /*****************freeMslab**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });
                    // Processing freeMslab
                    try {

                        FreeMslabController freemSlabController = new FreeMslabController(getActivity());
                        freemSlabController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Freemslab, networkFunctions.getFreeMslab());
                    } catch (Exception e) {
                        errors.add(e.toString());

                        throw e;
                    }

                    /*****************FreeHed**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });
                    // Processing freehed
                    try {

                        FreeHedController freeHedController = new FreeHedController(getActivity());
                        freeHedController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Freehed, networkFunctions.getFreeHed(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());

                        throw e;
                    }

                    /*****************Freedet**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });

                    // Processing freedet
                    try {

                        FreeDetController freeDetController = new FreeDetController(getActivity());
                        freeDetController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Freedet, networkFunctions.getFreeDet(repcode));

                    } catch (Exception e) {
                        errors.add(e.toString());

                        throw e;
                    }
//
                    /*****************freedeb**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });

                    // Processing freedeb
                    try {

                        FreeDebController freeDebController = new FreeDebController(getActivity());
                        freeDebController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Freedeb, networkFunctions.getFreeDebs(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************freeItem**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });


                    // Processing freeItem
                    try {

                        FreeItemController freeItemController = new FreeItemController(getActivity());
                        freeItemController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Freeitem, networkFunctions.getFreeItems());
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }


                    /*****************invHedL3**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (Last three invoices hed)...");
                        }
                    });

                    try {

                        FInvhedL3Controller finvHedl3Controller = new FInvhedL3Controller(getActivity());
                        finvHedl3Controller.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.invL3hed, networkFunctions.getLastThreeInvHed(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************invDetL3**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (Last three invoices det)...");
                        }
                    });

                    try {

                        FinvDetL3Controller finvdetl3Controller = new FinvDetL3Controller(getActivity());
                        finvdetl3Controller.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.invL3det, networkFunctions.getLastThreeInvDet(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Completed...");
                        }
                    });

                    /*****************end sync**********************************************************************/
                    return true;
                } else {
                    errors.add("SharedPref.getInstance(getActivity()).getLoginUser() = null OR !SharedPref.getInstance(getActivity()).isLoggedIn()");
                    Log.d("ERROR>>>>>", "Login USer" + SharedPref.getInstance(getActivity()).getLoginUser().toString() + " IS LoggedIn --> " + SharedPref.getInstance(getActivity()).isLoggedIn());
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                errors.add(e.toString());

                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing data");
            pdialog.setMessage("Download Completed..");

            downloadList = new ArrayList<>();
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();

                    int i = 1;
                    for (Control c : new DownloadController(getActivity()).getAllDownload()) {
                        downloadList.add(c);
                        i++;
                    }

                    if (downloadList.size() > 0) {
                        mDownloadResult(downloadList);
                    }
                }
                pref.setSuccessSyncedStatus(true);
                showErrorText("Successfully Synchronized");
            } else {


                if (pdialog.isShowing()) {
                    pdialog.dismiss();

                    int i = 1;
                    for (Control c : new DownloadController(getActivity()).getAllDownload()) {
                        downloadList.add(c);
                        i++;
                    }

                    if (downloadList.size() > 0) {
                        mDownloadResult(downloadList);
                    }
                }

//                StringBuilder sb = new StringBuilder();
//                if (errors.size() == 1) {
//                    sb.append(errors.get(0));
//                    showErrorText(sb.toString());
//                } else if (errors.size() == 0) {
//                    sb.append("Following errors occurred");
//                    for (String error : errors) {
//                        sb.append("\n - ").append(error);
//                        showErrorText(sb.toString());
//                    }
//                }

                pref.setSuccessSyncedStatus(false);
                ArrayList<Control> list = new ArrayList<Control>();

                Control aControl = new Control();

                aControl.setFCONTROL_DOWNLOAD_TITLE("Sync failed. Please try again..!");
                aControl.setFCONTROL_DOWNLOADCOUNT("");
                aControl.setFCONTROL_DOWNLOADEDCOUNT("");

                list.add(aControl);

                Log.d("%%************", "onPostExecute: sync fail");
                mDownloadResult(list);

            }

        }
    }

    private void showErrorText(String s) {
        Toast.makeText(getActivity(), "" + s, Toast.LENGTH_LONG).show();

    }

    /////////////***********************secondory sync finish***********************************/
    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    public void mUploadResult(String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("Upload Summary");

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private class Validate extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;
        private String macId, url, db;

        public Validate(String macId, String url, String db) {
            this.macId = macId;
            this.url = url;
            this.db = db;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Validating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {

            try {

                try {
                    ApiInterface apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
                    Call<ReadJsonList> resultCall = apiInterface.getSalRepResult(pref.getDistDB(), macId);
                    resultCall.enqueue(new Callback<ReadJsonList>() {
                        @Override
                        public void onResponse(Call<ReadJsonList> call, Response<ReadJsonList> response) {
                            ArrayList<SalRep> repList = new ArrayList<SalRep>();
                            for (int i = 0; i < response.body().getSalRepResult().size(); i++) {
                                repList.add(response.body().getSalRepResult().get(i));
                            }

                            if (repList.size() > 0) {
                                networkFunctions.setUser(repList.get(0));
                                pref.storeLoginUser(repList.get(0));
                            }

                        }

                        @Override
                        public void onFailure(Call<ReadJsonList> call, Throwable t) {
                            Log.d(">>>Error in failure", t.toString());
                        }
                    });

                    pref = SharedPref.getInstance(getActivity());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Authenticated...");
                        }
                    });

                    return true;

                } catch (Exception e) {
                    Log.e("networkFunctions ->", "IOException -> " + e.toString());
                    throw e;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pdialog.isShowing())
                pdialog.cancel();
            // pdialog.cancel();
            if (result) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                syncMasterDataDialog(getActivity());
            } else {
                Toast.makeText(getActivity(), "Invalid Mac Id", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void mDownloadResult(ArrayList<Control> downlodaList) {
        final Dialog sDialog = new Dialog(getActivity());
        sDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sDialog.setCancelable(false);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.setContentView(R.layout.download_dialog);

        ListView download_list = (ListView) sDialog.findViewById(R.id.download_listview);
//        ArrayList<Control> downlodaList = new DownloadController(getActivity()).getAllDownload();

        download_list.setAdapter(new downloadListAdapter(getActivity(), downlodaList));


        sDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
                downloadList.clear();
            }
        });

        sDialog.show();
    }

    public void networkWarning(String message, String title, Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
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

    private void navigateToNext()
    {
        Intent intnt = new Intent(getActivity(), ActivityHome.class);
        startActivity(intnt);
        getActivity().finish();
    }

    public void discardOrderData(String RefNo)
    {
        mSharedPref = new SharedPref(context);
        int result = new OrderController(getActivity()).restData(RefNo);

        if (result > 0) {
            new OrderDetailController(getActivity()).restData(RefNo);
            new PreProductController(getActivity()).mClearTables();
            mSharedPref.setDiscountClicked("0");
            mSharedPref.setOrdertHeaderNextClicked(false);
            mSharedPref.setIsQuantityAdded(false);
            mSharedPref.setOrdertHeaderNextClicked(false);
            Toast.makeText(context, "Active Order discard successfully", Toast.LENGTH_LONG).show();
        }
    }

    public void discardRecData(String RefNo)
    {
        new OutstandingController(getActivity()).ClearFddbNoteData();
        new ReceiptController(getActivity()).CancelReceiptS(RefNo);
        new ReceiptDetController(getActivity()).restDataForMR(RefNo);

        Toast.makeText(getActivity(), "Active Receipt discard successfully", Toast.LENGTH_LONG).show();
    }

    public void discardNPData(String RefNo)
    {
        try {
            new DayNPrdHedController(getActivity()).undoOrdHedByID(RefNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new DayNPrdDetController(getActivity()).OrdDetByRefno(RefNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Active NP discard successfully", Toast.LENGTH_LONG).show();
    }

    private void secondSync()
    {
        try {
            if (NetworkUtil.isNetworkAvailable(getActivity()))
            {
                if (isAnyActiveTransactions())
                {
                    Toast.makeText(getActivity(), "Please discard or complete all partially saved data", Toast.LENGTH_LONG).show();
                }
                else if (isAnyUploadPendingTransactions())
                {
                    Toast.makeText(getActivity(), "Please upload all transactions", Toast.LENGTH_LONG).show();
                }
                else
                {
                    syncMasterDataDialog(getActivity());
                }
            }
            else
            {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(">>>> Secondary Sync", e.toString());
        }
    }

    @SuppressLint("LongLogTag")
    private boolean isAnyUploadPendingTransactions()
    {
        //not uploaded status -
        // order isSync = '1', receipt isSync = '0', np isSync = '0'
        int ordHedListCount = new OrderController(getActivity()).getAllDayBeforeUnSyncOrdHed(new SalRepController(getActivity()).getCurrentRepCode());
        int receiptlistCount = new ReceiptController(getActivity()).getAllDayBeforeUnSyncRecHedCount(new SalRepController(getActivity()).getCurrentRepCode());
        int npHedListCount = new DayNPrdHedController(getActivity()).getAllDayBeforeUnSyncNonPrdCount(new SalRepController(getActivity()).getCurrentRepCode());

        if (ordHedListCount > 0 || receiptlistCount > 0 || npHedListCount > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private class GetUploadSpeed extends AsyncTask<String, Void, Double> {

        CustomProgressDialogUpdated pdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdialog = new CustomProgressDialogUpdated(getActivity(), "Upload Speed fetching...", "Upload Speed fetched");
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setInitMessage();
            pdialog.show();

        }

        public GetUploadSpeed() {
            this.pdialog = new CustomProgressDialogUpdated(getActivity(), "Upload Speed fetching...", "Upload Speed fetched");
        }

        @Override
        protected Double doInBackground(String... params) {
            String uploadUrl = params[0];
            try {
                // Generate a small dummy data to upload
                byte[] data = new byte[1024]; // 1 KB of data

                // Start time
                long startTime = System.currentTimeMillis();

                // Open connection to the upload URL
                URL url = new URL(uploadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/octet-stream");

                // Upload the data
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();

                // Get response code
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // End time
                    long endTime = System.currentTimeMillis();

                    // Calculate upload time in seconds
                    double uploadTime = (endTime - startTime) / 1000.0;

                    // Calculate upload speed in KBps
                    double uploadSpeedKBps = 1024 / uploadTime;

                    // Convert KBps to Mbps
                    double uploadSpeedMbps = (uploadSpeedKBps * 8) / 1024;

                    return uploadSpeedMbps;
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Double result) {
            if (result != null)
            {
                Log.wtf("Upload speed: ", "" + result + " MBps");
                Toast.makeText(getActivity(), "Upload speed: "+ result + " MBps", Toast.LENGTH_LONG).show();
                pdialog.setExitMessage();

                if (result > 5.0)
                {
                    try {
                        if (pdialog.isShowing()) {
                            pdialog.dismiss();
                        }

                        if (isAnyActiveTransactions())
                        {
                            showActiveTransAlert("You have partially saved transactions. Do you want to discard them?", "Partial Data");
                        }
                        else
                        {
                            uploadRecords();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    if (pdialog.isShowing()) {
                        pdialog.dismiss();
                    }
                    networkWarning("Unable to upload due to poor network", "Poor network", context);
                }
            } else {
                Toast.makeText(getActivity(), "Failed to measure upload speed", Toast.LENGTH_LONG).show();
                navigateToNext();
            }
        }
    }
}

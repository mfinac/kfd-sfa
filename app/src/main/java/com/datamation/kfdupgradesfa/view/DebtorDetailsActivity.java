package com.datamation.kfdupgradesfa.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import at.markushi.ui.CircleButton;

import com.astuetz.PagerSlidingTabStrip;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.controller.BaseUrlController;
import com.datamation.kfdupgradesfa.controller.CompanyDetailsController;
import com.datamation.kfdupgradesfa.controller.DayNPrdDetController;
import com.datamation.kfdupgradesfa.controller.DownloadController;
import com.datamation.kfdupgradesfa.controller.FInvhedL3Controller;
import com.datamation.kfdupgradesfa.controller.FItenrDetController;
import com.datamation.kfdupgradesfa.controller.FItenrHedController;
import com.datamation.kfdupgradesfa.controller.FinvDetL3Controller;
import com.datamation.kfdupgradesfa.controller.OutstandingController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReceiptDetController;
import com.datamation.kfdupgradesfa.controller.ReferenceController;
import com.datamation.kfdupgradesfa.controller.ReferenceDetailDownloader;
import com.datamation.kfdupgradesfa.controller.ReferenceSettingController;
import com.datamation.kfdupgradesfa.controller.RepDebtorController;
import com.datamation.kfdupgradesfa.controller.RouteController;
import com.datamation.kfdupgradesfa.controller.RouteDetController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.fragment.FragmentCategoryWiseDownload;
import com.datamation.kfdupgradesfa.fragment.debtordetails.CompetitorDetailsFragment;
import com.datamation.kfdupgradesfa.fragment.debtordetails.HistoryDetailsFragment;
import com.datamation.kfdupgradesfa.fragment.debtordetails.OutstandingDetailsFragment;
import com.datamation.kfdupgradesfa.fragment.debtordetails.PersonalDetailsFragment;
import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Control;
import com.datamation.kfdupgradesfa.model.Customer;
import com.datamation.kfdupgradesfa.model.Debtor;
import com.datamation.kfdupgradesfa.model.Route;
import com.datamation.kfdupgradesfa.model.RouteDet;
import com.datamation.kfdupgradesfa.model.SalRep;
import com.datamation.kfdupgradesfa.settings.TaskTypeDownload;
import com.datamation.kfdupgradesfa.utils.GPSTracker;
import com.datamation.kfdupgradesfa.utils.LocationProvider;
import com.datamation.kfdupgradesfa.utils.UtilityContainer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DebtorDetailsActivity extends AppCompatActivity {

    private CircleButton floatingActionsMenu;
    private CircleButton fabInvoice, fabUnproductive, fabReturnNote, fabSalesOrder, fabVansale, fabendcall;
    private TextView labelInvoice, labelUnproductive, labelReturnNote, labelSalesOrder, labelVanSale, labelMenu, lblend;

    private Debtor outlet;
    private Context context;

    private View overlay;

    private boolean famOpen = false;

    private DatabaseHelper dbHandler;
    private SharedPref sharedPref;
    NetworkFunctions networkFunctions;
    private boolean invoiced = false;

    private PersonalDetailsFragment personalDetailsFragment;
    private OutstandingDetailsFragment outstandingDetailsFragment;
    private HistoryDetailsFragment historyDetailsFragment;
    private CompetitorDetailsFragment competitorDetailsFragment;
    // private NearDebtorFragment nearDebtorFragment;

    private LocationManager locManager;

    private int[] famDisplayIntervals = {0, 100, 200, 300, 400, 500, 600, 700, 800, 900};

    private SalRep user;
    boolean isAnyActiveOrders = false;
    boolean isAnyActiveReturns = false;
    boolean isAnyActiveNonProds = false;
    boolean isAnyActiveInvoices = false;
    boolean isAnyActiveReceipt = false;

    private String retRefNo = "";

    GPSTracker gpsTracker;
    double lati = 0.0;
    double longi = 0.0;
    private String debCode;
    private double currentLatitude, currentLongitude;
    private Customer customer;
    private LocationProvider locationProvider;
    private static final String LOG_TAG = DebtorDetailsActivity.class.getSimpleName();
    private Location finalLocation;
    private int locType;
    private double locationAccuracy;


    private long capturedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtor_details);

        dbHandler = new DatabaseHelper(this);
        sharedPref = SharedPref.getInstance(DebtorDetailsActivity.this);
        // new BluetoothConnectionHelper(this).enableBluetooth(this);
        user = sharedPref.getLoginUser();
        context = this;
        gpsTracker = new GPSTracker(context);
        debCode = sharedPref.getSelectedDebCode();
        setTitle("CUSTOMER DETAILS (APP VERSION-"+getVersionCode()+") "+ new BaseUrlController(context).getActiveConnectionName(context));

        Intent dataIntent = getIntent();
        if (dataIntent.hasExtra("outlet")) {
           // outlet = (Debtor) dataIntent.getExtras().get("outlet");
            if (outlet == null) {
                //Toast.makeText(DebtorDetailsActivity.this, "Error receiving the outlet. Please try again.", Toast.LENGTH_SHORT).show();
                onBackPressed();

            }
        } else {
           // Toast.makeText(DebtorDetailsActivity.this, "Error receiving the outlet. Please try again.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // outlet = new Customer();
        // outlet.setCusName(SharedPref.getInstance(getApplicationContext()).getSelectedDebName());

        /****************************************Rashmi 2019-11-13******************************************************/

        locationProvider = new LocationProvider((LocationManager) getSystemService(Context.LOCATION_SERVICE),
                new LocationProvider.ICustomLocationListener() {

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.d(LOG_TAG, "Provider enabled");
                        locationProvider.startLocating();
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.d(LOG_TAG, "Provider disabled");
                        locationProvider.stopLocating();
                    }

                    @Override
                    public void onUnableToGetLocation() {
                        Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onGotLocation(Location location, int locationType) {
                        if (location != null) {
                            finalLocation = location;
                            SharedPref.getInstance(context).setGlobalVal("Longitude", String.valueOf(finalLocation.getLongitude()));
                            SharedPref.getInstance(context).setGlobalVal("Latitude", String.valueOf(finalLocation.getLatitude()));
                            Log.d("FROMLocationProvidLongi", ">>>" + String.valueOf(finalLocation.getLongitude()));
                            Log.d("FROMLocationProvidLati", ">>>" + String.valueOf(finalLocation.getLongitude()));

                            locType = locationType;
                            locationAccuracy = location.getAccuracy();

                            capturedTime = System.currentTimeMillis();

                            String type;
                            if (locType == LocationProvider.LOCATION_TYPE_GPS) {
                                type = "GPS";
                            } else if (locType == LocationProvider.LOCATION_TYPE_NETWORK) {
                                type = "Network";
                            } else {
                                type = "Undefined";
                            }
                        }
                    }

                    @Override
                    public void onProgress(int type) {
                        if (type == LocationProvider.LOCATION_TYPE_GPS) {
                            Log.d(LOG_TAG, "Getting location (GPS)");
//                            tvProgress.setVisibility(View.VISIBLE);
//                            tvProgress.setText("Getting location (GPS)");
//                            progressWheel.spin();
                        } else {
                            Log.d(LOG_TAG, "Getting location (Network)");
//                            tvProgress.setVisibility(View.VISIBLE);
//                            tvProgress.setText("Getting location (Network)");
//                            progressWheel.spin();
                        }
                    }
                });
        try {
            locationProvider.setOnGPSTimeoutListener(new LocationProvider.OnGPSTimeoutListener() {
                @Override
                public void onGPSTimeOut() {

                    MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                            .content("Please move to a more clear location to get GPS")
                            .positiveText("Try Again")
                            .positiveColor(getResources().getColor(R.color.material_alert_neutral_button))
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    locationProvider.startLocating();
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
            }, 0);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        /****************************************Rashmi 2019-11-13******************************************************/

        Toolbar toolbar = (Toolbar) findViewById(R.id.outlet_details_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText(sharedPref.getSelectedDebName());

        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.outlet_details_tab_strip);
        ViewPager viewPager = (ViewPager) findViewById(R.id.outlet_details_viewpager);


//        if (new SalesReturnController(context).getDirectSalesReturnRefNo().equals(""))
//        {
//            retRefNo = new ReferenceNum(context).getCurrentRefNo(getResources().getString(R.string.salRet));
//        }
//        else
//        {
//            retRefNo = new SalesReturnController(context).getDirectSalesReturnRefNo();
//        }

        // isAnyActiveOrders = new OrderDetailController(getApplicationContext()).isAnyActiveOrders();
        //isAnyActiveReturns = new SalesReturnDetController(getApplicationContext()).isAnyActiveReturnHedDet(retRefNo);
        //  isAnyActiveInvoices = new InvDetController(getApplicationContext()).isAnyActiveOrders();
        isAnyActiveReceipt = new ReceiptDetController(getApplicationContext()).isAnyActiveReceipt(debCode);
        isAnyActiveNonProds = new DayNPrdDetController(getApplicationContext()).isAnyActiveNPs();

        fabVansale = (CircleButton) findViewById(R.id.outlet_details_fab_van_sale);
        fabInvoice = (CircleButton) findViewById(R.id.outlet_details_fab_receipt);
        fabUnproductive = (CircleButton) findViewById(R.id.outlet_details_fab_non_productive);
        fabReturnNote = (CircleButton) findViewById(R.id.outlet_details_fab_sales_return);
        fabSalesOrder = (CircleButton) findViewById(R.id.outlet_details_fab_pre_sale);
        fabendcall = (CircleButton) findViewById(R.id.outlet_details_fab_endcall);
        floatingActionsMenu = (CircleButton) findViewById(R.id.outlet_details_floating_action_menu);
        floatingActionsMenu.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.right_arrow));

        labelInvoice = (TextView) findViewById(R.id.label_outlet_details_receipt);
        lblend = (TextView) findViewById(R.id.label_outlet_details_endcl);
        labelUnproductive = (TextView) findViewById(R.id.outlet_details_label_non_productive);
        labelReturnNote = (TextView) findViewById(R.id.labelReturn);
        labelVanSale = (TextView) findViewById(R.id.outlet_details_label_van_sale);
        labelSalesOrder = (TextView) findViewById(R.id.outlet_details_label_pre_sale);
        labelMenu = (TextView) findViewById(R.id.outlet_details_label_menu);

        // Setting the expanded options button drawables
        if (isAnyActiveOrders) {
            fabSalesOrder.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.presale_active));
        } else {
            fabSalesOrder.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.ic_sales));
        }

//        if (isAnyActiveReturns)
//        {
//            fabReturnNote.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.returns_active));
//        }
//        else {
//            fabReturnNote.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.ic_return));
//        }

//        if (isAnyActiveNonProds)
//        {
//            fabUnproductive.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.nonprod_pending));
//        }
//        else
//        {
        fabUnproductive.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.ic_nonprod));
        //}
        fabendcall.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.remove));

        if (isAnyActiveReceipt)
            fabInvoice.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.receipt_active));
        else
            fabInvoice.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.ic_receipt));

//        if(isAnyActiveInvoices)
//            fabVansale.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.vansale_active));
//        else
//            fabVansale.setImageDrawable(ContextCompat.getDrawable(DebtorDetailsActivity.this, R.drawable.ic_vansales));

        // The overlay when showing expanding the menu
        overlay = findViewById(R.id.outlet_details_view_overlay);
        overlay.setAlpha(0);

        //openFAM();
        floatingActionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (famOpen) {
                    closeFAM();
                } else {
                    openFAM();
                }
            }
        });
        // Collapsing the FAM when pressed on the overlay
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (famOpen) {
                    closeFAM();
                }
            }
        });

        OutletDetailsPagerAdapter adapter = new OutletDetailsPagerAdapter(getSupportFragmentManager());
        // GPSOutletDetailsPagerAdapter gpsAdapter = new GPSOutletDetailsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        Log.d("DEBTOR_DETAILS", "IS_GPS_DEBTOR:-" + sharedPref.getGPSDebtor());

        // AY - Alldebtorgps Yes, RY - Routedebtorgps Yes, AN - Alldebtorgps No, RN - Routedebtorgps No

//        if (sharedPref.getGPSDebtor().equals("AY") || sharedPref.getGPSDebtor().equals("RY"))
//        {
//            viewPager.setAdapter(adapter);
//        }
//        else if(sharedPref.getGPSDebtor().equals("AN") || sharedPref.getGPSDebtor().equals("RN"))
//        {
//            viewPager.setAdapter(gpsAdapter);
//        }

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);

        slidingTabStrip.setViewPager(viewPager);
        slidingTabStrip.setTextColor(getResources().getColor(android.R.color.black));
        slidingTabStrip.setIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        slidingTabStrip.setDividerColor(getResources().getColor(R.color.half_black));


        fabUnproductive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeFAM();

                // Only proceed if location service is available
                if (locationServiceEnabled()) {
                    Intent intent = new Intent(DebtorDetailsActivity.this, NonProductiveActivity.class);
                    intent.putExtra("outlet", outlet);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service to proceed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fabInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeFAM();

                // Only proceed if location service is available
                if (locationServiceEnabled()) {

                    // Toast.makeText(DebtorDetailsActivity.this, "Please wait. This may take a while", Toast.LENGTH_SHORT).show();
//                   if(!new CustomerController(DebtorDetailsActivity.this).getCustomerStatus(debCode).equals("")) {
                    if (new OutstandingController(getApplicationContext()).getDebtorBalance(debCode) > 0 || new OutstandingController(getApplicationContext()).getDebtorBalance(debCode) < 0) {
                        try {
                            if (new RouteController(getApplicationContext()).getRouteCount() > 0 && new RouteDetController(getApplicationContext()).getRouteDetCount() > 0) {
                                if(new ReferenceController(DebtorDetailsActivity.this).IsActiveReceipt(getResources().getString(R.string.RecNumVal))>0){
                                    new OutstandingController(DebtorDetailsActivity.this).ClearFddbNoteData();
                                    new ReceiptController(DebtorDetailsActivity.this).CancelReceiptS(getResources().getString(R.string.RecNumVal));
                                    new ReceiptDetController(DebtorDetailsActivity.this).restDataForMR(getResources().getString(R.string.RecNumVal));
                                }

                                Intent intent = new Intent(DebtorDetailsActivity.this, ReceiptActivity.class);
                                intent.putExtra("outlet", outlet);
                                intent.putExtra("sales_order", false);
                                startActivity(intent);
                                finish();
                            } else {
                                failedMsgDialog(DebtorDetailsActivity.this,"Click Ok to Download Routes");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("######", "onClick: ", e);
                        }

                    } else {
                        Toast.makeText(DebtorDetailsActivity.this, "No outstandings for this customer", Toast.LENGTH_SHORT).show();

                    }
//                   }
//                   else {
//                       Toast.makeText(DebtorDetailsActivity.this, "Not allow for receipts", Toast.LENGTH_SHORT).show();
//
//                   }
                } else {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fabVansale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeFAM();

                // Only proceed if location service is available
                if (locationServiceEnabled()) {
                    // Toast.makeText(DebtorDetailsActivity.this, "Please wait. This may take a while", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(DebtorDetailsActivity.this, VanSalesActivity.class);
                    //Intent intent = new Intent(DebtorDetailsActivity.this, BarCodeReaderActivity.class);
//                    if(new CustomerController(DebtorDetailsActivity.this).getCustomerStatus(debCode).equals("")) {
//                        Intent intent = new Intent(DebtorDetailsActivity.this, ActivityVanSales.class);
//                        intent.putExtra("outlet", outlet);
//                        intent.putExtra("sales_order", false);
//                        startActivity(intent);
//                        finish();
//                    }else {
//                        Toast.makeText(DebtorDetailsActivity.this, "Not allow for invoice", Toast.LENGTH_SHORT).show();
//
//                    }
                } else {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fabReturnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFAM();

                // Only proceed if location service is available
                if (locationServiceEnabled()) {
//                    Toast.makeText(DebtorDetailsActivity.this, "Please wait. This may take a while", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(DebtorDetailsActivity.this, SalesReturnActivity.class);
//                    intent.putExtra("outlet", outlet);
//                    intent.putExtra("sales_return", false);
//                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fabSalesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Only proceed if location service is available
                //Log.d("<<<Distance1<<<<", " " + distance1);

                //if(distance<=50) {

                if (new RouteController(getApplicationContext()).getRouteCount() > 0 && new RouteDetController(getApplicationContext()).getRouteDetCount() > 0) {
                    sharedPref.setHeaderNextClicked("0");

                    Toast.makeText(DebtorDetailsActivity.this, "Please wait. This may take a while", Toast.LENGTH_SHORT).show();
                    // Intent intent = new Intent(DebtorDetailsActivity.this, PreSalesActivity.class);
                    Intent intent = new Intent(DebtorDetailsActivity.this, OrderActivity.class);
                    intent.putExtra("outlet", outlet);
                    intent.putExtra("sales_order", false);
                    startActivity(intent);
                    finish();

                } else {
                    failedMsgDialog(DebtorDetailsActivity.this,"Click Ok to Download Routes");
                }



//                    }else{
//                        Toast.makeText(DebtorDetailsActivity.this, "You are out of customer location.Please go to customer's location to continue..", Toast.LENGTH_SHORT).show();
//
//                    }

            }
        });

        fabendcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Only proceed if location service is available
                if (locationServiceEnabled()) {
                    mEndCallDialog();
                } else {
                    Toast.makeText(DebtorDetailsActivity.this, "Please enable location service", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public String getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0";

    }
    public void mEndCallDialog() {
        if (isAnyActiveReturns || isAnyActiveOrders || isAnyActiveInvoices || isAnyActiveReceipt) {
            String message = "Please complete or discard active transaction/s.";
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("End Call");
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();

                }
            }).setNegativeButton("", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                }
            });

            AlertDialog alertD = alertDialogBuilder.create();
            alertD.show();
        } else {
            //sharedPref.setSelectedDebtorEnd(true);
            //sharedPref.setSelectedDebtorStart(false);
            MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                    .content("Do you want to end call for this customer?")
                    .positiveColor(ContextCompat.getColor(this, R.color.material_alert_positive_button))
                    .positiveText("Yes")
                    .negativeColor(ContextCompat.getColor(this, R.color.material_alert_negative_button))
                    .negativeText("No, Exit")
                    .callback(new MaterialDialog.ButtonCallback() {

                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);

                            UtilityContainer.ClearReturnSharedPref(getApplicationContext());
                            UtilityContainer.ClearReceiptSharedPref(getApplicationContext());
                            UtilityContainer.ClearCustomerSharedPref(getApplicationContext());
                            sharedPref.setGPSDebtor("");
//                            sharedPref.clearPref();
                            sharedPref.clearPrefWithoutSyncDate();
                            Intent intent = new Intent(getApplicationContext(), CustomerListActivity.class);
                            startActivity(intent);
                            finish();

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
    }


    private boolean locationServiceEnabled() {
        boolean gpsActive;
        boolean networkActive;

        try {
            gpsActive = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            gpsActive = false;
        }

        try {
            networkActive = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            networkActive = false;
        }

        return networkActive || gpsActive;
    }

    /**
     * Function to open the FAM with custom flowing animation
     */
    private void openFAM() {
        //ViewCompat.animate(floatingActionsMenu).rotation(135).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(0);
//        ViewPropertyAnimator.animate(floatingActionsMenu).rotation(135).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(0);
        ViewCompat.animate(overlay).alpha(1).setDuration(400).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                //commented by rashmi for hameedias 2020-09-10
//                if (gpsTracker.canGetLocation())
//                {
//                    gpsTracker = new GPSTracker(context);
//                    if(!sharedPref.getGlobalVal("Latitude").equals("") && !sharedPref.getGlobalVal("Longitude").equals(""))
//                    {
//                        lati = Double.parseDouble(sharedPref.getGlobalVal("Latitude"));
//                        longi = Double.parseDouble(sharedPref.getGlobalVal("Longitude"));
//
//                        if (sharedPref.getGPSDebtor().equals("AN") || sharedPref.getGPSDebtor().equals("RN")) // not GPS mode debtor selection
//                        {//allNoGPS, RouteNoGPS
//                            if (!sharedPref.getGPSUpdated().equals("Y"))// not updated from near debtor co-ordinates
//                            {
//                                if (new CustomerController(context).updateCustomerLocationByCurrentCordinates(debCode, lati, longi)>0)
//                                {
//                                    Toast.makeText(context, "Current co-ordinates updated for " + debCode , Toast.LENGTH_LONG).show();
//                                }
//                                else
//                                {
//                                    Toast.makeText(context, "Current co-ordinates not updated for " + debCode , Toast.LENGTH_LONG).show();
//                                }
//                                Log.d("DEBTOR_DETIALS_ACTIVITY","IS_GPS: NOT_UPDATED from near debtor co-ordinates: " + sharedPref.getGPSUpdated() + " coodis: " + lati + ", " + longi);
//
//                            }else{
//                                Log.d("DEBTOR_DETIALS_ACTIVITY","IS_GPS: UPDATED: " + sharedPref.getGPSUpdated() + " coodis: " + lati + ", " + longi);
//
//                            }
//                        }
//                        Log.d("DEBTOR_DETIALS_ACTIVITY","IS_GPS: " + sharedPref.getGPSDebtor() + ", IS_GPS_UPDATED:  coodis: " + lati + ", " + longi);
//
//                    }
//                    else
//                    {
//                        Log.d("DEBTOR_DETIALS_ACTIVITY","IS_GPS: " + sharedPref.getGPSDebtor() + ", ALREADY GPS HAS: coodis: " + lati + ", " + longi);
//
//                        //startActivityForResult(new Intent(Settings.ACTION_LOCALE_SETTINGS), 0);
//                    }
//
//                                    }
//                else
//                {
//                    gpsTracker.showSettingsAlert();
//                }


                fabSalesOrder.setVisibility(View.VISIBLE);
                fabInvoice.setVisibility(View.VISIBLE);
                fabReturnNote.setVisibility(View.GONE);
                fabUnproductive.setVisibility(View.VISIBLE);
                fabVansale.setVisibility(View.GONE);
                floatingActionsMenu.setVisibility(View.VISIBLE);
                fabendcall.setVisibility(View.VISIBLE);

                labelUnproductive.setVisibility(View.VISIBLE);
                labelSalesOrder.setVisibility(View.VISIBLE);
                labelInvoice.setVisibility(View.VISIBLE);
                labelReturnNote.setVisibility(View.GONE);
                labelVanSale.setVisibility(View.GONE);
                labelMenu.setVisibility(View.VISIBLE);
                lblend.setVisibility(View.VISIBLE);

                overlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(View view) {

            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });

        int index = 0;

//        if(!invoiced) {
//        }

        famOpen = true;
    }


    /**
     * Function to close the FAM with custom flowing animation
     */
    private void closeFAM() {
//        if(invoiced){
//            ViewCompat.animate(floatingActionsMenu).rotation(0).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(400);
//        } else {
//            ViewCompat.animate(floatingActionsMenu).rotation(0).setInterpolator(new OvershootInterpolator()).setDuration(400).setStartDelay(500);
//        }

        ViewCompat.animate(overlay).alpha(0).setDuration(400).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                //commented by rashmi for hameedias 2020-09-10
//                if (gpsTracker.canGetLocation())
//                {
//                    gpsTracker = new GPSTracker(context);
//                    if(!sharedPref.getGlobalVal("Latitude").equals("") && !sharedPref.getGlobalVal("Longitude").equals(""))
//                    {//first time current gps not null
//                        lati = Double.parseDouble(sharedPref.getGlobalVal("Latitude"));
//                        longi = Double.parseDouble(sharedPref.getGlobalVal("Longitude"));
//
//                        if (sharedPref.getGPSDebtor().equals("AN") || sharedPref.getGPSDebtor().equals("RN")) // not GPS mode debtor selection
//                        {//allNoGPS, RouteNoGPS
//                            if (!sharedPref.getGPSUpdated().equals("Y"))// not updated from near debtor co-ordinates
//                            {
//                                if (new CustomerController(context).updateCustomerLocationByCurrentCordinates(debCode, lati, longi)>0)
//                                {
//                                    Toast.makeText(context, "Current co-ordinates updated for " + debCode , Toast.LENGTH_LONG).show();
//                                }
//                                else
//                                {
//                                    Toast.makeText(context, "Current co-ordinates not updated for " + debCode , Toast.LENGTH_LONG).show();
//                                }
//                                Log.d("DEBTOR_DETIALS_ACTIVITY","IS_GPS: NOT_UPDATED from near debtor co-ordinates: " + sharedPref.getGPSUpdated() + " coodis: " + lati + ", " + longi);
//
//                            }else{
//                                Log.d("DEBTOR_DETIALS_ACTIVITY","IS_GPS: UPDATED: " + sharedPref.getGPSUpdated() + " coodis: " + lati + ", " + longi);
//
//                            }
//                        }
//                        Log.d("DEBTOR_DETIALS_ACTIVITY","IS_GPS: " + sharedPref.getGPSDebtor() + ", IS_GPS_UPDATED:  coodis: " + lati + ", " + longi);
//
//                        gpsTracker = new GPSTracker(context);
//                    }
//                    else
//                    {
//                        gpsTracker = new GPSTracker(context);
//                        Log.d("DEBTOR_DETIALS_ACTIVITY","IS_GPS: " + sharedPref.getGPSDebtor() + ", ALREADY GPS HAS: coodis: " + lati + ", " + longi);
////if first time GPS null, call second time
//                        //startActivityForResult(new Intent(Settings.ACTION_LOCALE_SETTINGS), 0);
//
//                    }
//
//                }
//                else
//                {
//                    gpsTracker.showSettingsAlert();
//                }

                overlay.setVisibility(View.GONE);
                //  overlay.setVisibility(View.GONE);
                // Set the visibility to visible to the animation will show
                labelSalesOrder.setVisibility(View.GONE);
                labelInvoice.setVisibility(View.GONE);
                labelUnproductive.setVisibility(View.GONE);
                labelReturnNote.setVisibility(View.GONE);
                labelVanSale.setVisibility(View.GONE);
                lblend.setVisibility(View.GONE);

                fabSalesOrder.setVisibility(View.GONE);
                fabInvoice.setVisibility(View.GONE);
                fabUnproductive.setVisibility(View.GONE);
                fabReturnNote.setVisibility(View.GONE);
                fabVansale.setVisibility(View.GONE);
                fabendcall.setVisibility(View.GONE);
                labelMenu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });

        int index = 0;

        famOpen = false;
        sharedPref.setGPSUpdated("0");
    }


    private class OutletDetailsPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"PERSONNEL", "OUTSTANDING"};

        public OutletDetailsPagerAdapter(FragmentManager fa) {
            super(fa);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (personalDetailsFragment == null)
                        personalDetailsFragment = new PersonalDetailsFragment();
                    return personalDetailsFragment;
                case 1:
                    if (outstandingDetailsFragment == null)
                        outstandingDetailsFragment = new OutstandingDetailsFragment();
                    return outstandingDetailsFragment;
//                case 1:
//                    if(historyDetailsFragment == null) historyDetailsFragment = new HistoryDetailsFragment();
//                    return historyDetailsFragment;
//                case 2:
//                    if(competitorDetailsFragment == null) competitorDetailsFragment = new CompetitorDetailsFragment();
//                    return competitorDetailsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void failedMsgDialog(final Context context,String msg) {

        networkFunctions = new NetworkFunctions(getApplicationContext());

        final Dialog repDialog = new Dialog(context);
        repDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        repDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        repDialog.setCancelable(false);
        repDialog.setCanceledOnTouchOutside(false);
        repDialog.setContentView(R.layout.route_save_fail_dialog);

        //initializations

        final TextView Refno = (TextView) repDialog.findViewById(R.id.lbl_refno);

        Refno.setText(msg);


        repDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    new routeDownload(SharedPref.getInstance(getApplicationContext())
                            .getLoginUser().getFREP_CODE()).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                repDialog.dismiss();
            }
        });


        repDialog.show();
    }

    //route download asynctask
    private class routeDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;

        public routeDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getApplicationContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getApplicationContext());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading routes...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getApplicationContext()).getLoginUser() != null && SharedPref.getInstance(getApplicationContext()).isLoggedIn()) {
                    RouteController routeController = new RouteController(getApplicationContext());
                    routeController.deleteAll();
                    RouteDetController routedetController = new RouteDetController(getApplicationContext());
                    routedetController.deleteAll();
                    FItenrHedController fItenrHedController = new FItenrHedController(getApplicationContext());
                    fItenrHedController.deleteAll();
                    FItenrDetController fItenrDetController = new FItenrDetController(getApplicationContext());
                    fItenrDetController.deleteAll();
                    /*****************route*****************************************************************************/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading route data...");
                        }
                    });

                    String route = "";
                    try {
                        route = networkFunctions.getRoutes(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (routes)...");
                        }
                    });

                    // Processing route
                    try {
                        JSONObject routeJSON = new JSONObject(route);
                        JSONArray routeJSONArray = routeJSON.getJSONArray("fRouteResult");
                        ArrayList<Route> routeList = new ArrayList<Route>();

                        for (int i = 0; i < routeJSONArray.length(); i++) {
                            routeList.add(Route.parseRoute(routeJSONArray.getJSONObject(i)));
                        }
                        routeController.createOrUpdateFRoute(routeList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end route**********************************************************************/
                    /*****************Route det**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Last invoices downloaded\nDownloading route details...");
                        }
                    });

                    String routedet = "";
                    try {
                        routedet = networkFunctions.getRouteDets(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (routes)...");
                        }
                    });

                    // Processing route
                    try {
                        JSONObject routeJSON = new JSONObject(routedet);
                        JSONArray routeJSONArray = routeJSON.getJSONArray("fRouteDetResult");
                        ArrayList<RouteDet> routeList = new ArrayList<RouteDet>();

                        for (int i = 0; i < routeJSONArray.length(); i++) {
                            routeList.add(RouteDet.parseRoute(routeJSONArray.getJSONObject(i)));
                        }
                        routedetController.InsertOrReplaceRouteDet(routeList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end route det**********************************************************************/


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    //errors.add("Please enter correct username and password");
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

            pdialog.setMessage("Finalizing item data");
            pdialog.setMessage("Download Completed..");



        }
    }

    private class OtherDownload extends AsyncTask<String, Integer, Boolean> {

        private String repcode;
        CustomProgressDialog pdialog;
        //  private Handler mHandler;
        private List<String> errors = new ArrayList<>();

        public OtherDownload(String repcode) {
            this.repcode = repcode;
            this.pdialog = new CustomProgressDialog(DebtorDetailsActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(DebtorDetailsActivity.this);
            // mHandler = new Handler(Looper.getMainLooper());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Authenticating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                if (SharedPref.getInstance(DebtorDetailsActivity.this).getLoginUser() != null && SharedPref.getInstance(DebtorDetailsActivity.this).isLoggedIn()) {

                    // ************************Settings**********************************************************************/
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading data (Company Settings)...");
                        }
                    });
                    // Processing Settings

                    try {
                        ReferenceSettingController settingController = new ReferenceSettingController(DebtorDetailsActivity.this);
                        settingController.deleteAll();
                        UtilityContainer.download(DebtorDetailsActivity.this, TaskTypeDownload.Settings, networkFunctions.getReferenceSettings());
                    } catch (IOException e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Reference**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Reference details...");
                        }
                    });

                    // Processing banks
                    try {
                        ReferenceDetailDownloader referenceDetaileControlle = new ReferenceDetailDownloader(DebtorDetailsActivity.this);
                        referenceDetaileControlle.deleteAll();
                        UtilityContainer.download(DebtorDetailsActivity.this, TaskTypeDownload.Reference, networkFunctions.getReferences(repcode));
                    } catch (IOException e) {
                        errors.add(e.toString());
                        throw e;
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Completed...");
                        }
                    });

                    /*****************end sync**********************************************************************/

                    return true;
                } else {

                    errors.add("SharedPref.getInstance(getActivity()).getLoginUser() = null OR !SharedPref.getInstance(getActivity()).isLoggedIn()");
                    Log.d("ERROR>>>>>", "Login USer" + SharedPref.getInstance(DebtorDetailsActivity.this).getLoginUser().toString() + " IS LoggedIn --> " + SharedPref.getInstance(DebtorDetailsActivity.this).isLoggedIn());
                    return false;
                }
            } catch (Exception e) {

                e.printStackTrace();
                errors.add(e.toString());

                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pdialog.setMessage("Finalizing data");
            pdialog.setMessage("Download Completed..");
        }
    }
}






package com.datamation.kfdupgradesfa.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.controller.AreaController;
import com.datamation.kfdupgradesfa.controller.AttendanceController;
import com.datamation.kfdupgradesfa.controller.BankController;
import com.datamation.kfdupgradesfa.controller.BaseUrlController;
import com.datamation.kfdupgradesfa.controller.BrandController;
import com.datamation.kfdupgradesfa.controller.CompanyDetailsController;
import com.datamation.kfdupgradesfa.controller.CostController;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.DayNPrdHedController;
import com.datamation.kfdupgradesfa.controller.DownloadController;
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
import com.datamation.kfdupgradesfa.controller.OutstandingController;
import com.datamation.kfdupgradesfa.controller.PaymentController;
import com.datamation.kfdupgradesfa.controller.PaymentDetailController;
import com.datamation.kfdupgradesfa.controller.PaymentHeaderController;
import com.datamation.kfdupgradesfa.controller.PushMsgHedDetController;
import com.datamation.kfdupgradesfa.controller.ReasonController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReferenceDetailDownloader;
import com.datamation.kfdupgradesfa.controller.ReferenceSettingController;
import com.datamation.kfdupgradesfa.controller.RepGPSLocationController;
import com.datamation.kfdupgradesfa.controller.RouteDetController;
import com.datamation.kfdupgradesfa.controller.SupplierController;
import com.datamation.kfdupgradesfa.controller.TownController;
import com.datamation.kfdupgradesfa.controller.TypeController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.fragment.FragmentMarkAttendance;
import com.datamation.kfdupgradesfa.fragment.FragmentTools;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Attendance;
import com.datamation.kfdupgradesfa.model.BaseURL;
import com.datamation.kfdupgradesfa.model.Control;
import com.datamation.kfdupgradesfa.model.Debtor;
import com.datamation.kfdupgradesfa.model.NonPrdHed;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.model.RepGPS;
import com.datamation.kfdupgradesfa.model.apimodel.ReadJsonList;
import com.datamation.kfdupgradesfa.settings.TaskTypeDownload;
import com.datamation.kfdupgradesfa.utils.GPSTracker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.datamation.sfa.controller.ItemController;
import com.datamation.kfdupgradesfa.fragment.FragmentHome;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;

import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Customer;
import com.datamation.kfdupgradesfa.controller.RouteController;
//import com.datamation.sfa.presale.OrderMainFragment;
import com.datamation.kfdupgradesfa.R;

import com.datamation.kfdupgradesfa.settings.UserSessionManager;
import com.datamation.kfdupgradesfa.utils.NetworkUtil;
import com.datamation.kfdupgradesfa.utils.UtilityContainer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHome extends AppCompatActivity {

    public static final String SETTINGS = "SETTINGS";
    public Customer selectedDebtor = null;
    public int cusPosition = 0;
    private Context context = this;
    public String TAG = "ActivityHome.class";
    NetworkFunctions networkFunctions;
    List<String> resultList;
    ArrayList<Attendance> tours;
    SharedPref pref;
    Debtor loggedUser;
    private long timeInMillis;
    String currentVersion = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    ApiInterface apiInterface;
    int New = 0;
    int Current;
    private LocationManager lm;
    public static BottomNavigationView navigation;
    public double latitude, longitude;
    private String selectedTitle;

    Spinner spConnection;

    private ScheduledExecutorService scheduler;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (locationServiceEnabled()) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        changeFragment(0);
                        return true;
                    case R.id.navigation_sales:

//                    Intent intent = new Intent(getApplicationContext(), CustomerListActivity.class);
//                    startActivity(intent);
//                    finish();

                        SharedPref sharedPref = SharedPref.getInstance(context);
                        Log.d("Current Date>>>>", dateFormat.format(new Date(timeInMillis)));
                        Log.d("Last Sync Date>>>>", sharedPref.getGlobalVal("SyncDate"));
                        tours = new AttendanceController(context).getIncompleteRecord();


                        if (sharedPref.getGlobalVal("SyncDate").equalsIgnoreCase(dateFormat.format(new Date(timeInMillis)))) {
                            Log.d("Test SecondarySync", "Secondary sync done");


                            ArrayList<Control> allDownload = new DownloadController(context).getAllDownload();
                            boolean allDone = false;

                            for (Control cn : allDownload) {
                                if (Integer.parseInt(cn.getFCONTROL_DOWNLOADCOUNT()) > Integer.parseInt(cn.getFCONTROL_DOWNLOADEDCOUNT())) {
                                    allDone = false;
                                    break;
                                } else {
                                    allDone = true;
                                }

                            }

                            if (allDone == true) {

                                if (tours.size() > 0 && !(sharedPref.getGlobalVal("DayStartDate").equalsIgnoreCase(dateFormat.format(new Date(timeInMillis))))) {
                                    UtilityContainer.mLoadFragment(new FragmentMarkAttendance(), ActivityHome.this);
                                    Toast.makeText(getApplicationContext(), "Please do the day end", Toast.LENGTH_LONG).show();
                                    Log.d("Test Attendance", "Day start without previous day end");
                                } else {

                                    Log.d("Test Attendance", "Day start ok for today");
                                    Intent intent = new Intent(getApplicationContext(), CustomerListActivity.class);
                                    startActivity(intent);
                                    finish();
//
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Data sync issue occured..! please do the sync again", Toast.LENGTH_LONG).show();

                            }

//
                        }

                    case R.id.navigation_tools:
                        UtilityContainer.mLoadFragment(new FragmentTools(), ActivityHome.this);
                        return true;
                    case R.id.navigation_logout:
                        Logout();
                        return true;
                }
            } else {
                Toast.makeText(ActivityHome.this, "Please enable location service", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    };


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pref = SharedPref.getInstance(this);
        networkFunctions = new NetworkFunctions(this);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        resultList = new ArrayList<>();
        //loggedUser = pref.getLoginUser();
        currentVersion = getVersionCode();
        timeInMillis = System.currentTimeMillis();
       // pref.setActiveStatus(false);
       // new BaseUrlController(context).deleteAll();


        ArrayList<String> splist = new ArrayList<>();
        splist.add("SLT");
        splist.add("Dialog");


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View customActionBarView = inflater.inflate(R.layout.action_bar_spinner, null);

            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(customActionBarView);


            Spinner actionBarSpinner = customActionBarView.findViewById(R.id.action_bar_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.titles_array, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            actionBarSpinner.setAdapter(adapter);

            selectedTitle = pref.getSelectedTitle();
            if (selectedTitle != null) {
                int spinnerPosition = adapter.getPosition(selectedTitle);
                actionBarSpinner.setSelection(spinnerPosition);
            }

            actionBarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedTitle = (String) parent.getItemAtPosition(position);
                    setTitle("HOME (APP VERSION - " + currentVersion + ")    " + selectedTitle);
                    BaseURL baseURL = new BaseURL();
                    if(!selectedTitle.equals("-SELECT CON-")){
                        pref.setActiveStatus(true);
                        pref.setSelectedTitle(selectedTitle);
                    }

                    if (pref.getActiveStatus()) {
                        if (actionBarSpinner.getSelectedItem().equals("SLT")) {
                            //baseURL.setBASE_URL_URL("http://124.43.5.227:1030");//Live-SLT
                            baseURL.setBASE_URL_URL("http://124.43.5.227:1031");//testing-slt
                            baseURL.setBASE_URL_NAME("SLT");
                            baseURL.setBASE_URL_STATUS("Active");
                            ArrayList<BaseURL> BaseUrlList = new ArrayList<BaseURL>();
                            BaseUrlList.add(baseURL);


                            new BaseUrlController(context).deleteAll();
                            new BaseUrlController(context).CreateOrUpdateBaseUrl(BaseUrlList);
                            Toast.makeText(context, "Base-URL " + baseURL.getBASE_URL_NAME(), Toast.LENGTH_SHORT).show();


                        } else if (actionBarSpinner.getSelectedItem().equals("DIALOG")) {
                           // baseURL.setBASE_URL_URL("http://123.231.15.146:1030");// Live-Dialog
                            baseURL.setBASE_URL_URL("http://123.231.15.146:1031");//testing-dialog
                            baseURL.setBASE_URL_NAME("Dialog");
                            baseURL.setBASE_URL_STATUS("Active");
                            ArrayList<BaseURL> BaseUrlList = new ArrayList<BaseURL>();
                            BaseUrlList.add(baseURL);


                            new BaseUrlController(context).deleteAll();
                            new BaseUrlController(context).CreateOrUpdateBaseUrl(BaseUrlList);
                            Toast.makeText(context, "Base-URL " + baseURL.getBASE_URL_NAME(), Toast.LENGTH_SHORT).show();

                        }

                    }else {
                        Toast.makeText(context, "No Base URL ", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        //set home frgament
        changeFragment(0);
//
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        pref.setImageFlag("1");


        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        int LOCATION_REFRESH_TIME = 15000; // 15 seconds to update
        int LOCATION_REFRESH_DISTANCE = 500; // 500 meters to update

        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


//        MMS - 2022/01 *#*#*##*#*#*#*//
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);

        if (locationServiceEnabled()) {
            Timer timer = new Timer();
            TimerTask hourlyTask = new TimerTask() {
                @Override
                public void run() {

                    String curdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    String pattern = "hh:mm:ss a";

                    //1. LocalTime
                    LocalTime now = null;
                    String timeNow = "";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        now = LocalTime.now();
                        timeNow = now.format(DateTimeFormatter.ofPattern(pattern));
                    }


                    RepGPS rGps = new RepGPS();
                    rGps.setFREP_CODE(pref.getUserId());
                    rGps.setFTXN_DATE(curdate);
                    rGps.setFTXN_TIME(timeNow);
                    rGps.setFLONGITUDE(String.format("%.6f", longitude));
                    rGps.setFLATITUDE(String.format("%.6f", latitude));
                    rGps.setFIS_SYNC("0");

                    BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
                    int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    rGps.setBATTERY_LVL(batLevel);

                 //   new RepGPSLocationController(context).AddGPSLocation(rGps);

                }
            };

            timer.schedule(hourlyTask, 0l, 1000 * 15 * 60);   // 1000*10*60 every 10 minut

        }

    }

    private boolean locationServiceEnabled() {
        boolean gpsActive;
        boolean networkActive;

        try {
            gpsActive = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            gpsActive = false;
        }

        try {
            networkActive = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            networkActive = false;
        }

        return networkActive || gpsActive;
    }

    @Override
    public void onBackPressed() {
        //nothing (disable backbutton)
    }


    public void validateSettings(final Context context) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.settings_sqlite_password_layout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.et_password);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Go",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                /** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
                                String user_text = (userInput.getText()).toString();

                                /** CHECK FOR USER'S INPUT **/
                                if (user_text.equals("admin@kfd")) {
                                    Log.d(user_text, "HELLO THIS IS THE MESSAGE CAUGHT :)");
                                    UtilityContainer.mLoadFragment(new FragmentTools(), ActivityHome.this);

                                } else {
                                    Log.d(user_text, "string is empty");
                                    String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            validateSettings(context);
                                        }
                                    });
                                    builder.create().show();

                                }
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                        }

                );

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

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

    public class checkVersion extends AsyncTask<String, String, String> {

        private SweetAlertDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new SweetAlertDialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String version = "0";
            try {
                URL json = new URL("" + pref.getBaseURL() + getResources().getString(R.string.connection_string) + "fControl/mobile123/" + pref.getDistDB());
                URLConnection jc = json.openConnection();

                BufferedReader readerfdblist = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                String line = readerfdblist.readLine();
                JSONObject jsonResponse = new JSONObject(line);
                JSONArray jsonArray = jsonResponse.getJSONArray("fControlResult");
                JSONObject jObject = (JSONObject) jsonArray.get(0);
                version = jObject.getString("AppVersion");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return version;
        }

        protected void onPostExecute(String newVersion) {
            super.onPostExecute(newVersion);
            if (dialog.isShowing())
                dialog.dismiss();

            if (newVersion != null && !newVersion.isEmpty()) {

                if (!newVersion.trim().equals("")) {
                    New = Integer.parseInt(newVersion.trim().replace(".", ""));
                }

                Current = Integer.parseInt(currentVersion.replace(".", ""));
                Log.v("New Version", ">>>>>>" + New);

                Log.v("old Version", ">>>>>>" + Current);
                //163>162
                if (New > Current) {
                    //show dialog
                    Log.v("UPDATE AVAILABLESSSS", "USSPDATE");
                    // Create custom dialog object

                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("New Update Available.")
                            .setContentText("Vesrion : " + newVersion)
                            .setConfirmText("Yes,Update!")
                            .setCancelText("No,cancel!")
                            .showCancelButton(false)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    //Loading();
                                    sDialog.dismiss();

                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

//                                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.datamation.dss"));
//                                    startActivity(intent);
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            })

                            .show();

                } else {
                    Toast.makeText(context, "Your application is up to date", Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(context, "Invalid response from server when check version", Toast.LENGTH_LONG).show();

            }


        }


    }

    public void Logout() {

        final Dialog Ldialog = new Dialog(ActivityHome.this);
        Ldialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Ldialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Ldialog.setContentView(R.layout.logout);


        //logout
        Ldialog.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSessionManager sessionManager = new UserSessionManager(context);
                sessionManager.Logout();
                finish();
                pref.setLoginStatus(false);
                pref.setUserId("");
                pref.setUserPwd("");
                pref.setUserPrefix("");
                pref.clearPref();
                pref.setActiveStatus(false);
            }
        });
        Ldialog.show();
    }

    public void viewRouteInfo() {
        final Dialog repDialog = new Dialog(context);
        repDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        repDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        repDialog.setCancelable(false);
        repDialog.setCanceledOnTouchOutside(false);
        repDialog.setContentView(R.layout.rep_route_profile);

        //initializations
        RouteController routeDS = new RouteController(context);
        //  String routes = routeDS.getRouteNameByCode(loggedUser.getRoute());

        TextView routeName = (TextView) repDialog.findViewById(R.id.routeName);
        // routeName.setText(routes);
        TextView routeCode = (TextView) repDialog.findViewById(R.id.routeCode);
        //   routeCode.setText(loggedUser.getRoute());


        //close
        repDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repDialog.dismiss();
            }
        });

        repDialog.show();
    }



    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    /**
     * To load fragments for sample
     *
     * @param position menu index
     */
    private void changeFragment(int position) {


        if (position == 0) {
            //   newFragment = new FragHome();
            Log.d(">>>>>>", "position0");
            UtilityContainer.mLoadFragment(new FragmentHome(), ActivityHome.this);
        } else if (position == 1) {
            Log.d(">>>>>>", "position1");
        }
    }

    public void bottomNav(Boolean cmd) {

        if (cmd == true) {

            navigation.setVisibility(View.GONE);
        } else {
            navigation.setVisibility(View.VISIBLE);
        }
    }


    // kaveesha - 23/11/2021
    public class secondarySync extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;
        private String repcode, orderNo, status;
        private List<String> errors = new ArrayList<>();
        private Handler mHandler;

        public secondarySync(String repcode) {
            this.repcode = repcode;
            this.pdialog = new CustomProgressDialog(ActivityHome.this);
            mHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(ActivityHome.this);
            mHandler = new Handler(Looper.getMainLooper());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Authenticating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            apiInterface = ApiCllient.getClient(ActivityHome.this).create(ApiInterface.class);
            mHandler = new Handler(Looper.getMainLooper());
            try {
                //     if (SharedPref.getInstance(ActivityHome.this).getLoginUser() != null && SharedPref.getInstance(ActivityHome.this).isLoggedIn()) {
                new CompanyDetailsController(ActivityHome.this).deleteAll();

/*****************company details**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Company Details...");
                    }
                });
                // Processing controls
                try {
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Controllist, networkFunctions.getCompanyDetails(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }

                /*****************outlets**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Customers...");
                    }
                });

                try {
                    CustomerController customerController = new CustomerController(ActivityHome.this);
                    customerController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Customers, networkFunctions.getCustomer(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }
                /*****************Settings*****************************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Settings...");
                    }
                });
                // Processing company settings
                try {
                    ReferenceSettingController settingController = new ReferenceSettingController(ActivityHome.this);
                    settingController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Settings, networkFunctions.getReferenceSettings());
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }

                /*****************Branches*****************************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading data (reference details)...");
                    }
                });


                // Processing Branches
                try {
                    ReferenceDetailDownloader branchController = new ReferenceDetailDownloader(ActivityHome.this);
                    branchController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Reference, networkFunctions.getReferences(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }


                /***************** Types -  *****************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Types....");
                    }
                });

                // Processing Types
                try {
                    TypeController typeController = new TypeController(ActivityHome.this);
                    typeController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Type, networkFunctions.getTypes());
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }


                /*****************Items*****************************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Item Details...");
                    }
                });

                // Processing item
                try {
                    ItemController itemController = new ItemController(ActivityHome.this);
                    itemController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Items, networkFunctions.getItems(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }

                /*****************ItemLoc*****************************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Item Loc Details...");
                    }
                });

                // Processing item loc
                try {
                    ItemLocController itemLocController = new ItemLocController(ActivityHome.this);
                    itemLocController.deleteAllItemLoc();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.ItemLoc, networkFunctions.getItemLocations(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }

                /*****************reasons**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Reasons...");
                    }
                });
                // Processing reasons

                try {
                    ReasonController reasonController = new ReasonController(ActivityHome.this);
                    reasonController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Reason, networkFunctions.getReasons());
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                /*****************ItemPri**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Item Pri Details...");
                    }
                });
                // Processing Item Pri

                try {
                    ItemPriceController itemPriceController = new ItemPriceController(ActivityHome.this);
                    itemPriceController.deleteAllItemPri();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.ItemPri, networkFunctions.getItemPrices(repcode));
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Banks...");
                    }
                });
                /*****************banks**********************************************************************/

                try {
                    BankController bankController = new BankController(ActivityHome.this);
                    bankController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Bank, networkFunctions.getBanks());
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                /*****************Route**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Routes...");
                    }
                });
                // Processing route

                try {
                    RouteController routeController = new RouteController(ActivityHome.this);
                    routeController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Route, networkFunctions.getRoutes(repcode));
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                /*****************Cost**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Cost...");
                    }
                });
                // Processing Cost

                try {
                    CostController costController = new CostController(ActivityHome.this);
                    costController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Cost, networkFunctions.getCost(repcode));
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                /*****************Supplier**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Supplier...");
                    }
                });

                // Processing Supplier

                try {
                    SupplierController supplierController = new SupplierController(ActivityHome.this);
                    supplierController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Supplier, networkFunctions.getSupplier(repcode));
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }


                /*****************Route det**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Route Det...");
                    }
                });

                // Processing route
                try {
                    RouteDetController routeDetController = new RouteDetController(ActivityHome.this);
                    routeDetController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.RouteDet, networkFunctions.getRouteDets(repcode));
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                /***************** Area - *************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Area Details...");
                    }
                });

                // Processing area
                try {
                    AreaController areaController = new AreaController(ActivityHome.this);
                    areaController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Area, networkFunctions.getArea());
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                /***************** Locations - ****************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Locations...");
                    }
                });

                // Processing locations
                try {
                    LocationsController locationsController = new LocationsController(ActivityHome.this);
                    locationsController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Locations, networkFunctions.getLocations(repcode));
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }


                /*****************Towns - ********************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Towns...");
                    }
                });

                // Processing towns
                try {
                    TownController townController = new TownController(ActivityHome.this);
                    townController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Towns, networkFunctions.getTowns());
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                /***************** Groups - ***********************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Groups...");
                    }
                });

                // Processing group
                try {
                    GroupController groupController = new GroupController(ActivityHome.this);
                    groupController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Groups, networkFunctions.getGroups());
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                /***************** Brands - ***********************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Brands...");
                    }
                });

                // Processing brands
                try {
                    BrandController brandController = new BrandController(ActivityHome.this);
                    brandController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Brand, networkFunctions.getBrands());
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                /*****************freeMslab**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Free...");
                    }
                });
                // Processing freeMslab
                try {

                    FreeMslabController freemSlabController = new FreeMslabController(ActivityHome.this);
                    freemSlabController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Freemslab, networkFunctions.getFreeMslab());
                } catch (Exception e) {
                    errors.add(e.toString());

                    throw e;
                }

                /*****************FreeHed**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Free...");
                    }
                });
                // Processing freehed
                try {

                    FreeHedController freeHedController = new FreeHedController(ActivityHome.this);
                    freeHedController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Freehed, networkFunctions.getFreeHed(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());

                    throw e;
                }

                /*****************Freedet**********************************************************************/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Free...");
                    }
                });

                // Processing freedet
                try {

                    FreeDetController freeDetController = new FreeDetController(ActivityHome.this);
                    freeDetController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Freedet, networkFunctions.getFreeDet(repcode));

                } catch (Exception e) {
                    errors.add(e.toString());

                    throw e;
                }

                /*****************freedeb**********************************************************************/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Free...");
                    }
                });

                // Processing freedeb
                try {

                    FreeDebController freeDebController = new FreeDebController(ActivityHome.this);
                    freeDebController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Freedeb, networkFunctions.getFreeDebs(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }

                /*****************freeItem**********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Free...");
                    }
                });


                // Processing freeItem
                try {

                    FreeItemController freeItemController = new FreeItemController(ActivityHome.this);
                    freeItemController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Freeitem, networkFunctions.getFreeItems());
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Outsatnding...");
                    }
                });

                // Processing fddbnote
                try {
                    OutstandingController outstandingController = new OutstandingController(ActivityHome.this);
                    outstandingController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.fddbnote, networkFunctions.getFddbNotes(repcode));
                } catch (IOException e) {
                    e.printStackTrace();
                    errors.add(e.toString());
                    throw e;
                }

                /*****************Push Msg HedDet **********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Push Msg Hed Det....");
                    }
                });
                // Processing Push Msg HedDet

                try {
                    PushMsgHedDetController pushMsgHedDetController = new PushMsgHedDetController(ActivityHome.this);
                    pushMsgHedDetController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.PushMsgHedDet, networkFunctions.getPushMsgHedDet(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }

                /***************** CusPRecHed  **********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading RecHed....");
                    }
                });
                // Processing Cusp RecHed

                try {
                    PaymentHeaderController paymentHeaderController = new PaymentHeaderController(ActivityHome.this);
                    paymentHeaderController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.CusPRecHed, networkFunctions.getCusPRecHed(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }

                /***************** CusPRecDet **********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading RecDet ....");
                    }
                });
                // Processing Cusp RecDet

                try {
                    PaymentDetailController paymentDetailController = new PaymentDetailController(ActivityHome.this);
                    paymentDetailController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.CusPRecDet, networkFunctions.getCusPRecDet(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }

                /***************** Payments **********************************************************************/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Downloading Payments ....");
                    }
                });
                // Processing Payments

                try {
                    PaymentController paymentController = new PaymentController(ActivityHome.this);
                    paymentController.deleteAll();
                    UtilityContainer.download(ActivityHome.this, TaskTypeDownload.Payments, networkFunctions.getPayments(repcode));
                } catch (Exception e) {
                    errors.add(e.toString());
                    throw e;
                }

//                /***************** Rep Details **********************************************************************/
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        pdialog.setMessage("Downloading Rep Details ....");
//                    }
//                });
//                // Processing Rep Details
//
//                try {
//                    SalRepController repDetailsController = new SalRepController(ActivityHome.this);
//                    repDetailsController.deleteAll();
//                    UtilityContainer.download(ActivityHome.this,TaskTypeDownload.RepDetails, networkFunctions.getRepDetails(repcode));
//                } catch (Exception e) {
//                    errors.add(e.toString());
//                    throw e;
//                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdialog.setMessage("Completed...");
                    }
                });

                /*****************end sync**********************************************************************/
                return true;
//                } else {
//                    errors.add("SharedPref.getInstance(ActivityHome.this).getLoginUser() = null OR !SharedPref.getInstance(ActivityHome.this).isLoggedIn()");
//                    Log.d("ERROR>>>>>", "Login USer" + SharedPref.getInstance(ActivityHome.this).getLoginUser().toString() + " IS LoggedIn --> " + SharedPref.getInstance(ActivityHome.this).isLoggedIn());
//                    return false;
//                }
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
            if (result) {
                if (pdialog.isShowing())
                    pdialog.dismiss();

                Toast.makeText(context, "Download Successfully.....", Toast.LENGTH_LONG).show();

                if (pref.getGlobalVal("placeAnOrder").equals("1")) {
//                    pref.setOrderId(0);
//                    pref.setEditOrderId(0);
                    Intent intent = new Intent(getApplicationContext(), CustomerListActivity.class);
                    intent.putExtra("From", "new");
                    startActivity(intent);
                    finish();
                }
//                  int i = 1;
//                    for (Control c : new CompanyDetailsController(ActivityHome.this).getAllDownload()) {
//                        downloadList.add(c);
//                        i++;
//                    }
//
//                    if(downloadList.size()>0) {
//                        mDownloadResult(downloadList);
//                    }
//
//                showErrorText("Successfully Synchronized");
            } else {
                if (pdialog.isShowing())
                    pdialog.dismiss();

//                    int i = 1;
//                    for (Control c : new CompanyDetailsController(ActivityHome.this).getAllDownload()) {
//                        downloadList.add(c);
//                        i++;
//                    }
//
//                    if(downloadList.size()>0) {
//                        mDownloadResult(downloadList);
//                    }
//                }
//                StringBuilder sb = new StringBuilder();
//                if (errors.size() == 1) {
//                    sb.append(errors.get(0));
//                    showErrorText(sb.toString());
//                } else if(errors.size() == 0) {
//                    sb.append("Following errors occurred");
//                    for (String error : errors) {
//                        sb.append("\n - ").append(error);
//                        showErrorText(sb.toString());
//                    }
//
            }

        }
    }

    //---------------kaveesha - 11/04/2020----------------------------------------
    public void ServerAvailability() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Request Server Connection !");
        alertDialogBuilder.setMessage("Server is unavailable .Please check your server connection");
        alertDialogBuilder.setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    public void CheckServerAvailable(String customerId, final String otp) {

        try {
            ApiInterface apiInterface = ApiCllient.getClient(ActivityHome.this).create(ApiInterface.class);
            Call<ReadJsonList> resultCall = apiInterface.getSalRepResult(pref.getDistDB(), customerId, otp);
            resultCall.enqueue(new Callback<ReadJsonList>() {
                @Override
                public void onResponse(Call<ReadJsonList> call, Response<ReadJsonList> response) {
                    CustomProgressDialog pdialog = new CustomProgressDialog(ActivityHome.this);
                    pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pdialog.setMessage("Validating...");
                    pdialog.show();
                    if (response.body() != null) {
                        ArrayList<Debtor> cusList = new ArrayList<Debtor>();
                        for (int i = 0; i < response.body().getDebtorResult().size(); i++) {
                            cusList.add(response.body().getDebtorResult().get(i));
                        }

                        if (cusList.size() > 0) {
                            pref.setServerConnectionStatus(true);
                            pref.setGlobalVal("SyncDate", dateFormat.format(new Date(timeInMillis)));
                            // new secondarySync(pref.getSelectedrepcode()).execute();
                            if (pdialog.isShowing()) {
                                pdialog.cancel();
                            }

                        } else {
                            pref.setServerConnectionStatus(false);
                            ServerAvailability();
                        }
                    } else {
                        pref.setServerConnectionStatus(false);
                        ServerAvailability();
                    }
                }

                @Override
                public void onFailure(Call<ReadJsonList> call, Throwable t) {
                    t.printStackTrace();
                    pref.setServerConnectionStatus(false);
                    ServerAvailability();
                }
            });
        } catch (Exception e) {
            Log.d(">>>ERROR Connection", ">>>" + e.toString());
            pref.setServerConnectionStatus(false);
        }
    }


}

package com.datamation.kfdupgradesfa.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.datamation.kfdupgradesfa.controller.AttendanceController;
import com.datamation.kfdupgradesfa.controller.CompanyDetailsController;
import com.datamation.kfdupgradesfa.controller.DownloadController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.model.Control;
import com.datamation.kfdupgradesfa.model.DevAuth;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.RepCredentials;
import com.datamation.kfdupgradesfa.model.SalRep;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.utils.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.karan.churi.PermissionManager.PermissionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    /*********modified by rashmi 2020-09-11********/
    EditText username, password;
    TextView txtver;
    SharedPref pref;
    NetworkFunctions networkFunctions;
    int tap;
    RelativeLayout loginlayout;
    private long timeInMillis;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private Context context = this;
    PermissionManager permissionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        networkFunctions = new NetworkFunctions(this);
        pref = SharedPref.getInstance(this);
        username = (EditText) findViewById(R.id.editText1);
        password = (EditText) findViewById(R.id.editText2);
        Button login = (Button) findViewById(R.id.btnlogin);
        txtver = (TextView) findViewById(R.id.textVer);
        loginlayout = (RelativeLayout) findViewById(R.id.loginLayout);
        txtver.setText("Version " + getVersionCode());
        timeInMillis = System.currentTimeMillis();
        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);

        login.setOnClickListener(this);

        txtver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tap += 1;
                // StartTimer(3000);
                if (tap >= 7) {
                    // validateDialog();
                }
            }
        });


    }

    public void reCallActivity() {
        Intent mainActivity = new Intent(ActivityLogin.this, ActivityLogin.class);
        startActivity(mainActivity);
        finish();
    }

    public void StartTimer(int timeout) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tap = 0;
            }
        }, timeout);

    }

    public String getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }

    //********************** kaveesha - 10/12/2020 -************************************************
    public void LoginFailedMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("please check your User Id and Password ");
        alertDialogBuilder.setTitle("Login Failed");
        alertDialogBuilder.setIcon(R.drawable.info);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                reCallActivity();
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin: {


                if (!(username.getText().toString().equalsIgnoreCase("")) && !(password.getText().toString().equalsIgnoreCase(""))) {
                    //temparary for datamation
                    Log.d(">>>", "Validation :: " + username.getText().toString());
                    Log.d(">>>", "Validation :: " + password.getText().toString());

                    if (NetworkUtil.isNetworkAvailable(ActivityLogin.this)) {
                        //GettingReps(username.getText().toString().trim(), password.getText().toString().trim());
                        new Validate(username.getText().toString().trim(), password.getText().toString().trim()).execute();
                    } else {
                        Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Log.d(">>>", "Validation :: " + username.getText().toString());
                    Log.d(">>>", "Validation :: " + password.getText().toString());
                    Toast.makeText(this, "Please fill the valid credentials", Toast.LENGTH_LONG).show();
                    username.setText("");
                    password.setText("");
                }


            }
            break;

            default:
                break;
        }
    }


//    public void SignUp() {
//        final Dialog repDialog = new Dialog(this);
//        repDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        repDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        repDialog.setCancelable(false);
//        repDialog.setCanceledOnTouchOutside(false);
//        repDialog.setContentView(R.layout.sign_up);
//        //initializations
//
//        final TextView cusID = (TextView) repDialog.findViewById(R.id.cID);
//        final EditText ExistingPw = (EditText) repDialog.findViewById(R.id.ePW);
//        final EditText newPW = (EditText) repDialog.findViewById(R.id.nPW);
//        final EditText conPW = (EditText) repDialog.findViewById(R.id.cPW);
//
//       // cusID.setText(pref.getLoginUser().getFDEBTOR_CUSID());
//       // ExistingPw.setText(pref.getLoginUser().getFDEBTOR_OTP());
//
//
//        //close
//        repDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(newPW.getText().toString().equalsIgnoreCase(conPW.getText().toString()) && !newPW.getText().toString().equals("") && !conPW.getText().toString().equals("")){
//                    repDialog.dismiss();
//               //     pref.setSelectedDebCode(pref.getLoginUser().getFDEBTOR_CODE());
//                    pref.setValidateStatus(true);
//                    pref.setLoginStatus(true);
//                    pref.storeNewOTP(newPW.getText().toString());
//                    String NewPass = sha1Hash(newPW.getText().toString());
//             //       new CustomerController(ActivityLogin.this).updateNewPassword(pref.getLoginUser().getFDEBTOR_CODE(),NewPass);
//                    if(NetworkUtil.isNetworkAvailable(ActivityLogin.this)){
//                //        ArrayList<Debtor> passwordupload = new CustomerController(ActivityLogin.this).uploadCustomerPassword(pref.getLoginUser().getFDEBTOR_CODE());
//                        new UploadPassword(ActivityLogin.this).execute(passwordupload);
//                        Log.v(">>8>>", "UploadPreSales execute finish");
//
//                    }else{
//                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
//
//                    }
//                    Intent loginActivity = new Intent(ActivityLogin.this, ActivityHome.class);
//                    startActivity(loginActivity);
//                    finish();
//                }else{
//                    Toast.makeText(getApplicationContext(), "Please check new password again", Toast.LENGTH_LONG).show();
//
//                }
//
//
//            }
//        });
//        repDialog.show();
//    }


    private void showErrorText(String s) {
        Snackbar snackbar = Snackbar.make(loginlayout, R.string.txt_msg, Snackbar.LENGTH_LONG);
        View snackbarLayout = snackbar.getView();
        snackbarLayout.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sync, 0, 0, 0);
        textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.body_size));
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    String sha1Hash(String toHash) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            // This is ~55x faster than looping and String.formating()
            hash = bytesToHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

//    private void GettingReps(final String username, final String password){//12-07-2022 - kaveesha
//
//        RepCredentials credentials = new RepCredentials();
//        credentials.setFREF_REPCODE(username);
//        credentials.setFREF_PASSWORD(password);
//
//        try {
//            String content_type = "application/json";
//            ApiInterface apiInterface = ApiCllient.getClient(context).create(ApiInterface.class);
//            JsonParser jsonParser = new JsonParser();
//            String orderJson = new Gson().toJson(credentials);
//            JsonObject objectFromString = jsonParser.parse(orderJson).getAsJsonObject();
//            Log.d(">>>gettingRef", ">>>objectFromString " + objectFromString.toString());
//
//            Call<SalRep> resultCall = apiInterface.gettingReps(objectFromString,content_type);
//            resultCall.enqueue(new Callback<SalRep>() {
//                @Override
//                public void onResponse(Call<SalRep> call, Response<SalRep> response) {
//                    CustomProgressDialog pdialog = new CustomProgressDialog(ActivityLogin.this);
//                    pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    pdialog.setMessage("Validating...");
//                    pdialog.show();
//
//                    int status = response.code();
//
//                    if (response.isSuccessful()) {
//                        response.body(); // have your all data
//
//                        if (response.body() != null) {
////                            SalRepController salRepController = new SalRepController(context);
////                            salRepController.deleteAll();
////                            ArrayList<SalRep> RepArrayList = new ArrayList<SalRep>();
////                            RepArrayList.add(response.body());
////                            pref.setUserId(response.body().getFREP_CODE().trim());
////                            pref.setUserPwd(password.trim());
////                            pref.setUserPrefix(response.body().getFREP_PREFIX().trim());
//
//                            JSONObject jsonObject = null;
//                            int totalRecords = 0;
//
//                            try {
//                                jsonObject = new JSONObject(response.body().toString());
//
//                            } catch (JSONException e) {
//                                Log.e("JSON ERROR>>>>>", e.toString());
//                            }
//
//                            try {
//                                JSONArray jsonArray = jsonObject.getJSONArray("data");
//                                totalRecords = jsonArray.length();
//
//                                ArrayList<SalRep> RepArrayList = new ArrayList<SalRep>();
//                                SalRepController salRepController = new SalRepController(context);
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    RepArrayList.add(SalRep.parseSalRep(jsonArray.getJSONObject(i)));
//                                }
//                                if (RepArrayList.size() > 0) {
//                                    salRepController.CreateOrUpdateSalRep(RepArrayList);
//
//                                    if (pdialog.isShowing()) {
//                                        pdialog.cancel();
//                                    }
//
//                                    if (username.equalsIgnoreCase(pref.getUserId()) && !password.trim().equals("")) {
//                                        pref.setValidateStatus(true);
//                                        pref.setLoginStatus(true);
//                                        Intent loginActivity = new Intent(ActivityLogin.this, ActivityHome.class);
//                                        startActivity(loginActivity);
//                                        finish();
//                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
//                                        reCallActivity();
//                                    }
//
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "Invalid username or password ", Toast.LENGTH_LONG).show();
//                                    reCallActivity();
//                                }
//                             //   salRepController.CreateOrUpdateSalRep(RepArrayList);
//
//                            } catch (JSONException | NumberFormatException e) {
//
//                                try {
//                                    throw e;
//                                } catch (JSONException e1) {
//                                    Log.e("JSON ERROR>>>>>", e.toString());
//                                }
//                            }
//
//
//
//                        }
//
//                    } else {
//                        Log.d(">>error response" + status, response.errorBody().toString());
//                        Toast.makeText(getApplicationContext(), "Invalid username or password ", Toast.LENGTH_LONG).show();
//                        reCallActivity();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<SalRep> call, Throwable t) {
//                    Toast.makeText(context, "Error response "+t.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    private class Validate extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;
        private String username, password;

        public Validate(String uid, String pwd) {
            this.username = uid;
            this.password = pwd;
            this.pdialog = new CustomProgressDialog(ActivityLogin.this);
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
                int recordCount = 0;
                int totalBytes = 0;
                String validateResponse = null;
                JSONObject validateJSON;
                try {

                    SalRepController salRepController = new SalRepController(ActivityLogin.this);
                    salRepController.deleteAll();

//                    validateResponse = networkFunctions.getSalRep(username, password,"Y");
                    validateResponse = networkFunctions.getSalRep(username,password);
                    Log.d("validateResponse", validateResponse);
                    validateJSON = new JSONObject(validateResponse);

                    if (validateJSON != null) {
                        pref = SharedPref.getInstance(ActivityLogin.this);
                        //dbHandler.clearTables();
                        // Login successful. Proceed to download other items

                        JSONArray repArray = validateJSON.getJSONArray("data");
                        ArrayList<SalRep> UserList = new ArrayList<>();
                        for (int i = 0; i < repArray.length(); i++) {
                            JSONObject userJSON = repArray.getJSONObject(i);
                            //    pref.storeLoginUser(SalRep.parseSalRep(userJSON));
                            pref.setUserId(userJSON.getString("repCode").trim());
                            pref.setUserPwd(password);
                            pref.setUserPrefix(userJSON.getString("repPreFix").trim());
                            UserList.add(SalRep.parseSalRep(userJSON));
                        }

                        salRepController.CreateOrUpdateSalRep(UserList);

//                        if(UserList.size()>0){
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    pdialog.setMessage("Authenticated...");
//                                }
//                            });
//                        }else{
//                            Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG).show();
//                            reCallActivity();
//                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pdialog.setMessage("Authenticated...");
                            }
                        });

                        return true;
                    } else {
                        Toast.makeText(ActivityLogin.this, "Invalid response from server when getting sales rep data", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                } catch (IOException e) {
                    Log.e("networkFunctions ->", "IOException -> " + e.toString());
                    throw e;
                } catch (JSONException e) {
                    Log.e("networkFunctions ->", "JSONException -> " + e.toString());
                    throw e;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

//


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pdialog.isShowing())
                pdialog.cancel();
            // pdialog.cancel();
            if (result) {
                if (result) {
                    Log.d(">>>password", ">>>" + password);
                    if (username.equalsIgnoreCase(pref.getUserId()) && !password.trim().equals("")) {
                        //when password is incorrect fpass array is empty
                        Log.d(">>>Response ok1", pref.getUserId() + ">>>" + pref.getUserPwd());
//                    if (pref.getUserId().trim().equals(username.trim()) && pref.getUserPwd().trim().equals(md5(password.trim()))) {
//                        Log.d(">>>Response ok2",pref.getUserId()+">>>"+pref.getUserPwd());

                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        pref.setValidateStatus(true);
                        pref.setLoginStatus(true);
                        Intent loginActivity = new Intent(ActivityLogin.this, ActivityHome.class);
                        startActivity(loginActivity);
                        finish();
//                        PrimeThread p = new PrimeThread();
//                        p.start();



                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                        reCallActivity();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Invalid response from server", Toast.LENGTH_LONG).show();
                    reCallActivity();
                }
            }
        }
    }


    class PrimeThread extends Thread {

        CustomProgressDialog pdialog;

        PrimeThread() {
            this.pdialog = new CustomProgressDialog(context);

            pdialog = new CustomProgressDialog(context);
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Please wait while for a device authenticating...");
            pdialog.show();


        }

        public void run() {

            Looper.prepare();

            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        devAuthentication();
                    }
                });
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (pdialog.isShowing()) {
                pdialog.dismiss();

            }
        }
    }

    private boolean devAuthentication() {

        DevAuth dev = new DevAuth();
        dev.setDistDB(pref.getDistDB());
        dev.setIns_RepCode(pref.getLoginUser().getFREP_CODE());
        dev.setIs_Installed("Y");

        try {
            String content_type = "application/json";
            ApiInterface apiInterface = ApiCllient.getClient(context).create(ApiInterface.class);
            JsonParser jsonParser = new JsonParser();
            String authJson = new Gson().toJson(dev);
            JsonObject objectFromString = jsonParser.parse(authJson).getAsJsonObject();

            Call<String> resultCall = apiInterface.updateRepInstallStatus(objectFromString, content_type);

            resultCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    int status = response.code();
                    String resmsg = "" + response.body();
                    if (status == 200 && !resmsg.equals("") && !resmsg.equals(null)) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        pref.setValidateStatus(true);
                        pref.setLoginStatus(true);
                        Intent loginActivity = new Intent(ActivityLogin.this, ActivityHome.class);
                        startActivity(loginActivity);
                        finish();
                    } else {
                        Toast.makeText(context, "Install authentication failed!" , Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(context, "Error response device Authentication" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M){
            permissionManager.checkResult(requestCode, permissions, grantResults);
        }else{

        }
//        permissionManager.checkResult(requestCode, permissions, grantResults);

    }
//    private void Validate(String repcode, final String pw){//2021-11-25
//
//        try{
//            ApiInterface apiInterface = ApiCllient.getClient(ActivityLogin.this).create(ApiInterface.class);
//            Call<ReadJsonList> resultCall = apiInterface.getSalRepResult(pref.getDistDB(),repcode,pw);
//            resultCall.enqueue(new Callback<ReadJsonList>() {
//                @Override
//                public void onResponse(Call<ReadJsonList> call, Response<ReadJsonList> response) {
//                    CustomProgressDialog pdialog = new CustomProgressDialog(ActivityLogin.this);
//                    pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    pdialog.setMessage("Validating...");
//                    pdialog.show();
//                    // System.out.println("test responce 01 " + response.body().getDebtorResult().size());
//                    //  System.out.println(response.body().getInvDetResult().get(1));]
//                    if (response.body() != null) {
//                        ArrayList<SalRep> salRepList = new ArrayList<SalRep>();
//                        if (response.body().getSalRepResult().size() > 0) {
//                            for (int i = 0; i < response.body().getSalRepResult().size(); i++) {
//                                salRepList.add(response.body().getSalRepResult().get(i));
//                            }
//                            new SalRepController(ActivityLogin.this).CreateOrUpdateSalRep(salRepList);
//                            networkFunctions.setUser(salRepList.get(0));
//                            pref.storeLoginUser(salRepList.get(0));
//                            System.out.println("CUSTOMER List " + salRepList.toString());
//                            if (salRepList.size() > 0) {
//                                if (pdialog.isShowing()) {
//                                    pdialog.cancel();
//                                }
//                                String hashtest = sha1Hash(pw);
//                                if (hashtest.equalsIgnoreCase(pref.getLoginUser().getFREP_PASSWORD())) {
//                                    pref.setSelectedDebCode(pref.getLoginUser().getFREP_CODE());
//                                    pref.setValidateStatus(true);
//                                    pref.setLoginStatus(true);
//                                    Intent loginActivity = new Intent(ActivityLogin.this, ActivityHome.class);
//                                    startActivity(loginActivity);
//                                    finish();
//                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "Invalid  password", Toast.LENGTH_LONG).show();
//                                    reCallActivity();
//                                }
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Invalid username or password ", Toast.LENGTH_LONG).show();
//                                reCallActivity();
//                            }
//                        } else {
//                            LoginFailedMessage();
//                        }
//                    }
//                }
//                @Override
//                public void onFailure(Call<ReadJsonList> call, Throwable t) {
//                    t.printStackTrace();
//                }
//            });
//        }catch (Exception e){
//            Log.d(">>>ERROR Validate",">>>"+e.toString());
//        }
//    }

}

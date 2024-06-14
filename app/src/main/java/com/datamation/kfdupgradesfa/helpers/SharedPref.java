package com.datamation.kfdupgradesfa.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.datamation.kfdupgradesfa.model.SalRep;

/**
 * Functions to access shared preferences.
 */
public class SharedPref {

    private static final String LOG_TAG = SharedPref.class.getSimpleName();

    //    private Context context;
    private SharedPreferences sharedPref;

    private static SharedPref pref;
    private boolean isGPSSwitched = false;

    public boolean isGPSSwitched() {
        return isGPSSwitched;
    }

    public void setGPSSwitched(boolean GPSSwitched) {
        isGPSSwitched = GPSSwitched;
    }

    public SharedPref() {
    }

    public static SharedPref getInstance(Context context) {
        if (pref == null) {
            pref = new SharedPref(context);
        }

        return pref;
    }

//    public boolean isSelectedDebtorStart() {
//        return selectedDebtorStart;
//    }
//
//    public void setSelectedDebtorStart(boolean selectedDebtorStart) {
//        this.selectedDebtorStart = selectedDebtorStart;
//    }
//
//    public boolean isSelectedDebtorEnd() {
//        return selectedDebtorEnd;
//    }
//
//    public void setSelectedDebtorEnd(boolean selectedDebtorEnd) {
//        this.selectedDebtorEnd = selectedDebtorEnd;
//    }

    public SharedPref(Context context) {
//        this.context = context;
        sharedPref = context.getSharedPreferences("app_data", Context.MODE_PRIVATE);
    }

    public void setLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("login_status", status).apply();
    }

    public boolean isLoggedIn() {
        return sharedPref.getBoolean("login_status", false);
    }

    public void setValidateStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("validate_status", status).apply();
    }


    public void setSuccessSyncedStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("sync_status", status).apply();
    }

    public boolean isSyncedSuccess() {
        return sharedPref.getBoolean("sync_status", false);
    }

    public void setOtherDownload(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("other_sync_status", status).apply();
    }

    public boolean isOtherDownload() {
        return sharedPref.getBoolean("other_sync_status", false);
    }

    public boolean isValidate() {
        return sharedPref.getBoolean("validate_status", false);
    }

    public void setTMReturn(String ordSale) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("TMReturn", ordSale);
        editor.apply();
    }

    public String getReceiptHedClicked() {
        return sharedPref.getString("IS_RECEIPTHED_CLICKED", "0");
    }

    public void setReceiptHedClicked(String val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("IS_RECEIPTHED_CLICKED", val);
        editor.apply();
    }

    public String getNextClick() {
        return sharedPref.getString("NEXT_CLICKED", "0");
    }

    public void setNextClick(String val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("NEXT_CLICKED", val);
        editor.apply();
    }

    public void setServerConnectionStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("connection_status", status).apply();
    }

    public boolean isNextClick() {
        return sharedPref.getBoolean("isNextClick", false);
    }

    public String getTMReturn() {
        return sharedPref.getString("TMReturn", "0");
    }

    public void setPMReturn(String ordSale) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Return_PM", ordSale);
        editor.apply();
    }

    public String getPMReturn() {
        return sharedPref.getString("Return_PM", "0");
    }

    public void setTMOrdSale(String ordSale) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Order_Sale_TM", ordSale);
        editor.apply();
    }

    public String getTMOrdSale() {
        return sharedPref.getString("Order_Sale_TM", "0");
    }

    public void setPMOrdSale(String ordSale) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Order_Sale_PM", ordSale);
        editor.apply();
    }

    public String getPMOrdSale() {
        return sharedPref.getString("Order_Sale_PM", "0");
    }

    public void setTMInvSale(String invSale) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Invoice_Sale", invSale);
        editor.apply();
    }

    public String getTMInvSale() {
        return sharedPref.getString("Invoice_Sale", "0");
    }

    public void setPMInvSale(String invSale) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("PMInvoice_Sale", invSale);
        editor.apply();
    }

    public String getPMInvSale() {
        return sharedPref.getString("PMInvoice_Sale", "0");
    }


    public void storeLoginUser(SalRep user) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_id", user.getFREP_CODE());
        editor.putString("user_name", user.getFREP_NAME());
        editor.putString("user_password", user.getFREP_PASSWORD());
        editor.putString("user_prefix", user.getFREP_PREFIX());
        // editor.putString("user_type", user.getRepType());

        editor.apply();
    }

    public SalRep getLoginUser() {

        SalRep user = new SalRep();
        user.setFREP_CODE(sharedPref.getString("user_id", ""));
        user.setFREP_NAME(sharedPref.getString("user_name", ""));
        user.setFREP_PASSWORD(sharedPref.getString("user_password", ""));
        user.setFREP_PREFIX(sharedPref.getString("user_prefix", ""));
        //  user.setRepType(sharedPref.getString("user_type", ""));

        if (user.getFREP_CODE().equals("")) {
            return null;
        } else {
            return user;
        }
    }


    public void storeNewOTP(String otp) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_otp", otp);
        editor.apply();
    }

    public void setGlobalVal(String mKey, String mValue) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mKey, mValue);
        editor.commit();
    }

    public String getGlobalVal(String mKey) {
        return sharedPref.getString(mKey, "");
    }


    public void setIsChangedReceiptAmount( boolean mValue) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("receipAmtChanged", mValue);
        editor.commit();
    }

    public Boolean getIsChangedReceiptAmount() {
        return sharedPref.getBoolean("receipAmtChanged", false);
    }


//    public long generateOrderId() {
//        long order_id = sharedPref.getLong("order_id",0);
//        return (order_id < 0 ? -order_id : order_id);
//    }
//
//    public  void  setOrderId(long time){
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putLong("order_id", time);
//        editor.apply();
//    }
//    public long getEditOrderId() {
//        long order_id = sharedPref.getLong("edit_order_id",0);
//        return (order_id < 0 ? -order_id : order_id);
//    }
//    public  void  setEditOrderId(long time){
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putLong("edit_order_id", time);
//        editor.apply();
//    }
//
//    public void storePreviousRoute(Route route) {
//        if (route != null) {
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putInt("previous_route_id", route.getRouteId());
//            editor.putString("previous_route_name", route.getRouteName());
//            editor.putFloat("previous_route_fixed_target", (float) route.getFixedTarget());
//            editor.putFloat("previous_route_selected_target", (float) route.getSelectedTarget());
//            editor.apply();
//        }
//    }

//    public Route getPreviousRoute() {
//
//        Route route = new Route(sharedPref.getInt("previous_route_id", 0), sharedPref.getString("previous_route_name", null),
//                sharedPref.getFloat("previous_route_fixed_target", 0), sharedPref.getFloat("previous_route_selected_target", 0));
//        if (route.getRouteId() != 0) {
//            return route;
//        }
//
//        return null;
//    }

    public void setUserId(String position) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_id", position);
        editor.apply();
    }

    public String getUserId() {
        Log.d("check value>>>", sharedPref.getString("user_id", ""));
        return sharedPref.getString("user_id", "");
    }

    public void setUserPwd(String position) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_Pwd", position);
        editor.apply();
    }

    public void setIsFirstInstallation(boolean stat) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isFirst", stat);
        editor.apply();
    }

    public Boolean getIsFirstInstallation() {
        Log.d("check value>>>", sharedPref.getBoolean("isFirst", false)+"");
        return sharedPref.getBoolean("isFirst", false);
    }

    public String getUserPwd() {
        Log.d("check value>>>", sharedPref.getString("user_Pwd", ""));
        return sharedPref.getString("user_Pwd", "");
    }

    public void setUserPrefix(String prefix) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_prefix", prefix);
        editor.apply();
    }

    public String getUserPrefix() {
        Log.d("check value>>>", sharedPref.getString("user_prefix", ""));
        return sharedPref.getString("user_prefix", "");
    }

    public void clearPref() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("selected_out_id", "");
        editor.putString("selected_out_name", "");
        editor.putString("selected_out_route_code", "");
        editor.putString("selected_pril_code", "");
        editor.putString("UserType", "");
        editor.apply();
    }

    public int getSelectedOutletId() {
        return sharedPref.getInt("selected_out_id", 0);
    }

    public void setSelectedOutletId(int outletId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("selected_out_id", outletId);
        editor.apply();
    }

    public String getSelectedDebCode() {
        return sharedPref.getString("selected_out_id", "0");
    }

    public void setSelectedDebCode(String code) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("selected_out_id", code);
        editor.apply();
    }

    // to maintain image flag from firebase ----------------------------

    public String getImageFlag() {
        return sharedPref.getString("img_flag", "0");
    }

    public void setImageFlag(String code) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("img_flag", code);
        editor.apply();
    }
    //-------------------------------------------------------


    // to maintain image flag from firebase ----------------------------

    public String getFirebaseTokenKey() {
        return sharedPref.getString("tokenKey", "a1b2c3");
    }

    public void setFirebaseTokenKey(String token) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("tokenKey", token);
        editor.apply();
    }
    //-------------------------------------------------------

    public String getSelectedDebName() {
        return sharedPref.getString("selected_out_name", "0");
    }

    public void setSelectedDebName(String name) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("selected_out_name", name);
        editor.apply();
    }

    public String getGPSDebtor() {
        return sharedPref.getString("IS_GPS_DEBTOR", "0");
    }

    public void setGPSDebtor(String val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("IS_GPS_DEBTOR", val);
        editor.apply();
    }

    public String getGPSUpdated() {
        return sharedPref.getString("IS_GPS_UPDATED", "0");
    }

    public void setGPSUpdated(String val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("IS_GPS_UPDATED", val);
        editor.apply();
    }

    public String getDiscountClicked() {

        return sharedPref.getString("IS_DISCOUNT_CLICKED", "0");
    }

    public void setDiscountClicked(String val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("IS_DISCOUNT_CLICKED", val);
        editor.apply();
    }

    public String getHeaderNextClicked() {
        return sharedPref.getString("IS_HEADER_CLICKED", "0");
    }

    public void setHeaderNextClicked(String val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("IS_HEADER_CLICKED", val);
        editor.apply();
    }

    public String getSelectedDebRouteCode() {
        return sharedPref.getString("selected_out_route_code", "0");
    }

    public void setSelectedDebRouteCode(String code) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("selected_out_route_code", code);
        editor.apply();
    }

    public String getSelectedDebtorPrilCode() {
        return sharedPref.getString("selected_pril_code", "0");
    }

    public void setSelectedDebtorPrilCode(String prilCode) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("selected_pril_code", prilCode);
        editor.apply();
    }


    //    public int startDay() {
//
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putBoolean("day_status", true);
//
//        int session = sharedPref.getInt("local_session", 0) + 1;
//        editor.putInt("local_session", session);
//
//        long timeOut = TimeUtils.getDayEndTime(System.currentTimeMillis());
//
//        Log.d(LOG_TAG, "Setting timeout time at " + TimeUtils.formatDateTime(timeOut));
//
//        editor.putLong("login_timeout", timeOut);
//
//        editor.apply();
//
//        return session;
//    }
    public void setMacAddress(String MacAddress) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("MAC_Address", MacAddress);
        editor.apply();
    }

    public void setPrinterMacAddress(String MacAddress) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Print_MAC_Address", MacAddress);
        editor.apply();
    }

    public String getMacAddress() {
        return sharedPref.getString("MAC_Address", "");
    }

    public String getPrinterMacAddress() {
        return sharedPref.getString("Print_MAC_Address", "");
    }


    public long getLoginTimeout() {
        return sharedPref.getLong("login_timeout", 0);
    }

    public void endDay() {
        Log.d(LOG_TAG, "Ending Day");
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("day_status", false);
        //   storePreviousRoute(getSelectedRoute());
        editor.putInt("selected_route_id", 0);
        editor.putString("selected_route_name", null);
        editor.putFloat("selected_route_fixed_target", 0);
        editor.putFloat("selected_route_selected_target", 0);
        editor.apply();
    }

    public int getLocalSessionId() {
        return sharedPref.getInt("local_session", 0);
    }

//    public void setDayStatus(boolean isDayStarted) {
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putBoolean("day_status", isDayStarted);
//        editor.apply();
//    }

    public boolean isDayStarted() {
        return sharedPref.getBoolean("day_status", false);
    }

    public void setTransferToDealerList(boolean flag) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("transfer_to_dlist", flag);
        editor.apply();
    }

    public boolean getTransferToDealerList(boolean inverse) {

        boolean result = sharedPref.getBoolean("transfer_to_dlist", false);

        if (inverse) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("transfer_to_dlist", false);
            editor.apply();
        }

        return result;
    }

    public boolean validForLogin(int outletId) {
        String key = "outlet_changed_".concat(String.valueOf(outletId));
        int updatedCount = sharedPref.getInt(key, 0);

        return updatedCount <= 2;
    }

    public void notifyOutletHasChanged(int outletId) {
        String key = "outlet_changed_".concat(String.valueOf(outletId));
        int updatedCount = sharedPref.getInt(key, 0);
        updatedCount++;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, updatedCount);
        editor.apply();
    }

    //<editor-fold desc="Time Management">
    public void createInitialTimeVariables(long correctTime) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("app_start_time", correctTime);
        editor.putLong("app_start_elapsed_time", SystemClock.elapsedRealtime());
        editor.putLong("time_differential", 0);
        editor.apply();
    }

    public void calculateTimeDifferential(long changedTime, long nowElapsedTime) {
        long initialTime = sharedPref.getLong("app_start_time", 0);
        long initialElapsedTime = sharedPref.getLong("app_start_elapsed_time", 0);
//        long initialDifferential = sharedPref.getLong("time_differential", 0);

        long currentCorrectTime = initialTime + (nowElapsedTime - initialElapsedTime);

        // The difference between the correct time and the changed time
        long differential = changedTime - currentCorrectTime;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("time_differential", differential);

        // Don't apply, commit. We need immediate change in the file.
        editor.commit();

    }

    public void compensateForDeviceReboot() {

        long newCorrectTime = System.currentTimeMillis() + sharedPref.getLong("time_differential", 0);
        long newElapsedTime = SystemClock.elapsedRealtime();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("app_start_time", newCorrectTime);
        editor.putLong("app_start_elapsed_time", newElapsedTime);

        // Don't apply, commit. We need immediate change in the file.
        editor.commit();
    }

    public long getRealTimeInMillis() {
        return System.currentTimeMillis() + sharedPref.getLong("time_differential", 0);
    }
    //</editor-fold>

    public void setPointingLocationIndex(int index) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("pointing_location", index);
        editor.apply();
    }

    public int getPointingLocationIndex() {
        return sharedPref.getInt("pointing_location", 0);
    }

    public void setBaseURL(String baseURL) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("baseURL", baseURL);
        editor.apply();
    }

    public String getBaseURL() {


        /*****Live****/ // return sharedPref.getString("baseURL", "http://123.231.15.146:1030");//Dialog
        /*****Live****/ //return sharedPref.getString("baseURL", "http://124.43.5.227:1030");//SLT

        /******Test******/  return sharedPref.getString("baseURL", "http://124.43.5.227:1031");//tesing SLT

       //  return sharedPref.getString("baseURL", "http://192.168.0.5:1035");


    }

    public void setConnectionName(String connectionName) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("connectionName", connectionName);
        editor.apply();
    }

    public String getConnectionName() {
        //return sharedPref.getString("baseURL", "https://19920502.000webhostapp.com");
        //return sharedPref.getString("baseURL", "http://203.143.21.121:8080");
        // return sharedPref.getString("baseURL", "http://13.76.45.176:1010");
        //  return sharedPref.getString("baseURL", "http://124.43.5.227:1030");
        return sharedPref.getString("connectionName", "Dialog Connection");
        //  return sharedPref.getString("baseURL", "http://192.168.0.5:1035");
        // return sharedPref.getString("baseURL", "http://123.231.15.146:8080");
        //return sharedPref.getString("baseURL", "http://192.168.43.62");

    }
    public void setCostCode(String costcd) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CostCode", costcd.trim());
        editor.apply();
    }

    public String getCostCode() {

        return sharedPref.getString("CostCode", "");

    }

    public String getConsoleDB() {
//        return sharedPref.getString("Console_DB", "KFD_Test");
        return sharedPref.getString("Console_DB", "KFD_NEW");
    }

    public void setDistDB(String dist) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Dist_DB", dist);
        editor.apply();
    }

    public String getDistDB() {

         /****Live****/  //return sharedPref.getString("Dist_DB", "KFD_NEW");

         /****Test****/  return sharedPref.getString("Dist_DB", "KFD_Test_SFA");
    }

    public void setCurrentMillage(double millage) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("millage", (float) millage);
        editor.apply();
    }

    public double getPrevoiusMillage() {
        return sharedPref.getFloat("millage", 0);
    }


    public void setVersionName(String versionName) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("app_version_name", versionName).commit();
    }

    public String getPayMode() {
        return sharedPref.getString("paymode", "");
    }

    public void setPayMode(String payMode) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("paymode", payMode).commit();
    }

    public String getVersionName() {
        return sharedPref.getString("app_version_name", "0.0.0");
    }

    public Boolean getOrderHeaderNextClicked() {
        return sharedPref.getBoolean("IS_ORDER_HEADER_CLICKED", false);
    }

    public void setOrdertHeaderNextClicked(boolean val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_ORDER_HEADER_CLICKED", val);
        editor.apply();
    }


    public Boolean getReceiptHeaderNextClicked() {
        return sharedPref.getBoolean("IS_ORDER_HEADER_CLICKED", false);
    }

    public void setReceipttHeaderNextClicked(boolean val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_ORDER_HEADER_CLICKED", val);
        editor.apply();
    }

    public Boolean getUpdateClicked() {
        return sharedPref.getBoolean("IS_UPDATE_CLICKED", false);
    }

    public void setUpdateClicked(boolean val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IS_UPDATE_CLICKED", val);
        editor.apply();
    }

    public Boolean getIsQuantityAdded() {
        return sharedPref.getBoolean("IsQuantityAdded", false);
    }

    public void setIsQuantityAdded(Boolean val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IsQuantityAdded", val);
        editor.apply();
    }

    public void setActiveStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("activeStatus", status);
        editor.apply();
    }

    public boolean getActiveStatus() {
        return sharedPref.getBoolean("activeStatus", false);
    }

    public void setSelectedTitle(String title) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("selectedTitle", title);
        editor.apply();
    }

    public String getSelectedTitle() {
        return sharedPref.getString("selectedTitle", null);
    }

    public void setLocCode(String costcd) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("LocCode", costcd.trim());
        editor.apply();
    }

    public String getLocCode() {

        return sharedPref.getString("LocCode", "");

    }
}

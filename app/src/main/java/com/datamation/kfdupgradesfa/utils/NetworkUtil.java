package com.datamation.kfdupgradesfa.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.datamation.kfdupgradesfa.helpers.ValueHolder;


/**
 *Created by Rashmi
 * Handles network based common functions.
 */

public class NetworkUtil {
    public static int TYPE_WIFI = 2;
    public static int TYPE_MOBILE = 1;
    public static int TYPE_NOT_CONNECTED = 0;
    /**
     * Function to check if network connectivity is available.
     * @param context The context of the application.
     * @return TRUE if network connectivity is available and FALSE if not.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    //------------------------------------------------------------------------------------------------------------------------------------
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            //status = "Wifi enabled";
            status = ValueHolder.CONNECT_TO_WIFI;
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            //status = "Mobile data enabled";
            status = getNetworkClass(context);
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = ValueHolder.NOT_CONNECT;
        }
        return status;
    }

    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info==null || !info.isConnected())
            return "-"; //not connected
        if(info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if(info.getType() == ConnectivityManager.TYPE_MOBILE){
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }

    public static boolean isNotPoorConnection(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get the active network
        Network network = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            network = connectivityManager.getActiveNetwork();
        }

        if (network != null) {
            // Get the network capabilities
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);

            if (networkCapabilities != null) {
                // Check if the network is capable of measuring bandwidth
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                    // Get download and upload speeds in bits per second
                    int downloadSpeedMbps = networkCapabilities.getLinkDownstreamBandwidthKbps();
                    int uploadSpeedMbps = networkCapabilities.getLinkUpstreamBandwidthKbps();

                    // Convert speeds to Mbps
                    double downloadSpeedMbpsss = downloadSpeedMbps / 1024.0;
                    double uploadSpeedMbpssss = uploadSpeedMbps / 1024.0;
                    double roundedSpeed = Math.round(uploadSpeedMbpssss);
                    Log.wtf("Network Util Class", String.valueOf(roundedSpeed));
                    Toast.makeText(context,"Network Speed - "+ String.valueOf(roundedSpeed + "Mbps"),Toast.LENGTH_LONG).show();

                    if (roundedSpeed >= 5.00)
                    {
                        return true;
                    }
                }
            }
            else
            {
                return false;
            }
        }
        return false;
    }
}

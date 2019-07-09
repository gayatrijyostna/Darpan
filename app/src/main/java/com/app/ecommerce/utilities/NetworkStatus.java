package com.app.ecommerce.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus
{

    // method to check internet connection
    public static boolean checkInternetConenction(Activity activity)
    {

        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean isNetworkAvailable(Activity activity)
    {
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connectivity.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connectivity.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connectivity.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else if
                (
                connectivity.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connectivity.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

}

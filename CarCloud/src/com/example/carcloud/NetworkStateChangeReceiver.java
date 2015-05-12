package com.example.carcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetworkStateChangeReceiver 
extends BroadcastReceiver {
   /**
    * Debugging tag used by the Android logger.
    */ 
   private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
//        WifiManager wifiManager = (WifiManager) context
//                .getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = intent
                .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (networkInfo != null) {
            Log.d(TAG, "Type : " + networkInfo.getType()
                    + "State : " + networkInfo.getState());
            /*
             * only report if the wifi status changes
             */
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                //get the different network states
                if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                    // call service factory method to report this change
                    
                } else if (networkInfo.getState() == NetworkInfo.State.CONNECTED){
                    // call service factory method to report this change
                }    
            }
        } // do nothing if no networkInfo
    }

}

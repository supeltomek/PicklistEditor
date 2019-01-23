package com.ts.mobilepicklist.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ts250231 on 2015-04-29.
 */
public class WifiConnected {

    private static WifiConnected instance;
    private ConnectivityManager connectivityManager;
    private NetworkInfo mWifi;
    private Context context;


    WifiConnected(Context context){
        this.context = context;
        init();
    }

    private void init(){
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }
    public boolean isConnected(){
        if(mWifi.isConnected()){
            return true;
        }
        return false;
    }
}

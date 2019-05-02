package com.example.haeseong.projectline1.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//서비스로 처리해야 하나??

public class NetworkHelper {
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 3;
    public static int networkStatus;
    public static int getConnectivityStatus(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null)
        {
            int type = networkInfo.getType();
            if(type == ConnectivityManager.TYPE_MOBILE)
            {
                return TYPE_MOBILE;
            }else if(type == ConnectivityManager.TYPE_WIFI)
            {
                return TYPE_WIFI;
            }
        }
        return TYPE_NOT_CONNECTED;
    }
}

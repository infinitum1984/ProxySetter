package com.infinitum_micro_tech.proxy_setter_pro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import com.infinitum_micro_tech.proxy_setter_pro.Proxy.ProxyData;
import com.infinitum_micro_tech.proxy_setter_pro.Proxy.ProxySetter;

import java.util.List;



public class ConnectionStatictic {
    final static String TAG = "myapp";

    final static String con_use_static_proxy="ST";
    final static String con_dont_use_static_proxy="NO";

    public static ProxyData NowProxyConnection(Context c) {
        ProxyData proxyData = new ProxyData();

        WifiManager manager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = ProxySetter.GetCurrentWifiConfiguration(manager);
        String s = config.toString();


        String Proxy_settings = s.substring(s.indexOf("Proxy settings: ") + 16, s.indexOf("Proxy settings: ") + 18);
        Log.d(TAG, "NowProxyConnection: " + Proxy_settings);

        if (Proxy_settings.equals(con_use_static_proxy)) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ProxyInfo info = config.getHttpProxy();
                proxyData.host = info.getHost();
                proxyData.port = info.getPort();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //Log.d(TAG, "NowProxyConnection: full_inf" + s); //android 7.1.1 OK!
                   // Log.d(TAG, "NowProxyConnection: "+s);
                    String proxy = s.substring(s.indexOf("HTTP proxy: ")+13,s.length());
                    //Log.d(TAG, "NowProxyConnection: "+proxy);
                    proxyData.host=proxy.substring(0,proxy.indexOf("]"));
                    int begin =(proxy.indexOf("] ")+2);

                    int port=0;
                    for (int i = begin+1 ; i < proxy.length()-1; i++) {
                        String num=proxy.substring(begin,i);

                        try {
                            port=Integer.parseInt(num);
                        }catch (Exception e){
                            break;
                        }
                    }
                    proxyData.port=port;
                    Log.d(TAG, "NowProxyConnection: "+proxyData.host+":"+String.valueOf(proxyData.port));
                } else {

                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                        String proxy = s.substring(s.indexOf("HttpProxy: ")+13,s.lastIndexOf(" }"));
                        //Log.d(TAG, "NowProxyConnection: "+proxy);
                        proxyData.host=proxy.substring(0,proxy.indexOf("]"));
                        proxyData.port=Integer.parseInt(proxy.substring(proxy.indexOf("] ")+2,proxy.length()));
                        Log.d(TAG, "NowProxyConnection: "+proxyData.host+":"+String.valueOf(proxyData.port));

                    }
                    if (Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
                        Log.d(TAG, "NowProxyConnection: "+s);
                        String proxy = s.substring(s.indexOf("HttpProxy: ")+12,s.length());
                        //Log.d(TAG, "NowProxyConnection: "+proxy);
                        proxyData.host=proxy.substring(0,proxy.indexOf("]"));
                        int begin =(proxy.indexOf("] ")+2);

                        int port=0;
                        for (int i = begin+1 ; i < proxy.length()-1; i++) {
                           String num=proxy.substring(begin,i);

                            try {
                                port=Integer.parseInt(num);
                            }catch (Exception e){
                                break;
                            }
                        }
                        proxyData.port=port;
                        Log.d(TAG, "NowProxyConnection: "+proxyData.host+":"+String.valueOf(proxyData.port));
                    }
                }
            }
     }else {
         proxyData.proxy_is=false;

     }
    return proxyData;


}

    public static boolean WifiConnected(Context c){
        WifiManager manager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);

            ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo != null && wifiInfo.isConnected())
            {
                return true;
            }


            return false;
        }

    public static boolean NetworkIs(Context c,String ssid){

        WifiManager manager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configurationList = manager.getConfiguredNetworks();
        Log.d(TAG, "NetworkIs: num configuration "+configurationList.size());
        for (int i = 0; i < configurationList.size(); ++i)
        {
            WifiConfiguration wifiConfiguration = configurationList.get(i);
            Log.d(TAG, "NetworkIs: con_name "+wifiConfiguration.SSID);
            if (wifiConfiguration.SSID.substring(1,wifiConfiguration.SSID.length()-1).equals(ssid)){
                Log.d(TAG, "NetworkIs: this network is");
                return true;
            }
        }
        Log.d(TAG, "NetworkIs: this network not is");

        return false;

    }
}

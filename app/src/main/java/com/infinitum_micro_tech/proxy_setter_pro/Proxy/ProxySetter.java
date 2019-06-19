package com.infinitum_micro_tech.proxy_setter_pro.Proxy;

import android.content.Context;
import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ProxySetter {
    static final String AppDom="com.infinitum_micro_tech.proxy_setter_pro";
    static final String TAG = "myapp";
    private static int networkID = -1;

    public static boolean MyNetwork(Context c){
        WifiManager manager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);

        WifiConfiguration curr = GetCurrentWifiConfiguration(manager);
        if (curr==null){return false;}
        String s =curr.toString();
        s=s.substring(s.indexOf("cname"),s.indexOf("luid"));
        s=s.replace("cname=","").trim();
        Log.d(TAG,"creator: "+s);
        if (s.equals(AppDom)){
            return true;
        }
        return false;
    }
   public static WifiConfiguration GetCurrentWifiConfiguration(WifiManager manager)
    {
        if (!manager.isWifiEnabled())
            return null;

        List<WifiConfiguration> configurationList = manager.getConfiguredNetworks();
        WifiConfiguration configuration = null;
        int cur = manager.getConnectionInfo().getNetworkId();
        for (int i = 0; i < configurationList.size(); ++i)
        {
            WifiConfiguration wifiConfiguration = configurationList.get(i);
            if (wifiConfiguration.networkId == cur)
                configuration = wifiConfiguration;
        }

        return configuration;
    }
    // My Network

    public static void SetProxyWifi(Context c,String host, int port){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            setWifiProxySettings6(c,host,port);
        }else {
            if (Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP) {
                setWifiProxySettings5(c, host, port);
            }else {

                Log.d(TAG, "SetProxy...");

                setWifiProxySettings(c, host, port);
            }
        }
    }
    private static void setWifiProxySettings5(Context c,String host,int port)
    {
        //get the current wifi configuration
        WifiManager manager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = GetCurrentWifiConfiguration(manager);
        if(null == config)
            return;

        try
        {
            //linkProperties is no longer in WifiConfiguration
            Class proxyInfoClass = Class.forName("android.net.ProxyInfo");
            Class[] setHttpProxyParams = new Class[1];
            setHttpProxyParams[0] = proxyInfoClass;
            Class wifiConfigClass = Class.forName("android.net.wifi.WifiConfiguration");
            Method setHttpProxy = wifiConfigClass.getDeclaredMethod("setHttpProxy", setHttpProxyParams);
            setHttpProxy.setAccessible(true);

            //Method 1 to get the ENUM ProxySettings in IpConfiguration
            Class ipConfigClass = Class.forName("android.net.IpConfiguration");
            Field f = ipConfigClass.getField("proxySettings");
            Class proxySettingsClass = f.getType();

            //Method 2 to get the ENUM ProxySettings in IpConfiguration
            //Note the $ between the class and ENUM
            //Class proxySettingsClass = Class.forName("android.net.IpConfiguration$ProxySettings");

            Class[] setProxySettingsParams = new Class[1];
            setProxySettingsParams[0] = proxySettingsClass;
            Method setProxySettings = wifiConfigClass.getDeclaredMethod("setProxySettings", setProxySettingsParams);
            setProxySettings.setAccessible(true);


            ProxyInfo pi = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                pi = ProxyInfo.buildDirectProxy(host,port);
            }
            //Android 5 supports a PAC file
            //ENUM value is "PAC"
            //ProxyInfo pacInfo = ProxyInfo.buildPacProxy(Uri.parse("http://localhost/pac"));

            //pass the new object to setHttpProxy
            Object[] params_SetHttpProxy = new Object[1];
            params_SetHttpProxy[0] = pi;
            setHttpProxy.invoke(config, params_SetHttpProxy);

            //pass the enum to setProxySettings
            Object[] params_setProxySettings = new Object[1];
            params_setProxySettings[0] = Enum.valueOf((Class<Enum>) proxySettingsClass, "STATIC");
            setProxySettings.invoke(config, params_setProxySettings);

            //save the settings
            manager.updateNetwork(config);
            manager.disconnect();
            manager.reconnect();
        }
        catch(Exception e)
        {
            Log.v("wifiProxy", e.toString());
        }
    }

   private static void setWifiProxySettings6(Context c,String host,int port) {
        WifiManager manager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config=GetCurrentWifiConfiguration(manager);
        try
        {
            Class proxySettings = Class.forName("android.net.IpConfiguration$ProxySettings");

            Class[] setProxyParams = new Class[2];
            setProxyParams[0] = proxySettings;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setProxyParams[1] = ProxyInfo.class;
            }

            Method setProxy = config.getClass().getDeclaredMethod("setProxy", setProxyParams);
            setProxy.setAccessible(true);

            ProxyInfo desiredProxy = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                desiredProxy = ProxyInfo.buildDirectProxy(host, port);
            }

            Object[] methodParams = new Object[2];
            methodParams[0] = Enum.valueOf(proxySettings, "STATIC");
            methodParams[1] = desiredProxy;

            setProxy.invoke(config, methodParams);

            manager.updateNetwork(config);
            //save the settings
            manager.disconnect();

            manager.reconnect();
            Log.d(TAG,"ProxySetter set ("+host+":"+String.valueOf(port)+") on: "+config.SSID);
        }
        catch(Exception e)
        {
            Log.d(TAG,"error: "+e.getMessage().toString());

        }

    }
    // Not My Network
   public static void SetProxyWifi(Context c,String host, int port, String ssid,String password){
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            setWifiProxySettings6(c,host,port, ssid,password);
        }else {

            setWifiProxySettings(c,host,port);
        }
    }


  private   static void setWifiProxySettings(Context c,String host,int port)
    {
        //get the current wifi configuration
        WifiManager manager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = GetCurrentWifiConfiguration(manager);
        if(null == config)
            return;

        try
        {
            //get the link properties from the wifi configuration
            Object linkProperties = getField(config, "linkProperties");
            if(null == linkProperties)
                return;

            //get the setHttpProxy method for LinkProperties
            Class proxyPropertiesClass = Class.forName("android.net.ProxyProperties");
            Class[] setHttpProxyParams = new Class[1];
            setHttpProxyParams[0] = proxyPropertiesClass;
            Class lpClass = Class.forName("android.net.LinkProperties");
            Method setHttpProxy = lpClass.getDeclaredMethod("setHttpProxy", setHttpProxyParams);
            setHttpProxy.setAccessible(true);

            //get ProxyProperties constructor
            Class[] proxyPropertiesCtorParamTypes = new Class[3];
            proxyPropertiesCtorParamTypes[0] = String.class;
            proxyPropertiesCtorParamTypes[1] = int.class;
            proxyPropertiesCtorParamTypes[2] = String.class;

            Constructor proxyPropertiesCtor = proxyPropertiesClass.getConstructor(proxyPropertiesCtorParamTypes);

            //create the parameters for the constructor
            Object[] proxyPropertiesCtorParams = new Object[3];
            proxyPropertiesCtorParams[0] = host;
            proxyPropertiesCtorParams[1] = port;
            proxyPropertiesCtorParams[2] = null;

            //create a new object using the params
            Object proxySettings = proxyPropertiesCtor.newInstance(proxyPropertiesCtorParams);

            //pass the new object to setHttpProxy
            Object[] params = new Object[1];
            params[0] = proxySettings;
            setHttpProxy.invoke(linkProperties, params);

            setProxySettings("STATIC", config);

            //save the settings
            manager.updateNetwork(config);
            manager.disconnect();
            manager.reconnect();
        }
        catch(Exception e)
        {
        }
    }

   private static void setWifiProxySettings6(Context c,String host,int port,String ssid,String password) {
        WifiManager manager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config=new WifiConfiguration();
        config.SSID="\""+ssid+"\"";
        if (!password.equals("")) {
            config.preSharedKey = "\"" + password + "\"";
            Log.d(TAG, "setWifiProxySettings6: pass "+password);

        }
        try
        {
            Class proxySettings = Class.forName("android.net.IpConfiguration$ProxySettings");

            Class[] setProxyParams = new Class[2];
            setProxyParams[0] = proxySettings;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setProxyParams[1] = ProxyInfo.class;
            }

            Method setProxy = config.getClass().getDeclaredMethod("setProxy", setProxyParams);
            setProxy.setAccessible(true);

            ProxyInfo desiredProxy = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                desiredProxy = ProxyInfo.buildDirectProxy(host, port);
            }

            Object[] methodParams = new Object[2];
            methodParams[0] = Enum.valueOf(proxySettings, "STATIC");
            methodParams[1] = desiredProxy;

            setProxy.invoke(config, methodParams);
            manager.addNetwork(config);
            //save the settings
            manager.enableNetwork(config.networkId,true);
            manager.disconnect();


            manager.reconnect();
            Log.d(TAG,"Network is add: "+config.SSID);
            Log.d(TAG,"ProxySetter set on: "+config.SSID);

        }
        catch(Exception e)
        {
            Log.d(TAG,"error: "+e.getMessage().toString());

        }

    }

    private static Object getField(Object obj, String name)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Field f = obj.getClass().getField(name);
        Object out = f.get(obj);
        return out;
    }

    private static void setEnumField(Object obj, String value, String name)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Field f = obj.getClass().getField(name);
        f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
    }

    private static void setProxySettings(String assign , WifiConfiguration wifiConf)
            throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
        setEnumField(wifiConf, assign, "proxySettings");
    }

    public static void unsetProxySettings(Context c)
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
          unsetWifiProxySettings6(c);
        }else {
            if(Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP){
                unsetWifiProxySettings5(c);
            }else {
                unsetWifiProxySettings(c);
            }
        }
    }
  private static void unsetWifiProxySettings(Context c)
    {
        WifiManager manager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = GetCurrentWifiConfiguration(manager);
        if(null == config)
            return;

        try
        {
            //get the link properties from the wifi configuration
            Object linkProperties = getField(config, "linkProperties");
            if(null == linkProperties)
                return;

            //get the setHttpProxy method for LinkProperties
            Class proxyPropertiesClass = Class.forName("android.net.ProxyProperties");
            Class[] setHttpProxyParams = new Class[1];
            setHttpProxyParams[0] = proxyPropertiesClass;
            Class lpClass = Class.forName("android.net.LinkProperties");
            Method setHttpProxy = lpClass.getDeclaredMethod("setHttpProxy", setHttpProxyParams);
            setHttpProxy.setAccessible(true);

            //pass null as the proxy
            Object[] params = new Object[1];
            params[0] = null;
            setHttpProxy.invoke(linkProperties, params);

            setProxySettings("NONE", config);
            Log.d(TAG,"ProxySetter unset: "+config.SSID);

            //save the config
            manager.updateNetwork(config);
            manager.disconnect();
            manager.reconnect();
        }
        catch(Exception e)
        {
            Log.d(TAG,"error: "+e.getMessage().toString());

        }
    }
    private static void unsetWifiProxySettings5(Context c){
        //get the current wifi configuration
        WifiManager manager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = GetCurrentWifiConfiguration(manager);
        if(null == config)
            return;

        try
        {
            //linkProperties is no longer in WifiConfiguration
            Class proxyInfoClass = Class.forName("android.net.ProxyInfo");
            Class[] setHttpProxyParams = new Class[1];
            setHttpProxyParams[0] = proxyInfoClass;
            Class wifiConfigClass = Class.forName("android.net.wifi.WifiConfiguration");
            Method setHttpProxy = wifiConfigClass.getDeclaredMethod("setHttpProxy", setHttpProxyParams);
            setHttpProxy.setAccessible(true);

            //Method 1 to get the ENUM ProxySettings in IpConfiguration
            Class ipConfigClass = Class.forName("android.net.IpConfiguration");
            Field f = ipConfigClass.getField("proxySettings");
            Class proxySettingsClass = f.getType();

            //Method 2 to get the ENUM ProxySettings in IpConfiguration
            //Note the $ between the class and ENUM
            //Class proxySettingsClass = Class.forName("android.net.IpConfiguration$ProxySettings");

            Class[] setProxySettingsParams = new Class[1];
            setProxySettingsParams[0] = proxySettingsClass;
            Method setProxySettings = wifiConfigClass.getDeclaredMethod("setProxySettings", setProxySettingsParams);
            setProxySettings.setAccessible(true);



            Object[] params_setProxySettings = new Object[1];
            params_setProxySettings[0] = Enum.valueOf((Class<Enum>) proxySettingsClass, "NONE");
            setProxySettings.invoke(config, params_setProxySettings);

            Log.d(TAG, "unsetWifiProxySettings5: restart network");
            //save the settings
            manager.updateNetwork(config);
            manager.disconnect();
            manager.reconnect();
        }
        catch(Exception e)
        {
            Log.v("wifiProxy", e.toString());
        }
    }
   private static void unsetWifiProxySettings6(Context c){
        WifiManager manager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config=GetCurrentWifiConfiguration(manager);

        try
        {

            Class proxySettings = Class.forName("android.net.IpConfiguration$ProxySettings");

            Class[] setProxyParams = new Class[2];
            setProxyParams[0] = proxySettings;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setProxyParams[1] = ProxyInfo.class;
            }

            Method setProxy = config.getClass().getDeclaredMethod("setProxy", setProxyParams);
            setProxy.setAccessible(true);



            Object[] methodParams = new Object[2];
            methodParams[0] = Enum.valueOf(proxySettings, "NONE");

            setProxy.invoke(config, methodParams);

            Log.d(TAG,"ProxySetter unset: "+config.SSID);

            //save the config
            manager.updateNetwork(config);
            manager.disconnect();
            manager.reconnect();
        }
        catch(Exception e)
        {
            Log.d(TAG,"error: "+e.getMessage().toString());

        }
    }
   public static void setProxyMobile(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String activeSIMNetwork = tm.getNetworkOperator();
        Log.d(TAG,activeSIMNetwork);

    }


}

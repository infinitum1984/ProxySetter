package com.infinitum_micro_tech.proxy_setter_pro.Proxy;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import com.infinitum_micro_tech.proxy_setter_pro.Config;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class Proxy{
   public static String NowHost;
   public static int NowPort;


    public static String pars_url(){
        return ProxyFindConfig.pars_url();
    }

    public static ProxyData getMyProxy(){
        ProxyData proxyData= new ProxyData();
        MyProxyData myProxyData=null;
        for (int i = 0; i < ProxyFindConfig.my_proxys.size(); i++) {
            if (ProxyFindConfig.my_proxys.get(i).isactive){
                myProxyData=ProxyFindConfig.my_proxys.get(i);
                break;
            }
        }
        proxyData.host=myProxyData.host_proxy;
        proxyData.port=myProxyData.port_proxy;
        return proxyData;
    }
    public static ArrayList<ProxyData> returnProxyList(Context c, boolean get_from_network){
        return ProxyFinder.ReturnProxyList(c,get_from_network);
    }




    public static boolean ItsMyNetwork(Context c){
        WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        String s = GetCurrWifiConfig(wifiManager).SSID;
        Log.d("myapp", "ItsMyNetwork: MyNetworks "+Config.MyNetworks+" "+Config.MyNetworks.contains(s.substring(1,s.length()-1))+"\n ProxySetter: "
                +ProxySetter.MyNetwork(c));
        return (Config.MyNetworks.contains(s.substring(1,s.length()-1)))&&(ProxySetter.MyNetwork(c));
    }

    public static void SetProxy(Context c,String host, int port){
        ProxySetter.SetProxyWifi(c,host,port);
    }
    public static void SetProxy(Context c,String host, int port,String ssid, String key){
        ProxySetter.SetProxyWifi(c,host,port,ssid,key);
    }

    public static void UnsetProxy(Context c){
        ProxySetter.unsetProxySettings(c);

    }
    public static ProxyData FindProxy(String TestLink,  Context c,boolean get_from_network){
        return ProxyFinder.findNewBestProxy(TestLink,c,get_from_network);
    }
    public static WifiConfiguration GetCurrWifiConfig(WifiManager wifiManager){
        return ProxySetter.GetCurrentWifiConfiguration(wifiManager);
    }
    public static void
    SaveProxyXML(Context c,String pars_url){
        SaveProxy saveProxy = new SaveProxy(c,pars_url);
        saveProxy.execute();
    }
    static class SaveProxy extends AsyncTask<Void, Void, Void> {


        Context c;
        String pars_url="";
        SaveProxy(Context context,String p){
            c=context;
            pars_url=p;
        }
        Document document =null;

        @Override
        protected Void doInBackground(Void... params) {


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            try {
                document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());
                Log.d("myapp", "doInBackground: ppars_usrl "+pars_url);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Time time_now = new Time();   time_now.setToNow();
            Config.SaveLastTime(c,time_now);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProxyFinder.saveFile(c,document);

        }
    }

}

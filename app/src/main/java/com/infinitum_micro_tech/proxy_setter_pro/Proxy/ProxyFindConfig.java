package com.infinitum_micro_tech.proxy_setter_pro.Proxy;

import android.util.Log;

import com.infinitum_micro_tech.proxy_setter_pro.Country;

import java.util.ArrayList;


public class ProxyFindConfig {
    public static ArrayList<Country> countrys_list;
    public static ArrayList<MyProxyData> my_proxys;

    public static boolean use_best_proxy;
    public static boolean use_port_filter;
    public static boolean use_country_filter;
    public static boolean use_type_filter;
    public static boolean use_low_delay_proxy;
    public static boolean   use_anon_filter;
    public static boolean use_my_proxy;


    public static String use_ports_string="";
    public static String use_countrys_string="";
    public static String use_anon_string="";
    public static String use_types_string="";
    public static String use_delay_string="";

    public static byte type_http=0;
    public static byte type_https=0;
    public static byte type_socks4=0;
    public static byte type_socks5=0;


    public static boolean use_special_url;
    public static String special_url;


    public static String[] use_countrys;

    final static String TAG="mayapp";
    public static String pars_url(){
        String s="http://hidemy.name/en/api/proxylist.php?out=xml&maxtime=350&code=440181633597239";
        if (use_port_filter){
            if (!use_ports_string.equals("")) {
                s = s + "&ports=" + use_ports_string;
            }
        }
        if ((!use_types_string.equals(""))&&use_type_filter){
            s=s+"&type="+use_types_string;
        }
        Log.d(TAG, "pars_url: "+s);


        return s;
    }
}

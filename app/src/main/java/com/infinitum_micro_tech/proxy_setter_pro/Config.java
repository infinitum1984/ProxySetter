package com.infinitum_micro_tech.proxy_setter_pro;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.util.Log;

import com.infinitum_micro_tech.proxy_setter_pro.Proxy.MyProxyData;
import com.infinitum_micro_tech.proxy_setter_pro.Proxy.ProxyData;
import com.infinitum_micro_tech.proxy_setter_pro.Proxy.ProxyFindConfig;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



import static android.content.Context.MODE_PRIVATE;

public class Config {
    public static String language;
    public static String MyNetworks;
    public static boolean its_first_setting_open;
    static final String key_use_best_broxy = "use_best_proxy";
    static final String key_use_country_filter= "use_country_filter";
    static final String key_use_port_filter= "use_port_filter";
    static final String key_use_type_filter= "use_type_filter";
    static final String key_use_low_delay="low_delay_proxy";


    static final String key_use_anon_filter="use_anon_filter";

    static final String key_use_ports= "use_ports";
    static final String key_use_types="use_types";

    static final String key_use_special_url="use_special_url";
    static final String key_special_url="special_url";
    static final String key_use_anon_string="use_anon_string";
    static final String key_filter_country="country_filter";
    static final String key_use_my_proxy="use_my_proxy";

    static final String key_host="HOST";
    static final String key_port="PORT";
    static final String key_type="TYPE";
    static final  String key_anon="ANON";
    static  final String key_country="COUNTRY";
    static final String key_delay="DELAY";

    static final String key_time_year="TIME_YEAR";
    static final String key_time_month="TIME_MONTHE";
    static final String key_time_day="TIME_DAY";
    static final String key_time_hour="TIME_HOUR";
    static final String key_time_minute="TIME_MINUTE";

   public static ArrayList<Language> languageArrayList;

    final static String[][] languages = {{
            "Deutsch", "English", "Español","Français","Italiano","Português","Русский"},{"de","en","es","fr","it","pt","ru"}};

    final static String[][] countrys = {{
            "Argentina", "France", "Germany", "India",
            "Indonesia","Italy","Netherlands","Poland","Russian Federation","Singapore","Thailand","Ukraine","United Kingdom","United States"},{"AR","FR","DE","IN","ID"

            ,"IT","NL","PL","RU","SG","TH","UA","GB","US"}};
    private static final String TAG = "myapp";

//This ass.

    public static void ConfigInit(Context c){
        InitLanguage(c);
        InitUseBestProxy(c);
        InitUseCountryFilter(c);
        InitUsePortFilter(c);
        InitUseTypeFilter(c);
        InitUseSpecialUrl(c);
        InitUseLowDelayProxy(c);
        InitUseAnonFilter(c);

        InitUseTypes(c);
        InitUsePorts(c);
        InitSpecialUrl(c);
        InitAnon(c);
        InitCountrys(c);
        InitMyProxys(c);
        InitUseMyProxy(c);
        InitMyNetworks(c);
        InitIsFirstSettingOpen(c);
        InitLanguagesArray(c);


    }

    //Init ProxyFindConfig
    public static void InitUseBestProxy(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);

        ProxyFindConfig.use_best_proxy=sharedPreferences.getBoolean(key_use_best_broxy,true);
    }
    public static void InitUseCountryFilter(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);

        ProxyFindConfig.use_country_filter=sharedPreferences.getBoolean(key_use_country_filter,false);

    }
    public static void InitUsePortFilter(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);

        ProxyFindConfig.use_port_filter=sharedPreferences.getBoolean(key_use_port_filter,false);
    }
    public static void InitUseTypeFilter(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);

        ProxyFindConfig.use_type_filter=sharedPreferences.getBoolean(key_use_type_filter,false);
    }
    public static void  InitUsePorts(Context c){
      SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
      String s = sharedPreferences.getString(key_use_ports,"");
      ProxyFindConfig.use_ports_string=s;

    }
    public static void  InitUseTypes(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        String s = sharedPreferences.getString(key_use_types,"");
        if (s.equals("")){
            s="hs";
        }
        String mas =s;
        if (mas.contains("h")){
            ProxyFindConfig.type_http=1;
        }
        if (mas.contains("s")){
            ProxyFindConfig.type_https=1;
        }
        if (mas.contains("4")){
            ProxyFindConfig.type_socks4=1;
        }
        if (mas.contains("5")){
            ProxyFindConfig.type_socks5=1;
        }

        ProxyFindConfig.use_types_string=s;

    }
    public static void  InitUseSpecialUrl(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);

        ProxyFindConfig.use_special_url=sharedPreferences.getBoolean(key_use_special_url,false);

    }
    public static void InitSpecialUrl(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        String s = sharedPreferences.getString(key_special_url,"");
        ProxyFindConfig.special_url=s;
    }
    public static void InitUseLowDelayProxy(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);

        ProxyFindConfig.use_low_delay_proxy=sharedPreferences.getBoolean(key_use_low_delay,false);

    }
    public static void InitUseAnonFilter(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);

        ProxyFindConfig.use_anon_filter=sharedPreferences.getBoolean(key_use_anon_filter,false);
    }
    public static void InitAnon(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        String s = sharedPreferences.getString(key_use_anon_string,"1234");
        ProxyFindConfig.use_anon_string=s;
    }
    public static void InitCountrys(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        String s = sharedPreferences.getString(key_filter_country,"");
        ProxyFindConfig.use_countrys_string=s;

        ProxyFindConfig.countrys_list=new ArrayList<>();
        ArrayList<Country> arr=new ArrayList<>();

        for (int i = 0; i < countrys[0].length; i++) {
            Country country = new Country(countrys[1][i],countrys[0][i],!s.contains(countrys[1][i]));



            arr.add(country);


        }
        ProxyFindConfig.countrys_list=arr;
    }
    public static void InitUseMyProxy(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);

        ProxyFindConfig.use_my_proxy=sharedPreferences.getBoolean(key_use_my_proxy,false);
    }
    public static void InitMyNetworks(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);

        MyNetworks=sharedPreferences.getString("my_networks","");
    }
    public static void InitIsFirstSettingOpen(Context c){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);

       its_first_setting_open=sharedPreferences.getBoolean("first_setting_open",true);
    }
    public static void InitLanguagesArray(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("config",MODE_PRIVATE);
       String l=sharedPreferences.getString("language","");
       if (l.equals("")){
           l=c.getString(R.string.this_language).toLowerCase();
       }
       languageArrayList=new ArrayList<>();
        ArrayList<Language> arr=new ArrayList<>();

        for (int i = 0; i < languages[0].length; i++) {
            Language country = new Language(languages[0][i],languages[1][i],languages[1][i].equals(l));



            arr.add(country);


        }
        languageArrayList=arr;



    }

    //Save ProxyFindConfig
    public static void SaveUseBestProxy(Context c,boolean b){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        ProxyFindConfig.use_best_proxy=b;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key_use_best_broxy,b);
        editor.commit();

    }
    public static void SaveUseCountryFilter(Context c,boolean b){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        ProxyFindConfig.use_country_filter=b;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key_use_country_filter,b);
        editor.commit();

    }
    public static void SaveUsePortFilter(Context c,boolean b){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        ProxyFindConfig.use_port_filter=b;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key_use_port_filter,b);
        editor.commit();

    }
    public static void SaveUseTypeFilter(Context c,boolean b){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        ProxyFindConfig.use_type_filter=b;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key_use_type_filter,b);
        editor.commit();

    }
    public static void SaveUsePorts(Context c,String mas){
        String s =mas;
        ProxyFindConfig.use_ports_string=mas;
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_use_ports,mas);
        editor.commit();

    }
    public static void SaveUseTypes(Context c,String mas){
        String s =mas;
        ProxyFindConfig.use_types_string=mas;
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_use_types,mas);


        if (mas.contains("h")){
            ProxyFindConfig.type_http=1;
        }else {
            ProxyFindConfig.type_http=0;

        }
        if (mas.contains("s")){
            ProxyFindConfig.type_https=1;
        }else {
            ProxyFindConfig.type_https=0;

        }
        if (mas.contains("4")){
            ProxyFindConfig.type_socks4=1;
        }else {
            ProxyFindConfig.type_socks4=0;

        }
        if (mas.contains("5")){
            ProxyFindConfig.type_socks5=1;
        }else {
            ProxyFindConfig.type_socks5=0;

        }

        editor.commit();

    }
    public static void SaveUseSpecialUrl(Context c,boolean b){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        ProxyFindConfig.use_special_url=b;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key_use_special_url,b);
        editor.commit();

    }
    public static void SaveSpecialUrl(Context c, String url){
        ProxyFindConfig.special_url=url;


        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_special_url,url);
        editor.commit();

    }
    public static void SaveUseLowDelayProxy(Context c,boolean b){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        ProxyFindConfig.use_low_delay_proxy=b;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key_use_low_delay,b);
        editor.commit();

    }
    public static void SaveUseAnonFilter(Context c,boolean b){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        ProxyFindConfig.use_anon_filter=b;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key_use_anon_filter,b);
        editor.commit();

    }
    public static void SaveAnon(Context c,String s){
        ProxyFindConfig.use_anon_string=s;


        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_use_anon_string,s);
        editor.commit();
    }
    public static void SaveFilterCountry(Context c,String s){
        ArrayList<Country> arr=new ArrayList<>();
        ProxyFindConfig.countrys_list=new ArrayList<>();
        for (int i = 0; i < countrys[0].length; i++) {

            Country country = new Country(countrys[1][i],countrys[0][i],!s.contains(countrys[1][i]));

            arr.add(country);
        }
        ProxyFindConfig.countrys_list=arr;
        ProxyFindConfig.use_countrys_string=s;
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_filter_country,s);
        editor.commit();
    }
    public static void SaveUseMyProxy(Context c,boolean b){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        ProxyFindConfig.use_my_proxy=b;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key_use_my_proxy,b);
        editor.commit();

    }
    public static void SaveItsFirstSettingOpen(Context c,boolean b){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        ProxyFindConfig.use_my_proxy=b;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first_setting_open",b);
        editor.commit();

    }


    public static void SaveMyNetworks(Context c, String s){
        SharedPreferences sharedPreferences=c.getSharedPreferences("config",MODE_PRIVATE);
        MyNetworks=MyNetworks+"("+s+")";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("my_networks",MyNetworks);
        editor.commit();

    }


    //MyProxys
    public static void SaveMyProxys(Context c, ArrayList<MyProxyData> myProxyData){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        ProxyFindConfig.my_proxys=myProxyData;
        try {

            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // создаем пустой объект Document, в котором будем
            // создавать наш xml-файл
            // создаем корневой элемент
            Element rootElement =
                    doc.createElementNS("namespaceURL", "Proxys");
            // добавляем корневой элемент в объект Document
            doc.appendChild(rootElement);

            // добавляем первый дочерний элемент к корневому
            for (int i = 0; i <myProxyData.size() ; i++) {
                MyProxyData m= myProxyData.get(i);
                Log.d(TAG, "SaveMyProxys: name "+m.name_proxy);
                m.id=i;

                rootElement.appendChild(getMyProxy(m,doc));

            }


            saveFile(c,doc);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static Node getMyProxy(MyProxyData mp,Document doc){
        Element proxy = doc.createElement("proxy");

        // устанавливаем атрибут id
        proxy.setAttribute("id", String.valueOf(mp.id));

        // создаем элемент name
        proxy.appendChild(getPrElements(doc, proxy, "name", mp.name_proxy));

        // создаем элемент age
        proxy.appendChild(getPrElements(doc, proxy, "host", mp.host_proxy));

        proxy.appendChild(getPrElements(doc, proxy, "port", String.valueOf(mp.port_proxy)));

        proxy.appendChild(getPrElements(doc, proxy, "is_active", String.valueOf(mp.isactive)));


        return proxy;
    }
    private static Node getPrElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
    private static void saveFile(Context context, Document document) {
        File xmlFile;
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        Properties outFormat = new Properties();
        outFormat.setProperty(OutputKeys.INDENT, "yes");
        outFormat.setProperty(OutputKeys.METHOD, "xml");
        outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        outFormat.setProperty(OutputKeys.VERSION, "1.0");
        outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperties(outFormat);
        DOMSource domSource =
                new DOMSource(document.getDocumentElement());
        //OutputStream output = new ByteArrayOutputStream();


        File docsFolder = context.getFilesDir();

        docsFolder.mkdirs();
        xmlFile = new File(context.getFilesDir().getAbsolutePath(), "my_proxys.xml");

        try {
            xmlFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean qwe = xmlFile.exists();

        StreamResult result = new StreamResult(xmlFile);
        try {
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        //String xmlString = output.toString();
        //xmlResult.setText(xmlString);

    }
    public static void InitMyProxys(Context c){
        Document document =null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        File directory = c.getFilesDir();

        File xml= new File(directory,"my_proxys.xml");
        try {
            document=factory.newDocumentBuilder().parse(xml);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        ArrayList<MyProxyData> myArray=new ArrayList<>();


        if (document!=null) {
            NodeList nList =            document.getElementsByTagName("proxy");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                Element element = (Element) nNode;
                MyProxyData m = new MyProxyData();
                m.id =i;
                m.host_proxy = element.getElementsByTagName("host").item(0).getTextContent();

                m.name_proxy = element.getElementsByTagName("name").item(0).getTextContent();
                m.port_proxy = Integer.parseInt(element.getElementsByTagName("port").item(0).getTextContent());
                m.isactive = Boolean.parseBoolean(element.getElementsByTagName("is_active").item(0).getTextContent());
                myArray.add(m);

            }
        }
        ProxyFindConfig.my_proxys=myArray;




    }

    //Init NowProxy
    public static ProxyData NowProxy(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("config",MODE_PRIVATE);

        ProxyData.def_value=c.getString(R.string.Dont_have_data);
        ProxyData p= new ProxyData();

        p.host=sharedPreferences.getString(key_host,p.def_value);
        p.port=sharedPreferences.getInt(key_port,0);
        p.type=sharedPreferences.getString(key_type,p.def_value);
        p.anonymity=sharedPreferences.getString(key_anon,p.def_value);
        p.country=sharedPreferences.getString(key_country,p.def_value);
        p.delay=sharedPreferences.getInt(key_delay,0);
        if (p.host.equals(ProxyData.def_value)){
            p.proxy_is=false;
        }
        return p;
    }

    //Save NowProxy
    public static void SaveNowProxy(Context c, ProxyData pd){
        SharedPreferences sharedPreferences = c.getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_host,pd.host);
        editor.putInt(key_port,pd.port);
        editor.putString(key_anon,pd.anonymity);
        editor.putString(key_type,pd.type);
        editor.putString(key_country,pd.country);
        editor.putInt(key_delay, (int) pd.delay);
        editor.commit();

    }


    //Time
    public static Time getLastSaveTime(Context c){
        Time t = new Time();
        SharedPreferences sharedPreferences = c.getSharedPreferences("config",MODE_PRIVATE);
        t.year=sharedPreferences.getInt(key_time_year,0);
        t.month=sharedPreferences.getInt(key_time_month,0);
        t.monthDay=sharedPreferences.getInt(key_time_day,0);
        t.hour=sharedPreferences.getInt(key_time_hour,0);
        t.minute=sharedPreferences.getInt(key_time_minute,0);
        return t;
    }
    public static void SaveLastTime(Context c,Time t){

        SharedPreferences sharedPreferences = c.getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key_time_year,t.year);
        editor.putInt(key_time_month,t.month);
        editor.putInt(key_time_day,t.monthDay);
        editor.putInt(key_time_hour,t.hour);
        editor.putInt(key_time_minute,t.minute);
        editor.commit();

    }

    public static void InitLanguage(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("config",MODE_PRIVATE);
        language=sharedPreferences.getString("language","");
    }
    public static void SaveLanguage(Context c,String s){
        SharedPreferences sharedPreferences = c.getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language",s);
        editor.commit();
    }

}

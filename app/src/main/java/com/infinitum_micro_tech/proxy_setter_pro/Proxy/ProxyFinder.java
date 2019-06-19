package com.infinitum_micro_tech.proxy_setter_pro.Proxy;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import com.infinitum_micro_tech.proxy_setter_pro.Config;
import com.infinitum_micro_tech.proxy_setter_pro.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


class ProxyFinder {
    private static File xmlFile;
    final  static String  TAG = "myapp";
    static int timeout = 5000;
//    final static String pars_url="https://hidemy.name/ru/proxy-list/?&maxtime=250&type=hs#list";

    //final static String pars_url="http://api.foxtools.ru/v2/Proxy.xml?type=None&anonymity=All&available=Yes&free=Any&port=&country=&uptime=1&page=&limit=&lang=Auto&cp=UTF-8&details=1";



    public static boolean check(String pHost, int pPort, String pType,String testLink) {
        SocketAddress addr = new InetSocketAddress(pHost, pPort);
        java.net.Proxy.Type _pType = (pType.contains("HTTP") ? java.net.Proxy.Type.HTTP : java.net.Proxy.Type.SOCKS);
        java.net.Proxy httpProxy = new java.net.Proxy(_pType, addr);
        HttpURLConnection urlConn = null;
        URL url;

        try {
            url = new URL(testLink);
            urlConn = (HttpURLConnection) url.openConnection(httpProxy);
            urlConn.setConnectTimeout(timeout);
            urlConn.connect();
            return (urlConn.getResponseCode() == 400);
        }
        catch(SocketException e) {
            Log.d(TAG,  "Proxy.check: "+pHost+":"+pPort  +"  Error: " + e);

            return false;}
        catch(SocketTimeoutException e) {
            Log.d(TAG,  "Proxy.check: "+pHost+":"+pPort  +"  Error: " + e);

            return false;}
        catch(Exception e) {
            Log.d(TAG,  "Proxy.check: "+pHost+":"+pPort  +"  Error: " + e);
            return false;
        }
    }

//    public static void main(String[] args) {
//        //System.out.println(check("87.103.174.123",8080,"HTTP","https://ok.ru/"));
//       findNewBestProxy("https://ok.ru/");
//    }

    public static ArrayList<ProxyData> ReturnProxyList(Context c,boolean get_from_network){


        String pars_url="http://api.foxtools.ru/v2/Proxy.xml?type=All&anonymity=All&available=Yes&free=Yes&port=&country=&uptime=&page=&limit=&lang=Auto&cp=UTF-8&details=1";

        ProxyData best_proxy=new ProxyData();

        best_proxy.error="Прокси не найден";
        best_proxy.proxy_is=false;
        Document document =null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        if (get_from_network) {
            Time time_now = new Time();   time_now.setToNow();
            Config.SaveLastTime(c,time_now);
            Log.d(TAG, "get data from network");

            try {

                String url = pars_url;


                document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            saveFile(c,document);
        }else {
            Log.d(TAG, "findNewBestProxy: get data from files");

            File directory = c.getCacheDir();

            File xml= new File(directory,"proxy.xml");
            if (xml.exists()){
                Log.d(TAG, "ReturnProxyList: proxy xml exist");
            }
            try {
                document=factory.newDocumentBuilder().parse(xml);
//                document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());
                Log.d(TAG, "ReturnProxyList: read file from disk, doc ");


            } catch (SAXException e) {
                try {
                    document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());
                    saveFile(c,document);
                    Time time_now = new Time();   time_now.setToNow();
                    Config.SaveLastTime(c,time_now);
                } catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            } catch (IOException e) {
                try {
                    document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());
                    saveFile(c,document);
                    Time time_now = new Time();   time_now.setToNow();
                    Config.SaveLastTime(c,time_now);
                } catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                try {
                    document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());
                    saveFile(c,document);
                    Time time_now = new Time();   time_now.setToNow();
                    Config.SaveLastTime(c,time_now);
                } catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            }
        }

        NodeList nList = document.getElementsByTagName("items").item(0).getChildNodes();
        Log.d(TAG, "ReturnProxyList: "+ nList.getLength());

        if (nList.getLength()!=0) {
            ArrayList<ProxyData> proxyData = new ArrayList<ProxyData>();
            for (int i = 0; i < nList.getLength(); i++) {
                ProxyData p=new ProxyData();
                Element element=null;
                if (nList.item(1).getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) nList.item(1);

                    //Host
                    p.host = element.getElementsByTagName("ip").item(0).getTextContent();

                    //Port
                    p.port = Integer.parseInt(element.getElementsByTagName("port").item(0).getTextContent());
                    Log.d(TAG, "ReturnProxyList: " + p.host + ":" + p.port);

//                //Type
//                String t= "";
//                if (Integer.parseInt(element.getElementsByTagName("http").item(0).getTextContent())==1){
//                    p.type_http=1;
//                    t=t+"HTTP ";
//                }else {
//                    p.type_http=0;
//
//                }
//                if (Integer.parseInt(element.getElementsByTagName("ssl").item(0).getTextContent())==1){
//                    t=t+"HTTPS ";
//                    p.type_https=1;
//
//                }else {
//                    p.type_https=0;
//
//                }
//                if (Integer.parseInt(element.getElementsByTagName("socks4").item(0).getTextContent())==1){
//                    t=t+"SOCKS4 ";
//                    p.type_socks4=1;
//
//                }else{
//                    p.type_socks4=0;
//
//                }
//                if (Integer.parseInt(element.getElementsByTagName("socks5").item(0).getTextContent())==1){
//                    t=t+"SOCKS5";
//                    p.type_socks5=1;
//
//                }else {
//                    p.type_socks5=0;
//                }
//                p.type=t;
//                //Log.d(TAG, "findNewBestProxy: type "+t);
//
                    //Anon
                    p.anon_num = element.getElementsByTagName("anonymity").item(0).getTextContent();

                    p.anonymity = p.anon_num;

                    //Country
                    p.country = element.getElementsByTagName("country").item(0).getChildNodes().item(0).getTextContent();
                    p.country_code = element.getElementsByTagName("country").item(0).getChildNodes().item(2).getTextContent();

                    //Delay
                    p.delay = Double.parseDouble(element.getElementsByTagName("uptime").item(0).getTextContent());

//                if (filter_proxy(p)) {
//                    proxyData.add(p);
//                }


                    proxyData.add(p);
                }
            }

            for (int i = 0; i < proxyData.size(); i++) {
                for (int j = 0; j < proxyData.size() - 1; j++) {

                    if (proxyData.get(j).delay > proxyData.get(j + 1).delay) {
                        ProxyData prox = new ProxyData();
                        prox = proxyData.get(j);
                        proxyData.set(j, proxyData.get(j + 1));
                        proxyData.set(j + 1, prox);

                    }
                }
            }

            return proxyData;



        }

        return null;
    }


    public static ProxyData findNewBestProxy(String test_link, Context c,boolean get_from_network){


        String pars_url="http://api.foxtools.ru/v2/Proxy.xml?type=All&anonymity=All&available=Yes&free=Yes&port=&country=&uptime=&page=&limit=&lang=Auto&cp=UTF-8&details=1";

        ProxyData best_proxy=new ProxyData();

        best_proxy.error="Прокси не найден";
        best_proxy.proxy_is=false;
        Document document =null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        if (get_from_network) {
            Time time_now = new Time();   time_now.setToNow();
            Config.SaveLastTime(c,time_now);
            Log.d(TAG, "findNewBestProxy: get data from network");

            try {
                document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            saveFile(c,document);
        }else {
            Log.d(TAG, "findNewBestProxy: get data from files");

            File directory = c.getFilesDir();

            File xml= new File(directory,"proxy.xml");
            try {
                //document=factory.newDocumentBuilder().parse(xml);
                document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());

            } catch (SAXException e) {
                try {
                    document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());
                    saveFile(c,document);
                    Time time_now = new Time();   time_now.setToNow();
                    Config.SaveLastTime(c,time_now);
                } catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            } catch (IOException e) {
                try {
                    document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());
                    saveFile(c,document);
                    Time time_now = new Time();   time_now.setToNow();
                    Config.SaveLastTime(c,time_now);
                } catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                try {
                    document = (Document) factory.newDocumentBuilder().parse(new URL(pars_url).openStream());
                    saveFile(c,document);
                    Time time_now = new Time();   time_now.setToNow();
                    Config.SaveLastTime(c,time_now);
                } catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            }
        }

        NodeList nList = document.getElementsByTagName("items").item(0).getChildNodes();

        if (nList.getLength()!=0) {
            ArrayList<ProxyData> proxyData = new ArrayList<ProxyData>();
            for (int i = 0; i < nList.getLength(); i++) {
                ProxyData p=new ProxyData();
                Node nNode = nList.item(i);
                Element element = (Element) nNode;

                //Host
                p.host=element.getElementsByTagName("ip").item(0).getTextContent();

                //Port
                p.port=Integer.parseInt(element.getElementsByTagName("port").item(0).getTextContent());
                Log.d(TAG, "ReturnProxyList: "+p.host+":"+ p.port);

//                //Type
//                String t= "";
//                if (Integer.parseInt(element.getElementsByTagName("http").item(0).getTextContent())==1){
//                    p.type_http=1;
//                    t=t+"HTTP ";
//                }else {
//                    p.type_http=0;
//
//                }
//                if (Integer.parseInt(element.getElementsByTagName("ssl").item(0).getTextContent())==1){
//                    t=t+"HTTPS ";
//                    p.type_https=1;
//
//                }else {
//                    p.type_https=0;
//
//                }
//                if (Integer.parseInt(element.getElementsByTagName("socks4").item(0).getTextContent())==1){
//                    t=t+"SOCKS4 ";
//                    p.type_socks4=1;
//
//                }else{
//                    p.type_socks4=0;
//
//                }
//                if (Integer.parseInt(element.getElementsByTagName("socks5").item(0).getTextContent())==1){
//                    t=t+"SOCKS5";
//                    p.type_socks5=1;
//
//                }else {
//                    p.type_socks5=0;
//                }
//                p.type=t;
//                //Log.d(TAG, "findNewBestProxy: type "+t);
//
                //Anon
                p.anon_num=element.getElementsByTagName("anonymity").item(0).getTextContent();

                p.anonymity=p.anon_num;

                //Country
                p.country=element.getElementsByTagName("country").item(0).getChildNodes().item(0).getTextContent();
                p.country_code=element.getElementsByTagName("country").item(0).getChildNodes().item(2).getTextContent();

                //Delay
                p.delay=Double.parseDouble(element.getElementsByTagName("uptime").item(0).getTextContent());

//                if (filter_proxy(p)) {
//                    proxyData.add(p);
//                }
//
//
                proxyData.add(p);
            }
            if (proxyData.isEmpty()){
                return best_proxy;
            }

            for (int i = 0; i < proxyData.size(); i++) {
                for (int j = 0; j < proxyData.size() - 1; j++) {

                    if (proxyData.get(j).delay > proxyData.get(j + 1).delay) {
                        ProxyData prox = new ProxyData();
                        prox = proxyData.get(j);
                        proxyData.set(j, proxyData.get(j + 1));
                        proxyData.set(j + 1, prox);

                    }
                }
            }

            if (ProxyFindConfig.use_special_url){
                Log.d(TAG, "findNewBestProxy: secial url "+ProxyFindConfig.special_url);
                for(ProxyData pd:proxyData){

                    String type="HTTP";
//                    if (pd.type_http==1){
//                        type="HTTP";
//                    }else {
//                        if (pd.type_https==1){
//                            type="HTTPS";
//                        }else {
//                            if (pd.type_socks4==1){
//                                type="SOCKS4";
//                            }else {
//                                if (pd.type_socks5==1){
//                                    type="SOCKS5";
//                                }
//                            }
//                        }
//                    }
                    if (check(pd.host,pd.port,type,ProxyFindConfig.special_url)){
                        Log.d(TAG, "findNewBestProxy: check peoxy");
                        return pd;
                    }
                }



            }else {
                if (ProxyFindConfig.use_low_delay_proxy){
                    if (!proxyData.isEmpty()) {
                        Log.d(TAG, "findNewBestProxy: return low delay proxy");
                        return proxyData.get(0);
                    }
                }
                if (proxyData.size() > 1) {
                    final Random random = new Random();

                    return proxyData.get(random.nextInt(proxyData.size() / 2));
                } else {
                    if (!proxyData.isEmpty()) {
                        return proxyData.get(0);
                    }
                }
            }

        }

        return best_proxy;
    }

  static boolean filter_proxy(ProxyData black_proxy){

      if (ProxyFindConfig.use_best_proxy){

          boolean type_true=false;
//
//          if ((black_proxy.type_http == 1)) {
//              type_true = true;
//          }
//          if ((black_proxy.type_https == 1)) {
//              type_true = true;
//          }
//          if (!type_true) {
//              return false;
//          }
          return true;

      }else {
          boolean type_true=false;

          if (ProxyFindConfig.use_type_filter){
          if ((black_proxy.type_http == ProxyFindConfig.type_http) && (black_proxy.type_http == 1)) {
              type_true = true;
          }
          if (black_proxy.type_https == ProxyFindConfig.type_https && (black_proxy.type_https == 1)) {
              type_true = true;
          }
          if (black_proxy.type_socks4 == ProxyFindConfig.type_socks4 && (black_proxy.type_socks4 == 1)) {
              type_true = true;
          }
          if (black_proxy.type_socks5 == ProxyFindConfig.type_socks5 && (black_proxy.type_socks5 == 1)) {
              type_true = true;
          }
          if (!type_true) {
              return false;
          }
          }else {
              if ((black_proxy.type_http == 1)) {
                  type_true = true;
              }
              if ((black_proxy.type_https == 1)) {
                  type_true = true;
              }
              if (!type_true) {
                  return false;
              }
          }
          if (ProxyFindConfig.use_port_filter){
              String port= String.valueOf(black_proxy.port);
              if (!ProxyFindConfig.use_ports_string.contains(port)){
                  return false;
              }
          }
          if (ProxyFindConfig.use_anon_filter){
              String anon=black_proxy.anon_num;
              if (!ProxyFindConfig.use_anon_string.contains(anon)){
                  return false;
              }
          }
          if (ProxyFindConfig.use_country_filter){
              String co=black_proxy.country_code;
              if (ProxyFindConfig.use_countrys_string.contains(co)){
                  return false;
              }
          }

      }
      Log.d(TAG, "filter_proxy: type "+black_proxy.type+"\n"+
              "filter_proxy: port "+black_proxy.port+"\n"+
              "filter_proxy: anon "+black_proxy.anonymity+"\n"
              +"filter_proxy: country "+black_proxy.country+"\n"+" "
      );

      return true;



  }

  public static void saveFile(Context context, Document document) {
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


       File docsFolder = context.getCacheDir();
       Log.d(TAG, "saveFile: "+context.getFilesDir().getAbsolutePath());

       docsFolder.mkdirs();
       xmlFile = new File(docsFolder.getAbsolutePath(), "proxy.xml");

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

//        public static ProxyData findNewBestProxy(String test_link){
//        JSONParser parser = new JSONParser();
//        String url = "http://pubproxy.com/api/proxy?format=json&limit=20&https=true";
//        JSONObject jsonObject=null;
//        ArrayList<ProxyData> proxyDataArrayList = new ArrayList<>();
//        try {
//            InputStream inputStream =new URL(url).openStream();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
//            String jsonText = readAll(rd);
//            jsonObject = (JSONObject) parser.parse(jsonText);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        JSONArray proxy_data=(JSONArray) jsonObject.get("data");
//        for (int i = 0; i <proxy_data.size() ; i++) {
//            JSONObject jo= (JSONObject) proxy_data.get(i);
//            ProxyData pd = new ProxyData();
//            pd.host= (String) jo.get("ip");
//            pd.port=Integer.parseInt((String) jo.get("port"));
//
//            System.out.println(pd.host+":"+pd.port+"  "+check(pd.host,pd.port,"HHTP",test_link));
//
//        }
//
//
//        return null;
//    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }






}

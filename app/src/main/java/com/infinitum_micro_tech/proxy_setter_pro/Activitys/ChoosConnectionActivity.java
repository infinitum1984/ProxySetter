package com.infinitum_micro_tech.proxy_setter_pro.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infinitum_micro_tech.proxy_setter_pro.Config;
import com.infinitum_micro_tech.proxy_setter_pro.ConnectionStatictic;
import com.infinitum_micro_tech.proxy_setter_pro.Proxy.Proxy;
import com.infinitum_micro_tech.proxy_setter_pro.R;

import java.util.ArrayList;
import java.util.List;



public class ChoosConnectionActivity extends Activity  {
    private ListView wifiDeviceList;
    private List<ScanResult> wifiList;
    private StringBuilder sb;
    private WifiManager mainWifi;
    static ArrayList<String> wifiScanResults;
    EditText pass;
    String SSID_to_try_connect="";
    SwipeRefreshLayout swipeRefreshLayout;

    ConnectivityManager conMan;
    private static final int MY_PERMISSIONS_REQUEST =1 ;
    

    private ChoosConnectionActivity.WifiReceiver receiverWifi;
    private WifiReceiverConnect receiverConnect;
    private String TAG="myapp";

    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choos_connection);
        wifiDeviceList=findViewById(R.id.list);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_list);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#236391"));

        Log.d(TAG, "onCreate: on create");
        mainWifi= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        conMan = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        receiverWifi = new WifiReceiver();
        receiverConnect=new WifiReceiverConnect();
        getWifi();

        wifiDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("myapp","Connect to: "+wifiScanResults.get(position)+" pos: "+String.valueOf(position));
                String s =wifiScanResults.get(position);
                if (!ConnectionStatictic.NetworkIs(getApplicationContext(),s)) {
                    connectToWifi(wifiScanResults.get(position));
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.choose_deleted_network),Toast.LENGTH_SHORT).show();
                }
                


            }
        });
        LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            showDialogOnLocation();
        }




    }
    private void getWifi() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "getWifi: version>marsmallo");
            if (ContextCompat.checkSelfPermission(ChoosConnectionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getWifi: location off");

                ActivityCompat.requestPermissions(ChoosConnectionActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);

            }

            else{
                Log.d(TAG, "getWifi: lovation on");
                Log.d(TAG, "getWifi: wifi start scan");
                mainWifi.startScan();
            }

        }else {

            Toast.makeText(ChoosConnectionActivity.this, "scanning", Toast.LENGTH_SHORT).show();
            mainWifi.startScan();
        }
    }
    protected void onPause() {
        unregisterReceiver(receiverWifi);
        unregisterReceiver(receiverConnect);
        super.onPause();
    }

    protected void onResume() {
        Log.d(TAG, "onResume: register receiver");
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registerReceiver(receiverConnect,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {



        if (requestCode == MY_PERMISSIONS_REQUEST) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(ChoosConnectionActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                getWifi();
            }else{
               // Toast.makeText(ChoosConnectionActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }



    public void Back(View view) {
        Intent intent = new Intent(ChoosConnectionActivity.this,FirstConnectionActivity.class);
        startActivity(intent);
    }

    public void RefreshList(View view) {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
        mainWifi.startScan();
    }
    void showDialogOnLocation(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_on_loction);

        final Button ok = (Button) dialog.findViewById(R.id.ok_proxy_dialog);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==ok.getId()){
                    swipeRefreshLayout.setRefreshing(false);

                    dialog.dismiss();

                }

            }
        };
        ok.setOnClickListener(onClickListener);

        dialog.show();
    }
    void ShowDialogAfterConnect(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.set_proxy_dialog);

        final Button ok = (Button) dialog.findViewById(R.id.ok_proxy_dialog);
        TextView t = dialog.findViewById(R.id.text_view);
        t.setText(getString(R.string.dialog_connect));
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==ok.getId()){
                    swipeRefreshLayout.setRefreshing(false);

                    dialog.dismiss();

                }

            }
        };
        ok.setOnClickListener(onClickListener);

        dialog.show();
    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            String action = intent.getAction();
            if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)){
                wifiList = mainWifi.getScanResults();
                Log.d("myapp wifiList size", String.valueOf(wifiList.size()));

                ArrayList<String>   deviceList = new ArrayList<String>();
                String s="";
                for(int i = 0; i < wifiList.size(); i++){
                    if (!wifiList.get(i).SSID.equals(s)) {
                        deviceList.add(wifiList.get(i).SSID);
                        Log.d(TAG, "onReceive: s" + i + " " + wifiList.get(i).SSID);
                    }
                    Log.d("myapp", "SSID: "+wifiList.get(i).SSID);
                    s=wifiList.get(i).SSID;
                }
                if (!deviceList.isEmpty()) {
                    wifiScanResults = deviceList;


                    BoxAdapterWifi adapter = new BoxAdapterWifi(ChoosConnectionActivity.this, wifiScanResults);


                    wifiDeviceList.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);


                }
            }
            

        }

    }
    class WifiReceiverConnect extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            String action = intent.getAction();
            if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
                Log.d(TAG, "onReceive: connection_changes");

                if (ConnectionStatictic.WifiConnected(getApplicationContext())){
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiConfiguration wifiConfiguration= Proxy.GetCurrWifiConfig(wifiManager);
                    Log.d(TAG, "onReceive: "+wifiConfiguration.SSID);
                    if (wifiConfiguration.SSID.substring(1,wifiConfiguration.SSID.length()-1).equals(SSID_to_try_connect)){
                        Config.SaveMyNetworks(getApplicationContext(),SSID_to_try_connect);
                        Log.d(TAG, "onReceive: ");
                        Intent i = new Intent(ChoosConnectionActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                }
            }


        }

    }
    class BoxAdapterWifi extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<String> objects;

        BoxAdapterWifi(Context context, ArrayList<String> countries) {
            ctx = context;
            objects = countries;
            lInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // кол-во элементов
        @Override
        public int getCount() {
            return objects.size();
        }

        // элемент по позиции
        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        // id по позиции
        @Override
        public long getItemId(int position) {
            return position;
        }

        // пункт списка
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // используем созданные, но не используемые view
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.wifi_list_item, parent, false);
            }

            String p = getProduct(position);

            // заполняем View в пункте списка данными из товаров: наименование, цена
            // и картинка


            TextView cbBuy =  view.findViewById(R.id.label);
            // присваиваем чекбоксу обработчик
            // пишем позицию


            cbBuy.setText(p);


            return view;
        }

        // товар по позиции
        String getProduct(int position) {
            return (String) getItem(position);
        }




    }


    private void connectToWifi(final String wifiSSID) {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Connect to Network");
        dialog.setContentView(R.layout.connect);
        TextView textSSID = (TextView) dialog.findViewById(R.id.textSSID1);

        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        pass = (EditText) dialog.findViewById(R.id.textPassword);
        textSSID.setText(wifiSSID+"\n"+getString(R.string.hint_connect));

        // if button is clicked, connect to the network;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SSID_to_try_connect=wifiSSID;
                String checkPassword = pass.getText().toString();
                if (pass.getText().equals(null)){
                    checkPassword="";
                }
                Proxy.SetProxy(getApplicationContext(), Proxy.NowHost,Proxy.NowPort,wifiSSID,checkPassword);
                ShowDialogAfterConnect();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
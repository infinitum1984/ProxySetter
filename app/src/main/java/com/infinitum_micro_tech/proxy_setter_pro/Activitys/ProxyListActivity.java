package com.infinitum_micro_tech.proxy_setter_pro.Activitys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infinitum_micro_tech.proxy_setter_pro.Config;
import com.infinitum_micro_tech.proxy_setter_pro.Proxy.Proxy;
import com.infinitum_micro_tech.proxy_setter_pro.Proxy.ProxyData;
import com.infinitum_micro_tech.proxy_setter_pro.R;

import org.w3c.dom.Document;

import java.util.ArrayList;




public class ProxyListActivity extends AppCompatActivity {
    ListView listProxy;
    private String TAG="myapp";
    public static ArrayList<ProxyData> proxyDataArrayList;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView proxy_source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy_list);
        listProxy=findViewById(R.id.proxy_list_listview);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_list_proxy);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#236391"));

        swipeRefreshLayout.setRefreshing(true);
        FillProxyList mt = new FillProxyList(this);
        mt.execute();
    }

    public void BackToMainActivity(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    class FillProxyList extends AsyncTask<Void, Void, Void> {
        Context c;
        String pars_url="";
        FillProxyList(Context context){
         c=context;
        }
        Document document =null;

        @Override
        protected Void doInBackground(Void... params) {
            Time time_now = new Time();   time_now.setToNow();
            Time last_save= Config.getLastSaveTime(c);
            proxyDataArrayList= Proxy.returnProxyList(c,DataOld(time_now,last_save));

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProxyListBoxAdapter adapter = new ProxyListBoxAdapter(c,proxyDataArrayList);
            listProxy.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);

        }
    }
    class ProxyListBoxAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<ProxyData> objects;
        ImageButton copy_ip_button;
        ImageButton set_proxy_button;
        ProxyListBoxAdapter(Context context, ArrayList<ProxyData> proxys) {
            ctx = context;
            objects = proxys;
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
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.proxy_list_item, parent, false);
            }
            TextView ip=view.findViewById(R.id.ip_item);
            TextView delay=view.findViewById(R.id.delay_item);
            TextView anon=view.findViewById(R.id.anon_item);
            TextView type=view.findViewById(R.id.type_item);
            TextView country=view.findViewById(R.id.country_item);
            ProxyData data=objects.get(position);
            String htmlTaggedString  = "<u><strong>IP</strong></u>: "+data.host+":"+String.valueOf(data.port);
            Spanned textSpan  =  android.text.Html.fromHtml(htmlTaggedString);

            ip.setText(textSpan);


            htmlTaggedString="<u><strong>"+getString(R.string.Proxy_Type)+"</strong></u>: "+data.type;
            textSpan  =  android.text.Html.fromHtml(htmlTaggedString);
            type.setText(textSpan);

                htmlTaggedString="<u><strong>"+getString(R.string.Server_Delay)+"</strong></u>: "+String.valueOf(data.delay)+"ms";
                textSpan  =  android.text.Html.fromHtml(htmlTaggedString);
                delay.setText(textSpan);


            htmlTaggedString="<u><strong>"+getString(R.string.Country)+"</strong></u>: "+data.country;
            textSpan  =  android.text.Html.fromHtml(htmlTaggedString);
            country.setText(textSpan);

            htmlTaggedString="<u><strong>"+getString(R.string.Anonimity)+"</strong></u>: "+data.anonymity;
            textSpan  =  android.text.Html.fromHtml(htmlTaggedString);

            anon.setText(textSpan);


            copy_ip_button=view.findViewById(R.id.copy_ip);
            set_proxy_button=view.findViewById(R.id.set_this);

            copy_ip_button.setTag(position);
            set_proxy_button.setTag(position);

            copy_ip_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id =(int) v.getTag();

                    ClipData clipData = ClipData.newPlainText("text",getProxyItem(id).host+
                            ":"+String.valueOf(getProxyItem(id).port));
                    ClipboardManager clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(getApplicationContext(),"IP ("+getProxyItem(id).host+
                            ":"+String.valueOf(getProxyItem(id).port)+") "+getString(R.string.Is_Copyies) ,Toast.LENGTH_SHORT).show();
                }
            });
            set_proxy_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id =(int) v.getTag();

                    SettingProxy(getProxyItem(id));
                    Intent intent = new Intent(ctx, MainActivity.class);
                    startActivity(intent);
                }
            });
            return view;


        }


       ProxyData getProxyItem(int position) {
            return ((ProxyData) getItem(position));
        }

    }
    static boolean DataOld(Time now_time, Time last_save){
        Log.d("myapp", "DataOld: time now:"+now_time.year+"."+now_time.month+"."+now_time.monthDay+"   "+now_time.hour+":"+now_time.minute);
        Log.d("myapp", "DataOld: time lastsave:"+last_save.year+"."+last_save.month+"."+last_save.monthDay+"   "+last_save.hour+":"+last_save.minute);
        int min_now=now_time.hour*60+now_time.minute;
        int min_lastsave=last_save.hour*60+last_save.minute;

        if (now_time.year!=last_save.year)
        {
            return true;
        }

        if (now_time.month!=last_save.month){
            return true;
        }

        if (now_time.monthDay!=last_save.monthDay){
            return true;
        }

        if (min_now-30>=min_lastsave){
            return true;
        }
        return false;

    }
    void SettingProxy(ProxyData pd){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (Proxy.ItsMyNetwork(getApplicationContext())) {
                Log.d(TAG, "Is My Network");
                Proxy.SetProxy(getApplicationContext(), pd.host, pd.port);

            } else {
                Proxy.NowHost = pd.host;
                Proxy.NowPort = pd.port;
                Intent intent = new Intent(getApplicationContext(), FirstConnectionActivity.class);
                startActivity(intent);


            }
        } else {
            Proxy.SetProxy(getApplicationContext(), pd.host, pd.port);

        }
        Config.SaveNowProxy(getApplicationContext(),pd);
        Toast.makeText(getApplicationContext(),getString(R.string.ProxyIsSet),Toast.LENGTH_SHORT).show();
    }

}

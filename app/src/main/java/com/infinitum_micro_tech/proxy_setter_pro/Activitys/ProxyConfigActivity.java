package com.infinitum_micro_tech.proxy_setter_pro.Activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.infinitum_micro_tech.proxy_setter_pro.Config;
import com.infinitum_micro_tech.proxy_setter_pro.Country;
import com.infinitum_micro_tech.proxy_setter_pro.Proxy.MyProxyData;
import com.infinitum_micro_tech.proxy_setter_pro.Proxy.ProxyFindConfig;
import com.infinitum_micro_tech.proxy_setter_pro.R;

import java.util.ArrayList;



import static android.widget.Toast.LENGTH_SHORT;

public class ProxyConfigActivity extends AppCompatActivity {
    String TAG="myapp";
    Spinner CountrysSpinner;

    Switch UseBestProxySwitch;
    Switch UseCountryFilterSwitch;
    Switch UsePortFilterSwitch;
    Switch UseTypeFilterSwitch;
    Switch SpecialUrlSwitch;
    Switch UseLowDelayProxy;
    Switch UseAnonFilterSwitch;
    Switch UseMyProxySwitch;

    ImageButton ChooseTypeButton;
    ImageButton ChooseAnonButton;
    ImageButton ChooseCoutrys;
    ImageButton ChooseMyProxyB;

    EditText PortsEditText;
    EditText URL_edit;

    String use_types="";
    String use_anon="";
    String filter_country="";
    ArrayList<MyProxyData> MyProxys;

    ListView myProxysListView;
    static   ArrayList<Country> countriesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy_config);

        //hiden keyboard
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.d(TAG, "SaveProxyConfig: special url oncrate"+ ProxyFindConfig.special_url);

        countriesArray=ProxyFindConfig.countrys_list;
        filter_country=ProxyFindConfig.use_countrys_string;
        UseBestProxySwitch=findViewById(R.id.use_best_proxy_switch);
        UseCountryFilterSwitch=findViewById(R.id.country_filtter_switch);
        UsePortFilterSwitch=findViewById(R.id.port_filtter_switch);
        UseTypeFilterSwitch=findViewById(R.id.type_filtter_switch);
        SpecialUrlSwitch=findViewById(R.id.special_url_switch);
        ChooseTypeButton=findViewById(R.id.choose_type);
        PortsEditText =findViewById(R.id.ports_edittext);
        URL_edit=findViewById(R.id.url_edittext);
        UseLowDelayProxy=findViewById(R.id.use_low_delay_proxy_switch);
        ChooseAnonButton=findViewById(R.id.choose_anon);
        UseAnonFilterSwitch=findViewById(R.id.anon_filtter_switch);
        ChooseCoutrys=findViewById(R.id.choose_countrys);
        UseMyProxySwitch=findViewById(R.id.use_my_proxy_switch);
        ChooseMyProxyB=findViewById(R.id.choose_my_proxy);

        MyProxys=ProxyFindConfig.my_proxys;
        for (int i = 0; i < MyProxys.size(); i++) {
            Log.d(TAG, "MyProxys: "+MyProxys.get(i).name_proxy+" "+MyProxys.get(i).host_proxy+" "+MyProxys.get(i).port_proxy+
                    " "+MyProxys.get(i).isactive);
        }


        UseTypeFilterSwitch.setChecked(ProxyFindConfig.use_type_filter);
        UseBestProxySwitch.setChecked(ProxyFindConfig.use_best_proxy);
        UseCountryFilterSwitch.setChecked(ProxyFindConfig.use_country_filter);
        UsePortFilterSwitch.setChecked(ProxyFindConfig.use_port_filter);
        PortsEditText.setText(ProxyFindConfig.use_ports_string);
        UseLowDelayProxy=findViewById(R.id.use_low_delay_proxy_switch);
        UseLowDelayProxy.setChecked(ProxyFindConfig.use_low_delay_proxy);
        UseAnonFilterSwitch.setChecked(ProxyFindConfig.use_anon_filter);
        use_anon=ProxyFindConfig.use_anon_string;
        ChooseAnonButton.setEnabled(ProxyFindConfig.use_anon_filter);
        use_types=ProxyFindConfig.use_types_string;
        URL_edit.setText(ProxyFindConfig.special_url);


        SpecialUrlSwitch.setChecked(ProxyFindConfig.use_special_url);
        URL_edit.setText(ProxyFindConfig.special_url);

        UseCountryFilterSwitch.setChecked(ProxyFindConfig.use_country_filter);
        ChooseCoutrys.setEnabled(ProxyFindConfig.use_country_filter);
        UseMyProxySwitch.setChecked(ProxyFindConfig.use_my_proxy);
        if (ProxyFindConfig.use_special_url){
            URL_edit.setEnabled(true);

        }else {
            URL_edit.setEnabled(false);
        }

        if (ProxyFindConfig.use_port_filter){
            PortsEditText.setEnabled(true);
        }else {
            PortsEditText.setEnabled(false);

        }
        ChooseTypeButton.setEnabled(ProxyFindConfig.use_type_filter);
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked&&buttonView.getId()!=UseBestProxySwitch.getId()){
                    UseBestProxySwitch.setChecked(false);
                }

                if(!UseTypeFilterSwitch.isChecked()&&!UseCountryFilterSwitch.isChecked()&&!UsePortFilterSwitch.isChecked()&&!SpecialUrlSwitch.isChecked()
                        &&!UseLowDelayProxy.isChecked()&&!UseAnonFilterSwitch.isChecked()&&!UseCountryFilterSwitch.isChecked()&&!UseMyProxySwitch.isChecked()){

                    UseBestProxySwitch.setChecked(true);
                }
                if (buttonView.getId()==UseCountryFilterSwitch.getId()){
                    if (isChecked){
                        ChooseCoutrys.setEnabled(true);
                    }else {
                        ChooseCoutrys.setEnabled(false);
                    }
                }
                if (buttonView.getId()==UseAnonFilterSwitch.getId()){
                    if (isChecked){
                        UseBestProxySwitch.setChecked(false);
                        ChooseAnonButton.setEnabled(true);
                    }else {
                        ChooseAnonButton.setEnabled(false);
                    }
                }
                if (buttonView.getId()==SpecialUrlSwitch.getId()){

                    if (isChecked) {

                        UseBestProxySwitch.setChecked(false);
                        URL_edit.setEnabled(true);
                    }else {
                        URL_edit.setEnabled(false);
                    }
                }
                if (buttonView.getId()==UseLowDelayProxy.getId()){
                    if (isChecked){
                        UseBestProxySwitch.setChecked(false);
                    }
                }


                if (buttonView.getId()==UseBestProxySwitch.getId()){

                    if (isChecked){
                        UsePortFilterSwitch.setChecked(false);
                        UseCountryFilterSwitch.setChecked(false);
                        SpecialUrlSwitch.setChecked(false);
                        UseTypeFilterSwitch.setChecked(false);
                        UseMyProxySwitch.setChecked(false);

                    }
                }
                if (buttonView.getId()==UseMyProxySwitch.getId()){

                    if (isChecked){
                        if(!MyProxys.isEmpty()) {

                            boolean b=false;
                            for (int i = 0; i < MyProxys.size(); i++) {
                                if (MyProxys.get(i).isactive){
                                    b=true;
                                    break;
                                }
                            }
                            if (b){
                                UsePortFilterSwitch.setChecked(false);
                                UseCountryFilterSwitch.setChecked(false);
                                SpecialUrlSwitch.setChecked(false);
                                UseTypeFilterSwitch.setChecked(false);
                                UseAnonFilterSwitch.setChecked(false);
                                UseLowDelayProxy.setChecked(false);
                                ChooseMyProxyB.setEnabled(true);
                            }else {
                                Toast.makeText(getApplicationContext(),getString(R.string.You_dont_choose_proxy),Toast.LENGTH_SHORT).show();
                                UseMyProxySwitch.setChecked(false);
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),getString(R.string.List_you_proxy_clean),Toast.LENGTH_SHORT).show();
                            UseMyProxySwitch.setChecked(false);
                        }
                    }
                }
                if (buttonView.getId()==UsePortFilterSwitch.getId()){

                    if (isChecked){
                        UseBestProxySwitch.setChecked(false);

                        PortsEditText.setEnabled(true);
                    }else {

                        PortsEditText.setEnabled(false);
                    }
                }
                if (buttonView.getId()==UseTypeFilterSwitch.getId()){

                    if (isChecked) {
                        UseBestProxySwitch.setChecked(false);
                    }
                    ChooseTypeButton.setEnabled(isChecked);
                }
                if (buttonView.getId()==UseCountryFilterSwitch.getId()){

                    if (isChecked){
                        UseBestProxySwitch.setChecked(false);
                    }
                    UseBestProxySwitch.setChecked(false);


                }


            }
        };
        UseBestProxySwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        UseCountryFilterSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        UsePortFilterSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        UseTypeFilterSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        SpecialUrlSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        UseLowDelayProxy.setOnCheckedChangeListener(onCheckedChangeListener);
        UseAnonFilterSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        UseCountryFilterSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        UseMyProxySwitch.setOnCheckedChangeListener(onCheckedChangeListener);
    }


   public  void BackToMainActivity(View view) {
        if (Config.its_first_setting_open){
            DialogFirstSave();
        }else {
            Config.SaveItsFirstSettingOpen(getApplicationContext(),false);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

   public void SaveProxyConfig(View view) {
       Time time_now = new Time();   time_now.setToNow();
       Time last_save=Config.getLastSaveTime(getApplicationContext());
       Config.SaveUseBestProxy(this, UseBestProxySwitch.isChecked());
       Config.SaveUsePortFilter(this, UsePortFilterSwitch.isChecked());
       Config.SaveUseCountryFilter(this, UseCountryFilterSwitch.isChecked());
       Config.SaveUseTypeFilter(this, UseTypeFilterSwitch.isChecked());
       Config.SaveUsePorts(this, PortsEditText.getText().toString());
       Config.SaveAnon(this,use_anon);
       Config.SaveUseAnonFilter(this,UseAnonFilterSwitch.isChecked());
       Config.SaveUseLowDelayProxy(this,UseLowDelayProxy.isChecked());
       Config.SaveUseCountryFilter(this,UseCountryFilterSwitch.isChecked());
       Config.SaveFilterCountry(this,filter_country);
       Config.SaveMyProxys(this,MyProxys);
       Config.SaveUseMyProxy(this,UseMyProxySwitch.isChecked());
           //Special URL
       String s= URL_edit.getText().toString();
       Config.SaveSpecialUrl(this, s);
        if (SpecialUrlSwitch.isChecked()) {
            if (!s.equals("")) {
                Config.SaveUseSpecialUrl(getApplicationContext(), true);

            } else {
                SpecialUrlSwitch.setChecked(false);
                Toast.makeText(this, getString(R.string.Url_empty), LENGTH_SHORT).show();

            }
        }
       Config.SaveUseSpecialUrl(this,SpecialUrlSwitch.isChecked());



       if (use_types.equals("")) {
           use_types = "hs";
       }
       Config.SaveUseTypes(this, use_types);
//           Proxy.SaveProxyXML(getApplicationContext(), ProxyFindConfig.pars_url());


       Log.d(TAG, "SaveProxyConfig: url+ "+ ProxyFindConfig.special_url);
           Toast.makeText(getApplicationContext(),getString(R.string.Setting_Save), LENGTH_SHORT).show();

    }
    private void DialogFirstSave() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.set_proxy_dialog);

        final Button ok = (Button) dialog.findViewById(R.id.ok_proxy_dialog);
        TextView t = dialog.findViewById(R.id.text_view);
        t.setText(getString(R.string.dont_forget_save));
        Config.SaveItsFirstSettingOpen(getApplicationContext(),false);
        Config.InitIsFirstSettingOpen(getApplicationContext());
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==ok.getId()){



                    dialog.dismiss();

                }

            }
        };
        ok.setOnClickListener(onClickListener);

        dialog.show();
    }

    public void ChooseAnonTypeDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_anon_type_dialog);
        final CheckBox NONE_CHECK=dialog.findViewById(R.id.none_anon);
        final CheckBox LOW_CHECK=dialog.findViewById(R.id.low_anon);
        final CheckBox MIDDLE_CHECK=dialog.findViewById(R.id.middle_anon);
        final CheckBox HIGH_CHECK=dialog.findViewById(R.id.high_anon);

        if (use_anon.contains("1")){
            NONE_CHECK.setChecked(true);
        }
        if (use_anon.contains("2")){
            LOW_CHECK.setChecked(true);
        }
        if (use_anon.contains("3")){
            MIDDLE_CHECK.setChecked(true);
        }
        if (use_anon.contains("4")){
            HIGH_CHECK.setChecked(true);
        }

        if (use_anon.equals("")) {
            use_anon="12345";
            NONE_CHECK.setChecked(true);
            LOW_CHECK.setChecked(true);
            MIDDLE_CHECK.setChecked(true);
            HIGH_CHECK.setChecked(true);
        }

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId()==NONE_CHECK.getId()){
                if (isChecked){
                    use_anon=use_anon+"1";
                }else {
                    use_anon=use_anon.replace("1","");
                }
            }
                if (buttonView.getId()==LOW_CHECK.getId()){
                    if (isChecked){
                        use_anon=use_anon+"2";
                    }else {
                        use_anon=use_anon.replace("2","");
                    }
                }
                if (buttonView.getId()==MIDDLE_CHECK.getId()){
                    if (isChecked){
                        use_anon=use_anon+"3";
                    }else {
                        use_anon=use_anon.replace("3","");
                    }
                }
                if (buttonView.getId()==HIGH_CHECK.getId()){
                    if (isChecked){
                        use_anon=use_anon+"4";
                    }else {
                        use_anon=use_anon.replace("4","");
                    }
                }






            }
        };
        NONE_CHECK.setOnCheckedChangeListener(onCheckedChangeListener);

        LOW_CHECK.setOnCheckedChangeListener(onCheckedChangeListener);
        MIDDLE_CHECK.setOnCheckedChangeListener(onCheckedChangeListener);
        HIGH_CHECK.setOnCheckedChangeListener(onCheckedChangeListener);
        dialog.show();
    }

    public void  ChooseTypeDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_type_dialog);
        final CheckBox HTTP_CHECK=dialog.findViewById(R.id.http_check);
        final CheckBox HTTPS_CHECK=dialog.findViewById(R.id.https_check);
        final CheckBox SOCKS4_CHECK=dialog.findViewById(R.id.socks4_check);
        final CheckBox SOCKS5_CHECK=dialog.findViewById(R.id.socks5_check);

        if (use_types.contains("h")){
            HTTP_CHECK.setChecked(true);
        }
        if (use_types.contains("s")){
            HTTPS_CHECK.setChecked(true);
        }
        if (use_types.contains("4")){
            SOCKS4_CHECK.setChecked(true);
        }
        if (use_types.contains("5")){
            SOCKS5_CHECK.setChecked(true);
        }

        if (use_types.equals("")) {
            use_types="hs";
            HTTP_CHECK.setChecked(true);
            HTTPS_CHECK.setChecked(true);
        }

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == HTTP_CHECK.getId()) {

                    if (isChecked) {
                        use_types = use_types + "h";
                        Log.d(TAG, "onCheckedChanged: "+use_types);

                    }else {
                        use_types = use_types.replace("h","");
                        Log.d(TAG, "onCheckedChanged: "+use_types);


                    }
                }
                if (buttonView.getId() == HTTPS_CHECK.getId()) {
                    if (isChecked) {
                        use_types = use_types + "s";
                        Log.d(TAG, "onCheckedChanged: "+use_types);

                    }else {
                        use_types = use_types.replace("s","");
                        Log.d(TAG, "onCheckedChanged: "+use_types);


                    }
                }
                if (buttonView.getId() == SOCKS4_CHECK.getId()) {
                    if (isChecked) {
                        use_types = use_types + "4";
                        Log.d(TAG, "onCheckedChanged: "+use_types);

                    }else {
                        use_types = use_types.replace("4","");
                        Log.d(TAG, "onCheckedChanged: "+use_types);


                    }
                }
                if (buttonView.getId() == SOCKS5_CHECK.getId()) {
                    if (isChecked) {
                        use_types = use_types + "5";
                        Log.d(TAG, "onCheckedChanged: "+use_types);

                    }else {
                        use_types = use_types.replace("5","");
                        Log.d(TAG, "onCheckedChanged: "+use_types);


                    }
                }






            }
        };
        HTTPS_CHECK.setOnCheckedChangeListener(onCheckedChangeListener);

        HTTP_CHECK.setOnCheckedChangeListener(onCheckedChangeListener);
        SOCKS4_CHECK.setOnCheckedChangeListener(onCheckedChangeListener);
        SOCKS5_CHECK.setOnCheckedChangeListener(onCheckedChangeListener);
        dialog.show();
    }
    public void  ChooseCountryDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_country_dialog);

        ListView listView = dialog.findViewById(R.id.listview);


        CountryBoxAdapter adapter = new CountryBoxAdapter(this,countriesArray);
        listView.setAdapter(adapter);

        dialog.show();
    }

    public void ChooseMyProxyDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_my_proxy_dialog);
        ImageButton add = dialog.findViewById(R.id.button_add_proxy);
        myProxysListView = dialog.findViewById(R.id.list_my_proxy);
        MyProxyBoxAdapter adapter = new MyProxyBoxAdapter(this,MyProxys);
        myProxysListView.setAdapter(adapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPropxyDialog(v);


            }
        });
        dialog.show();
    }

    public void AddPropxyDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_proxy_dialog);
        final EditText name=dialog.findViewById(R.id.name_new_proxy);
        final EditText host=dialog.findViewById(R.id.host_new_proxy);
        final EditText port=dialog.findViewById(R.id.port_new_proxy);
        ImageButton s = dialog.findViewById(R.id.save_my_proxy);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProxyData myProxyData = new MyProxyData();
                myProxyData.name_proxy= String.valueOf(name.getText());
                myProxyData.host_proxy= String.valueOf(host.getText());
                try {
                    myProxyData.port_proxy= Integer.parseInt(String.valueOf(port.getText()));

                    MyProxys.add(myProxyData);
                    myProxysListView.setAdapter(new MyProxyBoxAdapter(getApplicationContext(),MyProxys));

                    dialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),getString(R.string.Input_error), LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
    }

    public void ChangeMyProxy(final View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_my_proxy_dialog);
        final EditText name=dialog.findViewById(R.id.name_c_proxy);
        final EditText host=dialog.findViewById(R.id.host_c_proxy);
        final EditText port=dialog.findViewById(R.id.port_c_proxy);
        name.setText(MyProxys.get((int)view.getTag()).name_proxy);
        host.setText(MyProxys.get((int)view.getTag()).host_proxy);
        port.setText(String.valueOf(MyProxys.get((int)view.getTag()).port_proxy));

        ImageButton s = dialog.findViewById(R.id.save_my_proxy_changes);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProxyData myProxyData = new MyProxyData();
                myProxyData.name_proxy= String.valueOf(name.getText());
                myProxyData.host_proxy= String.valueOf(host.getText());
                try {
                    myProxyData.port_proxy= Integer.parseInt(String.valueOf(port.getText()));

                    MyProxys.set((int)view.getTag(),myProxyData);
                    myProxysListView.setAdapter(new MyProxyBoxAdapter(getApplicationContext(),MyProxys));
                    dialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),getString(R.string.Input_error), LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
    }


    class MyProxyBoxAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<MyProxyData> objects;

        MyProxyBoxAdapter(Context context, ArrayList<MyProxyData> countries) {
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
                view = lInflater.inflate(R.layout.my_proxy_item, parent, false);
            }

            MyProxyData p = getProduct(position);

            // заполняем View в пункте списка данными из товаров: наименование, цена
            // и картинка
            RelativeLayout layout = view.findViewById(R.id.main_layout);
            layout.setOnClickListener(onClickListenerlayout);
            CheckBox cbBuy = (CheckBox) view.findViewById(R.id.my_proxy_check);
            cbBuy.setOnCheckedChangeListener(null);
            CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // меняем данные товара (в корзине или нет)
                    getProduct((Integer) buttonView.getTag()).isactive = isChecked;
                    int id = (Integer) buttonView.getTag();
                    if (isChecked) {
                        for (int i = 0; i < MyProxys.size(); i++) {
                            MyProxyData m =MyProxys.get(i);
                            if (i==id){
                                m.isactive=true;
                            }else {
                                m.isactive=false;
                            }
                            MyProxys.set(i,m);

                        }
                        MyProxyBoxAdapter adapter = new MyProxyBoxAdapter(getApplicationContext(),MyProxys);
                        myProxysListView.setAdapter(adapter);

                    }else {
                        boolean b=false;
                        for (int i = 0; i < MyProxys.size(); i++) {
                            MyProxyData m =MyProxys.get(i);
                            if (m.isactive){
                                b=true;
                                break;
                            }

                        }
                        if (b==false){
                            UseMyProxySwitch.setChecked(false);
                        }
                        MyProxyData m =  MyProxys.get(id);
                        m.isactive=false;
                        MyProxys.set(id,m);

                    }


                }
            };
            ImageButton delet_this_proxy;
            ImageButton change_this_proxy;
            TextView name_proxy=view.findViewById(R.id.name_my_proxy);
            TextView ip_proxy=view.findViewById(R.id.ip_my_proxy);
            delet_this_proxy=view.findViewById(R.id.delet_my_proxy);
            change_this_proxy=view.findViewById(R.id.change_my_proxy);
            String htmlTaggedString  = "<u><strong>"+getString(R.string.Name_string)+"</strong></u>: "+p.name_proxy;
            Spanned textSpan  =  android.text.Html.fromHtml(htmlTaggedString);

            name_proxy.setText(textSpan);
            htmlTaggedString  = "<u><strong>IP</strong></u>: ("+p.host_proxy+":"+ String.valueOf(p.port_proxy)+")";
            textSpan  =  android.text.Html.fromHtml(htmlTaggedString);
            ip_proxy.setText(textSpan);
            layout.setTag(position);


            cbBuy.setTag(position);
            layout.setTag(position);
            delet_this_proxy.setTag(position);
            change_this_proxy.setTag(position);

            delet_this_proxy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: try delte item "+(int)v.getTag());
                    MyProxys.remove((int)v.getTag());
                    if (MyProxys.isEmpty()){
                        UseMyProxySwitch.setChecked(false);
                    }

                    MyProxyBoxAdapter adapter =new MyProxyBoxAdapter(getApplicationContext(),MyProxys);
                    Log.d(TAG, "onClick: delet item "+v.getTag());

                    myProxysListView.setAdapter(adapter);


                }
            });
            change_this_proxy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeMyProxy(v);

                }
            });


            // заполняем данными из товаров: в корзине или нет
            cbBuy.setChecked(p.isactive);
            cbBuy.setOnCheckedChangeListener(myCheckChangeList);

            Log.d(TAG, "getView: "+p.name_proxy);
            return view;


        }

        // товар по позиции
        MyProxyData getProduct(int position) {
            return ((MyProxyData) getItem(position));
        }

        View.OnClickListener onClickListenerlayout = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag();

                boolean isChecked = !getProduct(id).isactive;
                if (isChecked) {
                    for (int i = 0; i < MyProxys.size(); i++) {
                        MyProxyData m =MyProxys.get(i);
                        if (i==id){
                            m.isactive=true;
                        }else {
                            m.isactive=false;
                        }
                        MyProxys.set(i,m);

                    }
                    MyProxyBoxAdapter adapter = new MyProxyBoxAdapter(getApplicationContext(),MyProxys);
                    myProxysListView.setAdapter(adapter);

                }else {

                    MyProxyData mp =  MyProxys.get(id);
                    mp.isactive=false;
                    MyProxys.set(id,mp);

                    boolean b=false;
                    for (int i = 0; i < MyProxys.size(); i++) {
                        MyProxyData m =MyProxys.get(i);
                        if (m.isactive){
                            b=true;
                            break;
                        }

                    }
                    if (b==false){
                        UseMyProxySwitch.setChecked(false);
                    }
                    MyProxyBoxAdapter adapter = new MyProxyBoxAdapter(getApplicationContext(),MyProxys);
                    myProxysListView.setAdapter(adapter);


                }

            }
        };
        // обработчик для чекбоксов

    }

    class CountryBoxAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<Country> objects;

        CountryBoxAdapter(Context context, ArrayList<Country> countries) {
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
                view = lInflater.inflate(R.layout.country_item, parent, false);
            }

            Country p = getProduct(position);

            // заполняем View в пункте списка данными из товаров: наименование, цена
            // и картинка


            CheckBox cbBuy = (CheckBox) view.findViewById(R.id.box);
            // присваиваем чекбоксу обработчик
            // пишем позицию
            cbBuy.setOnCheckedChangeListener(null);
            cbBuy.setTag(position);
            cbBuy.setText(p.country_name);

            // заполняем данными из товаров: в корзине или нет
            cbBuy.setChecked(p.check);
            cbBuy.setOnCheckedChangeListener(myCheckChangeList);

            Log.d(TAG, "getView: "+p.country_name);
            return view;
        }

        // товар по позиции
        Country getProduct(int position) {
            return ((Country) getItem(position));
        }

        // содержимое корзины
        ArrayList<Country> getBox() {
            ArrayList<Country> box = new ArrayList<>();
            for (Country p : objects) {
                // если в корзине
                if (p.check)
                    box.add(p);
            }
            return box;
        }

        // обработчик для чекбоксов
        CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // меняем данные товара (в корзине или нет)
                getProduct((Integer) buttonView.getTag()).check = isChecked;
                Log.d(TAG, "onCheckedChanged: country check "+ String.valueOf(buttonView.getTag())+"  "+getProduct((Integer) buttonView.getTag()).country_name);

                    if (isChecked) {
                        filter_country = filter_country.replace("("+getProduct((Integer) buttonView.getTag()).country_code+")", "");

                    } else {
                        filter_country = filter_country +"("+getProduct((Integer) buttonView.getTag()).country_code+")";
                    }

                Log.d(TAG, "onCheckedChanged: filter "+filter_country);
            }
        };
    }
}

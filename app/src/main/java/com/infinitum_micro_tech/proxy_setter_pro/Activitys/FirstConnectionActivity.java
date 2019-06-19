package com.infinitum_micro_tech.proxy_setter_pro.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.infinitum_micro_tech.proxy_setter_pro.R;


public class FirstConnectionActivity extends AppCompatActivity {
    LinearLayout OpenWifiSettings;
    Button OpenWifiList;
    private String TAG="myapp";
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_connection);
        OpenWifiList=findViewById(R.id.open_wifi_list);
        imageView=findViewById(R.id.imageView);
        if (getString(R.string.this_language).equals("RU")){
            imageView.setImageResource(R.drawable.img_screen_ru);
        }else {
            imageView.setImageResource(R.drawable.img_screen_en);

        }

    }

    public void ForgetConnection(View view) {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        OpenWifiList.setClickable(true);


    }

    public void openWifiListActivity(View view) {
        Intent i = new Intent(FirstConnectionActivity.this,ChoosConnectionActivity.class);
        startActivity(i);
    }

}

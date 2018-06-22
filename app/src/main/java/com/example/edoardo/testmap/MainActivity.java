package com.example.edoardo.testmap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //BroadcastReceiver br=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent startIntent = getIntent();
        String action =startIntent.getAction();
        if(action.equals(Intent.ACTION_SEND)){
            this.onDestroy();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText indTxt = (EditText)findViewById(R.id.indirizzoText);
        indTxt.setText("Via Pascucci 40 Albinia");

        Button b = (Button) findViewById(R.id.findLocButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String indirizzo = indTxt.getText().toString();

                List<Address> addrLs=null;
                Geocoder gc = new Geocoder(v.getContext());
                try {
                    addrLs=gc.getFromLocationName(indirizzo,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addrLs!=null && !addrLs.isEmpty()){
                    Address a = addrLs.get(0);
                    double
                            lat = a.getLatitude(),
                            lon = a.getLongitude();

                    TextView latlongTxt= (TextView)findViewById(R.id.latlongText);
                    latlongTxt.setText("Lat "+lat+" : Long "+lon);
                } else {
                    TextView latlongTxt= (TextView)findViewById(R.id.latlongText);
                    latlongTxt.setText("Location non Trovata");
                }
            }
        });

        Button bMap = (Button) findViewById(R.id.mapButton);
        bMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText indTxt = (EditText)findViewById(R.id.indirizzoText);
                String indirizzo = indTxt.getText().toString();

                Uri intUri = Uri.parse("geo:0,0?q="+Uri.encode(indirizzo));
                Intent iToSend = new Intent(Intent.ACTION_VIEW,intUri);
                iToSend.setPackage("com.google.android.apps.maps");

                if(iToSend.resolveActivity(getPackageManager())!=null){
                    startActivity(iToSend);
                }

            }
        });

        /*
        br = new BReciver();

        Intent intToFilt = new Intent();
        intToFilt.setPackage("com.google.android.apps.maps");

        IntentFilter intFilt = new IntentFilter(Intent.ACTION_SEND_MULTIPLE);
        intFilt.addAction(Intent.ACTION_SEND);
        intFilt.addAction(Intent.ACTION_SENDTO);
        //intFilt.addCategory(Intent.CATEGORY_APP_MAPS);
        getApplicationContext().registerReceiver(br,intFilt);
        * */

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extr = intent.getExtras();
        String str;
        String pack = intent.getPackage();
        if(extr.containsKey(Intent.EXTRA_TEXT)){
            str=extr.getString(Intent.EXTRA_TEXT);

            int toRemove=str.indexOf("https");
            String sToRemove="";
            for (int i=toRemove;i<str.length();i++){
                sToRemove+=str.charAt(i);
            }
            str=str.replace(sToRemove," ");

            EditText indTxt = (EditText)findViewById(R.id.indirizzoText);
            indTxt.setText(str);
        }
    }


    public class BReciver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intIndirizzo = intent;

            Bundle bund = intIndirizzo.getExtras();
            String str = bund.getString(Intent.EXTRA_TEXT);

            EditText et = (EditText) findViewById(R.id.indirizzoText);
            et.setText(str);
        }
    }
}

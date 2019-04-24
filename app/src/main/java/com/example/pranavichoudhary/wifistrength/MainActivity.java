package com.example.pranavichoudhary.wifistrength;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    int ip;
    int rssi;
    int linkSpeed;
    String ssid;
    String ipAdd;
    int strength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        Context appContext = getApplicationContext();
        WifiManager wm = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        super.onStart();
        WifiInfo wi = wm.getConnectionInfo();
        ip = wi.getIpAddress();
        rssi = wi.getRssi();
        linkSpeed = wi.getLinkSpeed();
        ssid = wi.getSSID();
        ipAdd = Formatter.formatIpAddress(ip);
        strength = wm.calculateSignalLevel(rssi , 5);
    }


    public void getInfo(View v){

        String output =
                "\n SSID : " +ssid+
                        "\n Ip Address : " +ipAdd+
                        "\n Signal strength indicator : " +rssi+"dB"+
                        "\n Speed : " +linkSpeed+"Mbps"+
                        "\n Strength : " +strength+" level out of 5";
        TextView txt = (TextView)findViewById(R.id.textView);
        txt.setText(output);
    }

    public String timeStamp(){
        Date myDate = new Date();
        return (DateFormat.getDateInstance().format(myDate) + " " + DateFormat.getTimeInstance().format(myDate));
    }

   public void saveInfo(View v) throws IOException {
       String filename = timeStamp();

       //final FileOutputStream fos = openFileOutput("hi", Context.MODE_PRIVATE);
       File f = new File(getExternalFilesDir(null), "wifi" + System.currentTimeMillis() / 1000 + ".txt");
       f.createNewFile();
       final FileOutputStream fos = new FileOutputStream(f);


       new CountDownTimer(10000, 1000) {
           Context appContext = getApplicationContext();
           WifiManager wm = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
            TextView txt = (TextView) findViewById(R.id.textView);
            public void onTick(long millisUntilFinished) {
                WifiInfo wi = wm.getConnectionInfo();
                rssi = wi.getRssi();
                String out = "Seconds " + millisUntilFinished / 1000;
                String data = out + rssi + "\n";
                byte b[] = data.getBytes();
                try {
                    fos.write(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                txt.setText(out);
            }

            public void onFinish() {
                txt.setText("done!");
            }
        }.start();
    }

}
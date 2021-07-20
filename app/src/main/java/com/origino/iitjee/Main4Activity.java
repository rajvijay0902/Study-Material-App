package com.origino.iitjee;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main4Activity extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener {
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    ArrayList<String> arrayList;
    String passed;
    Typeface typeface;
    Intent intent;
    String selectedSubject;
    String selectedChapter;
    String selectedFold;
    String selectedKey;
    String selectedKeyValue;
    JSONObject  menu;
    public void move()
    {
        passed=selectedKeyValue;
        Intent intent=new Intent(this,Main5Activity.class);
        intent.putExtra("pass",passed);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_left_enter,R.transition.slide_left_exit);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main4);
        checkInternetConnection();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AudienceNetworkAds.initialize(this);

        showAdmobBanner();

        intent=getIntent();
        String passed=intent.getStringExtra("pass");
        Log.i("info",passed);

        String[] arrOfStr = passed.split("<", 10);
        selectedFold=arrOfStr[0];
        selectedSubject=arrOfStr[2];
        selectedChapter=arrOfStr[1];

        listView=findViewById(R.id.listview);
        arrayList=new ArrayList<>();
        JSONObject object = null;
        try {
            object = new JSONObject(readJSON());
            assert selectedChapter != null;
            menu = object.getJSONObject(selectedSubject).getJSONObject(selectedChapter).getJSONObject(selectedFold);
            Map<String,String> map = new HashMap<String,String>();
            Iterator iter = menu.keys();

            while(iter.hasNext()){
                String key = (String)iter.next();
                String value = menu.getString(key);
                Log.i("info",key);
                arrayList.add(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter<String>(Main4Activity.this, android.R.layout.simple_list_item_activated_1, arrayList){
            @SuppressLint("WrongConstant")
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);
                typeface = ResourcesCompat.getFont(Main4Activity.this, R.font.basic);
                item.setTypeface(typeface);
                item.setTypeface(typeface);

                // Set the typeface/font for the current item

                // item.setTypeface(Typeface.createFromAsset(parent
                // .getContext().getAssets(), "Manuale-Bold.ttf"));

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#FF1744"));

                // Set the item text style to bold
                // item.setTypeface(Typeface.DEFAULT);

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);


                // return the view
                return item;
            }};

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedKey=arrayList.get(position);
                try {
                    selectedKeyValue=menu.getString(selectedKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("info","hello");
                move(); }}); }


    public String readJSON() {
        String json = null;
        try {
            // Opening data.json file
            InputStream inputStream = getAssets().open("app.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            // read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        };
        return json;
    }
    private void checkInternetConnection()
    {
        boolean isConnected=ConnectivityReceiver.isConnected();
        Log.i("info", String.valueOf(isConnected))   ;
        if(isConnected)
        {


        }
        if(!isConnected)
        {
            Intent intent=new Intent(this,gone.class);
            startActivity(intent);
            overridePendingTransition(R.transition.fade_enter,R.transition.fade_exit);

        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        checkInternetConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction((ConnectivityManager.CONNECTIVITY_ACTION));
        ConnectivityReceiver connectivityReceiver=new ConnectivityReceiver();
        registerReceiver(connectivityReceiver,intentFilter);
        MyApp.getInstance().setConnectivityListener((ConnectivityReceiver.ConnectivityReceiverListener) this);
    }
    public void clickedback (View view)
    {
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.transition.slide_right_enter,R.transition.slide_right_exit);

    }
    public void showAdmobBanner(){
        LinearLayout linearLayout=findViewById(R.id.adContainer);
        linearLayout.setGravity(Gravity.CENTER);
        AdView adView=new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-5062772449300897/1011203920");

        linearLayout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                //showFaceBookBanner();
                super.onAdFailedToLoad(loadAdError);
            }});
    }

    public void showFaceBookBanner(){
        LinearLayout linearLayout=findViewById(R.id.adContainer);
        linearLayout.setGravity(Gravity.CENTER);

        linearLayout.setGravity(Gravity.CENTER);
        com.facebook.ads.AdView adView=new com.facebook.ads.AdView(this,"355315145792262_355475902442853", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        linearLayout.addView(adView);
        adView.loadAd();



    }
}




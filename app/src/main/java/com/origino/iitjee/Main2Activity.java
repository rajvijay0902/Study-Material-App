package com.origino.iitjee;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity {
    ArrayAdapter<String> arrayAdapter;
    Typeface typeface;
    ListView listView;
    ArrayList<String> arrayList;
    ArrayList<String> realjsonlist;

    String selectedChapter;
    String selectedSubject;
    String key2;
    InterstitialAd mInterstitialAd;
    RewardedAd rewardedAd;
    public void move()
    {
        Intent intent=new Intent(this,Main3Activity.class);
        intent.putExtra("pass",selectedChapter+"<"+selectedSubject);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_left_enter,R.transition.slide_left_exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
       /* MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });*/


       // AudienceNetworkAds.initialize(this);
        //showAdmobInterstitial();
      //  showFacebookInterstitial();
//new add added&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
       // StartAppAd startAppAd = new StartAppAd(this);

        //startAppAd.showAd();
        //new add added end&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

        //reward again
        /*
        rewardedAd = new RewardedAd(this,
                "ca-app-pub-5062772449300897/1677240723");

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                if (rewardedAd.isLoaded()) {

                    Activity activityContext = Main2Activity.this;
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
                        }

                        @Override
                        public void onRewardedAdFailedToShow(AdError adError) {

                            // Ad failed to display.
                        }

                    };
                    rewardedAd.show(activityContext, adCallback);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                showFacebookInterstitial();
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        //reward ad work stops
        */
         




        Intent intent=getIntent();
        selectedSubject=intent.getStringExtra("subject");

        listView=findViewById(R.id.listview);
        arrayList=new ArrayList<>();
        realjsonlist=new ArrayList<>();


        JSONObject object = null;

        try {
            object = new JSONObject(readJSON());
            assert selectedSubject != null;
            JSONObject  menu = object.getJSONObject(selectedSubject);
            Map<String,String> map = new HashMap<String,String>();
            Iterator iter = menu.keys();

            while(iter.hasNext()){
                String key = (String)iter.next();
                String value = menu.getString(key);
                if(key.charAt(2)==' ')
                {
                    key2=key.substring(5);
                }
                else
                {
                    key2=key.substring(6);
                }
                arrayList.add(key2);
                realjsonlist.add(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_list_item_activated_1, arrayList){
            @SuppressLint("WrongConstant")
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);
                typeface = ResourcesCompat.getFont(Main2Activity.this, R.font.basic);
                item.setTypeface(typeface);
                item.setTypeface(typeface);


                item.setTextColor(Color.parseColor("#FFAB00"));


                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);

                // return the view
                return item;
            }};
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedChapter=realjsonlist.get(position);

                move();
            }
        });
    }


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
    public void clickedback (View view)
    {
        finish();


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.transition.slide_right_enter,R.transition.slide_right_exit);

    }
    public void showFacebookInterstitial(){
        final com.facebook.ads.InterstitialAd interstitialAd=new com.facebook.ads.InterstitialAd(Main2Activity.this,"355315145792262_355475675776209");
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());



    }
    public void showAdmobInterstitial(){

        AdRequest adRequest = new AdRequest.Builder().build();


        InterstitialAd.load(this,"ca-app-pub-5062772449300897/4648124589", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Main2Activity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });






    }



}



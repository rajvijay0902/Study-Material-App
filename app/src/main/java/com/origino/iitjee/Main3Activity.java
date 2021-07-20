package com.origino.iitjee;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;


public class Main3Activity extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener {
     int MY_REQUEST_CODE = 0;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    ArrayList<String> arrayList;
    String passed;
    Intent intent;
    String selectedSubject;
    String selectedChapter;
    String selectedFold;
    String selectedKeyValue;
    JSONObject  menu;
    int movement=0;
    String selectedKey;
    Typeface typeface;
    RewardedAd rewardedAd;
    AppUpdateManager appUpdateManager;
    UpdateManager mUpdateManager;
    Task<AppUpdateInfo> appUpdateInfoTask;



    public void move()
    {
        if(movement==0)
        {
            passed=selectedFold+"<"+selectedChapter+"<"+selectedSubject;
            Log.i("info",passed);
            Intent intent=new Intent(this,Main4Activity.class);
            intent.putExtra("pass",passed);
            startActivity(intent);
            overridePendingTransition(R.transition.slide_left_enter,R.transition.slide_left_exit);

        } else { passed=selectedKeyValue;
            Log.i("info",passed);
            Intent intent=new Intent(this,Main5Activity.class);
            intent.putExtra("pass",passed);
            startActivity(intent);
            overridePendingTransition(R.transition.slide_left_enter,R.transition.slide_left_exit);

        }}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main3);
        checkInternetConnection();
        //jan update work


        appUpdateManager = AppUpdateManagerFactory.create(Main3Activity.this);

// Returns an intent object that you use to check for an update.
         appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                finishAndRemoveTask();
                System.exit(0);
            }
        });






        //ad initialze


        AudienceNetworkAds.initialize(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //ad initilize stops
        intent=getIntent();
        String passed=intent.getStringExtra("pass");
        String[] arrOfStr = passed.split("<", 10);
        selectedSubject=arrOfStr[1];
        selectedChapter=arrOfStr[0];
        Log.i("info",selectedChapter);
        Log.i("info",selectedSubject);
        listView=findViewById(R.id.listview);
        arrayList=new ArrayList<>();
        JSONObject object = null;
        try {
            object = new JSONObject(readJSON());
            assert selectedChapter != null;
            menu = object.getJSONObject(selectedSubject).getJSONObject(selectedChapter);
            Map<String,String> map = new HashMap<String,String>();
            Iterator iter = menu.keys();

            while(iter.hasNext()){
                String key = (String)iter.next();
                String value = menu.getString(key);
                Log.i("info",value);

                arrayList.add(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter<String>(Main3Activity.this, android.R.layout.simple_list_item_activated_1, arrayList){
            @SuppressLint("WrongConstant")
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);
                typeface = ResourcesCompat.getFont(Main3Activity.this, R.font.basic);
                item.setTypeface(typeface);
                item.setTypeface(typeface);

                // Set the typeface/font for the current item

                // item.setTypeface(Typeface.createFromAsset(parent
                // .getContext().getAssets(), "Manuale-Bold.ttf"));

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#558B2F"));

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
                selectedKey =arrayList.get(position);
                try {
                    selectedKeyValue = menu.getString(selectedKey);
                    if(selectedKeyValue.contains("{"))
                    { selectedFold=selectedKey;
                        movement=0;
                        Log.i("info", String.valueOf(movement)); }
                    else { movement=1;
                        Log.i("info", String.valueOf(movement)); }move();
                } catch (JSONException e) {
                    e.printStackTrace();
                } }});}




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





        //update part
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.IMMEDIATE,
                                            this,
                                            MY_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
        //update part




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




    public void showFaceBookBanner(){
        LinearLayout linearLayout=findViewById(R.id.adContainer);
        linearLayout.setGravity(Gravity.CENTER);

        linearLayout.setGravity(Gravity.CENTER);
        com.facebook.ads.AdView adView=new com.facebook.ads.AdView(this,"355315145792262_355475675776209", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        linearLayout.addView(adView);
        adView.loadAd();




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {

                Log.i("info","Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
                appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            // For a flexible update, use AppUpdateType.FLEXIBLE
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Request the update.
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                    appUpdateInfo,
                                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                    AppUpdateType.IMMEDIATE,
                                    // The current activity making the update request.
                                    this,
                                    // Include a request code to later monitor this update request.
                                    MY_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                });

        }
    }



    // Checks that the update is not stalled during 'onResume()'.
// However, you should execute this check at all entry points into the app.


   
}




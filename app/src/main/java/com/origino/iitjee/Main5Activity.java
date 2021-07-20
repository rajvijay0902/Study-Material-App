package com.origino.iitjee;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.pdfview.PDFView;

import com.suddenh4x.ratingdialog.AppRating;


import java.io.File;
import java.util.Objects;


public class Main5Activity extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener {

    String url;
    String urlname;
    RewardedAd rewardedAd;

    String dirPath;
    Intent intent;
    PDFView pdfView;
    Button button;
    private int currentApiVersion;
    private InterstitialAd mInterstitialAd;
    int countclick;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main5);
        currentApiVersion = android.os.Build.VERSION.SDK_INT;


        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {


            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }



        //oncreate review
        showRate();

        //oncreate review



        int totalNumFiles=0;
        dirPath = getCacheDir().getAbsolutePath() + File.separator + "downloads";
        File dir = new File(dirPath);
        if(dir.listFiles()!=null)
        {
            totalNumFiles = Objects.requireNonNull(dir.listFiles()).length;

        }



        if(totalNumFiles>22)
        {
            for(File file: Objects.requireNonNull(dir.listFiles()))
                if (!file.isDirectory())
                    file.delete();
        }



        intent = getIntent();

        url = intent.getStringExtra("pass");

        urlname=url.replaceAll("[^a-zA-Z0-9]", "");
        File file = new File(dirPath, urlname);
        if(file.exists())
        {
            TextView textView = findViewById(R.id.textView);
            textView.setVisibility(View.INVISIBLE);
            pdfView = findViewById(R.id.activityMainPdfView);
            pdfView.fromFile(dirPath + "/"+urlname);
            pdfView.setDoubleTapZoomScale(0.32f);
            pdfView.setFitsSystemWindows(true);
            pdfView.setKeepScreenOn(true);
            pdfView.setMinimumHeight(2);


            pdfView.isHorizontalScrollBarEnabled();

            pdfView.show();
        }else
        {
            int downloadId = downloadFile(url);
        }





        checkInternetConnection();


        //initialize both admob and fb and startapp
        /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });*/
        //AudienceNetworkAds.initialize(this);
        //initialize done

        sharedPref = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        editor = sharedPref.edit();

        countclick = sharedPref.getInt("clickcount", 0);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        loadAdmobInterstitial();




        // showAdmobInterstitial();
         //showAdmobBanner();
        //showFacebookInterstitial();

        //banner delay

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // change

                if(countclick<3){
                    showAdmobBanner();
                }

            }

        }, 13000); // 5000ms delay

        //banner delay







    }


    private int downloadFile (String url){

        String fileName = url.replaceAll("[^a-zA-Z0-9]", "");
        dirPath = getCacheDir().getAbsolutePath() + File.separator + "downloads";

        //final String dirPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"downloads";

        return /*download id*/ PRDownloader.download(url, dirPath, fileName)
                .build()
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.i("info", "onError: down" + dirPath);
                        TextView textView = findViewById(R.id.textView);
                        textView.setVisibility(View.INVISIBLE);
                        pdfView = findViewById(R.id.activityMainPdfView);
                        pdfView.fromFile(dirPath + "/"+urlname);
                        pdfView.setDoubleTapZoomScale(0.32f);
                        pdfView.setFitsSystemWindows(true);
                        pdfView.setKeepScreenOn(true);
                        pdfView.setMinimumHeight(2);

                        pdfView.isHorizontalScrollBarEnabled();
                        pdfView.show();



                    }


                    @Override
                    public void onError(Error error) {
                        Log.i("INFO", "'ok");
                    }
                });


    }
    private void checkInternetConnection ()
    {
        boolean isConnected = ConnectivityReceiver.isConnected();
        Log.i("info", String.valueOf(isConnected));
        if (isConnected) {


        }
        if (!isConnected) {
            Intent intent = new Intent(this, gone.class);
            startActivity(intent);
            overridePendingTransition(R.transition.fade_enter,R.transition.fade_exit);

        }
    }


    @Override
    public void onNetworkConnectionChanged ( boolean isConnected){
        checkInternetConnection();
    }

    @Override
    protected void onResume () {
        if (adView != null) {
            adView.resume();
        }
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction((ConnectivityManager.CONNECTIVITY_ACTION));
        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);
        MyApp.getInstance().setConnectivityListener((ConnectivityReceiver.ConnectivityReceiverListener) this);
    }
    @SuppressLint("NewApi")

    @Override
    public void onWindowFocusChanged ( boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }
    public void clickedback (View view)
    {
        showAdmobInterstitial();
        finish();
    }

    @Override
    public void onBackPressed() {
        showAdmobInterstitial();
        super.onBackPressed();
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
        com.facebook.ads.AdView adView=new com.facebook.ads.AdView(this,"355315145792262_355475902442853", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        linearLayout.addView(adView);
        adView.loadAd();




    }
    public void loadAdmobInterstitial(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-4921696615642321/8044802249", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                                mInterstitialAd = null;

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });




    }
    public void showAdmobInterstitial(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
    }


    public void showRate(){
        new AppRating.Builder(this)
                .setMinimumLaunchTimes(4)
                .setMinimumDays(3)
                .setMinimumLaunchTimesToShowAgain(2)
                .setMinimumDaysToShowAgain(2)

                .showIfMeetsConditions();




    }
    public void showAdmobBanner(){
        LinearLayout linearLayout=findViewById(R.id.adContainer);
        linearLayout.setGravity(Gravity.CENTER);
         adView=new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-4921696615642321/3463393553");

        linearLayout.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                countclick=countclick+1;

               // Log.i("okk", String.valueOf(countclick));

                editor.putInt("clickcount", countclick);
                editor.apply();
                if(countclick>=3){
                    adView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    adView.destroy();

            }}

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.




                }



            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}













package com.origino.iitjee;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class gone extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gone);
    }
    private void checkInternetConnection()
    {
        boolean isConnected=ConnectivityReceiver.isConnected();
        Log.i("info","inside");

        if(isConnected)
        {
            Log.i("info", String.valueOf(isConnected))   ;
            kill();

        }
        if(!isConnected)
        {

        }
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        checkInternetConnection();
    }
    private void kill()
    {
        finish();
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.transition.fade_enter,R.transition.fade_exit);

    }
}

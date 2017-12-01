package com.example.xunself.x8hot_os;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    private Handler splashDelayHandler;

    private int networkStatus;

    private final int NETWORK_UNAVAILABLE = 0;
    private final int NETWORK_AVAILABLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);        //全屏模式
        setContentView(R.layout.activity_splash);
        getNetworkStatus();
        splashDelay();
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
    private void splashDelay(){
        splashDelayHandler = new Handler();
        splashDelayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);                             //退出全屏模式
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                intent.putExtra("network_status",networkStatus);
                startActivity(intent);
                finish();
            }
        },SPLASH_DISPLAY_LENGTH);
    }
    private void getNetworkStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()){
            networkStatus = NETWORK_AVAILABLE;
        }else{
            networkStatus = NETWORK_UNAVAILABLE;
        }

    }
}

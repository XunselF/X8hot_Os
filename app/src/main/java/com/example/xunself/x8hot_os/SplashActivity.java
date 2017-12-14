package com.example.xunself.x8hot_os;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    private Handler splashDelayHandler;

    private TextView dayText;
    private TextView timeText;
    //日期的提醒




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);        //全屏模式
        setContentView(R.layout.activity_splash);
        getTime();
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
                startActivity(intent);
                finish();
            }
        },SPLASH_DISPLAY_LENGTH);
    }

    /**
     * 获取时间
     */
    private void getTime(){

        SimpleDateFormat sdf1=new SimpleDateFormat("EEEE");
        SimpleDateFormat sdf2=new SimpleDateFormat("HH");
        String day = "";
        int hour = 0;
        try {
            Date Date = new Date();

            day = sdf1.format(Date);
            hour = Integer.parseInt(sdf2.format(Date));
            dayText = (TextView) findViewById(R.id.day_text);
            timeText = (TextView) findViewById(R.id.time_text);

            if (hour < 5){
                //凌晨
                timeText.setText("凌晨了！得入睡啦~");
            }else if (hour >= 6 && hour < 12){
                //早上
                timeText.setText("早上好！新的一天！");
            } else if (hour >= 12 && hour < 14){
                //中午
                timeText.setText("中午好！按时吃饭~");
            }else if (hour >= 14 && hour < 18){
                timeText.setText("下午好！");
            }
            else{
                //晚上
                timeText.setText("晚上好！晚安-_-");
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        dayText.setText(day + "，");


    }


}

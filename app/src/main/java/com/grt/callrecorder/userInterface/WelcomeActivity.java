package com.grt.callrecorder.userInterface;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.grt.callrecorder.R;
import com.grt.callrecorder.storage.MyPreferences;

/**
 * Created by DEVEN SINGH on 9/8/2015.
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        setFullScreenActivity();
        if(getIntent().getExtras()!=null){
            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(getIntent().getExtras().getInt("notificationId"));
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(new MyPreferences(WelcomeActivity.this).isPasswordProtected()){
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }else {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 1000);
    }

    public void setFullScreenActivity() {
        if (Build.VERSION.SDK_INT < 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}

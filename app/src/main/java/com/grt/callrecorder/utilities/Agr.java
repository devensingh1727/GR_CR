package com.grt.callrecorder.utilities;

import android.app.Application;

/**
 * Created by DEVEN SINGH on 9/4/2015.
 */
public class Agr extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyTypeface.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Light.ttf");
    }
}

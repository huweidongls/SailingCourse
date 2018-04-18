package com.a99zan.sailingcourse.app;

import android.app.Application;

/**
 * Created by 99zan on 2018/4/16.
 */

public class MyApp extends Application {
    private static MyApp instance;

    public MyApp() {

    }

    public synchronized static MyApp getInstance() {
        if (instance == null) {
            instance = new MyApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

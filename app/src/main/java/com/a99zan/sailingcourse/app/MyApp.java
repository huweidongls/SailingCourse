package com.a99zan.sailingcourse.app;

import android.app.Application;

import java.io.File;

import ren.yale.android.cachewebviewlib.CacheWebView;

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
        File cacheFile = new File(this.getCacheDir(),"cache_path");
        CacheWebView.getCacheConfig().init(this,cacheFile.getAbsolutePath(),1024*1024*100,1024*1024*10)
                .enableDebug(true);//100M 磁盘缓存空间,10M 内存缓存空间
    }
}

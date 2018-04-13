package com.a99zan.sailingcourse.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by 99zan on 2018/4/13.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public static final String URL = "http://teachcourse.cn";

    public String getUrl() {
        return URL;
    }

}

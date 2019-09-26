package com.example.hotfix;

import android.app.Application;
import android.content.Context;

import com.example.lib.BingFix;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //application被实例化之后最先被调用的方法，比onCreate还快
        //修复
        BingFix.installPatch(this,"/sdcard/patch.jar");
    }
}

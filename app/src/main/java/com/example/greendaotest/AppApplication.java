package com.example.greendaotest;

import android.app.Application;

import com.example.greendaotest.db.DBManager;

public class AppApplication extends Application {
    private static AppApplication mInstance;

    public static AppApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        DBManager.getInstance().init();
    }
}

package com.example.taobaounion.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
    private static Context appContent;

    @Override
    public void onCreate() {
        super.onCreate();
        appContent = getBaseContext();
    }

    public static Context getAppContent() {
        return appContent;
    }
}

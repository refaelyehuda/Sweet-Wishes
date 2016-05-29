package com.menachi.class3demo;

import android.app.Application;
import android.content.Context;

/**
 * This class save the application context during the session
 */
public class MyApplication extends Application{
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}

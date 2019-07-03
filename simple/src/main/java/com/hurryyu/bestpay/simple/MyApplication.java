package com.hurryyu.bestpay.simple;

import android.app.Application;

import com.hurryyu.bestpay.BestPay;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BestPay.init(this);
    }
}

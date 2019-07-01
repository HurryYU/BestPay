package com.hurryyu.bestpay.simple;

import android.app.Application;

import com.hurryyu.bestpay.annotations.wx.EnableWxPay;


@EnableWxPay(basePackage = "com.hurryyu.bestpay2")
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}

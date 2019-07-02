package com.hurryyu.bestpay.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hurryyu.bestpay.annotations.wx.WxPayModel;


@WxPayModel(basePackage = "com.hurryyu.bestpay",appId = "wx4504566301b")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

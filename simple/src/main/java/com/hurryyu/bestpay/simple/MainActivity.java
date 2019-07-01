package com.hurryyu.bestpay.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hurryyu.bestpay.annotations.wx.EnableWxPay;


@EnableWxPay(basePackage = "com.hurryyu.bestpay")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

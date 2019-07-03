package com.hurryyu.bestpay.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hurryyu.bestpay.BestPay;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WeChatPayModel weChatPayModel = new WeChatPayModel();
        weChatPayModel.setAppid("myAppid");
        weChatPayModel.setMypackage("myPackage");
        weChatPayModel.setNoncestr("myNoncestr");
        weChatPayModel.setPartnerid("myPartnerid");
        weChatPayModel.setSign("mySign");
        weChatPayModel.setTimestamp(201811111188L);

        BestPay.wxPay(weChatPayModel, null);
    }
}

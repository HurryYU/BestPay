package com.hurryyu.bestpay.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hurryyu.bestpay.BestPay;
import com.hurryyu.bestpay.OnPayResultListener;
import com.hurryyu.bestpay.simple.bean.WeChatPayModel;
import com.hurryyu.bestpay.simple.net.NetUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startWxPay(NetUtils.getWxPayConfig());
//        startAlipay(NetUtils.getAlipayOrderInfo());
    }

    private void startWxPay(WeChatPayModel model) {
        BestPay.wxPay(model, new OnPayResultListener() {
            @Override
            public void onPaySuccess() {
            }

            @Override
            public void onPayError(int errorCode, String errorStr) {
            }

            @Override
            public void onPayCancel() {
            }
        });
    }

    private void startAlipay(String orderInfo) {
        BestPay.aliPay(this, orderInfo, new OnPayResultListener() {
            @Override
            public void onPaySuccess() {
            }

            @Override
            public void onPayError(int errorCode, String errorStr) {
            }

            @Override
            public void onPayCancel() {
            }
        });
    }
}

package com.hurryyu.bestpay.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hurryyu.bestpay.BestPay;
import com.hurryyu.bestpay.OnPayResultListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WeChatPayModel weChatPayModel = new WeChatPayModel();
        weChatPayModel.setAppid("wx8888888888888888");
        weChatPayModel.setMypackage("Sign=WXPay");
        weChatPayModel.setNoncestr("5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
        weChatPayModel.setPartnerid("1900000109");
        weChatPayModel.setPrepayid("WX1217752501201407033233368018");
        weChatPayModel.setSign("C380BEC2BFD727A4B6845133519F3AD6");
        weChatPayModel.setTimestamp(1412000000);

        BestPay.wxPay(weChatPayModel, new OnPayResultListener() {
            @Override
            public void onPaySuccess() {
                System.out.println("success");
            }

            @Override
            public void onPayError(int errorCode, String errorStr) {
                System.out.println("error");
            }

            @Override
            public void onPayCancel() {
                System.out.println("cancel");
            }
        });
    }
}

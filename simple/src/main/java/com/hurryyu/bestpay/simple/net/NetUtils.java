package com.hurryyu.bestpay.simple.net;

import com.hurryyu.bestpay.simple.bean.WeChatPayModel;

public class NetUtils {
    /**
     * 模拟服务器返回微信支付所需信息
     */
    public static WeChatPayModel getWxPayConfig() {
        WeChatPayModel weChatPayModel = new WeChatPayModel();
        weChatPayModel.setAppid("wx8888888888888888");
        weChatPayModel.setMypackage("Sign=WXPay");
        weChatPayModel.setNoncestr("5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
        weChatPayModel.setPartnerid("1900000109");
        weChatPayModel.setPrepayid("WX1217752501201407033233368018");
        weChatPayModel.setSign("C380BEC2BFD727A4B6845133519F3AD6");
        weChatPayModel.setTimestamp(1412000000);
        return weChatPayModel;
    }

    /**
     * 模拟服务器返回支付宝订单信息
     */
    public static String getAlipayOrderInfo() {
        return "testOrderInfo";
    }
}

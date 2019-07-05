package com.hurryyu.bestpay.simple.bean;

import com.hurryyu.bestpay.annotations.wx.*;

@WxPayModel(basePackage = "com.hurryyu.bestpay.simple", appId = "wx8888888888888888")
public class WeChatPayModel {
    @WxAppId
    private String appid;
    @WxPartnerId
    private String partnerid;
    @WxPrepayId
    private String prepayid;
    @WxPackage
    private String mypackage;
    @WxNonceStr
    private String noncestr;
    @WxTimestamp
    private long timestamp;
    @WxSign
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getMypackage() {
        return mypackage;
    }

    public void setMypackage(String mypackage) {
        this.mypackage = mypackage;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

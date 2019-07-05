package com.hurryyu.bestpay;

class WxPayBean {
    private String appId;
    private String partnerId;
    private String prepayId;
    private String packageStr;
    private String nonceStr;
    private String timestamp;
    private String sign;

    @Override
    public String toString() {
        return "WxPayBean{" +
                "appId='" + appId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", packageStr='" + packageStr + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    String getAppId() {
        return appId;
    }

    void setAppId(String appId) {
        this.appId = appId;
    }

    String getPartnerId() {
        return partnerId;
    }

    void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    String getPrepayId() {
        return prepayId;
    }

    void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    String getPackageStr() {
        return packageStr;
    }

    void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    String getNonceStr() {
        return nonceStr;
    }

    void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    String getTimestamp() {
        return timestamp;
    }

    void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    String getSign() {
        return sign;
    }

    void setSign(String sign) {
        this.sign = sign;
    }
}

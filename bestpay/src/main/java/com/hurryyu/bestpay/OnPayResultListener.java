package com.hurryyu.bestpay;

public interface OnPayResultListener {
    void onPaySuccess();

    void onPayError(int errorCode, String errorStr);

    void onPayCancel();
}

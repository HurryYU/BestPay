package com.hurryyu.bestpay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PayResultReceiver extends BroadcastReceiver {

    private OnPayResultListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Constants.PAY_RESULT_BROADCAST_ACTION.equals(intent.getAction()) || mListener == null) {
            return;
        }

        String type = intent.getStringExtra("type");
        switch (type) {
            case Constants.PAY_TYPE_OK:
                mListener.onPaySuccess();
                break;
            case Constants.PAY_TYPE_ERROR:
                int errCode = intent.getIntExtra("errCode", 0);
                String errStr = intent.getStringExtra("errStr");
                mListener.onPayError(errCode, errStr);
                break;
            case Constants.PAY_TYPE_CANCEL:
                mListener.onPayCancel();
                break;
        }
        mListener = null;
    }

    public void setOnPayResultListener(OnPayResultListener listener) {
        this.mListener = listener;
    }
}

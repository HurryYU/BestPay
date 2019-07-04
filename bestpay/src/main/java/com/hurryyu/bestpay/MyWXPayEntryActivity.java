package com.hurryyu.bestpay;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * ===========================================================
 * Author: HurryYu Coding：https://coding.net/u/yuzihao
 * Email: 1037914505@qq.com
 * Time: 2019/7/1 23:33
 * Version: 1.0
 * Description:
 * ===========================================================
 */
public class MyWXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    @Override
    public void onReq(BaseReq baseReq) {
        System.out.println(baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent(Constants.PAY_RESULT_BROADCAST_ACTION);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0) {
                intent.putExtra("type", Constants.PAY_TYPE_OK);
            } else if (baseResp.errCode == -2) {
                intent.putExtra("type", Constants.PAY_TYPE_CANCEL);
            } else {
                intent.putExtra("type", Constants.PAY_TYPE_ERROR)
                        .putExtra("errCode", baseResp.errCode)
                        .putExtra("errStr", baseResp.errStr);
            }
            broadcastManager.sendBroadcast(intent);
        }

        finish();
    }
}

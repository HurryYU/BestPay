package com.hurryyu.bestpay;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.hurryyu.bestpay.annotations.wx.WxAppId;
import com.hurryyu.bestpay.annotations.wx.WxNonceStr;
import com.hurryyu.bestpay.annotations.wx.WxPackage;
import com.hurryyu.bestpay.annotations.wx.WxPartnerId;
import com.hurryyu.bestpay.annotations.wx.WxPayModel;
import com.hurryyu.bestpay.annotations.wx.WxPrepayId;
import com.hurryyu.bestpay.annotations.wx.WxSign;
import com.hurryyu.bestpay.annotations.wx.WxTimestamp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

public class BestPay {

    private static Application sApplication;

    public static void init(Context context) {
        if (context == null) {
            init(getApplicationByReflect());
            return;
        }
        init((Application) context.getApplicationContext());
    }

    private static void init(Application app) {
        if (sApplication == null) {
            sApplication = app;
        } else {
            if (app != null && app.getClass() != sApplication.getClass()) {
                sApplication = app;
            }
        }
    }

    public synchronized static void wxPay(Object payModel, @NonNull final OnPayResultListener listener) {
        if (sApplication == null) {
            throw new NullPointerException("you should call BestPay.init() in Application first");
        }
        WxPayBean wxPayBean = parseWxPayModel(payModel);

        final PayResultReceiver payResultReceiver = new PayResultReceiver();
        OnPayResultListener listenerProxy = (OnPayResultListener) Proxy.newProxyInstance(
                BestPay.class.getClassLoader(),
                new Class[]{OnPayResultListener.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object invoke = method.invoke(listener, args);
                        LocalBroadcastManager.getInstance(sApplication).unregisterReceiver(payResultReceiver);
                        return invoke;
                    }
                });
        payResultReceiver.setOnPayResultListener(listenerProxy);
        LocalBroadcastManager
                .getInstance(sApplication)
                .registerReceiver(payResultReceiver,
                        new IntentFilter(Constants.PAY_RESULT_BROADCAST_ACTION));

        startWxPay(wxPayBean);
    }

    private static void startWxPay(WxPayBean payBean) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(sApplication, payBean.getAppId(), false);
        iwxapi.registerApp(payBean.getAppId());

        PayReq payRequest = new PayReq();
        payRequest.appId = payBean.getAppId();
        payRequest.partnerId = payBean.getPartnerId();
        payRequest.prepayId = payBean.getPrepayId();
        payRequest.nonceStr = payBean.getNonceStr();
        payRequest.timeStamp = payBean.getTimestamp();
        payRequest.sign = payBean.getSign();
        payRequest.packageValue = payBean.getPackageStr();
        iwxapi.sendReq(payRequest);
    }

    private static WxPayBean parseWxPayModel(Object payBean) {
        Class<?> clazz = payBean.getClass();
        if (!clazz.isAnnotationPresent(WxPayModel.class)) {
            throw new RuntimeException("You must use the @WxPayModel annotation on the WeChat payment entity class");
        }

        WxPayBean wxPayBean = new WxPayBean();
        WxPayModel wxPayModelAnnotation = clazz.getAnnotation(WxPayModel.class);
        wxPayBean.setAppId(wxPayModelAnnotation.appId());
        wxPayBean.setPackageStr("Sign=WXPay");

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(WxAppId.class)) {
                wxPayBean.setAppId(parseWxPayModelField(field, payBean));
            } else if (field.isAnnotationPresent(WxNonceStr.class)) {
                wxPayBean.setNonceStr(parseWxPayModelField(field, payBean));
            } else if (field.isAnnotationPresent(WxPackage.class)) {
                wxPayBean.setPackageStr(parseWxPayModelField(field, payBean));
            } else if (field.isAnnotationPresent(WxPartnerId.class)) {
                wxPayBean.setPartnerId(parseWxPayModelField(field, payBean));
            } else if (field.isAnnotationPresent(WxPrepayId.class)) {
                wxPayBean.setPrepayId(parseWxPayModelField(field, payBean));
            } else if (field.isAnnotationPresent(WxSign.class)) {
                wxPayBean.setSign(parseWxPayModelField(field, payBean));
            } else if (field.isAnnotationPresent(WxTimestamp.class)) {
                wxPayBean.setTimestamp(parseWxPayModelField(field, payBean));
            }
        }
        return wxPayBean;
    }

    private static String parseWxPayModelField(Field field, Object object) {
        boolean isAccessible = field.getModifiers() == Modifier.PUBLIC;
        if (!isAccessible) {
            field.setAccessible(true);
        }

        String data = "";
        try {
            data = String.valueOf(field.get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (!isAccessible) {
                field.setAccessible(false);
            }
        }
        return data;
    }

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("you should call BestPay.init() in Application first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }
}
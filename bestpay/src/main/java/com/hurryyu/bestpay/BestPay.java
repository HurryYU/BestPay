package com.hurryyu.bestpay;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.hurryyu.bestpay.annotations.wx.WxAppId;
import com.hurryyu.bestpay.annotations.wx.WxNonceStr;
import com.hurryyu.bestpay.annotations.wx.WxPackage;
import com.hurryyu.bestpay.annotations.wx.WxPartnerId;
import com.hurryyu.bestpay.annotations.wx.WxPayModel;
import com.hurryyu.bestpay.annotations.wx.WxPrepayId;
import com.hurryyu.bestpay.annotations.wx.WxSign;
import com.hurryyu.bestpay.annotations.wx.WxTimestamp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BestPay {

    private static Application sApplication;

    private static List<Class<? extends Annotation>> annotationsList =
            new ArrayList<Class<? extends Annotation>>() {{
                add(WxAppId.class);
                add(WxNonceStr.class);
                add(WxPackage.class);
                add(WxPartnerId.class);
                add(WxPayModel.class);
                add(WxPrepayId.class);
                add(WxSign.class);
                add(WxTimestamp.class);
            }};

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

    public static void wxPay(Object payModel, OnPayResultListener listener) {
        if (sApplication == null) {
            throw new NullPointerException("you should call BestPay.init() in Application first");
        }
        WxPayBean wxPayBean = parseWxPayModel(payModel);
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
        field.setAccessible(true);
        String data = "";
        try {
            data = String.valueOf(field.get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(false);
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
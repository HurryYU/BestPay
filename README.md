# BestPay

[![Download](https://api.bintray.com/packages/hurryyu/BestPay/bestpay/images/download.svg?version=1.0.0)](https://bintray.com/hurryyu/BestPay/bestpay/1.0.0/link)

## 怎么下载（How to download）

在要使用本库的`module`的`build.gradle`中添加以下代码（Add the following code to `build.gradle` of the `module` to use the library）：

```groovy
implementation 'com.hurryyu:bestpay:last-version'
annotationProcessor 'com.hurryyu:bestpay-compiler:last-version'
```

请自行将`last-version`替换为最新版本号（Please replace `last-version` with the latest version number）

> 请注意：本框架会自动引入微信支付以及支付宝支付的官方SDK，因此你无需再次引入。（Please note: this framework will automatically introduce WeChat payment and Alibaba payment SDK , so you don't need to introduce it again.）



## 怎么使用（How to use）

首先你需要在`Application`中初始化框架（First you need to initialize the framework in `Application`）：

```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BestPay.init(this);
    }
}
```

### 微信支付：

众所周知如果你使用微信官方提供的支付SDK，你需要在项目package下新建一个名为`wxapi`的包，然后创建`WxPayEntryActivity`，此配置对项目具有极大的侵入性。并且所有关于支付的回调通知都只能在`WxPayEntryActivity`中处理，虽然可以使用`EventBus`等框架解决这个问题，不过这一切都太麻烦了。

有了BestPay，这一切都变得如此简单：

- 使用注解配置订单实体类

我们知道在调起支付前，需要向服务器获取本次支付的订单信息，这些信息一般包括：appid、partnerid、prepayid、package、noncestr、timestamp、sign。在Android中我们通常会使用一个实体类来保存这些信息：

```java
public class WeChatPayModel {
    private String appid;
    private String partnerid;
    private String prepayid;
    private String mypackage;
    private String noncestr;
    private long timestamp;
    private String sign;
    
    //... get and set ...
}
```

现在我们需要给这个实体类以及实体类中的字段加上对应的注解：

```java
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
}
```

`@WxPayModel`中的`basePackage`参数值必须与你自己项目中`AndroidManifest.xml`的`package`保持一致。

> 如果实体类中使用了@WxAppId注解，那么会优先使用被@WxAppId注解的字段的值作为appId，否则会使用@WxPayModel中配置的appId作为默认appId。

- 发起支付并监听结果

```java
WeChatPayModel model = ...向服务器请求数据
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
```

### 支付宝支付：

支付宝支付相比微信支付更加简单，首先仍然需要向服务器获取本次支付的订单信息，这个订单信息是`String`类型的。拿到后，就可以像这样来调起支付并监听结果了：

```java
String orderInfo = ...向服务器请求数据
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
```

更加值得高兴的是，这些回调都是运行在主线程上的，你可以直接在回调中操作UI。



## 感谢使用（Thank you for using）

如果你在使用过程中遇到任何问题，或是有更好的建议，欢迎与我联系，谢谢！ If you encounter any problems or have better suggestions, please feel free to contact me, thank you!

QQ：1037914505

Email：cqbbyzh@gmail.com
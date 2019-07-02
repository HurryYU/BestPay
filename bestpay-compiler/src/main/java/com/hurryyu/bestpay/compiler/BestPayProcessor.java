package com.hurryyu.bestpay.compiler;

import com.google.auto.service.AutoService;
import com.hurryyu.bestpay.annotations.wx.WxPayModel;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class BestPayProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> supportedAnnotation : getSupportedAnnotations()) {
            types.add(supportedAnnotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(WxPayModel.class);
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Set<? extends Element> annotatedWith = env.getElementsAnnotatedWith(WxPayModel.class);
        if (annotatedWith.size() < 1) {
            return true;
        }
        if (annotatedWith.size() > 1) {
            error("@WxPayModel only one can be added");
            return true;
        }
        Element element = annotatedWith.iterator().next();
        WxPayModel annotation = element.getAnnotation(WxPayModel.class);
        String basePackage = annotation.basePackage();
        String appId = annotation.appId();
        generateWXPayEntryActivity(basePackage + ".wxapi", appId);
        return true;
    }

    private void generateWXPayEntryActivity(String generatePackage, String appId) {

        ClassName myWXPayEntryActivity =
                ClassName.get("com.hurryyu.bestpay", "MyWXPayEntryActivity");

        ClassName bundleClassName = ClassName.get("android.os", "Bundle");

        ClassName IwxpaiClassName = ClassName.get("com.tencent.mm.opensdk.openapi", "IWXAPI");

        ClassName IntentClassNmae = ClassName.get("android.content", "Intent");

        MethodSpec onCreate = MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PROTECTED)
                .returns(void.class)
                .addParameter(bundleClassName, "savedInstanceState")
                .addAnnotation(Override.class)
                .addStatement(buildWxPayEntryOnCreateStatement(appId))
                .build();

        MethodSpec onNewIntent = MethodSpec.methodBuilder("onNewIntent")
                .addModifiers(Modifier.PROTECTED)
                .returns(void.class)
                .addParameter(IntentClassNmae, "intent")
                .addAnnotation(Override.class)
                .addStatement("super.onNewIntent(intent)")
                .addStatement("setIntent($N)", "intent")
                .addStatement("$N.handleIntent($N, this)", "api", "intent")
                .build();

        TypeSpec WxPayEntryActivity = TypeSpec.classBuilder("WXPayEntryActivity")
                .addModifiers(Modifier.PUBLIC)
                .superclass(myWXPayEntryActivity)
                .addField(IwxpaiClassName, "api", Modifier.PRIVATE)
                .addMethod(onCreate)
                .addMethod(onNewIntent)
                .build();

        JavaFile file = JavaFile.builder(generatePackage, WxPayEntryActivity).build();
        try {
            file.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CodeBlock buildWxPayEntryOnCreateStatement(String appId) {
        ClassName wxapiFactoryClassName =
                ClassName.get("com.tencent.mm.opensdk.openapi", "WXAPIFactory");

        return CodeBlock.builder()
                .add("super.onCreate(savedInstanceState);\n")
                .add("this.$N = $T.createWXAPI(this, $S);\n", "api", wxapiFactoryClassName, appId)
                .add("$N.handleIntent(getIntent(), this)", "api")
                .build();
    }

    private void error(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

}

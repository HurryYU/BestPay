package com.hurryyu.bestpay.compiler;

import com.google.auto.service.AutoService;
import com.hurryyu.bestpay.annotations.wx.EnableWxPay;
import com.squareup.javapoet.ClassName;
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
        annotations.add(EnableWxPay.class);
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Set<? extends Element> annotatedWith = env.getElementsAnnotatedWith(EnableWxPay.class);
        if (annotatedWith.size() < 1) {
            return true;
        }
        if (annotatedWith.size() > 1) {
            error("@EnableWxPay only one can be added, recommended on your Application class");
            return true;
        }
        Element element = annotatedWith.iterator().next();
        EnableWxPay annotation = element.getAnnotation(EnableWxPay.class);
        String basePackage = annotation.basePackage();
        generateWXPayEntryActivity(basePackage + ".wxapi");
        return true;
    }

    private void generateWXPayEntryActivity(String generatePackage) {

        ClassName myWXPayEntryActivity = ClassName.get("com.hurryyu.bestpay", "MyWXPayEntryActivity");

        TypeSpec hello = TypeSpec.classBuilder("WXPayEntryActivity")
                .addModifiers(Modifier.PUBLIC)
                .superclass(myWXPayEntryActivity)
                .build();

        JavaFile file = JavaFile.builder(generatePackage, hello).build();
        try {
            file.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void error(String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args));
    }

}

package com.xiaowenhou.reids.operate.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ProceedingJoinPointUtils {
    private ProceedingJoinPointUtils(){}

    public static <A extends Annotation> A getMethodAnnotation(ProceedingJoinPoint point, Class<A> annotationClass) {
        MethodSignature methodSig = (MethodSignature) point.getSignature();
        return methodSig.getMethod().getAnnotation(annotationClass);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A getTypeAnnotation(ProceedingJoinPoint point, Class<A> annotationClass) {
        MethodSignature methodSig = (MethodSignature) point.getSignature();
        return (A) methodSig.getDeclaringType().getAnnotation(annotationClass);
    }


    public static Parameter[] getMethodParameters(ProceedingJoinPoint point) {
        MethodSignature methodSig = (MethodSignature) point.getSignature();
        return methodSig.getMethod().getParameters();
    }
}

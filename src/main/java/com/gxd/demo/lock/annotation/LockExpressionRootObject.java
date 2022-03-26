package com.gxd.demo.lock.annotation;

import java.lang.reflect.Method;

public class LockExpressionRootObject {
    private final Method method;
    private final Object[] args;
    private final Object target;
    private final Class<?> targetClass;

    public LockExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass) {
        this.method = method;
        this.args = args;
        this.target = target;
        this.targetClass = targetClass;
    }

    public Method getMethod() {
        return method;
    }

    public String getMethodName() {
        return method.getName();
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getTarget() {
        return target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }
}

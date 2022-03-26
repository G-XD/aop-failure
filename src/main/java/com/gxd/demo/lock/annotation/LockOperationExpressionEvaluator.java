package com.gxd.demo.lock.annotation;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockOperationExpressionEvaluator extends CachedExpressionEvaluator {
    private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap(64);

    LockOperationExpressionEvaluator() {
    }

    public EvaluationContext createEvaluationContext(Method method, Object[] args, Object target, Class<?> targetClass, Method targetMethod, @Nullable BeanFactory beanFactory) {
        LockExpressionRootObject rootObject = new LockExpressionRootObject(method, args, target, targetClass);
        MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(rootObject, targetMethod, args, this.getParameterNameDiscoverer());
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }

        return evaluationContext;
    }

    @Nullable
    public Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return this.getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext);
    }

    void clear() {
        this.keyCache.clear();
    }
}


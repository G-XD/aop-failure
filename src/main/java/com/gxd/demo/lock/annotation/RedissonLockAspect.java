package com.gxd.demo.lock.annotation;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Aspect
@Component
@Order(1)
public class RedissonLockAspect {
    private static final Logger log = LoggerFactory.getLogger(RedissonLockAspect.class);
    private final RedissonClient redissonClient;
    LockOperationExpressionEvaluator evaluator = new LockOperationExpressionEvaluator();
    private BeanFactory beanFactory;

    public RedissonLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Pointcut("@annotation(com.gxd.demo.lock.annotation.RedissonLock)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object target = joinPoint.getTarget();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock redissonLock = (RedissonLock) method.getAnnotation(RedissonLock.class);
        Class<?> targetClass = target.getClass();
        Method targetMethod = !Proxy.isProxyClass(targetClass) ? AopUtils.getMostSpecificMethod(method, targetClass) : method;
        EvaluationContext evaluationContext = this.evaluator.createEvaluationContext(method, args, target, targetClass, targetMethod, this.beanFactory);
        Object key = this.evaluator.key(redissonLock.key(), new AnnotatedElementKey(targetMethod, targetClass), evaluationContext);
        Object lock;
        if (key instanceof Collection) {
            Stream<RLock> stream = ((Collection) key).stream().map((e) -> {
                return this.redissonClient.getLock((String) e);
            });
            lock = new RedissonMultiLock((RLock[]) stream.toArray((x$0) -> {
                return new RLock[x$0];
            }));
        } else {
            if (!(key instanceof String)) {
                throw new Exception("redisson lock的key最终结果必须为非空String或Collection<非空String>");
            }

            lock = this.redissonClient.getLock((String) key);
        }
        int timeout = redissonLock.timeout();
        int waitTime = 5;
        if (((RLock) lock).tryLock((long) waitTime, (long) timeout, TimeUnit.SECONDS)) {
            Object var14;
            try {
                log.info("locked==" + key);
                var14 = joinPoint.proceed();
            } finally {
                ((RLock) lock).unlock();
                log.info("lock release!");
            }
            return var14;
        } else {
            System.out.println("failed");
            log.error("get redis lock failed");
            throw new RuntimeException("redisson lock failed");
        }
    }


}

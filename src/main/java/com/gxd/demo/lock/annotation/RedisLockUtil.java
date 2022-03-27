package com.gxd.demo.lock.annotation;

import java.util.concurrent.TimeUnit;

public class RedisLockUtil {

    private final String key;

    public RedisLockUtil(String key) {
        this.key = key;
    }

    public boolean tryLock(long waitTime, long timeout, TimeUnit timeUnit) {
        return true;
    }

    public void unlock() {
    }
}

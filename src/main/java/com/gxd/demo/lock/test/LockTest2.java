package com.gxd.demo.lock.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LockTest2 {

    @Autowired
    private LockTest1 lockTest1;

    public void callLockTest1() throws InterruptedException {
        lockTest1.lockTest();
    }
}

package com.gxd.demo.lock.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;


@Service
public class HelloService {

    @Autowired
    private LockTest1 lockTest1;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //@RedissonLock(key = "#root.targetClass.getName")
    //@Scheduled(cron = "1/10 * * * * ? ")
    public void scheduledJob() throws InterruptedException {
        //lockTest.lockTest();
        System.out.println("-------------------");
    }
}

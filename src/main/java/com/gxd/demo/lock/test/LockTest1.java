package com.gxd.demo.lock.test;

import com.gxd.demo.lock.annotation.RedissonLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class LockTest1 {

    @Autowired
    private LockTest2 lockTest2;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public String key = "test";

    @RedissonLock(key = "#root.targetClass.getName()")
    @Scheduled(cron = "1/10 * * * * ? ")
    public void lockTest() throws InterruptedException {
        System.out.println("Scheduled task：" + dateFormat.format(new Date()));
    }
}

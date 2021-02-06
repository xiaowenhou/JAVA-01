package com.xiaowenhou.homework;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WithLockCondition {
    private static int result = 0;
    private final static Lock LOCK = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        //int result = sum(); //这是得到的返回值
        Condition condition = LOCK.newCondition();
        new Thread(() ->{
            LOCK.lock();
            try {
                result = sum();
                condition.signalAll();
            } finally {
                LOCK.unlock();
            }
        }).start();

        while (result == 0) {
            LOCK.lock();
            try {
                condition.await();
            } finally {
                LOCK.unlock();
            }
        }

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }
}

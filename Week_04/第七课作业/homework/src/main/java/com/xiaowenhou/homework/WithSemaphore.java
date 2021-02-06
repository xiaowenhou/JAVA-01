package com.xiaowenhou.homework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class WithSemaphore {

    private static int result = 0;
    public static void main(String[] args) throws InterruptedException {

        Semaphore semaphoreA = new Semaphore(1);
        Semaphore semaphoreB = new Semaphore(0);
        long start=System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

       // int result = 0; //这是得到的返回值
        new Thread(() -> {
            //新创建的线程先获取许可， 执行完之后再给主线程发放许可
            try {
                semaphoreA.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = sum();
            semaphoreB.release();
        }).start();

        semaphoreB.acquire();

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

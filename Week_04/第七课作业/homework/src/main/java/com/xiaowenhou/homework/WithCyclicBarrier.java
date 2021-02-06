package com.xiaowenhou.homework;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class WithCyclicBarrier {

    private static int result = 0;

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        long start = System.currentTimeMillis();

        CyclicBarrier barrier = new CyclicBarrier(2);

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        //int result = sum(); //这是得到的返回值
        new Thread(() -> {
            result = sum();
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();
        barrier.await();

        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }
}

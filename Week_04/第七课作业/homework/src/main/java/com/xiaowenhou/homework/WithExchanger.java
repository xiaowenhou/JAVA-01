package com.xiaowenhou.homework;


import java.util.concurrent.Exchanger;

public class WithExchanger {
    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();
        //定义Exchanger对象
        Exchanger<Integer> exchanger = new Exchanger<>();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        //int result = sum(); //这是得到的返回值
        new Thread(() -> {
            int sum = sum();
            try {
                //交换两个线程的值
                exchanger.exchange(sum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        //交换两个线程的值， 该result就是新线程中计算的结果
        int result = exchanger.exchange(0);
        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
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

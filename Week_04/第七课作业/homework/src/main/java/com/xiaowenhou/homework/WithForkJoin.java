package com.xiaowenhou.homework;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class WithForkJoin {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        // 创建ForkJoin
        ForkJoinPool fjp = new ForkJoinPool(1);
        //创建任务
        Compute compute = new Compute();
        //执行任务
        int result = fjp.invoke(compute);

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

    //定义分治的任务类， 该类继承RecursiveTask
    static class Compute extends RecursiveTask<Integer> {
        @Override
        protected Integer compute() {
            return sum();
        }
    }
}

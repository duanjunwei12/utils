package com.util.concurrency.synchronizeds;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String args[]){
        int parties = 3;

        CyclicBarrier cyclicBarrier = new CyclicBarrier(parties);
        for (int i = 0; i < parties; i++) {
            new WorkThread(cyclicBarrier).start();
        }
    }
}
class WorkThread extends Thread {
    private CyclicBarrier cyclicBarrier;

    public WorkThread(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            int threadId = (int) Thread.currentThread().getId();
            System.out.println("线程" + threadId + "开始...");
            if(threadId % 2 == 0){
                System.out.println("线程" + threadId + "准备睡觉了");
                Thread.sleep(5000);
                System.out.println("线程" + threadId + "醒了");
            }
            System.out.println("线程" + threadId + "结束");

            System.out.println("线程" + threadId + "等待...");
            cyclicBarrier.await();

            System.out.println("线程" + Thread.currentThread().getId() + "解锁，go !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.util.concurrency.synchronizeds;

public class SellTicket {
    public static void main(String[] args) {
        // 创建票对象
        Ticket ticket = new Ticket();
        // 创建3个窗口
        Thread t1 = new Thread(ticket, "窗口1");
        Thread t2 = new Thread(ticket, "窗口2");
        Thread t3 = new Thread(ticket, "窗口3");
        t1.start();
        t2.start();
        t3.start();
    }
}
// 模拟票
class Ticket implements Runnable {
    // 共100票
    int ticket = 100;

    Object lock = new Object();

    public void run() {
        // 模拟卖票
        while (true) {
            // 同步方法
            method();
        }
    }

    // 同步方法,锁对象this
    public synchronized void method() {
        if (ticket > 0) {
            // 模拟选坐的操作
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "正在卖票:"
                    + ticket--);
        }
    }
}

package com.util.concurrency.synchronizeds;

import java.util.HashSet;

public class DeadLock {
    Object obj1 = new Object();
    Object obj2 = new Object();

    public void a() {
        synchronized (obj1) {
            synchronized (obj2) {
                System.out.println("a");
            }
        }
    }
    public void b() {
        synchronized (obj2) {
            synchronized (obj1) {
                System.out.println("b");
            }
        }
    }

    public static void main(String[] args) {
        HashSet set = new HashSet();
        final DeadLock d = new DeadLock();
        new Thread(new Runnable() {
            public void run() {
                d.a();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                d.b();
            }
        }).start();
    }
}

package com.util.concurrency.synchronizeds;
/*
上述的代码，我们分析一下，两个方法，方法a和方法b都被synchronized关键字修饰，锁对象是当前对象实例，按照上文我们对synchronized的了解，如果调用方法a,在方法a还没有执行完之前，我们是不能执行方法b的，方法a必须先释放锁，方法b才能执行，方法b处于等待状态，那样不就形成死锁了吗？那么事实真的如分析一致吗？

代码很快就执行完了，实验结果与分析不一致，这就引入了另外一个概念：重入锁。在 java 内部，同一线程在调用自己类中其他 synchronized 方法/块或调用父类的 synchronized 方法/块都不会阻碍该线程的执行。就是说同一线程对同一个对象锁是可重入的，而且同一个线程可以获取同一把锁多次，也就是可以多次重入。在JDK1.5后对synchronized关键字做了相关优化。


* */
public class ReentrantLockDemo {
    public synchronized void a() {
        System.out.println("a");
        b();
    }

    private synchronized void b() {
        System.out.println("b");
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                ReentrantLockDemo d = new ReentrantLockDemo();
                d.a();
            }
        }).start();
    }
}

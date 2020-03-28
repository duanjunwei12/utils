package com.util.concurrency.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public static void main(String args[]){
        AtomicInteger i = new AtomicInteger();
        i.addAndGet(1);
        System.out.println(i);
        System.out.println(i.get());
        i.compareAndSet(1,3);
        System.out.println(i);
    }
}

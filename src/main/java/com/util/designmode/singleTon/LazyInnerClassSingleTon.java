package com.util.designmode.singleTon;

import java.io.Serializable;

public class LazyInnerClassSingleTon implements Serializable {


    // 使用InnerClass generate 的时候默认会先加载内部类
    // 如果没使用，内部类不会加载

    private LazyInnerClassSingleTon() {
        System.out.println("cons");
    }

    public static final LazyInnerClassSingleTon getInstance(){
        System.out.println("out class");
        LazyInnerClassSingleTon lazyInnerClassSingleTon = LazyHolder.getInstance();
        return lazyInnerClassSingleTon;
    }
    private static class LazyHolder{
        private LazyHolder() {
            System.out.println("inner cons");
        }

        private static final LazyInnerClassSingleTon getInstance(){
            System.out.println("inner class");
            return new LazyInnerClassSingleTon();
        }
    }
    public static void main(String[] args){
        LazyInnerClassSingleTon.getInstance();

    }
}


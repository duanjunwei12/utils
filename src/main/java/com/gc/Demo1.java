package com.gc;

public class Demo1 {
    public static Demo1 me;
    //垃圾回收 测试
    public static void main(String[] args) throws InterruptedException {
        me = new Demo1();
        me = null;
        if(me != null){
            System.out.println("我 还活着 ！！");
        }else {
            System.out.println("我 死 了！！");
        }

    }

}

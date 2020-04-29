package com.gc;

public class Demo{
    public static Demo me;
    //垃圾回收 测试
    public static void main(String[] args) throws InterruptedException {
        me = new Demo();
        me = null;
        System.gc();
        //finalizer 方法执行优先级较低，暂停0.5秒
        Thread.sleep(500);
        if(me != null){
            me.isAlive();
        }else {
            System.out.println("我 死 了！！");
        }
        me = null;
        System.gc();
        //finalizer 方法执行优先级较低，暂停0.5秒
        Thread.sleep(500);
        if(me != null){
            me.isAlive();
        }else {
            System.out.println("我 死 了！！");
        }

    }
    public void isAlive(){
        System.out.println("我还活着。。。");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalized method execute...");
        Demo.me = this;
    }
}

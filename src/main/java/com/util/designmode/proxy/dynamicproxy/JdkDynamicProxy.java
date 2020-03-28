package com.util.designmode.proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicProxy {
    public static void main(String[] args) {
        IGamePlayer gamePlayer = new GamePlayer();
        JdkProxy jdkProxy = new JdkProxy();
        //获取代理对象，因为挂在接口IGamePlayer下，所以声明代理对象IGamePlayer
        IGamePlayer proxy = (IGamePlayer) jdkProxy.getProxy(gamePlayer);
        proxy.play();
    }
}



interface IGamePlayer{
    void play();
}
class GamePlayer implements IGamePlayer{

    public void play() {
        System.out.println("玩游戏，升级...");
    }
}

class JdkProxy implements InvocationHandler {
    //目标对象
    private Object target;
    /**
     * 建立代理对象和真实对象的代理关系，并返回代理对象
     * @param object    真实对象
     * @return      代理对象
     *
     * 其中newProxyInstance方法包含了3个参数：
     *  （1）第一个是类加载器，采用了target本身的类加载器
     *  （2）第二个是把生成的动态代理对象挂在哪些接口下，这个就是挂在target实现的接口下
     *  （3）第三个定义实现方法逻辑的代理类，this表示当前对象
     */
    public Object getProxy(Object object){
        this.target = object;
        Object proxy = Proxy.newProxyInstance(object.getClass().getClassLoader(),object.getClass().getInterfaces(),this);
        return proxy;
    }
    /**
     * @param proxy     代理对象
     * @param method    当前调度方法
     * @param args      当前方法参数
     * @return          代理结果返回
     * @throws Throwable        异常
     *
     * invoke方法可以实现代理逻辑，invoke方法3个参数含义如下：
     * （1）proxy：代理对象，就是bind生成的对象
     * （2）method：当前调度的方法。
     * （3）args：调度方法的参数
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target,args);
    }
}

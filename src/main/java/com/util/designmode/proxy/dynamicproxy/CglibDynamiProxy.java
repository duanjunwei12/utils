package com.util.designmode.proxy.dynamicproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibDynamiProxy implements MethodInterceptor {

    public Object getProxy(Class clazz){
        //CGLIB enhancer增强类对象
        Enhancer enhancer = new Enhancer();
        //设置增强类型（父类）
        enhancer.setSuperclass(clazz);
        //定义代理逻辑对象为当前对象，要求当前对象实现MethodInterceptor方法
        enhancer.setCallback(this);
        //生成并返回代理对象
        return enhancer.create();
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return methodProxy.invokeSuper(proxy, args);
    }
    public static void main(String[] args) {
        CglibDynamiProxy cglibProxy = new CglibDynamiProxy();
        GamePlayer proxy = (GamePlayer) cglibProxy.getProxy(GamePlayer.class);
        proxy.play();
    }

}

package com.util.io.nio.time;

public class TimeClient {
    private static int port = 8080;
    public static void main(String args[]){
//        try{
//            port = Integer.valueOf(args[0]);
//        }catch (Exception e){
//
//        }
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClient-001").start();
    }
}

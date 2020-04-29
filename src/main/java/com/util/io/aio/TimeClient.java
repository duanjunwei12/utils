package com.util.io.aio;

public class TimeClient {
    public static void main(String args[]){
        int port = 8081;
        AsyncTimeClientHandler timeClientHandler = new AsyncTimeClientHandler("127.0.0.1",port);
        new Thread(timeClientHandler,"AIO-client-001").start();
    }
}

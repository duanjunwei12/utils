package com.util.io.aio;

public class TimeServer {
    public static void main(String args[]){
        int port = 8081;
        AsyncTimeServerHandler timeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(timeServerHandler,"AIO-server-001").start();
    }
}

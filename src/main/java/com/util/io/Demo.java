package com.util.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class Demo {
    static IntBuffer intBuffer = null;
    public static void main(String args[]) throws Exception {
        intBuffer = IntBuffer.allocate(20);//20*4字节的内存空间
        for (int i = 0; i < 5;i++){
            intBuffer.put(i);//写入5个数字
        }
        intBuffer.flip();//反转
        intBuffer.rewind();//倒带


        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
//        serverSocketChannel.bind(new InetSocketAddress(80));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(selector.select()>0){

        }
    }
}

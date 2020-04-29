package com.util.io.nio.time;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandle implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandle(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try{
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (! stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                     key = it.next();
                     it.remove();
                     try{
                         handleInput(key);
                     }catch (Exception e){
                         if (key != null){
                             key.cancel();
                             if (key.channel() != null)
                                 key.channel().close();
                         }
                     }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if (selector != null){
            try{
                selector.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException{
        if (key.isValid()){
            SocketChannel sc = (SocketChannel)key.channel();
            if (key.isConnectable()){
                if (sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc);
                }else
                    System.exit(1);//连接失败， 进程退出
            }
            if (key.isReadable()){
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes > 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("NOW IS : " + body);
                    this.stop = true;
                }else if (readBytes < 0){
                    key.channel();
                    sc.close();
                }else
                    ;


            }
        }
    }
    private  void doConnect() throws IOException{
        //判断是否连接成功，如果连接成功，则直接注册读状态，到多路复用器，
        // 如果没有连接成功，异步连接，返回false ,说明 客户端已经发送了sync包，服务端没有返回 ack的包，物理链路没有建立
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector,SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
    }
    private void doWrite(SocketChannel sc) throws IOException {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeByffer = ByteBuffer.allocate(req.length);
        writeByffer.put(req);
        writeByffer.flip();
        sc.write(writeByffer);
        if (!writeByffer.hasRemaining()){
            System.out.println("send order 2 server succeed.");
        }

    }
}

package com.util.io.nio.time;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable{
    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run() {
        while (!stop){
            try{
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while(it.hasNext()){
                    key = it.next();
                    it.remove();
                    try{
                        handleInput(key);
                    }catch(Exception e){
                        if (key!=null){
                            key.cancel();
                            if (key.channel() != null)
                                key.channel().close();
                        }
                        e.printStackTrace();
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
        if (selector != null){
            try{
                selector.close();
            }catch(Exception e){

            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException{
        if (key.isValid()){
            if (key.isAcceptable()){
                ServerSocketChannel ssc  = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();//监听到 有新的客户端接入时，处理新的接入请求，完成TCP三次握手，建立物理连接
                sc.configureBlocking(false);//设置客户端链路为非阻塞模式
                sc.register(selector,SelectionKey.OP_READ);//将新接入的客户端注册到选择器上，监听读操作

            }
            if (key.isReadable()){
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);//异步读取客户端请求消息至缓冲区
                if (readBytes > 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("the time server receive order : " + body);
                    String currentTime = "QUERY TIME ORDER"
                            .equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString()
                            : "BAD ORDER";
                    doWrite(sc,currentTime);
                }else if (readBytes < 0){
                    key.cancel();
                    sc.close();
                }else {
                    ;
                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException{
        if(response != null && response.trim().length() >0){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }

    public void stop(){
        this.stop = true;
    }



}

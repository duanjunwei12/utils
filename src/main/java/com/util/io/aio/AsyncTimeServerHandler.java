package com.util.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandler implements Runnable {
    private int port;

    CountDownLatch latch;

    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try{
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("the time server is start in port:"+port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        latch = new CountDownLatch(1);
        try{
            latch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void doAccept(){
        asynchronousServerSocketChannel.accept(this,new AcceptCompletionHandler());
    }
}
class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {


    @Override
    public void completed(AsynchronousSocketChannel result,
                          AsyncTimeServerHandler attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment,this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer,buffer,new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
class ReadCompletionHandler implements CompletionHandler<Integer,ByteBuffer>{

    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel) {
        if (this.channel == null)
            this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String req = new String(body,"UTF-8");
            System.out.println("the time server receive order : " + req);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req)?
                    new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
            doWrite(currentTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void doWrite(String currentTime){
        if (currentTime != null && currentTime.trim().length() > 0){
            byte[] bytes = currentTime.getBytes();
            final ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer, writeBuffer,
                    new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            //如果没有发送完成，继续发送
                            if (buffer.hasRemaining())
                                channel.write(buffer,buffer,this);
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                channel.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}

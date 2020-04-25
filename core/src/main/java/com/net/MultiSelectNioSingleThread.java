package com.net;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiluo
 * @ClassName
 * @description 采用 Select 多路复用的方式来完成 NIO 的编程，避免掉 NIOTest 中在一个线程中空转消耗系统资源的情况
 * @date 2020/4/25 23:03
 * @Version 单线程版本
 **/
public class MultiSelectNioSingleThread {

    public static void main(String[] args) throws Exception {

        ExecutorService service = new ThreadPoolExecutor(1, 1,
                60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8088));
        server.configureBlocking(false);

        Selector selector = Selector.open();
        // 将当前 server 注册到 selector 上，并且指定 selector 对当前 server 的 OP_ACCEPT 事件感兴趣
        server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println(MultiSelectNioSingleThread.class + " Server started: " + server);

        while (true) {
            int nbr = selector.select(0);
            if (nbr > 0) {
                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keySet.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    // 必须要删除掉，防止之前被 select 到的 key 还存在 selected keys 中
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        handleAccept(key, selector);
                    } else if (key.isReadable()) {
                        /**
                         * 这里是不可以采用异步的方式进行处理的
                         * 数据的读取必须是同步的
                         * 否则会出现，异步线程在读取数据的同时，selector.select() 方法同样可以读取到当前 channel 是可读状态的情况
                         * 故对于 channel 中的数据只能采用同步读取的方式读取
                         * 如果一定要做异步，则只能把数据读出来之后，再次对读出来的数据做异步处理了
                         */
                        /*service.execute(() -> {
                                    try {
                                        handleRead(key);
                                    } catch (Exception i) {
                                        i.printStackTrace();
                                    }
                                }
                        );*/

                        handleRead(key);
                    }
                }
            }
        }
    }

    private static void handleRead(SelectionKey key) throws Exception {
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel channel = (SocketChannel) key.channel();

        buffer.clear();
        byte[] buf = new byte[1024];
        int sig;
        while ((sig = channel.read(buffer)) > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                int len = Math.min(buf.length, buffer.remaining());
                buffer.get(buf, 0, len);
                System.out.println("read from client: " + new String(buf, 0, len, Charset.defaultCharset()));
            }

            buffer.clear();
        }

        if (sig == -1) {
            key.cancel();
            System.out.println("client closed: " + channel);
            channel.close();
        }

        if (sig == 0) {
            System.out.println("client read 0: " + channel);
        }
    }

    private static void handleAccept(SelectionKey key, Selector selector) throws Exception {
        // 注意这里，因为是监听的是 ACCEPT 事件，故进入的肯定是 ServerSocketChannel
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel newChannel = server.accept();

        // 防止异常情况下的空指针异常
        if (newChannel != null) {
            newChannel.configureBlocking(false);
            newChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(4096));
            System.out.println("accept new connection: " + newChannel);
        }
    }
}

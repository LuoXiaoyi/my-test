package com.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xiluo
 * @ClassName
 * @description 采用 Select 多路复用的方式来完成 NIO 的编程，避免掉 NIOTest 中在一个线程中空转消耗系统资源的情况
 * @date 2020/4/25 23:40
 * @Version 多线程版本
 **/
public class MultiSelectNioMultiThread {

    public static void main(String[] args) throws Exception {
        new NioThread(2).start();
    }

    static class NioThread extends Thread {
        private Selector[] workerSelectors;

        NioThread(int workerNbr) throws IOException {
            workerSelectors = new Selector[workerNbr];

            for (int i = 0; i < workerNbr; ++i) {
                workerSelectors[i] = Selector.open();
            }
        }

        @Override
        public void run() {
            try {
                ServerSocketChannel server = ServerSocketChannel.open();
                server.bind(new InetSocketAddress(8088));
                server.configureBlocking(false);

                Selector bossSelector = Selector.open();
                // 将当前 server 注册到 selector 上，并且指定 selector 对当前 server 的 OP_ACCEPT 事件感兴趣
                server.register(bossSelector, SelectionKey.OP_ACCEPT);
                System.out.println(MultiSelectNioMultiThread.class + " Server started: " + server);

                for (Selector workerSelector : workerSelectors) {
                    new Thread(() -> {
                        try {
                            while (true) {
                                doSelect(workerSelector);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }

                while (true) {
                    doSelect(bossSelector);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void doSelect(Selector selector) throws Exception {
            int nbr = selector.select(10);
            if (nbr > 0) {
                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keySet.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    // 必须要删除掉，防止之前被 select 到的 key 还存在 selected keys 中
                    keyIterator.remove();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        }

        private void handleRead(SelectionKey key) throws Exception {
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
                System.out.println("client closed: " + channel);
                channel.close();
            }

            if (sig == 0) {
                System.out.println("client read 0: " + channel);
            }
        }

        private void handleAccept(SelectionKey key) throws Exception {
            // 注意这里，因为是监听的是 ACCEPT 事件，故进入的肯定是 ServerSocketChannel
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel newChannel = server.accept();

            // 防止异常情况下的空指针异常
            if (newChannel != null) {
                newChannel.configureBlocking(false);
                int idx = newChannel.hashCode() % this.workerSelectors.length;
                newChannel.register(this.workerSelectors[idx],
                        SelectionKey.OP_READ, ByteBuffer.allocate(4096));
                System.out.println("accept new connection[" + idx + "]: " + newChannel);
            }
        }
    }
}

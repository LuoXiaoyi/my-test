package com.net;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiluo
 * @ClassName
 * @description TODO
 * @date 2020/4/25 22:32
 * @Version 1.0
 **/
public class NIOTest {

    public static void main(String[] args) throws Exception {
        List<SocketChannel> clients = new LinkedList<>();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8088));
        server.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        System.out.println("Server started...");

        while (true) {
            SocketChannel newClient = server.accept();
            if (newClient != null) {
                // 将客户端设置为非阻塞的，这样在调用客户端读取数据的时候就能立即返回，不会出现阻塞的状态
                newClient.configureBlocking(false);
                clients.add(newClient);
                System.out.println("Accept new client: " + newClient);
            } else {
                Thread.sleep(1000);
            }

            // 遍历多个 Client，然后读取其中可能返回的数据
            Iterator<SocketChannel> iterator = clients.iterator();
            while(iterator.hasNext()){
                SocketChannel client = iterator.next();
                byteBuffer.clear();
                int readCnt = client.read(byteBuffer);
                if (readCnt > 0) {
                    System.out.println("receive msg from client: " + client.getRemoteAddress());
                    byteBuffer.flip();
                    byte[] buf = new byte[byteBuffer.limit()];
                    while (byteBuffer.hasRemaining()) {
                        byteBuffer.get(buf);
                        System.out.print(new String(buf, Charset.defaultCharset()));
                    }
                    System.out.println();
                } else if (readCnt == -1) {
                    System.out.println("client: " + client.getRemoteAddress() + " is closed.");
                    client.close();
                    iterator.remove();
                }
            }
        }
    }
}

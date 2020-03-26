package com.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xiluo
 * @createTime 2019/3/8 18:35
 **/
public class ScktServer {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345);
        Socket client = serverSocket.accept();

        System.out.println("accept client: " + client.getInetAddress());
        client.getOutputStream().write(0XFF);
        /* 这样判断是否关闭是行不通的
        while(!client.isClosed()){
            Thread.sleep(1);
        }
        */
        InputStream is = client.getInputStream();
        while (true) {
            try {
                int a = is.read();
                System.out.println("a: " + a);
                Thread.sleep(1000);

            /*try {
                // 如果客户端已经关闭，则抛出IO 异常，从而获取客户端连接是否已经关闭
                // 这个数据不会发送到客户端
                client.sendUrgentData(0XFF);
            } catch (IOException t) {
                t.printStackTrace();
                break;
            }*/
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        // System.out.println("client is closed: " + System.currentTimeMillis());
    }

}

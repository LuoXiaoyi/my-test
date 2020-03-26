package com.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author xiluo
 * @createTime 2019/3/8 18:35
 **/
public class ScktClient {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("127.0.0.1", 12345);
        int cnt = 0, c = 0;
        while (cnt++ < 2) {
            /*InputStream is = socket.getInputStream();
            if (c == 0)
                c = is.read();*/

            OutputStream os = socket.getOutputStream();
            os.write("hello".getBytes());
            System.out.println("c: " + c);
            Thread.sleep(1000);
        }
        socket.close();
        System.out.println("client closed: " + System.currentTimeMillis());
    }

}

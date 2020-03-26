package com.perfma.license;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author xiaoyiluo
 * @createTime 2018/12/18 16:24
 **/
public class XNatureClientTest {

    public static void main(String[] args) throws IOException {
        Socket socket = connection();
        // testGetMachine(socket);
        // sendLic2(socket);
        // updateLic(socket);
        getLicTime(socket);
        socket.close();
    }

    private static void getLicTime(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(GET_LICENSE_TIME_CMD.getBytes());

        InputStream is = socket.getInputStream();
        byte[] buf = new byte[24];
        int rct = is.read(buf);
        String rsp = new String(buf, 0, rct, "UTF-8");
        System.out.println("rsp: " + rsp);

        License license = new License(buf);
        System.out.println(license);
    }

    private static void updateLic(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(REFRESH_LICENSE_CMD.getBytes());

        InputStream is = socket.getInputStream();
        byte[] buf = new byte[8];
        int rct = is.read(buf);
        String rsp = new String(buf, 0, rct, "UTF-8");
        System.out.println("rsp: " + rsp);
    }

    private static void sendLic2(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        String path = "/Users/xiaoyiluo/Desktop/perfma-vip-perfma.lic";
        File file = new File(path);
        byte[] licBytes = new byte[128];
        FileInputStream fis = new FileInputStream(file);
        int rct = fis.read(licBytes);
        System.out.println("read cnt: " + rct);
        byte[] cmdBytes = SEND_LICENSE_CMD.getBytes();
        byte[] sendBytes = new byte[cmdBytes.length + rct + 1];

        copy(sendBytes, cmdBytes, 0, 0, cmdBytes.length);
        copy(sendBytes, ":".getBytes(), cmdBytes.length, 0, 1);
        copy(sendBytes, licBytes, cmdBytes.length + 1, 0, rct);

        os.write(sendBytes);

        InputStream is = socket.getInputStream();
        rct = is.read(licBytes);
        if (rct > 0) {
            System.out.println(new String(licBytes, 0, rct, "UTF-8"));
        }
    }

    private static void copy(byte[] dest, byte[] src, int destOffSet, int srcStart, int srcLen) {
        for (int i = 0; i < srcLen; ++i) {
            dest[destOffSet + i] = src[srcStart + i];
        }
    }

    private static void sendLic(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(SEND_LICENSE_CMD.getBytes());

        InputStream is = socket.getInputStream();
        byte[] buf = new byte[8];
        int rct = is.read(buf);
        String rsp = new String(buf, 0, rct, "UTF-8");
        System.out.println("rsp: " + rsp);

        if ("OK".equals(rsp)) {
            String path = "/Users/xiaoyiluo/Desktop/perfma-vip-perfma.lic";
            File file = new File(path);
            byte[] licBytes = new byte[128];
            FileInputStream fis = new FileInputStream(file);
            rct = fis.read(licBytes);
            System.out.println("read cnt: " + rct);

            os.write(licBytes,0,rct);
        }
    }

    private static void testGetMachine(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(GET_MACHINE_CMD.getBytes());

        InputStream is = socket.getInputStream();
        byte[] buf = new byte[512];

        int rCnt = is.read(buf), i = 0;
        while (rCnt > 0) {
            ++i;
            System.out.print(new String(buf, 0, rCnt, "UTF-8"));
            rCnt = is.read(buf);
        }

        System.out.println("\ni: " + i);
    }

    private static Socket connection() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("xowl.stable.perfma-inc.net", 7890));

        OutputStream os = socket.getOutputStream();
        os.write(XNATURE_REQUEST_TOKEN.getBytes());

        InputStream is = socket.getInputStream();
        byte[] buf = new byte[128];
        int cnt = is.read(buf);
        String resp = new String(buf, 0, cnt, "UTF-8");
        System.out.println(resp);

        return socket;
    }


    private static final String XNATURE_REQUEST_TOKEN = "XNATURE";
    private static final String GET_MACHINE_CMD = "GET_MACHINE";
    private static final String SEND_LICENSE_CMD = "SEND_LICENSE";
    private static final String REFRESH_LICENSE_CMD = "REFRESH_LICENSE";
    private static final String GET_LICENSE_TIME_CMD = "GET_LICENSE_TIME";
}

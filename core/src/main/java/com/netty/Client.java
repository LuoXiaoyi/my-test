package com.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-07-10 22:01
 **/
public class Client {

    public static String host = "192.168.0.66";
    public static int port = 30001;

    public static void main(String[] args) {

        final Client client = new Client();

        new Thread(() -> {

            // 控制台输入
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    String line = in.readLine();
                    if (line == null) {
                        continue;
                    }
                    /**
                     * 向服务端发送在控制台输入的文本 并用"\r\n"结尾
                     * 之所以用\r\n结尾 是因为我们在handler中添加了 DelimiterBasedFrameDecoder 帧解码。
                     * 这个解码器是一个根据\n符号位分隔符的解码器。所以每条消息的最后必须加上\n否则无法识别和解码
                     */
                    client.writeAndFlush(line + "\r\n");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }).start();

        try {
            client.connect();
        } catch (Exception ie) {
            System.out.println("main --> exception...");
            ie.printStackTrace();
        }
    }

    public synchronized void reconnect(EventLoopGroup loopGroup) throws InterruptedException {
        if (channel != null && channel.isActive()) {
            return;
        }

        if (bootstrap == null) {
            bootstrap = new Bootstrap();

            //使用nio传输方式并绑定自己的处理流程
            bootstrap.group(loopGroup).channel(NioSocketChannel.class)
                    .handler(new HelloClientInitializer(this));
        }

        // 连接服务端
        ChannelFuture cf = bootstrap.connect(host, port);
        cf.addListener(new ConnectionListener(this));
        channel = cf.sync().channel();

        System.out.println("连接成功: " + channel);
    }

    public synchronized void connect() throws InterruptedException {
        if (group == null) {
            group = new NioEventLoopGroup();
            reconnect(group);
        }
    }

    public void writeAndFlush(String msg) {
        if (channel != null) {
            channel.writeAndFlush(msg);
            System.out.println("send: " + msg);
        }
    }

    public void shutdownGracefully() {
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private Channel channel;
}

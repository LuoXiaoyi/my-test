package com.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.TimeUnit;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-07-10 22:20
 **/
public class HelloClientHandler extends SimpleChannelInboundHandler<String> {
    private Client client;

    public HelloClientHandler(Client client) {
        this.client = client;
    }

    /**
     * 服务端发送信息时触发此方法
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("Server say : " + s);
    }

    /**
     * 连接成功时触发此方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active ");
        super.channelActive(ctx);
    }

    /**
     * 服务器关闭时触发此方法
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("HelloClientHandler --> Client close ");
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    client.reconnect(eventLoop);
                    System.out.println("HelloClientHandler --> reconnect OK...");
                } catch (Exception ie) {
                    System.out.println("HelloClientHandler --> 重连失败。。。");
                    ie.printStackTrace();
                }
            }
        }, 1L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }
}

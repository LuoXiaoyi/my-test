package com.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-07-11 10:50
 **/
public class ConnectionListener implements ChannelFutureListener {
    private Client client;

    public ConnectionListener(Client client) {
        this.client = client;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (!channelFuture.isSuccess()) {
            System.out.println("ConnectionListener --> Reconnect");
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        client.reconnect(loop);
                        System.out.println("ConnectionListener --> 重连成功。。。");
                    } catch (Exception ie) {
                        System.out.println("ConnectionListener --> reconnect error...");
                        ie.printStackTrace();
                    }
                }
            }, 1L, TimeUnit.SECONDS);
        }
    }
}

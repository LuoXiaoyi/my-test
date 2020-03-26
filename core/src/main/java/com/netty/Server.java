package com.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-07-10 22:01
 **/
public class Server {

    /**
     * 服务端监听的端口地址
     */
    private static final int port = 9527;

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            //使用主从Reactor多线程模型
            b.group(bossGroup, workerGroup);

            //使用nio传输方式
            b.channel(NioServerSocketChannel.class);

            //添加我们自己的handler处理流程
            b.childHandler(new HelloServerInitializer());

            // 服务器绑定端口监听
            ChannelFuture f = b.bind(port).sync();
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("Server shutdown");
        }
    }
}

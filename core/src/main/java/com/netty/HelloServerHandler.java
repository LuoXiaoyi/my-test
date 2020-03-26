package com.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-07-10 22:14
 **/
public class HelloServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 每一次收到消息时执行此方法
     *
     * 字符串最后面的"\n"是必须的。
     * 因为我们在前面的解码器DelimiterBasedFrameDecoder是一个根据字符串结尾为“\n”来结尾的。
     * 假如没有这个字符的话。解码会出现问题。
     *
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        // 收到消息直接打印输出
        System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);

        // 返回客户端消息 - 我已经接收到了你的消息
        ctx.writeAndFlush("Received your message !\n");
    }

    /**
     * 覆盖 channelActive 方法
     * 在channel被启用的时候触发 (在建立连接的时候)
     *
     * 这里channeActive的意思是当连接活跃(建立)的时候触发.输出消息源的远程地址。并返回欢迎消息。
     *
     * 在3.x版本中此处有很大区别。在3.x版本中write()方法是自动flush的。
     * 在4.x版本的前面几个版本也是一样的。但是在4.0.9之后修改为WriteAndFlush。
     * 普通的write方法将不会发送消息。需要手动在write之后flush()一次
     *
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

        ctx.write( "Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");
        ctx.flush();

        super.channelActive(ctx);
    }
}

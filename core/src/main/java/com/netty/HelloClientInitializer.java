package com.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-07-10 22:18
 **/
public class HelloClientInitializer extends ChannelInitializer<SocketChannel> {
    private Client client;

    public HelloClientInitializer(Client client) {
        this.client = client;
    }

    /**
     * 初始化HelloClientInitializer时执行此方法，并定义流程
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        /**
         * 这个地方的 必须和服务端对应上。否则无法正常解码和编码
         */
        // 以("\n")为结尾分割的 解码器
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

        // 字符串解码 和 编码
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

        // 自己的逻辑Handler
        pipeline.addLast("handler", new HelloClientHandler(this.client));
    }
}

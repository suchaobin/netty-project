package com.atguigu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author suchaobin
 * @description http服务器初始化器
 * @date 2021/12/22 10:08 AM
 **/
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 管道内添加编解码器
        pipeline.addLast(new HttpServerCodec());
        // 管道内添加自己的handler
        pipeline.addLast(new HttpServerHandler());
    }
}

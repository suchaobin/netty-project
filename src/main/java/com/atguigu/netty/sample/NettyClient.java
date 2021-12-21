package com.atguigu.netty.sample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author suchaobin
 * @description netty客户端
 * @date 2021/12/21 4:29 PM
 **/
public class NettyClient {
    public static void main(String[] args) throws Exception {
        // 启动线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 启动类
            Bootstrap bootstrap = new Bootstrap();
            // 配置
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    // 初始化就执行的handler
                    .handler(new NettyClientHandler());
            System.out.println("客户端已就绪~");
            // 连接服务器
            ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 6666)).sync();
            // 使主线程wait等待，当有关闭通道的事件发生后再继续执行
            future.channel().closeFuture().sync();
        } finally {
            // 优雅的关闭
            group.shutdownGracefully();
        }
    }
}

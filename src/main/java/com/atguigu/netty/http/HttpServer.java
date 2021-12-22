package com.atguigu.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author suchaobin
 * @description 基于netty编写的http服务器
 * @date 2021/12/22 10:05 AM
 **/
public class HttpServer {
    public static void main(String[] args) throws Exception {
        // 创建线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 创建启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 配置
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new HttpServerInitializer());
            // 绑定端口，发现一个坑，谷歌禁用6666端口，所以一直请求失败，还以为是自己代码bug。。。
            ChannelFuture channelFuture = serverBootstrap.bind(6660).sync();
            // 监听关闭事件
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}

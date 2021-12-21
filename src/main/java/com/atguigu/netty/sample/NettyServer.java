package com.atguigu.netty.sample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author suchaobin
 * @description netty服务器
 * @date 2021/12/21 3:57 PM
 **/
public class NettyServer {
    public static void main(String[] args) throws Exception {
        // 启动线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 配置
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    // 初始化就执行.handler中的xxxHandler
                    //.handler(null)
                    // 和客户端connect后才会执行childHandler
                    .childHandler(new NettyServerHandlerTaskQ())
                    /**
                     * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
                     * 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                     */
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 启用心跳保活机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            System.out.println("服务器已就绪~");
            // 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();
            // 这里只是监听，使主线程wait等待，只有关闭通道时才进行处理，这句话不是直接关闭了通道
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅的关闭
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

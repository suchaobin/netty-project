package com.atguigu.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suchaobin
 * @description 群聊服务器
 * @date 2021/12/28 1:13 PM
 **/
@Data
@Slf4j
@AllArgsConstructor
public class GroupChatServer {
    /**
     * 端口
     */
    private int port;

    public void run() throws Exception {
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
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 添加编解码器
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            // 添加自定义处理器
                            pipeline.addLast(new GroupChatServerHandler());
                        }
                    });
            // 启动
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // 同步等待关闭的异步事件
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync();
            workGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        GroupChatServer server = new GroupChatServer(7777);
        server.run();
    }
}

package com.atguigu.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author suchaobin
 * @description web服务器
 * @date 2022/1/26 3:50 PM
 **/
public class WebServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 因为基于 HTTP 协议，所以需要使用 HTTP 的编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 添加块处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                                说明：
                                1. 因为 HTTP 数据传输时是分段的，HttpObjectAggregator 可以将多个端聚合
                                2. 这就是为什么浏览器发送大量数据时，就会发出多次 HTTP 请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                                说明：
                                1. 对于 WebSocket 是以 帧 的形式传递的
                                2. 后面的参数表示 ：请求的 URL
                                3. WebSocketServerProtocolHandler 将 HTTP 协议升级为 WebSocket 协议，即保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            // 自定义的 Handler
                            pipeline.addLast(new WebServerHandler());
                        }
                    });
            System.out.println("服务器准备好了");
            ChannelFuture channelFuture = bootstrap.bind(8000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}


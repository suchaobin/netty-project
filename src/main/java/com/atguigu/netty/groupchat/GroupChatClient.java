package com.atguigu.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author suchaobin
 * @description 群聊客户端
 * @date 2021/12/31 10:47 AM
 **/
@Data
@Slf4j
@AllArgsConstructor
public class GroupChatClient {
    /**
     * ip
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    public void run() throws Exception {
        // 创建线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建启动器
            Bootstrap bootstrap = new Bootstrap();
            // 配置
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });
            // 连接
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            System.err.println("当前客户端地址:" + channel.localAddress());
            // 发送消息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                channel.writeAndFlush(scanner.nextLine() + "\n");
            }
            // 阻塞等待关闭事件
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        GroupChatClient client = new GroupChatClient("localhost", 7777);
        client.run();
    }
}

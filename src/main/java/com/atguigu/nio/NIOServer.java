package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务器
 *
 * @author suchaobin
 * @date 2023/1/14 14:00
 **/
public class NIOServer {
    public static void main(String[] args) throws Exception {
        // 创建服务器通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口并设置为非阻塞
        serverSocketChannel.bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);
        // 创建选择器
        Selector selector = Selector.open();
        // 将服务器注册到选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            if (selector.select(1000) == 0) {
                System.err.println("服务器等待了1s，无连接");
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 有新的客户端连接
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将客户端注册到选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                // 可读消息
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.err.println("from 客户端 " + new String(buffer.array()));
                }
                // 手动清除key
                iterator.remove();
            }
        }
    }
}

package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author suchaobin
 * @description nio客户端
 * @date 2021/12/11 11:29 AM
 **/
public class NIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(6666));
        socketChannel.configureBlocking(false);
        socketChannel.write(ByteBuffer.wrap("哈哈哈".getBytes(StandardCharsets.UTF_8)));
        System.in.read();
    }
}

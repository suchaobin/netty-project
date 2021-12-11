package com.atguigu.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author suchaobin
 * @description nio服务器
 * @date 2021/12/11 11:57 AM
 **/
public class NewIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
            while (true) {
                int read = socketChannel.read(byteBuffer);
                if (read == -1) {
                    break;
                }
            }
        }
    }
}

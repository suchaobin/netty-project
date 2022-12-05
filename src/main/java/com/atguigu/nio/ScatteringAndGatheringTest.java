package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author suchaobin
 * @description 分散和聚集
 * @date 2022/12/5 09:29
 **/
public class ScatteringAndGatheringTest {
    /**
     * Scattering：将数据写入到Buffer时，可以采用Buffer数组，依次写入【分散】
     * Gathering：从Buffer读取数据时，可以采用Buffer数组，依次读
     */
    public static void main(String[] args) throws Exception {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);
        serverSocketChannel.bind(inetSocketAddress);
        // 创建Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        int messageLength = 8;
        // 等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {
            // 循环读
            int readCount = 0;
            while (readCount < messageLength) {
                long read = socketChannel.read(byteBuffers);
                readCount += read;
                System.err.println("readCount=" + readCount);
                Arrays.stream(byteBuffers)
                        .forEach(buffer -> {
                            String str = "position=" + buffer.position() + ",limit=" + buffer.limit();
                            System.err.println(str);
                            // 对每个buffer翻转，以便接下来的操作
                            buffer.flip();
                        });
            }
            // 循环写，把数据回显到客户端
            int writeCount = 0;
            while (writeCount < messageLength) {
                long write = socketChannel.write(byteBuffers);
                writeCount += write;
                System.err.println("writeCount=" + writeCount);
                // 写完后需要对每个buffer进行复位操作
                Arrays.stream(byteBuffers).forEach(ByteBuffer::clear);
            }
        }
    }
}

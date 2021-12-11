package com.atguigu.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author suchaobin
 * @description nio客户端
 * @date 2021/12/11 12:00 PM
 **/
public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 8888));
        String filePath = "/Users/suchaobin/IdeaProjects/netty-project/src/main/resources/nio/img.png";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        FileChannel fileChannel = fileInputStream.getChannel();

        long startTime = System.currentTimeMillis();
        /**
         * 在 linux 下一个 transferTo 方法就可以完成传输
         * 在 windows 下一次调用 transferTo 只能发送 8m, 就需要分段传输文件,而且要主要
         */
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        // 三次测试时间分别为 2 2 1 毫秒
        System.out.println("发送的总的字节数 = " + transferCount + " 耗时: " + (System.currentTimeMillis() - startTime));

        socketChannel.close();
        fileChannel.close();
        fileInputStream.close();
    }
}

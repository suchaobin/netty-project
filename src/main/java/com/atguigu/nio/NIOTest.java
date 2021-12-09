package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author suchaobin
 * @description nio测试
 * @date 2021/12/9 8:38 PM
 **/
public class NIOTest {

    public static void main(String[] args) throws Exception {
        test6();
    }

    private static void test1() throws Exception {
        FileInputStream fis = new FileInputStream("/Users/suchaobin/IdeaProjects/netty-project/src/main/resources/nio/a.txt");
        FileChannel channel = fis.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            int read = channel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            System.err.println(new String(byteBuffer.array()));
        }
        channel.close();
        fis.close();
    }

    private static void test2() throws Exception {
        FileOutputStream fos = new FileOutputStream("/Users/suchaobin/IdeaProjects/netty-project/src/main/resources/nio/b.txt");
        FileChannel channel = fos.getChannel();
        String msg = "你好呀~";
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(msg.getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        channel.write(byteBuffer);
        channel.close();
        fos.close();
    }

    private static void test3() throws Exception {
        FileInputStream fis = new FileInputStream("/Users/suchaobin/IdeaProjects/netty-project/src/main/resources/nio/img.png");
        FileChannel fisChannel = fis.getChannel();
        FileOutputStream fos = new FileOutputStream("/Users/suchaobin/IdeaProjects/netty-project/src/main/resources/nio/nio.png");
        FileChannel fosChannel = fos.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            int read = fisChannel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            fosChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        fisChannel.close();
        fosChannel.close();
        fis.close();
        fos.close();
    }

    private static void test4() throws Exception {
        RandomAccessFile file = new RandomAccessFile("/Users/suchaobin/IdeaProjects/netty-project/src/main/resources/nio/c.txt", "rw");
        FileChannel channel = file.getChannel();
        MappedByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        byteBuffer.put(0, (byte) 'H');
        byteBuffer.put(3, (byte) 'L');
        channel.write(byteBuffer);
        channel.close();
        file.close();
    }

    private static void test5() throws Exception {
        ByteBuffer head = ByteBuffer.allocate(5);
        head.put("hello".getBytes(StandardCharsets.UTF_8));
        ByteBuffer body = ByteBuffer.allocate(10);
        body.put("你好吗?".getBytes(StandardCharsets.UTF_8));
        ByteBuffer[] buffers = {head, body};

        FileOutputStream fos = new FileOutputStream("/Users/suchaobin/IdeaProjects/netty-project/src/main/resources/nio/d.txt");
        FileChannel fosChannel = fos.getChannel();
        Arrays.stream(buffers).forEach(ByteBuffer::flip);
        fosChannel.write(buffers);

        FileInputStream fis = new FileInputStream("/Users/suchaobin/IdeaProjects/netty-project/src/main/resources/nio/d.txt");
        FileChannel fisChannel = fis.getChannel();
        Arrays.stream(buffers).forEach(ByteBuffer::flip);
        while (true) {
            long read = fisChannel.read(buffers);
            if (read == -1) {
                break;
            }
            for (ByteBuffer buffer : buffers) {
                System.err.println(new String(buffer.array()));
                buffer.clear();
            }
        }

        fisChannel.close();
        fosChannel.close();
        fis.close();
        fos.close();
    }

    private static void test6() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(inetSocketAddress);
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.err.println("有客户端连接~");

        int totalLen = 15;
        ByteBuffer head = ByteBuffer.allocate(5);
        ByteBuffer body = ByteBuffer.allocate(10);
        ByteBuffer[] buffers = {head, body};

        while (true) {
            int readCount = 0;
            while (readCount < totalLen) {
                long read = socketChannel.read(buffers);
                if (read == -1) {
                    break;
                }
                for (ByteBuffer buffer : buffers) {
                    if (buffer.remaining() > 0) {
                        System.err.println("客户端发送消息:" + new String(buffer.array()));
                    }
                }
                readCount += read;
            }

            Arrays.stream(buffers).forEach(ByteBuffer::flip);
            socketChannel.write(buffers);
            Arrays.stream(buffers).forEach(ByteBuffer::clear);
        }
    }
}

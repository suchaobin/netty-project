package com.atguigu.nio.groupchat;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author suchaobin
 * @description 群里客户端
 * @date 2021/12/11 10:27 AM
 **/
@Data
@Slf4j
public class GroupChatClient {
    /**
     * 客户端通道
     */
    private SocketChannel socketChannel;
    /**
     * 选择器
     */
    private Selector selector;
    /**
     * 用户名
     */
    private String username;
    /**
     * IP
     */
    private static final String HOST = "127.0.0.1";
    /**
     * 端口
     */
    private static final int PORT = 8888;

    public GroupChatClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
            //得到 username
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " is ok...");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 读消息
     *
     * @throws IOException IO异常
     */
    private void readMsg() throws IOException {
        if (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    byteBuffer.clear();
                    StringBuilder sb = new StringBuilder();
                    while (channel.read(byteBuffer) > 0) {
                        sb.append(new String(byteBuffer.array()));
                    }
                    System.err.println(sb.toString().trim());
                }
                iterator.remove();
            }
        }
    }

    /**
     * 发送消息
     *
     * @param msg 消息
     */
    private void sendMsg(String msg) {
        try {
            msg = username + "说：" + msg;
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        GroupChatClient chatClient = new GroupChatClient();
        // 创建一个线程每隔3s去读取一次数据
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(() -> {
            while (true) {
                try {
                    chatClient.readMsg();
                    Thread.sleep(3000);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // 主线程阻塞获取控制台输入的消息发送出去
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            chatClient.sendMsg(msg);
        }
    }
}

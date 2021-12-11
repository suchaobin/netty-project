package com.atguigu.nio.groupchat;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author suchaobin
 * @description 群聊服务器
 * @date 2021/12/11 9:40 AM
 **/
@Slf4j
@Data
public class GroupChatServer {
    /**
     * 服务器通道
     */
    private ServerSocketChannel serverSocketChannel;
    /**
     * 选择器
     */
    private Selector selector;
    /**
     * 端口
     */
    private static final int PORT = 8888;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 监听处理
     */
    private void listen() {
        try {
            while (true) {
                if (this.selector.select(2000) <= 0) {
                    System.err.println("服务器等待2s，没有客户端操作~");
                }
                Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // 有客户端连接
                    if (key.isAcceptable()) {
                        SocketChannel channel = this.serverSocketChannel.accept();
                        channel.configureBlocking(false);
                        channel.register(this.selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                        System.err.println(channel.getRemoteAddress() + "上线~");
                    }
                    // 有客户端发送消息
                    if (key.isReadable()) {
                        readMsg(key);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 从客户端读信息
     *
     * @param key SelectionKey
     * @throws IOException IO异常
     */
    private void readMsg(SelectionKey key) throws IOException {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
            byteBuffer.clear();
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while ((len = channel.read(byteBuffer)) > 0) {
                String msg = new String(byteBuffer.array(), 0, len);
                byteBuffer.clear();
                sb.append(msg);
            }
            System.err.println("from 客户端：" + sb);
            // 转发消息
            transferMsg(sb.toString(), key);
        } catch (IOException e) {
            System.err.println(channel.getRemoteAddress() + "下线~");
            // 取消注册
            key.cancel();
            // 关闭通道
            channel.close();
        }
    }

    /**
     * 转发消息
     *
     * @param msg 信息
     * @param currentKey 当前的SelectionKey
     * @throws IOException IO异常
     */
    private void transferMsg(String msg, SelectionKey currentKey) throws IOException {
        System.out.println("服务器转发消息中...");
        Selector selector = currentKey.selector();
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            // 不转发给自己
            if (key == currentKey) {
                continue;
            }
            // 不是SocketChannel直接跳过
            if (!(key.channel() instanceof SocketChannel channel)) {
                continue;
            }
            channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}

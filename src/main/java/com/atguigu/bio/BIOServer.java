package com.atguigu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author suchaobin
 * @description bio服务器
 * @date 2021/12/6 9:04 下午
 **/
public class BIOServer {
    public static void main(String[] args) throws Exception {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.err.println("服务器启动了");
        while (true) {
            final Socket socket = serverSocket.accept();
            System.err.println("有一个客户端发起连接");
            cachedThreadPool.submit(() -> handle(socket));
        }
    }

    private static void handle(Socket socket) {
        String msg = String.format("线程ID=%s，name=%s", Thread.currentThread().getId(), Thread.currentThread().getName());
        System.err.println(msg);
        while (true) {
            try {
                InputStream inputStream = socket.getInputStream();
                byte[] bytes = new byte[1024];
                int read = inputStream.read(bytes);
                if (read == -1) {
                    break;
                }
                System.err.println("客户端发送消息：" + new String(bytes, 0, read));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

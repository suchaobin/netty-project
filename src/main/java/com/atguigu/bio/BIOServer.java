package com.atguigu.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO服务器
 *
 * @author suchaobin
 * @date 2022/11/26 9:59
 **/
public class BIOServer {

    public static void main(String[] args) throws IOException {
        // 创建一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 创建一个ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true) {
            // 连接客户端
            final Socket socket = serverSocket.accept();
            System.err.println("有一个客户端发起连接");
            executorService.execute(() -> handler(socket));
        }
    }

    private static void handler(Socket socket) {
        String msg = String.format("线程ID=%s，name=%s", Thread.currentThread().getId(), Thread.currentThread().getName());
        System.err.println(msg);
        try {
            while (true) {
                InputStream inputStream = socket.getInputStream();
                byte[] bytes = new byte[1024];
                int read = inputStream.read(bytes);
                if (read == -1) {
                    break;
                }
                System.err.println("客户端发送消息：" + new String(bytes, 0, read));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

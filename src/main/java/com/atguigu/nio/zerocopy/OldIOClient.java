package com.atguigu.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author suchaobin
 * @description 传统IO客户端
 * @date 2021/12/11 11:46 AM
 **/
public class OldIOClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8889);
        String filePath = "/Users/suchaobin/IdeaProjects/netty-project/src/main/resources/nio/img.png";

        FileInputStream fileInputStream = new FileInputStream(filePath);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] bytes = new byte[4096];
        int read = 0;
        long total = 0;

        long s = System.currentTimeMillis();
        while ((read = fileInputStream.read(bytes, 0, bytes.length)) >= 0) {
            dataOutputStream.write(bytes, 0, read);
            total += read;
        }
        long e = System.currentTimeMillis();

        // 三次测试时间：5 5 4 毫秒
        System.err.println("发送总字节数:" + total + "，耗时:" + (e - s));
        dataOutputStream.close();
        socket.close();
        fileInputStream.close();
    }
}

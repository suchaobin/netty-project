package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author suchaobin
 * @description 读文件
 * @date 2022/12/1 11:47
 **/
public class NIOFileChannel02 {

    public static void main(String[] args) throws Exception {
        // 声明file
        File file = new File("src/main/resources/test01.txt");
        // 创建输入流
        FileInputStream fis = new FileInputStream(file);
        // 获取通道
        FileChannel channel = fis.getChannel();
        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        // 读取数据
        channel.read(buffer);
        // 关闭资源
        channel.close();
        fis.close();
        // 打印
        System.err.println(new String(buffer.array()));
    }
}

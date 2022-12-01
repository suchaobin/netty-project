package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author suchaobin
 * @description 复制文件
 * @date 2022/12/1 15:55
 **/
public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {
        // 创建读取文件的通道
        File file = new File("src/main/resources/test01.txt");
        FileInputStream fis = new FileInputStream(file);
        FileChannel sourceChannel = fis.getChannel();
        // 创建写入文件的通道
        FileOutputStream fos = new FileOutputStream("src/main/resources/test02.txt");
        FileChannel targetChannel = fos.getChannel();
        // 创建ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(2);
        while (true) {
            // buffer清空，否则有可能因为position和limit相同，读取的read一直是0而无法接着读取
            buffer.clear();
            int read = sourceChannel.read(buffer);
            // 读取结束了
            if (read == -1) {
                break;
            }
            buffer.flip();
            targetChannel.write(buffer);
        }
        // 关闭资源
        sourceChannel.close();
        targetChannel.close();
        fis.close();
        fos.close();
    }
}

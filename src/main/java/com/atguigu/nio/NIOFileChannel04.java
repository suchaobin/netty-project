package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author suchaobin
 * @description 使用零拷贝复制文件
 * @date 2022/12/1 16:12
 **/
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        // 创建源通道
        File file = new File("src/main/resources/img01.png");
        FileInputStream fis = new FileInputStream(file);
        FileChannel sourceChannel = fis.getChannel();
        // 创建目标通道
        FileOutputStream fos = new FileOutputStream("src/main/resources/img02.png");
        FileChannel targetChannel = fos.getChannel();
        // 复制
        targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        // 关闭资源
        sourceChannel.close();
        targetChannel.close();
        fis.close();
        fos.close();
    }
}

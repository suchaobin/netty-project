package com.atguigu.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author suchaobin
 * @description 写文件
 * @date 2022/12/1 10:56
 **/
public class NIOFileChannel01 {

    public static void main(String[] args) throws Exception {
        String str = "hello,尚硅谷";
        // 创建一个文件流
        FileOutputStream fos = new FileOutputStream("src/main/resources/test01.txt", false);
        // 根据流获取到channel
        FileChannel channel = fos.getChannel();
        // 把文本数据放入Buffer中
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(str.getBytes());
        // 把Buffer中的数据写入到channel中
        buffer.flip();
        channel.write(buffer);
        // 关闭资源
        channel.close();
        fos.close();
    }
}

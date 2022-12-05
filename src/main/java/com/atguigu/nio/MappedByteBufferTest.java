package com.atguigu.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author suchaobin
 * @description MappedByteBuffer可让文件直接在内存（堆外内存）修改，操作系统不需要再拷贝一次
 * @date 2022/12/1 16:52
 **/
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile accessFile = new RandomAccessFile("src/main/resources/test01.txt", "rw");
        // 获取通道
        FileChannel channel = accessFile.getChannel();
        /**
         * 参数1：FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数2: 0：可以直接修改的起始位置
         * 参数3：5：映射内存的大小
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        // 进行修改
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
        // 关闭资源
        channel.close();
        accessFile.close();
    }
}

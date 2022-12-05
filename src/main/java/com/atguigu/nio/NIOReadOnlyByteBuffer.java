package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * @author suchaobin
 * @description 只读的ByteBuffer
 * @date 2022/12/1 16:35
 **/
public class NIOReadOnlyByteBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        buffer.flip();
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        // 读取
        while (readOnlyBuffer.hasRemaining()) {
            System.err.println(readOnlyBuffer.get());
        }
        // 尝试再次写入
        readOnlyBuffer.putInt(1);
    }
}

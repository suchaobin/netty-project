package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * @author suchaobin
 * @description ByteBuffer类型化
 * @date 2022/12/1 16:31
 **/
public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putInt(1024);
        buffer.putChar('苏');
        buffer.putLong(123456789L);
        buffer.putShort((short) 66);

        buffer.flip();
        System.err.println(buffer.getInt());
        System.err.println(buffer.getChar());
        System.err.println(buffer.getLong());
        System.err.println(buffer.getShort());
    }
}

package com.atguigu.nio;

import java.nio.IntBuffer;

/**
 * 基础NIO
 *
 * @author suchaobin
 * @date 2022/11/27 11:42
 **/
public class BasicNIO {

    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);
        for (int i = 0; i < 10; i++) {
            buffer.put(i);
        }
        // 翻转
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.err.println(buffer.get());
        }
    }
}

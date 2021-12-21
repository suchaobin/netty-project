package com.atguigu.netty.sample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suchaobin
 * @description netty服务器处理器
 * @date 2021/12/21 4:52 PM
 **/
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 通道内有读事件的时候
     *
     * @param ctx 上下文对象，包含 管道、通道、地址
     * @param msg 客户端发送的消息，默认是Object类型
     * @throws Exception 异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println("ctx=" + ctx);
        // 将消息转成ByteBuf类型，ByteBuf是netty基于ByteBuffer的封装
        ByteBuf byteBuf = (ByteBuf) msg;
        // 将byteBuf转成UTF-8格式的字符串
        System.err.println("客户端发送消息：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.err.println("客户端地址：" + ctx.channel().remoteAddress());

        // pipeline本质是一个双向链表，和channel是互相包含的关系，都可以获取到对方实例
        ChannelPipeline pipeline = ctx.channel().pipeline();
        Channel channel = ctx.pipeline().channel();
    }

    /**
     * 数据读取完毕后
     *
     * @param ctx 上下文对象，包含 管道、通道、地址
     * @throws Exception 异常
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 给客户端发送消息
        String msg = "服务端读取消息完毕~";
        ctx.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    }

    /**
     * 当有异常发送的时候
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        // 关闭通道
        ChannelFuture channelFuture = ctx.close();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    // 执行具体的业务...，比如这个人掉线了，我要通知他的好友们：您的好友xxx已下线
                }
            }
        });
        // 除了用在这里外，还可以用在客户端发起连接的时候，判断连接是否成功建立，如果没有连接成功，则尝试重新连接
    }
}

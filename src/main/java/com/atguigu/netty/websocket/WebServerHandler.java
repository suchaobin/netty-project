package com.atguigu.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @author suchaobin
 * @description 处理器，TextWebSocketFrame 类型是 WebSocket 的一个子类，表示一个文本帧
 * @date 2022/1/26 3:56 PM
 **/
public class WebServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息：" + msg.text());
        // 回复浏览器
        channelHandlerContext.channel().writeAndFlush(
                new TextWebSocketFrame("【服务器】" + LocalDateTime.now() + " | " + msg.text()));
    }

    /**
     * web 连接后触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // id 表示标识，asLongText 输出的是唯一的，asShortText 不一定是唯一的
        System.out.println("handlerAdded 被调用-- " + ctx.channel().id().asLongText() + " (LongText)");
        System.out.println("handlerAdded 被调用-- " + ctx.channel().id().asShortText() + " (ShortText)");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // id 表示标识，asLongText 输出的是唯一的，asShortText 不一定是唯一的
        System.out.println("handlerRemoved 被调用-- " + ctx.channel().id().asLongText() + " (LongText)");
        System.out.println("handlerRemoved 被调用-- " + ctx.channel().id().asShortText() + " (ShortText)");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("【异常】 " + cause.getMessage());
        ctx.close();
    }
}


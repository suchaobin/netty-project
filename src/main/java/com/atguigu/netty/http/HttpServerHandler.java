package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author suchaobin
 * @description http服务器处理器
 * @date 2021/12/22 10:11 AM
 **/
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest request) {

            URI uri = new URI(request.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.err.println("不处理图标请求");
                return;
            }

            System.err.println("httpObject 的类型 ：" + httpObject.getClass());
            System.err.println("客户端地址：" + ctx.channel().remoteAddress());

            // 构建一个byteBuf
            ByteBuf byteBuf = Unpooled.copiedBuffer("Hello，我是服务器", CharsetUtil.UTF_8);
            // 创建HttpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            // 设置文本类型和字符编码
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            // 设置文本长度
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            // 将response返回给客户端
            ctx.writeAndFlush(response);
        }
    }
}

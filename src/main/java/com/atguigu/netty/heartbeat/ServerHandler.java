package com.atguigu.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author suchaobin
 * @description 服务器处理器
 * @date 2021/12/31 4:40 PM
 **/
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            IdleState state = event.state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                ctx.disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

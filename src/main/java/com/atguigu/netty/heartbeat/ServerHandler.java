package com.atguigu.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author suchaobin
 * @description 服务器处理器
 * @date 2021/12/31 4:40 PM
 **/
public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 对 空闲事件 的处理
     *
     * @param ctx 上下文
     * @param evt 传递过来的事件
     * @throws Exception
     */
    private int list[] = new int[3];

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            int index = -1;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    index = 0;
                    break;
                case WRITER_IDLE:
                    index = 1;
                    break;
                case ALL_IDLE:
                    index = 2;
                    break;
                default:
                    break;
            }
            list[index]++;
            System.out.println("[超时事件] " + ctx.channel().remoteAddress() + " 发生了 " + eventType + "---第" + list[index] + "次");
            System.out.println("服务器进行相应处理");
            if (list[index] >= 3) {
                ctx.channel().close();
                System.out.println("关闭该通道");
            }
        }
    }
}

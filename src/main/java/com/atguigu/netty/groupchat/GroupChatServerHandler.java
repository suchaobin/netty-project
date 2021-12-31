package com.atguigu.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author suchaobin
 * @description 群里服务器自定义处理器
 * @date 2021/12/28 1:19 PM
 **/
@Slf4j
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 定义一个 Channel 线程组，管理所有的 Channel, 参数 执行器
     * GlobalEventExecutor => 全局事件执行器
     * INSTANCE => 表示是单例的
     */
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 定义一个时间的输出格式
     */
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Date date = new Date(System.currentTimeMillis());
        CHANNEL_GROUP.writeAndFlush("[客户端] [" + dateFormat.format(date) + "] " + channel.remoteAddress() + " 加入群聊~\n");
        CHANNEL_GROUP.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Date date = new Date(System.currentTimeMillis());
        CHANNEL_GROUP.writeAndFlush("[客户端] [" + dateFormat.format(date) + "] " + channel.remoteAddress() + " 离开群聊~\n");
        // 不需要从CHANNEL_GROUP中手动remove，当通道断开连接的时候，netty内部已经实现了从CHANNEL_GROUP中remove当前channel
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel curChannel = ctx.channel();
        Date date = new Date(System.currentTimeMillis());
        // 转发消息
        for (Channel channel : CHANNEL_GROUP) {
            if (channel == curChannel) {
                continue;
            }
            channel.writeAndFlush("[客户端] [" + dateFormat.format(date) + "] " + curChannel.remoteAddress() + " 发送了消息 ：" + msg + "\n");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("[" + dateFormat.format(date) + "] " + ctx.channel().remoteAddress() + " 已上线~");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("[" + dateFormat.format(date) + "] " + ctx.channel().remoteAddress() + " 已下线~");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}

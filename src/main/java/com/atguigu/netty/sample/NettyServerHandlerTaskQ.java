package com.atguigu.netty.sample;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author suchaobin
 * @description netty服务器队列处理器
 * @date 2021/12/21 5:33 PM
 **/
@Slf4j
public class NettyServerHandlerTaskQ extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 1.先体验同步等待的任务
        // doTask(ctx);
        // 2.把任务放到普通队列中异步执行
        // ctx.channel().eventLoop().execute(() -> doTask(ctx));
        // 3.把任务放到计划中的队列中异步执行
        ctx.channel().eventLoop().schedule(() -> doTask(ctx), 5, TimeUnit.SECONDS);
        /**
         *  注意：虽然放到队列中是异步执行的，但是在队列中是同步的，就是说要等待队列中的前面任务先执行完，
         *  才会到该任务执行，所以该任务什么时候被执行，不能只看该任务的等待时间
         */
    }

    private void doTask(ChannelHandlerContext ctx) {
        try {
            Thread.sleep(10 * 1000);
            ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端，这是一个执行耗时长的任务", CharsetUtil.UTF_8));
            System.out.println("耗时长的任务执行完毕，继续");
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 数据读取完毕后，返回消息给客户端
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.channel().close();
    }
}

package com.game.base.client;



import com.game.base.model.vo.MessageVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

public class NettyClientHandler extends SimpleChannelInboundHandler<MessageVo> {
    private Boolean isOk = false;
    //利用写空闲发送心跳检测消息
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:{
//
//                    Header header = new Header("");
//                    Message message = new Message();
//                    ctx.writeAndFlush(message);
//                    ctx.writeAndFlush(pingMsg);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageVo msg) throws Exception {
        ReferenceCountUtil.release(msg);
//        System.out.println("here is read message "+msg.toString());
//        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
//        System.out.println("host "+address.getHostString() +" port "+address.getPort());
//        try {
//
//        }finally {
//            ReferenceCountUtil.releaseLater(msg);
//        }
//        StrategyContext.getInstance().invokeStrategy(msg,ctx);
//        System.out.println("here is receive message"+msg.getBody());
//        ctx.fireChannelRead(msg);
//        if (msg.getHeader().getType())
//        switch (msg.getRequestType()){
//            case REQUEST:{
//                System.out.println(msg.toString());
//
//                Result result = JSON.parseObject(msg.getData(),Result.class);
////                if (result.getStatus() == ResultStatus.SUCCESS){
////                    System.out.println("nihao"+ msg.getData());
////                }
//            }break;
//            case RESPONSE:{
//                Result result = JSON.parseObject(msg.getData(),Result.class);
//
//                System.out.println("nihao"+ msg.getData());
//
//            }break;
//        }
//        log.info(msg.toString());
    }
}

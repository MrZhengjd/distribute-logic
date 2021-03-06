package com.game.base.server.handler;



import com.game.base.coder.MessageType;
import com.game.base.model.msg.Message;
import com.game.base.model.vo.MessageVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartbeatHandler extends SimpleChannelInboundHandler<MessageVo> {
    private int idleCount = 0;
    private int heartBeatCount = 0;
    private int maxHeartbeatCount = 66;
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE){
                idleCount ++;
                if (idleCount > 3){
                    ctx.close();
                }

            }
        }else {
            idleCount = 0;
        }
    }



//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        MessageVo message = (MessageVo) msg;
//        if (message.getHeader().getType() == MessageType.PING.getValue()){
//            Message response = Message.buildPongMessage(200,"success");
//            ctx.writeAndFlush(response);
//            this.heartBeatCount ++;
//            if (this.heartBeatCount > maxHeartbeatCount){
//                ctx.close();
//            }
//        }else {
//            this.heartBeatCount = 0;
//            this.idleCount = 0;
//            ctx.fireChannelRead(message);
//        }
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageVo messageVo) throws Exception {

    }
    //    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
//    }
}

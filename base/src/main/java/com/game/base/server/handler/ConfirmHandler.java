package com.game.base.server.handler;



import com.game.base.cache.ChannleMap;
import com.game.base.coder.MessageType;
import com.game.base.model.msg.Body;
import com.game.base.model.msg.Message;
import com.game.base.model.vo.MessageVo;
import com.game.base.model.vo.PlayerChannel;
import com.game.base.redis.JsonRedisManager;
import com.game.base.relation.Constants;
import com.game.base.schedule.ScheduleBean;
import com.game.base.serialize.DataSerialize;
import com.game.base.serialize.DataSerializeFactory;
import com.game.base.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Log4j2
public class ConfirmHandler extends SimpleChannelInboundHandler<MessageVo> {
    private static Logger logger = LoggerFactory.getLogger(ConfirmHandler.class);
//    private static Map<String,Integer> connectMap = new HashMap<>();
    private ChannleMap channleMap;
    private JsonRedisManager jsonRedisManager;
    private ScheduleBean scheduleBean;
    private static final long delay = 10000;
    private volatile boolean confirmSuccess = false;// 标记连接是否认证成功
    private ScheduledFuture<?> future;// 定时器的返回值
//    private static Logger
    private JWTUtil.TokenBody tokenBody;
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();

    public JWTUtil.TokenBody getTokenBody() {
        return tokenBody;
    }

    public ConfirmHandler(ChannleMap channleMap,JsonRedisManager  jsonRedisManager) {
        this.channleMap = channleMap;
        this.jsonRedisManager = jsonRedisManager;
    }
    public ConfirmHandler(ChannleMap channleMap,JsonRedisManager  jsonRedisManager,ScheduleBean scheduleBean) {
        this.channleMap = channleMap;
        this.jsonRedisManager = jsonRedisManager;
        this.scheduleBean = scheduleBean;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)  {
//
        if (scheduleBean != null){
            scheduleBean.scheduleJob(new TimerTask() {
                @Override
                public void run(Timeout timeout) throws Exception {
                    if (!confirmSuccess){
                        ctx.close();
                    }
                }
            },delay);
        }else {
            future = ctx.channel().eventLoop().schedule(()->{
                if (!confirmSuccess){
                    ctx.close();
                }
            },delay, TimeUnit.MILLISECONDS);
        }

        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (future != null){
            future.cancel(true);
        }
        if (tokenBody != null){
            long playerId = tokenBody.getPlayerId();
            this.channleMap.removeChannle(playerId);
//            channelService.removeChannel(playerId,ctx.channel());
        }
        ctx.fireChannelInactive();
    }

//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//
//        super.exceptionCaught(ctx, cause);
//        if (ctx.channel().isActive()){
//            ctx.close();
//        }
//    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageVo message) throws Exception {
//        Message message = (Message) msg;
        if (message.getHeader().getType() == MessageType.AUTH.getValue()){
//            Body body = (Body) message.getBody();
//            String token = body.getMessage().toString();
            String token = dataSerialize.deserialize(message.getData(),String.class);
            if (StringUtils.isEmpty(token)){
                ctx.close();
                return;
            }
            try {

                tokenBody = JWTUtil.getTokenBody(token);
                if (checkPlayerInGame(tokenBody.getPlayerId())) {
                    sendOriginMessage(ctx);
                    ctx.close();
                    return;
                }
                repeatConnect();
                this.confirmSuccess = true;
                this.channleMap.addChannel(tokenBody.getPlayerId(),ctx.channel());
                //保存玩家登录服务器信息 后面用来别的服务转发消息
                jsonRedisManager.setObjectHash1(Constants.PLAYER_SERVER,String.valueOf(tokenBody.getPlayerId()),Constants.SERVER_ID);
//                System.out.println("use time "+(System.currentTimeMillis() - timeMillis));
//                channelService.addChannel(tokenBody.getPlayerId(),ctx.channel());
                Message success = Message.buildAuthResponse(2000,"sucees");
                ChannelFuture future = ctx.channel().writeAndFlush(success);
                future.addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
//                        if (future.isSuccess()){
//                            System.out.println("here is send error");
//                        }else {
//                            System.out.println("error "+future.cause());
//                        }
                    }
                });
                ctx.fireChannelRead(message);
                //后面可以写发送消息给各个服务器通知玩家的已经登录成功了
//                String ip = AddressUtil.getRemoteIP(ctx.channel());
//                sendConnectStatusMsg(true,ctx.executor(),ip);
//                ctx.close();
            }catch (Exception e){
                System.out.println("here is error ------------");
                e.printStackTrace();
                if (e instanceof ExpiredJwtException){
                    Message fail = Message.buildAuthResponse(2400,"out of date");
                    ctx.writeAndFlush(fail);
                    ctx.close();
                }else {
                    ctx.close();
                }
            }

        }else {
            if (!confirmSuccess){
                ctx.close();
                logger.info("here is channel close "+(tokenBody == null ? " null ":tokenBody.getPlayerId()));
                return;
            }
            ctx.fireChannelRead(message);
        }
    }

    //发送原来游戏的服务器信息
    private void sendOriginMessage(ChannelHandlerContext ctx) {

    }

    //如果玩家正在其他游戏就不让他再这里登陆，返回原登陆的服务器地址
    private boolean checkPlayerInGame(long playerId) {
        return false;
    }


    //里面的方法不对，应该通过redis来获取用户所在的服务器，
    //如果所在的服务器存在，就给那个服务器发送下线的消息，同时通知用户异地登陆
    private void repeatConnect() {
        if (tokenBody != null){
            PlayerChannel playerChannel = channleMap.getByPlayerId(tokenBody.getPlayerId());
            if (playerChannel  == null){
                return;
            }
            Channel exist = playerChannel.getChannel();
            if (exist != null){
//
                Message message = Message.buildAuthResponse(2100,"differ channel error");
                exist.writeAndFlush(message);
                exist.close();


            }
        }
    }

}

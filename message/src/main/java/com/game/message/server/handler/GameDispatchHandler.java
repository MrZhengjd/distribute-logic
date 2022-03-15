package com.game.message.server.handler;

import com.game.base.cache.ChannleMap;
import com.game.base.cache.PlayerRoomService;
import com.game.base.coder.MessageType;
import com.game.base.concurrent.GameEventExecutorGroup;
import com.game.base.concurrent.IGameEventExecutorGroup;
import com.game.base.model.PlayerRequest;
import com.game.base.model.holder.RoomMap;
import com.game.base.model.vo.MessageVo;
import com.game.base.mq.MqProduceFactory;
import com.game.base.redis.JsonRedisManager;
import com.game.base.register.BalanceProvider;
import com.game.base.register.ServerData;
import com.game.base.relation.room.Room;
import com.game.base.relation.room.RoomManager;
import com.game.base.relation.vo.RoomServerVo;
import com.game.base.util.IncommingCount;
import com.game.base.util.JWTUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutor;
import org.apache.rocketmq.common.message.Message;

/**
 * @author zheng
 */
public class GameDispatchHandler extends SimpleChannelInboundHandler<MessageVo> {
    private BalanceProvider balanceProvider;
    private JsonRedisManager jsonRedisManager;
    private PlayerRoomService playerRoomService;
    private ChannleMap channleMap;

    public GameDispatchHandler(JsonRedisManager jsonRedisManager, ChannleMap channleMap) {
        this.jsonRedisManager = jsonRedisManager;
        this.channleMap = channleMap;
    }
    public GameDispatchHandler(JsonRedisManager jsonRedisManager, ChannleMap channleMap,BalanceProvider balanceProvider) {
        this.jsonRedisManager = jsonRedisManager;
        this.channleMap = channleMap;
        this.balanceProvider = balanceProvider;
    }

    public GameDispatchHandler(JsonRedisManager jsonRedisManager, ChannleMap channleMap, PlayerRoomService playerRoomService) {
        this.jsonRedisManager = jsonRedisManager;
        this.channleMap = channleMap;
        this.playerRoomService = playerRoomService;
    }

    private JWTUtil.TokenBody tokenBody;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageVo message) throws Exception {
//        int serviceId = message.getHeader().getServiceId();
//        System.out.println("welcome to dispatcher");
        IncommingCount.getAndIncrement();
        if (message.getHeader().getType() == MessageType.AUTH.value) {
            return;
        }
        //confirm handler 就用channelMap来获取玩家信息
//        PlayerChannel byChannel = channleMap.getByChannel(ctx.channel());
//        if (byChannel == null){
//            return;
//        }
//        Long playerId = byChannel.getPlayerId();
        Long playerId = Long.valueOf(message.getHeader().getFromServerId());
//        String clientIp = NettyUtils.getRemoteIP(ctx.channel());
        RoomServerVo vo = playerRoomService.getByPlayerId(playerId);
//        RoomServerVo roomServerVo = jsonRedisManager.getObjectHash(Constants.PLAYER_ROOM, String.valueOf(playerId), RoomServerVo.class);
        if (vo != null) {
//            safeExecute(GameEventExecutorGroup.getInstance().select(vo.getRoomNumber()), new Runnable() {
            safeExecute(IGameEventExecutorGroup.getInstance().selectByHash(vo.getRoomNumber()), new Runnable() {
                @Override
                public void run() {
                    Room room = RoomMap.getByRoomNumber(vo.getRoomNumber());
//                    Room room = jsonRedisManager.getObjectHash(Constants.ROOM_MAP, String.valueOf(vo.getRoomNumber()), Room.class);
                    PlayerRequest playerRequest = new PlayerRequest();
                    playerRequest.setRoom(room);
                    playerRequest.setRequestType(message.getHeader().getServiceId());
                    RoomManager roomManager = new RoomManager(room, jsonRedisManager);
                    roomManager.handleRequest(playerRequest);
                    sendMessageToMq(message);
                }


            });

        } else {
            System.out.println("here is room empty");
        }
        ctx.fireChannelRead(message);
    }

    /**
     *
     * @param message
     */
    private void sendMessageToMq(MessageVo message) {
        if (balanceProvider != null){
            String serviceName = "logic";
            ServerData data = (ServerData) balanceProvider.getBalanceItem(serviceName);

            Message mqMessage = new Message();
            mqMessage.setTopic(serviceName+"-"+data.getBalance());
            mqMessage.setBody(message.getData());
            mqMessage.setTags("undo");
            MqProduceFactory.getDefaultMqProduce().send(mqMessage);
        }
    }
    private void safeExecute(EventExecutor executor, Runnable task) {
        if (executor.inEventLoop()) {
            task.run();
        } else {
            executor.submit(task);
        }
//        executor.execute(task);
    }
}

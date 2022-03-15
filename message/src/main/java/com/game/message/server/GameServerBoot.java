package com.game.message.server;


import com.game.base.cache.ChannleMap;
import com.game.base.cache.PlayerRoomService;
import com.game.base.coder.MessageDecoder;
import com.game.base.coder.MessageEncoder;
import com.game.base.concurrent.GameEventExecutorGroup;
import com.game.base.concurrent.IGameEventExecutorGroup;
import com.game.base.mq.MqProduce;
import com.game.base.redis.JsonRedisManager;
import com.game.base.server.NettyServer;
import com.game.message.server.handler.GameDispatchHandler;
import com.google.common.util.concurrent.RateLimiter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GameServerBoot {

    @Autowired
    private ChannleMap channleMap;

    @Autowired
    private JsonRedisManager jsonRedisManager;

    @Autowired
    private PlayerRoomService playerRoomService;

    @Autowired
    @Qualifier("defaultMqProduce")
    protected MqProduce mqProduce;
    //    @Autowired
//    private ScheduleBean scheduleBean;
//    @PostConstruct
//    public void initRedis(){
//
//    }
    private RateLimiter globalRateLimiter;
    @Autowired
    private NettyServer nettyServer;

    private void startServer(String host, int port, ChannelInitializer initializer) {
        globalRateLimiter = RateLimiter.create(3000);
        nettyServer.serverBootstrap(initializer);
        nettyServer.start(host, port);
    }

    public void startServer(String host, int port) {
        globalRateLimiter = RateLimiter.create(30000);
        ChannelInitializer initializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast("encoder", new MessageEncoder())
                        .addLast("decoder", new MessageDecoder())
//                        .addLast("confirmHandler",new ConfirmHandler(channleMap,jsonRedisManager,scheduleBean))
//                        .addLast("request limit",new RequestRateLimiterHandler(globalRateLimiter,100000))
                        .addLast(new IdleStateHandler(30, 12, 45))
                        .addLast(IGameEventExecutorGroup.getInstance(), new GameDispatchHandler(jsonRedisManager, channleMap, playerRoomService))
                ;
//                        .addLast("heartbeart handler",new HeartbeatHandler());

            }
        };
        nettyServer.setServerBoot(nettyServer.serverBootstrap(initializer));
        nettyServer.start(host, port);
    }
}

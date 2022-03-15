package com.game.base.relation.room;

import com.game.base.concurrent.GameEventExecutorGroup;
import com.game.base.concurrent.IGameEventExecutorGroup;
import com.game.base.flow.component.PlayerContext;
import com.game.base.model.PlayerRequest;
import com.game.base.redis.JsonRedisManager;
import com.game.base.relation.Constants;
import com.game.base.relation.CoreEngine;
import com.game.base.relation.role.PlayerRole;
import com.game.base.relation.role.Role;
import com.game.base.relation.role.RoleManager;
import com.game.base.schedule.ScheduleBean;
import com.game.base.schedule.ScheduleJob;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * @author zheng
 */
public class RoomManager {
    private Room room;
    private JsonRedisManager jsonRedisManager;
    private ScheduledFuture<?> scheduledFuture;
    private ScheduledFuture<?> buhuaSchedule;
//    private EventExecutor executor;
    public Long getPlayingIndex(){
        return room.getPlayingIndex();
    }
    public RoomManager(Room room,JsonRedisManager jsonRedisManager) {
        this.room = room;
        this.jsonRedisManager = jsonRedisManager;
//        executor = GameEventExecutorGroup.getInstance().select(room.getRoomNum());
    }

    public RoomManager(Room room) {
        this.room = room;
//        executor = GameEventExecutorGroup.getInstance().select(room.getRoomNum());

    }
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void addRole(PlayerRole role){
        room.getPlayerMap().put(role.getPlayerId(),role);
//        room.addObserver(role);
    }

    public PlayerRole getById(Long id){
        return room.getPlayerMap().get(id);
    }

//    public RoleManager getOrCreateRoleManager(Long id){
//        RoleManager roleManager;
//        if (roleManagers.containsKey(id)){
//            roleManager = roleManagers.get(id);
//        }else {
//            roleManager = new RoleManager(getById(id));
//        }
//        return roleManager;
//    }
    private void safeExecute(EventExecutor executor,Runnable task) {
        executor.submit(task);
//        if (executor.inEventLoop()) {
//            task.run();
//        } else {
//
//            executor.submit(task);
//        }
    }


    public void handleTimerTask(Runnable runnable){
//        initExecutor();
//        safeExecute(executor,runnable);
    }
//    private void initExecutor(){
//        if (executor == null){
//            executor = GameEventExecutorGroup.getInstance().select(room.getRoomNum());
//        }
//
//    }

    public void handleTimerTask(ScheduleJob job, ScheduleBean scheduleBean){
//        initExecutor();

        safeExecute(GameEventExecutorGroup.getInstance().select(room.getRoomNum()), new Runnable() {
            @Override
            public void run() {
                PlayerContext playerContext = new PlayerContext();
                PlayerRequest playerRequest = new PlayerRequest();
                playerRequest.setRoom(room);
                playerContext.setPlayerRequest(playerRequest);
                job.scheduleJob(room);
                if (jsonRedisManager != null){
                    if (scheduleBean.getCurrent() - room.getLastPlayingTime() > 1000){
                        jsonRedisManager.setObjectHash1(Constants.ROOM_MAP,String.valueOf(room.getRoomNum()),room);
                    }

                }
            }
        });
    }

    public void handleTimerTask1(ScheduleJob job){
//        initExecutor();
//        safeExecute(executor, new Runnable() {
//            @Override
//            public void run() {
//                PlayerContext playerContext = new PlayerContext();
//                PlayerRequest playerRequest = new PlayerRequest();
//                playerRequest.setRoom(room);
//                playerContext.setPlayerRequest(playerRequest);
//                job.scheduleJob(room);
//                if (jsonRedisManager != null){
//                    jsonRedisManager.setObjectHash1(Constants.ROOM_MAP,String.valueOf(room.getRoomNum()),room);
//                }
//            }
//        });
    }
    /**
     * 处理进来的请求
     * @param request
     */
    public void handleRequest(final PlayerRequest request){
//        if (executor == null){
//            executor = GameEventExecutorGroup.getInstance().select(room.getRoomNum());
//        }
//        initExecutor();
//        safeExecute(GameEventExecutorGroup.getInstance().select(room.getRoomNum()), new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                    }
//                });
        CoreEngine engine = new CoreEngine(request);
        engine.process();
//        if (jsonRedisManager != null){
//            jsonRedisManager.setObjectHash1(Constants.ROOM_MAP,String.valueOf(room.getRoomNum()),room);
//        }

    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public void tuoGuan(Runnable runnable, Long init, Long delay, TimeUnit timeUnit){
//        scheduledFuture = executor.scheduleAtFixedRate(runnable,init,delay,timeUnit);

    }
    public ScheduledFuture<?> scheduledFuture(Runnable runnable, Long init, Long delay, TimeUnit timeUnit,Integer roomNumber){
//        executor.scheduleWithFixedDelay()

//        return GameEventExecutorGroup.getInstance().select(roomNumber).scheduleAtFixedRate(runnable,init,delay,timeUnit);
        return IGameEventExecutorGroup.getInstance().selectByHash(roomNumber).scheduleAtFixedRate(runnable,init,delay,timeUnit);

    }
//    public void tuoGuan(){
//        scheduledFuture = executor.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                if (room.getGameStatus() == 3){
//                    scheduledFuture.cancel(true);
//                }
//            }
//        },10,1000, TimeUnit.MILLISECONDS);
//    }

//    public void tuoGuanBuHua(BuHuaOperation buHuaOperation){
//        buhuaSchedule = executor.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                buHuaOperation.buhua(room);
//                buhuaSchedule.cancel(true);
//            }
//        }, 0, 100, TimeUnit.MILLISECONDS);
//    }
    public interface BuHuaOperation{
        void buhua(Room room);
    }


    public void initPlayingIndex(){
//        room.getRoomInfos().put(Constant.PLAYING_INDEX,1);
        room.setPlayingIndex(1l);
    }
    public void changePlayingIndex(){
//        Integer o = (Integer) room.getRoomInfos().get(Constant.PLAYING_INDEX);
//        changeNameValuePair(Constant.PLAYING_INDEX,"2");
        room.setPlayingIndex(2l);
    }

}

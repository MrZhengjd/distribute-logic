package com.game.base.schedule;

import com.game.base.concurrent.GameEventExecutorGroup;
import com.game.base.redis.JsonRedisManager;
import com.game.base.relation.room.Room;
import com.game.base.relation.room.RoomManager;
import com.game.base.server.NamedThreadFactory;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Contended;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zheng
 * 如果是时间短的任务用hashWheelTimer就可以了
 * 如果是耗时久的就用ScheduleFuture 详细看RoomManager类
 */
@Component
public class ScheduleBean {
    @Autowired
    private JsonRedisManager jsonRedisManager;
    private HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(new NamedThreadFactory("hash-timer"),10,TimeUnit.MILLISECONDS,8*16,true,1024*32);
    @Contended
    private volatile boolean start;
    @Contended
    private volatile long current = 0;
    public Long getCurrent(){
        if (!start){
           startLock();

        }
        return current;
    }
    public void startLock(){
        start = true;
        rotateLock();

    }
    private void rotateLock(){
        current = System.currentTimeMillis();
        hashedWheelTimer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                timeout.cancel();
                rotateLock();
            }
        },10,TimeUnit.MILLISECONDS);
    }
    public void roomTuoGuan(Room room, TimerTask timerTask,long delay){
        hashedWheelTimer.newTimeout(timerTask,delay, TimeUnit.MILLISECONDS);
        if (room.getGameStatus() != 3 && room.getGameStatus() != 0){
            roomTuoGuan(room,timerTask,delay);
        }
    }
    public void tuoGuanSchedule(Map<Integer,Room> roomMap, long delay, ScheduleJob job){
        ScheduleBean bean = this;
        hashedWheelTimer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                tuoGuanSchedule(roomMap,delay,job);
                for (Integer roomNumber : roomMap.keySet() ){
                    RoomManager roomManager = new RoomManager(roomMap.get(roomNumber),jsonRedisManager);
                    roomManager.handleTimerTask(job,bean);
                }

            }
        }, delay, TimeUnit.MILLISECONDS);

    }
    public void tuoGuanSchedule(long delay,ScheduleJob job,RoomManager roomManager){
        ScheduleBean bean = this;
        hashedWheelTimer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {

//                RoomManager roomManager = new RoomManager(room,jsonRedisManager);
                roomManager.handleTimerTask(job,bean);
                timeout.cancel();

                if (roomManager.getRoom().getGameStatus() != 3 && roomManager.getRoom().getGameStatus() != 0){
                    tuoGuanSchedule(delay,job,roomManager);
                }
            }
        }, delay, TimeUnit.MILLISECONDS);

    }
    public void scheduleJob(TimerTask timerTask, long delay){
        hashedWheelTimer.newTimeout(timerTask,delay,TimeUnit.MILLISECONDS);
    }
    public void start(){
        hashedWheelTimer.start();
    }
    public void stop(){
        hashedWheelTimer.stop();
    }


}

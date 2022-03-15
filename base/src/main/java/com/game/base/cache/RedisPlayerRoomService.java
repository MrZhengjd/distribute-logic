package com.game.base.cache;

import com.game.base.redis.JsonRedisManager;
import com.game.base.relation.Constants;
import com.game.base.relation.room.Room;
import com.game.base.relation.vo.RoomServerVo;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
@Service
public class RedisPlayerRoomService implements PlayerRoomService {
//    @Autowired
//    private StringRedisTemplate redisTemplate;
    @Autowired
    private JsonRedisManager jsonRedisManager;
    private Map<Long,RoomServerVo> roomServerVoMap = new HashMap<>();
    private ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    @Override
    public RoomServerVo getByPlayerId(Long playerId) {
        return readWriteLockOperate.readLockReturnOperation( new ReturnOperate<RoomServerVo>() {
            @Override
            public RoomServerVo operate() {
//                RoomServerVo result = jsonRedisManager.getObjectHash(Constants.PLAYER_ROOM, String.valueOf(playerId), RoomServerVo.class);
////                RoomServerVo result = (RoomServerVo) redisTemplate.opsForHash().get(Constants.PLAYER_ROOM, playerId);
//                if (result == null){
//                    jsonRedisManager.hashPutIfNotExistSimple(Constants.PLAYER_ROOM, String.valueOf(playerId),null);
//                }
//                return result ;
                return roomServerVoMap.get(playerId);
            }
        });


    }

    @Override
    public void putInfo(Long playerId, RoomServerVo roomServerVo) {
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                jsonRedisManager.setObjectHash1(Constants.PLAYER_ROOM, String.valueOf(playerId),roomServerVo);
//                redisTemplate.opsForHash().put(Constants.PLAYER_ROOM, playerId,roomServerVo);
                roomServerVoMap.put(playerId,roomServerVo);
            }
        });
    }
}

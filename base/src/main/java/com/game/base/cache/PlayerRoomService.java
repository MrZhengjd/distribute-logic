package com.game.base.cache;

import com.game.base.relation.room.Room;
import com.game.base.relation.vo.RoomServerVo;

/**
 * @author zheng
 */
public interface PlayerRoomService {
    RoomServerVo getByPlayerId(Long playerId);

    void putInfo(Long playerId, RoomServerVo roomServerVo);
}

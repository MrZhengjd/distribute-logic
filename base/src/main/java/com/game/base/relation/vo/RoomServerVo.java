package com.game.base.relation.vo;

import java.io.Serializable;

/**
 * @author zheng
 */
public class RoomServerVo implements Serializable {
    private Integer roomNumber;
    private Integer serverId;

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public RoomServerVo(Integer roomNumber, Integer serverId) {
        this.roomNumber = roomNumber;
        this.serverId = serverId;
    }
}

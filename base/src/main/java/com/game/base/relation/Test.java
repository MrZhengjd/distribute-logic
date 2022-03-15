package com.game.base.relation;

import com.game.base.relation.room.Room;
import com.game.base.relation.room.RoomManager;

/**
 * @author zheng
 */
public class Test {
    public static void main(String[] args) {
        Room room = new Room();
        RoomManager roomManager = new RoomManager(room);
        roomManager.initPlayingIndex();
        roomManager.changePlayingIndex();
        System.out.println(roomManager.getPlayingIndex());
    }
}

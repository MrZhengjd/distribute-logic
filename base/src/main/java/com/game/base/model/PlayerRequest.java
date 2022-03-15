package com.game.base.model;

import com.game.base.eventdispatch.Event;
import com.game.base.eventdispatch.EventAnnotationManager;
import com.game.base.flow.model.Request;
import com.game.base.relation.command.OperateCountCommand;
import com.game.base.relation.role.PlayerRole;
import com.game.base.relation.room.Room;
import com.game.base.relation.organ.Organ;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * @author zheng
 */
public class PlayerRequest  extends Observable implements Event {
    private Integer requestType;

    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    private Map<String ,Object> requestMap = new HashMap<>();

    public Map<String, Object> getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(Map<String, Object> requestMap) {
        this.requestMap = requestMap;
    }
    private PlayerRole playerRole;
    private Room room;

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void organOperate(Class< ? extends Organ> organ,Object data){
        Organ dst = playerRole.getOrganMap().get(organ.getSimpleName());
        playerRole.callEvent(dst,data);
    }
    public void organOperateWithName(Class< ? extends Organ> organ,String name){
        Organ dst = playerRole.getOrganMap().get(organ.getSimpleName());
        EventAnnotationManager.getInstance().sendPlayerRoleEvent(dst,name);
    }
    public void organOperateWithName(Class< ? extends Organ> organ,String name,Object data){
        Organ dst = playerRole.getOrganMap().get(organ.getSimpleName());
//        RequestOrgan requestOrgan = new RequestOrgan(data,dst);
        EventAnnotationManager.getInstance().sendPlayerRoleWithArgue(dst,name,data);
    }

    public void organCommandExecute(Class< ? extends Organ> organ,Object data){
        Organ dst = playerRole.getOrganMap().get(organ.getSimpleName());
//        RequestOrgan requestOrgan = new RequestOrgan(data,dst);
//        EventAnnotationManager.getInstance().sendPlayerRoleWithArgue(dst,name,data);
        OperateCountCommand chuPaiCommand = new OperateCountCommand();
//        chuPaiCommand.execute(dst,data);
    }

}

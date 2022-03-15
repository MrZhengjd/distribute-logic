package com.game.base.relation.room;

import com.game.base.relation.RuleDescription;
import com.game.base.relation.organ.Organ;
import com.game.base.relation.pai.Pai;
import com.game.base.relation.pai.PaiManager;
import com.game.base.relation.player.Player;
import com.game.base.relation.player.PlayerBaseInfo;
import com.game.base.relation.role.PlayerRole;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zheng
 */
public class Room implements Serializable {
    private Integer roomNum;
    private Integer gameStatus;//0准备    1 开始游戏      2结算阶段       3游戏结束
    private AtomicInteger continueCount = new AtomicInteger(0);
    private Integer dataCount = 0;
    private Long lastPlayingTime = 0l;

    public Long getLastPlayingTime() {
        return lastPlayingTime;
    }

    public void setLastPlayingTime(Long lastPlayingTime) {
        this.lastPlayingTime = lastPlayingTime;
    }

    public Integer getDataCount() {
        return dataCount;
    }

    public void setDataCount(Integer dataCount) {
        this.dataCount = dataCount;
    }

    public void getAndIncrement(){
        continueCount.getAndIncrement();
    }

    public Integer getCount(){
        return continueCount.get();
    }

    public Integer getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Integer gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    private Map<Long, PlayerBaseInfo> baseInfoMap = new HashMap<>();

    public Map<Long, PlayerBaseInfo> getBaseInfoMap() {
        return baseInfoMap;
    }

    public void setBaseInfoMap(Map<Long, PlayerBaseInfo> baseInfoMap) {
        this.baseInfoMap = baseInfoMap;
    }

    private Map<Long, PlayerRole> playerMap = new HashMap<>();
    private RuleDescription ruleDescription;
    private Integer gameType;
    private List<Pai> paiList = PaiManager.getTotalPaiList();

    public List<Pai> getPaiList() {
        return paiList;
    }

    public void setPaiList(List<Pai> paiList) {
        this.paiList = paiList;
    }

    //    private PaiManager paiManager;
    private GameRule gameRule;
    private Map<String , Organ> roomInfo = new HashMap<>();

    public Map<String, Organ> getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(Map<String, Organ> roomInfo) {
        this.roomInfo = roomInfo;
    }
    public GameRule getGameRule() {
        return gameRule;
    }

    public void setGameRule(GameRule gameRule) {
        this.gameRule = gameRule;
    }

//    public PaiManager getPaiManager() {
//        return paiManager;
//    }
//
//    public void setPaiManager(PaiManager paiManager) {
//        this.paiManager = paiManager;
//    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public RuleDescription getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(RuleDescription ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public Room(RuleDescription ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public Room() {
    }

    private Long playingIndex;

    public Long getPlayingIndex() {
        return playingIndex;
    }

    public void setPlayingIndex(Long playingIndex) {
        this.playingIndex = playingIndex;
    }

    public Map<Long, PlayerRole> getPlayerMap() {
        return playerMap;
    }

    public void setPlayerMap(Map<Long, PlayerRole> playerMap) {
        this.playerMap = playerMap;
    }



//    public void roomNotifyObserver(Object o){
//        setChanged();
//
//        notifyObservers(o);
//    }

}

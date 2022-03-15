package com.game.base.relation.room.controller;

import com.game.base.model.PlayerRequest;
import com.game.base.relation.role.PlayerRole;

/**
 * @author zheng
 */
public class HuController {

    private PlayerRequest playerRequest;

    public HuController(PlayerRequest playerRequest) {
        this.playerRequest = playerRequest;
    }

    public PlayerRequest getPlayerRequest() {
        return playerRequest;
    }

    public void setPlayerRequest(PlayerRequest playerRequest) {
        this.playerRequest = playerRequest;
    }
    public void huOperation(){
        double score = 5;
        for (PlayerRole playerRole : playerRequest.getRoom().getPlayerMap().values()){
            if (playerRole.getPlayerId().equals(playerRequest.getPlayerRole().getPlayerId())){
                continue;
            }
//            playerRequest.getRoom().roomNotifyObserver(score);

        }
    }
}

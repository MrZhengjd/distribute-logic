package com.game.base.relation.room.controller;

import com.game.base.relation.organ.HandOrgan;
import com.game.base.relation.organ.InnerOrgan;
import com.game.base.relation.pai.Pai;
import com.game.base.relation.role.PlayerRole;

/**
 * @author zheng
 */
public class ChuPaiController {
    private PlayerRole playerRole;

    public ChuPaiController(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }
    public void chuPai(Integer paiId){
        System.out.println("welcome to chu pai controller");
        HandOrgan handOrgan = playerRole.getOrganFromName(HandOrgan.class);
        InnerOrgan innerOrgan = playerRole.getOrganFromName(InnerOrgan.class);
        boolean remove = false;
        if (handOrgan != null && handOrgan.getMoPai() != null && handOrgan.getMoPai().getPaiId() == paiId) {
            remove = true;
            handOrgan.setMoPai(null);
        }
        if (remove){
            return;
        }
        if (innerOrgan != null){
            for (Pai tem: innerOrgan.getInnerPais()){
                if (tem.getPaiId() == paiId) {
                    innerOrgan.getInnerPais().remove(tem);
                    return;
                }
            }
        }

    }
}

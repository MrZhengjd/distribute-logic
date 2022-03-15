package com.game.base.relation.organ;

import com.game.base.relation.pai.Pai;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
public class OutterOrgan implements Organ {
    private List<List<Pai>> outterPaiList = new ArrayList<>();

    public List<List<Pai>> getOutterPaiList() {
        return outterPaiList;
    }

    public void setOutterPaiList(List<List<Pai>> outterPaiList) {
        this.outterPaiList = outterPaiList;
    }

    @Override
    public void reset() {
        outterPaiList.clear();
    }
}

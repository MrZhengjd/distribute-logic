package com.game.base.relation.organ;

import com.game.base.relation.pai.Pai;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
public class InnerOrgan implements Organ {
    private List<Pai> innerPais = new ArrayList<>();

    public List<Pai> getInnerPais() {
        return innerPais;
    }

    public void setInnerPais(List<Pai> innerPais) {
        this.innerPais = innerPais;
    }



    @Override
    public void reset() {
        innerPais.clear();
    }
}

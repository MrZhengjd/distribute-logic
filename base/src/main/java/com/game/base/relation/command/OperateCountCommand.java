package com.game.base.relation.command;

import com.game.base.relation.organ.OperateCountOrgan;
import com.game.base.relation.organ.Organ;
import com.game.base.relation.role.BaseRole;

/**
 * @author zheng
 */
public class OperateCountCommand implements Command<OperateCountOrgan,Integer> {


    @Override
    public void execute(OperateCountOrgan organ, Integer data) {
        organ.setOperateCount(data);
    }
}

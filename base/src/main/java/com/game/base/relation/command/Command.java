package com.game.base.relation.command;

import com.game.base.relation.organ.Organ;
import com.game.base.relation.role.BaseRole;

/**
 * @author zheng
 */
public interface Command<T extends Organ,V> {
    void execute(T organ, V data);
}

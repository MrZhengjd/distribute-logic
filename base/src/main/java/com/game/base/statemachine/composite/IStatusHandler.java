package com.game.base.statemachine.composite;

import com.game.base.statemachine.Status;

/**
 * @author zheng
 */
public interface IStatusHandler {
    Status handleStatus(Status status);
}

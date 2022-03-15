package com.game.base.statemachine;

/**
 * @author zheng
 */
public interface StatusMachine {
    public Status getNextStatus(Status status, StateEvent event);
}

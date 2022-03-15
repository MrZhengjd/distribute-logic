package com.game.base.statemachine.composite;

import com.game.base.statemachine.StateEvent;
import com.game.base.statemachine.Status;

/**
 * @author zheng
 */
public interface StateEventHandler {
    IStatus handleStateEvent(StateEvent event);
}

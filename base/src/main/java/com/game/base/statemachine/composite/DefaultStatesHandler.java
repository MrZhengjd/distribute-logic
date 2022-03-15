package com.game.base.statemachine.composite;

import com.game.base.statemachine.StateEvent;
import com.game.base.statemachine.Status;

/**
 * @author zheng
 */
public class DefaultStatesHandler implements IStatusHandler {
    @Override
    public Status handleStatus(Status status) {
        switch (status){
            case PERMIT_SUBMIT:
                return Status.LEADER_PERMIT;

        }
        return Status.PERMIT_FAIL;
    }

}

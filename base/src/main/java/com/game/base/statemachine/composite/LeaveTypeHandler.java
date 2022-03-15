package com.game.base.statemachine.composite;

import com.game.base.statemachine.LeavePermitType;

/**
 * @author zheng
 */
public interface LeaveTypeHandler {
    Rank handleLeaveType(IStatus iStatus, LeaveRequest leaveRequest);
}

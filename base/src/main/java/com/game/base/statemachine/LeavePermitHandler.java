package com.game.base.statemachine;

import com.game.base.eventdispatch.EventDispatchService;
import com.game.base.eventdispatch.EventListenerAnnotation;
import com.game.base.statemachine.composite.LeaveRequest;
import com.game.base.statemachine.composite.LeaveRequestEngine;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@EventDispatchService
@Component
public class LeavePermitHandler {
    @EventListenerAnnotation(value = LeavePermit.class)
    public void process(Object object,LeavePermit leavePermit){
        Engine engine = new Engine(leavePermit);
        engine.process();
    }
    @EventListenerAnnotation(value = LeaveRequest.class)
    public void processLeaveRequest(StateEvent event,LeaveRequest leaveRequest){
        LeaveRequestEngine engine = new LeaveRequestEngine(leaveRequest);
        engine.processEvent(event);
    }
}

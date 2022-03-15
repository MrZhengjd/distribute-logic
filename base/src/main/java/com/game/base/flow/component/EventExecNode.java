package com.game.base.flow.component;

import com.game.base.eventdispatch.EventAnnotationManager;
import com.game.base.flow.model.Context;
import com.game.base.flow.model.FastContextHolder;
import com.game.base.flow.model.Node;
import com.game.base.flow.model.Response;
import com.game.base.model.PlayerRequest;

/**
 * @author zheng
 */
public class EventExecNode implements ExecNode {
    @Override
    public Response executeNode(Node node) {
        PlayerContext runtimeContext = FastContextHolder.getRuntimeContext();
        EventAnnotationManager.getInstance().sendPlayerEvent(node.getComponent(),node,  runtimeContext.getPlayerRequest());
        return runtimeContext.getResponse();
    }
}

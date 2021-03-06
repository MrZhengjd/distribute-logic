package com.game.base.flow.component;


import com.game.base.flow.model.ContextHolder;
import com.game.base.flow.model.Node;
import com.game.base.flow.model.Response;

public class MethodExecNode extends AbstractExecNode  {

    @Override
    public Response executeNode(Node node) {
        if (HandleFactory.getInstance().getRequestByType(node.getReqType()) != null){
            return HandleFactory.getInstance().getRequestByType(node.getReqType()).handleRequest(ContextHolder.getRuntimeContext().getRequest());

        }
        throw new IllegalArgumentException("cannot find the reqType "+node.getReqType() + " node info "+node.getId());

    }
}

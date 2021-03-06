package com.game.base.flow.component;


import com.game.base.flow.model.ContextHolder;
import com.game.base.flow.model.FastContextHolder;
import com.game.base.flow.model.Node;
import com.game.base.flow.model.Response;
import com.game.base.flow.register.FlowDefinitonFactory;
import com.game.base.flow.register.FlowMapDefinitonFactory;
import org.springframework.util.StringUtils;


public class FlowHander {
    public Response handleNodeResult(Node node){

        ExecNode parser = ExecNodeFactory.getInstance().getNodeParserByType(node.getType());
        ContextHolder.getRuntimeContext().setResponse(parser.executeNode(node));
        String nextNodeId = node.getNextNodeId();
        if (!StringUtils.isEmpty(nextNodeId) && FlowDefinitonFactory.getInstance().getNodeMap().get(nextNodeId) != null){
            handleNodeResult(FlowDefinitonFactory.getInstance().getNodeMap().get(nextNodeId));
        }
        return ContextHolder.getRuntimeContext().getResponse();
    }

    public Response handleEvent(Node node){

        ExecNode parser = ExecNodeFactory.getInstance().getNodeParserByType(node.getType());
        FastContextHolder.getRuntimeContext().setResponse(parser.executeNode(node));
        String nextNodeId = node.getNextNodeId();
        if (!StringUtils.isEmpty(nextNodeId) && ContextHolder.getRuntimeContext().getResponse().getStatus_code() == 200 && FlowMapDefinitonFactory.getInstance().getNodeMap().get(nextNodeId) != null){
            handleEvent(FlowMapDefinitonFactory.getInstance().getNodeMap().get(nextNodeId));
        }
        return FastContextHolder.getRuntimeContext().getResponse();
    }
}

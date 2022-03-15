package com.game.base.flow.handler;

import com.game.base.flow.component.ExecNode;
import com.game.base.flow.component.ExecNodeFactory;
import com.game.base.flow.component.FlowHander;
import com.game.base.flow.component.PlayerContext;
import com.game.base.flow.model.*;
import com.game.base.flow.register.FlowDefinitonFactory;
import com.game.base.flow.register.FlowMapDefinitonFactory;
import com.game.base.model.PlayerRequest;

/**
 * @author zheng
 */
public class PlayerRequestHandler implements FlowHandler {

    @Override
    public Response handlePlayerRequest(PlayerRequest playerRequest, GameFlow gameFlow) {

//        Request request = ClassUtil.newInstance(flow.getInput(),Request.class);

        Response response = new Response();
        PlayerContext context = new PlayerContext();
        context.setPlayerRequest(playerRequest);
        context.setResponse(response);
        FastContextHolder.setCurrentContext(context);
        ExecNode parser ;
        for (Node node : gameFlow.getNodeList()){
            parser = ExecNodeFactory.getInstance().getNodeParserByType(node.getType());
            FastContextHolder.getRuntimeContext().setResponse(parser.executeNode(node));
        }
//        Node startNode = FlowMapDefinitonFactory.getInstance().getNodeMap().get(gameFlow.getStart());
//        FlowHander handed = new FlowHander();
//        return handed.handleEvent(startNode);
        return FastContextHolder.getRuntimeContext().getResponse();

    }
}

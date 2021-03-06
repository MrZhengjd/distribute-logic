package com.game.base.relation;

import com.game.base.controller.RequestMap;
import com.game.base.flow.handler.FlowHandler;
import com.game.base.flow.handler.PlayerRequestHandler;
import com.game.base.flow.model.FlowMap;
import com.game.base.flow.model.GameFlow;
import com.game.base.flow.register.FlowMapDefinitonFactory;
import com.game.base.model.PlayerRequest;
import com.game.base.model.msg.Message;

/**
 * @author zheng
 */
public class CoreEngine {
    private PlayerRequest playerRequest;

    private Message message;

    public CoreEngine(Message message) {
        this.message = message;
    }

    public CoreEngine(PlayerRequest playerRequest) {
        this.playerRequest = playerRequest;
    }

    public void process(){
        String gameType = String.valueOf(playerRequest.getRoom().getGameType());
//        gameType = gameType == null ? "32":gameType;
        FlowMap flowMap = FlowMapDefinitonFactory.getInstance().getFlowByteId(gameType);
//        String operate;
        if (flowMap != null){
            GameFlow gameFlow = flowMap.getFlowMap().get(RequestMap.getByRequestType(playerRequest.getRequestType()));
            FlowHandler flowHandler = new PlayerRequestHandler();
            flowHandler.handlePlayerRequest(playerRequest, gameFlow);
        }
//        FlowHandler
    }
}

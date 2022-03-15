package com.game.base.flow.handler;

import com.game.base.flow.model.Flow;
import com.game.base.flow.model.GameFlow;
import com.game.base.flow.model.Response;
import com.game.base.model.PlayerRequest;

/**
 * @author zheng
 */
public interface FlowHandler {
    Response handlePlayerRequest(PlayerRequest playerRequest, GameFlow gameFlow);
}

package com.game.base.flow.component;


import com.game.base.flow.model.Flow;
import com.game.base.flow.model.Request;
import com.game.base.flow.model.Response;

public interface FlowParser {
    Response parseFlow(Flow flow, Request request);
}

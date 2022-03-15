package com.game.base.flow.component;


import com.game.base.flow.model.Request;
import com.game.base.flow.model.Response;

public interface HandleRequest {
    Response handleRequest(Request request);
}

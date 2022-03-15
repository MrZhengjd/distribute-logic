package com.game.base.flow.component;


import com.game.base.flow.model.Node;
import com.game.base.flow.model.Response;

public interface ExecNode {
    Response executeNode(Node node);

}

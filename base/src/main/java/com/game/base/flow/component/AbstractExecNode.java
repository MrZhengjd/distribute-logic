package com.game.base.flow.component;


import com.game.base.flow.model.Node;

public abstract class AbstractExecNode implements ExecNode {


   public void start(Node node ){

       executeNode(node);
   }
}

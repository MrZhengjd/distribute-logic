package com.game.base.flow.register;


import com.game.base.flow.model.Flow;

import java.util.Map;

public interface FlowDefintionRegistry {
    Map<String, Flow> registry()throws Exception;
}

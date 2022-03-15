package com.game.base.flow.register;

import com.game.base.flow.model.FlowMap;

import java.util.Map;

/**
 * @author zheng
 */
public interface FlowMapDefintionRegistry {
    Map<String, FlowMap> registry()throws Exception;
}

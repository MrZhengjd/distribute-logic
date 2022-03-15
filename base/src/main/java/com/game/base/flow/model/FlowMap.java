package com.game.base.flow.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class FlowMap {
    private String  id;
    private String name;
    private Map<String ,GameFlow> flowMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, GameFlow> getFlowMap() {
        return flowMap;
    }

    public void setFlowMap(Map<String, GameFlow> flowMap) {
        this.flowMap = flowMap;
    }
}

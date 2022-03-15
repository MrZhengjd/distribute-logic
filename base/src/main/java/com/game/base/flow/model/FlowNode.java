package com.game.base.flow.model;

import java.util.List;

/**
 * @author zheng
 */
public class FlowNode {
    private String name;
    private String start;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<TempNode> nodes;

    public List<TempNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TempNode> nodes) {
        this.nodes = nodes;
    }
}

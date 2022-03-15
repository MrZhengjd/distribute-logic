package com.game.base.flow.register;


import com.game.base.flow.model.Flow;
import com.game.base.flow.model.FlowMap;
import com.game.base.flow.model.GameFlow;
import com.game.base.flow.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowMapDefinitonFactory {
    private static Map<String, FlowMap> registryMap = new HashMap<>();
    private static Map<String, Node> nodeMap = new HashMap<>();

    public  Map<String, Node> getNodeMap() {
        return nodeMap;
    }

    private static List<FlowMapDefintionRegistry> registries = new ArrayList<>();
    static {
        registries.add(new FlowMapDefinetionRegistry());
        initFactory();

    }

    public static Map<String, FlowMap> getRegistryMap() {
        return registryMap;
    }
    public  FlowMap getFlowByteId(String  id){
        return registryMap.get(id);
    }

    private FlowMapDefinitonFactory(){}
    public static FlowMapDefinitonFactory getInstance(){
        return FlowInstance.INSTANCE;
    }
    private static class FlowInstance{
        private static FlowMapDefinitonFactory INSTANCE = new FlowMapDefinitonFactory();
    }

    public static void initFactory(){
        registries.forEach(register->{
            try {
                registryMap.putAll(register.registry());
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}

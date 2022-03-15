package com.game.base.mq;

import java.util.HashMap;
import java.util.Map;

public class MqProduceFactory {

    private MqProduceFactory() {
    }

    private static class InstanceHolder{
        static MqProduceFactory instance = new MqProduceFactory();
    }
    public static MqProduceFactory getInstance(){
        return InstanceHolder.instance;
    }
    private static Map<String,MqProduce> produceMap = new HashMap<>();
    static {
//        produceMap.put("default",new DefaultMqProduce());
    }
    public static MqProduce getDefaultMqProduce(){
        return produceMap.get("default");
    }
}

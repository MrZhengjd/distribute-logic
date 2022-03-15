package com.game.base.mq;

public class MqConsumerFactory {

    private MqConsumerFactory() {
    }

    private static class InstanceHolder{
        static MqConsumerFactory instance = new MqConsumerFactory();
    }
    public static MqConsumerFactory getInstance(){
        return InstanceHolder.instance;
    }

}

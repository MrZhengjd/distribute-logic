package com.game.base.mq;

import org.apache.rocketmq.common.message.Message;

public interface MqProduce {

    void start();
    void send(Message message);
}

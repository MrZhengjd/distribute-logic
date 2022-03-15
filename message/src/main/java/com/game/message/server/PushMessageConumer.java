package com.game.message.server;


import com.game.base.mq.ConsumerConfig;
import com.game.base.mq.DefaultConsumerConfigure;
import com.game.base.serialize.DataSerialize;
import com.game.base.serialize.DataSerializeFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Getter
@Setter
@Component
public class PushMessageConumer extends DefaultConsumerConfigure  {
    @Value("${rocketmq.consumer.topic}")
    private String topic;
    @Value("${rocketmq.consumer.tag}")
    private String tag;
    private static PushMessageConumer pushMessageconsumer;
    @Autowired
    protected ConsumerConfig consumerConfig;
    @PostConstruct
    public void init(){
        pushMessageconsumer = this;
        pushMessageconsumer.consumerConfig = this.consumerConfig;
    }
    private final DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    protected DefaultMQPushConsumer getDefaultMQPushConsumer(){
        try {
            System.out.println("mq listen to the message " + topic);
            return defaultMQPushConsumer();

        }catch (Exception e){
            log.error("error "+e,e);
        }
        return null;
    }
    @Bean
    public DefaultMQPushConsumer defaultMQPushConsumer(){
        try {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(pushMessageconsumer.consumerConfig.getGroupName());
            consumer.setNamesrvAddr(pushMessageconsumer.consumerConfig.getNamesrvAddr());
            consumer.subscribe(topic, tag);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    return dealBody(msgs);
                }
            });
            log.info("consumer start ----------"+consumer);
            return consumer;
        }catch (Exception e){
            log.error("start consumer error "+e,e);
        }
        return null;
    }
    @Override
    public ConsumeConcurrentlyStatus dealBody(List<MessageExt> msgs) {
        log.info("consumer start receive message");
        try {
            for (MessageExt messageExt:msgs
                 ) {
//                PushMessage pushMessage = dataSerialize.deserialize(messageExt.getBody(),PushMessage.class);
//                PushTaskQueue.pushPushMessage(pushMessage);
//                log.info("mq receive message" + pushMessage);
                System.out.println(messageExt);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("error on consumer "+ e);
        }
        return null;
    }


    public PushMessageConumer() {

    }

}

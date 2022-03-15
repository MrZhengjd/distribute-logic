package com.game.message;

import com.game.base.eventdispatch.EventAnnotationManager;
import com.game.base.mq.MqConsumer;
import com.game.base.mq.MqConsumerFactory;
import com.game.base.mq.MqProduce;
import com.game.base.mq.MqProduceFactory;
import com.game.base.register.*;
import com.game.message.server.GameServerBoot;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import static com.game.base.register.DefaultBalanceProvider.PROVIDER;
import static com.game.base.register.DefaultBalanceProvider.ZOOKEEPER_CENTER;

@ComponentScan({"com.game.base", "com.game.message"})
@SpringBootApplication
public class MessageApplication {

    public static void main(String[] args) {

//        SpringApplication.run(MessageApplication.class, args);
        ConfigurableApplicationContext run = new SpringApplicationBuilder(MessageApplication.class).run(args);
//        ApplicationContext context = SpringApplication.run(GatewayStarterApplication.class,args);
//        IdGenerator idGenerator = IdGeneratorFactory.getDefaultGenerator();
        GameServerBoot boot = run.getBean(GameServerBoot.class);
        DefaultMQProducer producer = run.getBean(DefaultMQProducer.class);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        DefaultMQPushConsumer consumer = run.getBean(DefaultMQPushConsumer.class);
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        String host = "127.0.0.1";
        int port = 12346;
        boot.startServer(host, port);
        EventAnnotationManager manager = new EventAnnotationManager();
        manager.init(run);
        System.out.println("welcome-----------");
//        ServiceDiscover serviceDiscover = run.getBean(ServiceDiscover.class);
//        serviceDiscover.testService();
        DefaultRegistProvider registProvider = run.getBean(DefaultRegistProvider.class);
        String serviceName = "message";
        String serverPath = ZOOKEEPER_CENTER.concat("/").concat(serviceName).concat("/").concat(PROVIDER);
//        Long serverId = idGenerator.generateId();
        Long serverId = 2l;
        ServerData sd = new ServerData(serviceName,host, port,serverId.intValue());
        try {
            registProvider.regist(new ZookeeperRegistContext(serverId, serverPath, sd));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

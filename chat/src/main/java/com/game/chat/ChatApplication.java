package com.game.chat;

import com.game.base.eventdispatch.EventAnnotationManager;
import com.game.base.mq.DefaultMqProduce;
import com.game.base.mq.MqProduce;
import com.game.base.mq.MqProduceFactory;
import com.game.base.register.*;
import com.game.chat.server.GameServerBoot;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ResourceBundle;

import static com.game.base.register.DefaultBalanceProvider.PROVIDER;
import static com.game.base.register.DefaultBalanceProvider.ZOOKEEPER_CENTER;


@SpringBootApplication(scanBasePackages = "com.game")
//@EnableDiscoveryClient
public class ChatApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = new SpringApplicationBuilder(ChatApplication.class).run(args);
//        ApplicationContext context = SpringApplication.run(GatewayStarterApplication.class,args);
//        IdGenerator idGenerator = IdGeneratorFactory.getDefaultGenerator();
        GameServerBoot boot = run.getBean(GameServerBoot.class);
        DefaultMQProducer producer = run.getBean(DefaultMQProducer.class);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

//        MqProduce produce = MqProduceFactory.getDefaultMqProduce();
//        produce.start();
        String host = "127.0.0.1";
        int port = 12345;
        boot.startServer(host, port);
        EventAnnotationManager manager = new EventAnnotationManager();
        manager.init(run);
        System.out.println("welcome-----------");
//        ServiceDiscover serviceDiscover = run.getBean(ServiceDiscover.class);
//        serviceDiscover.testService();
        DefaultRegistProvider registProvider = run.getBean(DefaultRegistProvider.class);
        String serviceName = "chat";
        String serverPath = ZOOKEEPER_CENTER.concat("/").concat(serviceName).concat("/").concat(PROVIDER);
//        Long serverId = idGenerator.generateId();
        Long serverId = 1l;
        ServerData sd = new ServerData(serviceName,host, port,serverId.intValue());
        try {
            registProvider.regist(new ZookeeperRegistContext(serverId, serverPath, sd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultBalanceProvider defaultBalanceProvider = run.getBean(DefaultBalanceProvider.class);
        ServerData balanceItem = defaultBalanceProvider.getBalanceItem(serviceName);
        System.out.println(balanceItem);
        String testserviceName = "message";
        ServerData data =  defaultBalanceProvider.getBalanceItem(testserviceName);
        String test = "test";
        Message mqMessage = new Message();
        mqMessage.setTopic(testserviceName+"-"+data.getBalance());
        mqMessage.setBody(test.getBytes());
        mqMessage.setTags("undo");
        try {
            SendResult send = producer.send(mqMessage);
            System.out.println("send status "+send.getSendStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("here is send message to mq");
    }

}

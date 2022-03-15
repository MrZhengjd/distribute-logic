package com.game.base.register;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class DefaultBalanceProvider extends AbstractBalanceProvider<ServerData> {
//    private final ZkClient zc;
    private final ZookeeperCuratorUtil curatorUtil;
    public final static String PROVIDER = ResourceBundle.getBundle("zookeeper").getString("zookeeper.path.provider");
    public final static String ZOOKEEPER_CENTER = ResourceBundle.getBundle("zookeeper").getString("zookeeper.server.center");
    public DefaultBalanceProvider() {
//        this.zc = new ZkClient(ZookeeperCuratorUtil.SERVER, SESSION_TIME_OUT, CONNECT_TIME_OUT,new MyZkSerializer());
        this.curatorUtil = ZookeeperCuratorUtil.getInstance();
    }


    @Override
    protected ServerData balanceAlgorithm(List<ServerData> items) {
        try {
            if (items.size() > 0) {
                Collections.sort(items);
                return items.get(0);
            }
        } catch (Exception e) {
            log.error("exception " + e);
        }

        return null;
    }

    @Override
    protected List<ServerData> getBalanceItems(String serviceName) {
        try {
            String searchPath = ZOOKEEPER_CENTER.concat("/").concat(serviceName).concat("/").concat(PROVIDER);
            List<ServerData> sdList = new ArrayList<>();
            List<String> childrens = curatorUtil.readAll(searchPath);
            if (childrens.isEmpty() || childrens.size() == 0) {
                log.error("no data receive from zookeeper");
                throw new RuntimeException("no data receive from zookeeper");
            }
            for (String children : childrens) {
                String[] results = children.split(":");
                String host = results[0];
                String port = results[1];
                String balance = results[2];
                ServerData sd =new ServerData(host,Integer.valueOf(port));
                sd.setName(serviceName);
                sd.setBalance(Integer.valueOf(balance));
                sdList.add(sd);
            }
            return sdList;
        } catch (Exception e) {
            log.error("exception cause at get data from zookeeper " + e);
            throw new RuntimeException("exception cause at get data from zookeeper");
        }
    }

    @Override
    public void regist(String path,String content){

        try {
            curatorUtil.write(path,content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

package com.game.base.relation.role;

import com.game.base.relation.organ.Organ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class BaseRole implements Serializable {

    protected Map<String, Organ> organMap = new HashMap<>();
//    private Map<String, Event> actionMap = new HashMap<>();
    //存储玩家可以操作的信息到map
    private Map<String ,Object> operateInfo= new HashMap<>();

    public Map<String, Object> getOperateInfo() {
        return operateInfo;
    }

    public void setOperateInfo(Map<String, Object> operateInfo) {
        this.operateInfo = operateInfo;
    }

    protected BaseRole(Map<String, Organ> organMap) {
        this.organMap = organMap;

    }

    public Map<String, Organ> getOrganMap() {
        return organMap;
    }

    public void setOrganMap(Map<String, Organ> organMap) {
        this.organMap = organMap;
    }

//    public Map<String, Event> getActionMap() {
//        return actionMap;
//    }
//
//    public void setActionMap(Map<String, Event> actionMap) {
//        this.actionMap = actionMap;
//    }
}

package com.game.base.relation.room;

import com.alibaba.fastjson.JSONObject;
import com.game.base.flow.model.GameFlow;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class GameJsonMap {
    public static Map<Integer, SoftReference<JSONObject>> softReferenceMap = new HashMap<>();
    public static JSONObject getByGameType(int gameType){
        if (softReferenceMap.get(gameType) == null){
            return null;
        }
        return softReferenceMap.get(gameType).get();
    }
    public static void putInfo(Integer gameType, JSONObject jsonObject){
        softReferenceMap.put(gameType,new SoftReference<JSONObject>(jsonObject));
    }

    public static Map<Integer, SoftReference<GameRule>> softReferenceGameRuleMap = new HashMap<>();
    public static GameRule getGameRuleByGameType(int gameType){
        if (softReferenceMap.get(gameType) == null){
            return null;
        }
        return softReferenceGameRuleMap.get(gameType).get();
    }
    public static void putInfo(Integer gameType, GameRule gameRule){
        softReferenceGameRuleMap.put(gameType,new SoftReference<GameRule>(gameRule));
    }
}

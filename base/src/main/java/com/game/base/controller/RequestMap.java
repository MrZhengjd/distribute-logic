package com.game.base.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class RequestMap {
    private static Map<Integer,String> requestMap = new HashMap<>();
    static {
        requestMap.put(10001,"huPai");
    }
    public static String getByRequestType(Integer requestType){
        return requestMap.get(requestType);
    }
}

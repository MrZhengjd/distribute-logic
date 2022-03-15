package com.game.base.relation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class RuleDescription {
    private Map<String,Object> ruleInfo = new HashMap<>();

    public Map<String, Object> getRuleInfo() {
        return ruleInfo;
    }

    public void setRuleInfo(Map<String, Object> ruleInfo) {
        this.ruleInfo = ruleInfo;
    }
}

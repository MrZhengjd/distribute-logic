package com.game.base.util;

import com.game.base.checkHu.PaiInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
public class PaiUtil {
    public static List<PaiInfo> buildInfoWithIds(Integer... paiIds){
        List<PaiInfo> result = new ArrayList<>();
        for (Integer paiId : paiIds){
            result.add(PaiInfo.buildByPaiId(paiId));
        }
        return result;
    }
}

package com.game.chat.recommand;

import com.sun.org.apache.regexp.internal.RE;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zheng
 */
public class ItemCF {
    private static Map<String,Integer> itemIdMap = new HashMap<>();
    private static Map<Integer,String> idToItemMap = new HashMap<>();
    private static Map<String ,HashMap<String ,Double>> itemScoreMap = new HashMap<>();

    private static Map<String,Integer> userIdMap = new HashMap<>();
    private static Map<Integer,String> idToUserMap = new HashMap<>();
    private static Map<String ,HashMap<String ,Double>> userMap = new HashMap<>();

    private static double[][] simMatrix;
    private static int TOP_K = 25;
    private static int TOP_N = 20;

    /**
     * 读取一列数据 一列里面的数据有 userId movieId score
     * @throws Exception
     */
    public static void readUI() throws Exception {
        String fileName = "F://attack.txt";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
        String line ;
        String[] splitLine;
        int itemId = 0;
        int userId = 0;
        while ((line = bufferedReader.readLine()) != null){
            splitLine = line.split("\t");
            if (!itemIdMap.containsKey(splitLine[1])){
                HashMap<String ,Double> currentUserMap = new HashMap<>();
                currentUserMap.put(splitLine[0],Double.parseDouble(splitLine[2]));
                itemScoreMap.put(splitLine[1],currentUserMap);
                itemIdMap.put(splitLine[1],itemId);
                idToItemMap.put(itemId,splitLine[1]);
                itemId++;
            }else {
                HashMap<String, Double> map = itemScoreMap.get(splitLine[1]);
                map.put(splitLine[0],Double.parseDouble(splitLine[2]));
                itemIdMap.put(splitLine[1],itemId);
            }

            if (!userMap.containsKey(splitLine[0])){
                userIdMap.put(splitLine[0],userId);
                idToUserMap.put(userId,splitLine[0]);
                userId++;
                HashMap<String ,Double> currentUserMap = new HashMap<>();
                currentUserMap.put(splitLine[1],Double.parseDouble(splitLine[2]));
                userMap.put(splitLine[0],currentUserMap);
            }else {
                HashMap<String, Double> map = userMap.get(splitLine[1]);
                map.put(splitLine[1],Double.parseDouble(splitLine[2]));
                userMap.put(splitLine[0],map);
            }

        }
    }

    /**
     * 产品之间的相似度
     */
    public static void itemSimilarry(){
        simMatrix = new double[itemScoreMap.size()][itemScoreMap.size()];
        int itemCount = 0;
        for (Map.Entry<String,HashMap<String,Double>> entry : itemScoreMap.entrySet()){
            Set<String> ratedUserSet = new HashSet<>();
            for (Map.Entry<String,Double> userEntry : entry.getValue().entrySet()){
                ratedUserSet.add(userEntry.getKey());
            }
            int rateUserSize = ratedUserSet.size();
            for (Map.Entry<String,HashMap<String,Double>> entry2 : itemScoreMap.entrySet()){
                if (itemIdMap.get(entry2.getKey()) > itemIdMap.get(entry.getKey())){
                    Set<String> ratedUserSet2 = new HashSet<>();
                    for (Map.Entry<String,Double> userEntry : entry2.getValue().entrySet()){
                        ratedUserSet2.add(userEntry.getKey());
                    }
                    int rateUserSize2 = ratedUserSet2.size();
                    int sameUserSize = interCount(ratedUserSet,ratedUserSet2);
                    double similarity = sameUserSize / (Math.sqrt(rateUserSize * rateUserSize2));
                    simMatrix[itemIdMap.get(entry.getKey())][itemIdMap.get(entry2.getKey())] =similarity;
                    simMatrix[itemIdMap.get(entry.getKey())][itemIdMap.get(entry2.getKey())] =similarity;
                }


//            for (Map.Entry)
            }
            itemCount ++;
//            for (Map.Entry)
        }
    }
    public static <K extends Comparable,V extends Comparable> Map<K,V> sortMapByValues(Map<K,V> map){
        HashMap<K,V> finalOut = new LinkedHashMap<>();
        map.entrySet().stream().sorted((p1,p2)->p2.getValue().compareTo(p1.getValue())).collect(Collectors.toList()
        ).forEach(each->finalOut.put(each.getKey(),each.getValue()));
        return finalOut;
    }

    public static void recommend() throws Exception {
        String fileName = "F://result.txt";
        BufferedWriter bufferedReader = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))));
        Map<Integer,HashSet<Integer>> nearsetItemMap = new HashMap<>();
        for (int i = 0;i<itemScoreMap.size();i++){
            Map<Integer,Double> simMap = new HashMap<>();
            for (Integer j = 0;j < itemScoreMap.size();j++){
                simMap.put(j,simMatrix[i][j]);
            }
            simMap = sortMapByValues(simMap);

            int simItemCount = 0;
            HashSet<Integer> nearestItemSet = new HashSet<>();
            for (Map.Entry<Integer,Double> entry : simMap.entrySet()){
                if (simItemCount < TOP_K){
                    nearestItemSet.add(entry.getKey());
                    simItemCount ++;
                }else {
                    break;
                }
            }
            nearsetItemMap.put(i,nearestItemSet);
        }

        int i = 0;
        for (Map.Entry<String, HashMap<String, Double>> entry : userMap.entrySet()) {
            HashSet<Integer> currentUserSet = new HashSet<>();
            Map<String ,Double> preRatingMap = new HashMap<>();
            for (Map.Entry<String, Double> userEntry :entry.getValue().entrySet()) {
                currentUserSet.add(itemIdMap.get(userEntry.getKey()));
            }
            int j = 0;
            for (Map.Entry<String, HashMap<String, Double>> mapEntry : itemScoreMap.entrySet()) {
                double preRating = 0;
                double sumSim = 0;
//                if (currentUserSet.contains(entry.getKey())){
//                    continue;
//                }
                Set<Integer> interSet = interSet(currentUserSet,nearsetItemMap.get(j));
                if (!interSet.isEmpty()) {
                    for (Integer item :interSet){
                        sumSim += simMatrix[j][item];
                        preRating += simMatrix[j][item] * userMap.get(idToUserMap.get(i)).get(idToItemMap.get(item));
                    }
                    if (sumSim != 0){
                        preRating = preRating / sumSim;
                    }else {
                        preRating = 0;
                    }
                }else {
                    preRating = 0;
                }
                preRatingMap.put(idToItemMap.get(j),preRating);
                j ++;
            }
            preRatingMap = sortMapByValues(preRatingMap);
            if (!preRatingMap.isEmpty()){
                bufferedReader.append(idToUserMap.get(j)+":");
            }
            int recCount = 0;
            for (Map.Entry<String, Double> stringDoubleEntry : preRatingMap.entrySet()) {
                if (recCount < TOP_N){
                    bufferedReader.append(entry.getKey()+" ");
                    recCount ++;
                    bufferedReader.flush();
                }
            }
            bufferedReader.newLine();
            bufferedReader.flush();
            i++;
        }
        bufferedReader.flush();
        bufferedReader.close();
    }


    public static Set<Integer> interSet(Set<Integer> set,Set<Integer> other){
        Set<Integer> result = new HashSet<>();
        for (Integer o : set){
            if (other.contains(o)){
                result.add(o);
            }
        }
        return result;
    }
    /**
     * 两个集合的交集
     * @param ratedUserSet
     * @param ratedUserSet2
     * @return
     */

    private static int interCount(Set<String> ratedUserSet, Set<String> ratedUserSet2) {
        int sameCount = 0;
        for (Object o : ratedUserSet){
            if (ratedUserSet2.contains(o)){
                sameCount ++;
            }
        }
        return sameCount;
    }

    public static void main(String[] args) {
        try {
            readUI();
            itemSimilarry();
            recommend();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

package com.game.base.relation.role;

import com.game.base.eventdispatch.Event;
import com.game.base.eventdispatch.EventAnnotationManager;
import com.game.base.relation.Constants;
import com.game.base.relation.pai.Pai;
import com.game.base.relation.organ.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


/**
 * @author zheng
 */
public class PlayerRole extends BaseRole implements Cloneable, Observer {
    private Long playerId;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    private double currentRoundScore;
    private double totalRoundScore;

    public double getCurrentRoundScore() {
        return currentRoundScore;
    }

    public void setCurrentRoundScore(double currentRoundScore) {
        this.currentRoundScore = currentRoundScore;
    }

    public double getTotalRoundScore() {
        return totalRoundScore;
    }

    public void setTotalRoundScore(double totalRoundScore) {
        this.totalRoundScore = totalRoundScore;
    }

    @Override
    public void update(Observable o, Object arg) {
        setCurrentRoundScore((Double) arg);
    }

    private static class PlayerRoleBuilder{
        private PlayerRole playerRole =buildPlayerRole();
        public  PlayerRole build(){
            return playerRole;
        }

    }
    private PlayerRole(Map<String, Organ> organMap) {

        super(organMap);
    }
    public static PlayerRole defaultPlayerRole(){
        OutterOrgan outterOrgan = new OutterOrgan();
        InnerOrgan innerOrgan = new InnerOrgan();
        OuttedOrgan outtedOrgan = new OuttedOrgan();
        HandOrgan handOrgan = new HandOrgan();
        return buildWithOrgan(outtedOrgan,outterOrgan,innerOrgan,handOrgan);
    }
    public static PlayerRole buildPlayerRole(){
         OutterOrgan outterOrgan = new OutterOrgan();
         InnerOrgan innerOrgan = new InnerOrgan();
         OuttedOrgan outtedOrgan = new OuttedOrgan();
//         ChuPaiAction chuPaiAction = new ChuPaiAction();
         Map<String, Organ> organMap = new HashMap<>();
         addOrgan(organMap,outtedOrgan);
         addOrgan(organMap,outterOrgan);
         addOrgan(organMap,innerOrgan);
         addOrgan(organMap,new HandOrgan());
//         actionMap.put("chuPaiAction",chuPaiAction);
         return new PlayerRole(organMap);
    }
    public static PlayerRole getInstance(){
        return Holder.instance;
    }
    private static class Holder{
        private static PlayerRole instance = new PlayerRole(new HashMap<>());
    }
    private static PlayerRole buildWithOrgan(Organ... organs){
        Map<String, Organ> organMap = new HashMap<>();
        for (Organ organ : organs){
            addOrgan(organMap,organ);
        }
        return new PlayerRole(organMap);
    }

    private PlayerRole buildWithOrganFromInstance(Organ... organs){
        PlayerRole instance = getInstance();
        Map<String, Organ> organMap = instance.organMap;
        for (Organ organ : organs){
            addOrgan(organMap,organ);
        }
        return instance;
    }
    public static void addOrgan(Map<String, Organ> organMap,Organ organ){
        if (organMap == null){
            return;
        }
        organMap.put(organ.getClass().getSimpleName(),organ);
    }

    public  void addOrgan(Organ organ){
        if (getOrganMap() == null){
            return;
        }

        organMap.put(organ.getClass().getSimpleName(),organ);
    }
    public Organ getOrganFromSimpleName(Class organ){
        return organMap.get(organ.getSimpleName());
    }

    public <T> T getOrganFromName(Class<? extends Organ> organ){
        return (T)organMap.get(organ.getSimpleName());
    }
    public HandOrgan getHandOrgan(){
        return (HandOrgan) getOrganFromSimpleName(HandOrgan.class);
    }
    public OuttedOrgan getOuttedOrgan(){
        return (OuttedOrgan) getOrganMap().get(Constants.OUTTED_ORGAN);
    }
    public OutterOrgan getOutterOrgan(){
        return (OutterOrgan) getOrganMap().get(Constants.OUTTER_ORGAN);
    }
    public InnerOrgan getInnerOrgan(){
        return (InnerOrgan) getOrganFromSimpleName(InnerOrgan.class);
    }

    public void callEvent(Event event,Object object){
        EventAnnotationManager.getInstance().sendEvent(event,object);
    }

    public void chuPai(Integer paiId){

        HandOrgan handOrgan = getHandOrgan();
        InnerOrgan innerOrgan = getInnerOrgan();
        boolean remove = false;
        if (handOrgan.getMoPai() != null && handOrgan.getMoPai().getPaiId() == paiId) {
            remove = true;
            handOrgan.setMoPai(null);
        }
        if (remove){
            return;
        }
        for (Pai tem: innerOrgan.getInnerPais()){
            if (tem.getPaiId() == paiId) {
                innerOrgan.getInnerPais().remove(tem);
                return;
            }
        }
    }
}

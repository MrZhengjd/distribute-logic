package com.game.base.eventdispatch;

import com.game.base.flow.model.FastContextHolder;
import com.game.base.flow.model.Response;
import com.game.base.model.PlayerRequest;
import com.game.base.relation.organ.OperateCountOrgan;
import com.game.base.relation.room.controller.ChuPaiController;
import com.game.base.relation.room.controller.HuController;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zheng
 */
@EventDispatchService
@Component
public class DisPlayerHandler {

    private AtomicLong handleCount = new AtomicLong(0);

    public AtomicLong getHandleCount() {
        return handleCount;
    }

    @EventListenerAnnotation(DisPlayEvent.class)
    public void display(Object origin, DisPlayEvent event) {
        System.out.println("here is displayer here "+event+ " and origin "+origin);
    }
    @EventListenerAnnotation(value = DisPlayEvent.class)
    public Response response(Object origin,DisPlayEvent disPlayEvent){
        Response response = new Response();
        response.setResult("test");
        return response;
    }
    @EventListenerAnnotation(value = PlayerRequest.class)
    public void pengResponse(PlayerRequest playerRequest){
        FastContextHolder.setSuccessResult("data");

    }

    @EventListenerAnnotation(value = PlayerRequest.class)
    public void chuPai(PlayerRequest playerRequest){
//
        ChuPaiController chuPaiController = new ChuPaiController(playerRequest.getPlayerRole());
        chuPaiController.chuPai((Integer) playerRequest.getRequestMap().get("paiId"));
//
        //修改operateCount
        FastContextHolder.setSuccessResult("data1");

    }

    @EventListenerAnnotation(value = PlayerRequest.class)
    public void huPai(PlayerRequest playerRequest){
//
//        HuController huController = new HuController(playerRequest);
//        huController.huOperation();
//        System.out.println("welcome hu operation");
        //修改operateCount
        FastContextHolder.setSuccessResult("data1");
        handleCount.getAndIncrement();

    }

//
    @EventListenerAnnotation(value = OperateCountOrgan.class)
    public void incrementOperate(OperateCountOrgan operateCountOrgan){
        operateCountOrgan.setOperateCount(operateCountOrgan.getOperateCount()+1);
    }
    @EventListenerAnnotation(value = OperateCountOrgan.class)
    public void incrementOperate1(OperateCountOrgan operateCountOrgan,Integer data){

        operateCountOrgan.setOperateCount(data);
    }

}

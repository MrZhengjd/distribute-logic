package com.game.base.statemachine.solder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class StateMachine {
    private Play play;

    private static Map<StateType,HandleStateChange> map = new HashMap<>();
    static {
        SolderStateChangeHandler solderStateChangeHandler = new SolderStateChangeHandler();
        SecondHandle secondHandle = new DefaultSecondHandle();
        HandleStateChange equitmentState = new EquitMentStateHandle();
        StateChangeProxy complex = new StateChangeProxy(solderStateChangeHandler,secondHandle,equitmentState);
        map.put(StateType.NORMAL,complex);
    }
    public StateMachine(Play play) {
        this.play = play;
    }
    public void handle(Play play,StateChange stateChange){
        HandleStateChange handleStateChange = map.get(play.getStateType());
//        play.getEquitmenState()
        handleStateChange.enterState(stateChange,play);
    }

    public static void main(String[] args) {
        Play play = new Play(StateType.NORMAL);
        StateMachine stateMachine = new StateMachine(play);
        stateMachine.handle(play,StateChange.Enter);
    }
}

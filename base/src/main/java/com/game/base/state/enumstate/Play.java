package com.game.base.state.enumstate;

/**
 * @author zheng
 */
public class Play {
    private EquitmenState equitmenState;
    private State0 state0;

    public void input(){
        equitmenState.equitment();
        state0.cool();
    }
    public EquitmenState getEquitmenState() {
        return equitmenState;
    }

    public void setEquitmenState(EquitmenState equitmenState) {
        this.equitmenState = equitmenState;
    }

    public State0 getState0() {
        return state0;
    }

    public void setState0(State0 state0) {
        this.state0 = state0;
    }
}

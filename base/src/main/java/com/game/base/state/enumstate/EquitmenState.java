package com.game.base.state.enumstate;

/**
 * @author zheng
 */
public enum EquitmenState {
    AIRPLANE{
        @Override
        public void equitment() {
            System.out.println("airplane---------");
        }
    };
    public abstract void equitment();
}

package com.game.base.state.enumstate;

/**
 * @author zheng
 */
public enum State0 {
    OFF{
        @Override
        public void power() {

        }

        @Override
        public void cool() {

        }
    },
    COOL{
        @Override
        public void power() {

        }

        @Override
        public void cool() {

        }
    };
    public abstract void power();
    public abstract void cool();
}

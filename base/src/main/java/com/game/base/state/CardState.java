package com.game.base.state;

/**
 * @author zheng
 */
public abstract class CardState {
    protected CardPlay cardPlay;
    abstract void play();
    abstract void doubleScore();
    abstract void handCards();
    abstract void peekCards();
}

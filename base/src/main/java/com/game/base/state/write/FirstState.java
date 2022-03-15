package com.game.base.state.write;

/**
 * @author zheng
 */
public class FirstState extends State {
    public FirstState() {
    }
    public FirstState(Play play) {
        this.play = play;
    }
    public FirstState(State state) {
        this.play = state.play;
    }
    @Override
    public void display() {

    }
}

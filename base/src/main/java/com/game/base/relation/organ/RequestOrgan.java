package com.game.base.relation.organ;

import com.game.base.eventdispatch.Event;

/**
 * @author zheng
 */
public class RequestOrgan implements Event {
    private Object input;
    private Organ orgin;

    public RequestOrgan(Object input, Organ orgin) {
        this.input = input;
        this.orgin = orgin;
    }

    public Organ getOrgin() {
        return orgin;
    }

    public void setOrgin(Organ orgin) {
        this.orgin = orgin;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }
}

package com.company.listen;

import org.springframework.context.ApplicationEvent;

public class FirstEvent extends ApplicationEvent{
    private int n;

    public FirstEvent(Object source) {
        super(source);
    }

    public FirstEvent(Object source, int n) {
        super(source);
        this.n = n;
    }

    public int getN() {
        return n;
    }
}

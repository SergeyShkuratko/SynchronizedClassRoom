package com.company;

public class CounterWrapper {

    private volatile int counter;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void add() {
        counter++;
    }

}

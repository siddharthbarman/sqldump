package com.sbytestream;

public class StopWatch {
    public void start() {
        begin = System.currentTimeMillis();
    }

    public long stop() {
        long result = System.currentTimeMillis() - begin;
        begin = 0;
        return result;
    }

    public long reset() {
        long result = System.currentTimeMillis() - begin;
        begin = System.currentTimeMillis();
        return result;
    }

    private long begin;
}

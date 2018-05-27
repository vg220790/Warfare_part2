package com.afekawar.bl.base.Interface.Time;

public class MyTime implements SystemTime, Runnable {
    private long startTime;

    @Override
    public int getTime(){
        return Math.toIntExact((System.nanoTime() - startTime)/1000000000);
    }

    @Override
    public void run() {
        System.out.println("Timer runs....");
        this.startTime = System.nanoTime();

    }
}

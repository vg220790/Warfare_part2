package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Entities.Missile;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.logging.Logger;
import java.util.concurrent.ThreadLocalRandom;

public class MissileLauncher implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private String id;
    private Logger logger;             // TODO - implement Logger
    private Queue<Missile> missiles;
    private boolean isAlive;
    private boolean isHidden;
    private Thread activeMissileThread;
    private Point2D coordinates;


    public MissileLauncher(String id, boolean isHidden){
        int randomNumbersMinX[] = {680,740,610,450};
        int randomNumbersMaxX[] = {760,870,660,620};

        int randomNumbersMinY[] = {140,40,220,400};
        int randomNumbersMaxY[] = {210,125,300,500};

        int index = ThreadLocalRandom.current().nextInt(0,4);

        this.id = id;
        this.isHidden = isHidden;
        this.isAlive = true;
        missiles = new PriorityQueue<>();
        activeMissileThread = null;
        coordinates = new Point2D(ThreadLocalRandom.current().nextInt(randomNumbersMinX[index], randomNumbersMaxX[index] + 1),ThreadLocalRandom.current().nextInt(randomNumbersMinY[index], randomNumbersMaxY[index] + 1));  // Set Random coordinate within Gaza Strip

    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public String getId() {
        return id;
    }
    public boolean getHidden() {
        return isHidden;
    }
    public boolean getAlive() {
        return isAlive;
    }
    public Thread getActiveMissileThread(){
        return activeMissileThread;
    }
    public Queue<Missile> getMissiles() {
        return missiles;
    }
    public Point2D getCoordinates(){
        return coordinates;
    }
    public Logger getLogger() {
        return logger;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setMissiles(Queue<Missile> missiles) {
        this.missiles = missiles;
    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }


    public void stopThread(){                                                       // Missile launcher destroy func
        System.out.println("Missile Launcher n` " + id + " Got destroyed....");
        if(activeMissileThread != null)
            activeMissileThread.interrupt();
        isAlive = false;
        missiles.clear();
    }






    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
            Long time = System.nanoTime();                                        // Current nanosec value
            System.out.println("Missile Launcher n` " + id + " Started...");

            while (!missiles.isEmpty()) {
                if (activeMissileThread != null) {
                    try {
                        activeMissileThread.join();               // Wait for previous missile finish it's work
                        Thread.sleep(20);                   // To let time for graphics to update..
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (isAlive && !missiles.isEmpty()) {
                    Missile m =  missiles.peek();
                    Long waitTime = m.getLaunchTime() - ((System.nanoTime() - time) / 1000000000);     // Check if the next missile's launch time is later than earlier missile finished it's fly...
                    if (waitTime > 0)
                        try {
                            isHidden = true;
                            System.out.println("Launcher n` " + id + " Waiting " + waitTime + " seconds till next Missile..");
                            Thread.sleep(waitTime * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isHidden = false;
                        System.out.println("Missile n` " + m.getId() + " From Launcher n` " + id + " Launched at " + ((System.nanoTime() - time) / 1000000000) + " seconds");
                        Thread missileThread = new Thread((Runnable) m);
                        missileThread.setName(m.getId());
                        missileThread.start();
                        activeMissileThread = missileThread;                // Keep the missile thread reference.




                }
            }

            if(isAlive)
                System.out.println("Missile Launcher n` " + id + " All missiles out after " + ((System.nanoTime() - time) / 1000000000) + " seconds");
        }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.awt.Color;
import traffic_congestion_simulator.Buffer;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author chenhanxi
 */
public class Light_Pool implements Runnable{
    
    Light_Set[] lightpool;
    
    final Buffer shared;
    
    public Light_Pool(int numOfIntersection, Buffer shared) {
        
        lightpool = new Light_Set[numOfIntersection];
        
        for (int i = 0; i < lightpool.length; i++) {
            // I'm using TCSConstant for now. We will change to have a specific file for it.
            lightpool[i] = new Light_Set(TCSConstant.lightposition[i][0], TCSConstant.lightposition[i][1]);
        }
        
        this.shared = shared;
    }
    
    private void runCycleUnit() {
        for (Light_Set lightpool1 : lightpool) {
            lightpool1.runCycleUnit();
        }
    }

    // This is the thread
    @Override
    public void run() {
        
        while(true) {
            synchronized (shared) {
                if (shared.getState() != 1) {
                    try {
                        shared.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    System.out.println("Light increment by 1 unit second");
                    runCycleUnit();
                    Thread.sleep(1000); //  I need a unit second
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                // Change to Lane
                shared.setState(2);
                shared.notifyAll();
            }
        }
        
        
    }
}

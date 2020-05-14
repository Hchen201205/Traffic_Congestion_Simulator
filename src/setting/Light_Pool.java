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
//Creates different situations for cars, and to mimic real life traffic light.
public class Light_Pool implements Runnable {

    LightSet[] lightpool;

    final Buffer shared;
    
    //Creates all the traffic lights.
    public Light_Pool(Buffer shared) {

        lightpool = new LightSet[TCSConstant.NUMOFINTERSECTION + 1];

        for (int i = 0; i < lightpool.length - 1; i++) {
            // I'm using TCSConstant for now. We will change to have a specific file for it.
            lightpool[i] = new LightSet(TCSConstant.LIGHTPOSITION[i], TCSConstant.NUMOFLANE[i]);
        }
        // Export lane.
        lightpool[lightpool.length - 1] = null;

        this.shared = shared;
    }

    public LightSet getLight_Set(int index) {
        return lightpool[index];
    }
    
    private void runCycleUnit() {
        for (LightSet lightpool1 : lightpool) {
            lightpool1.runCycleUnit();
        }
    }

    // This is the thread
    @Override
    public void run() {

        while (true) {
            synchronized (shared) {
                if (shared.getState() != 1) {
                    try {
                        shared.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Light increment by 1 unit second");
                runCycleUnit();
                // Change to Lane
                shared.setState(2);
                shared.notifyAll();
            }
        }

    }
}

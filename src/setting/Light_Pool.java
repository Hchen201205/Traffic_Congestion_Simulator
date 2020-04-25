/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.awt.Color;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author chenhanxi
 */
public class Light_Pool implements Runnable{
    
    Light_Set[] lightpool;
    
    public Light_Pool(int numOfIntersection) {
        
        lightpool = new Light_Set[numOfIntersection];
        
        for (int i = 0; i < lightpool.length; i++) {
            // I'm using TCSConstant for now. We will change to have a specific file for it.
            lightpool[i] = new Light_Set(TCSConstant.lightposition[i][0], TCSConstant.lightposition[i][1]);
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                for (int i = 0; i < lightpool.length; i++) {
                    
                }
            }
        }
        
        
    }
}

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
public class Light_Set implements TCSConstant {


    double xvalue;
    double yvalue;
    Light[] light_set;

    static double time = 0;

    /**
     * Because each light in an intersection is following a certain light cycle,
     * they should have a time schedule according to each other Please change it
     * later on so that this class can cover all the lights (not only 1
     * intersection).
     *
     * @param x
     * @param y
     */
    public Light_Set(double x, double y) {
        xvalue = x;
        yvalue = y;
        
        light_set = new Light[4];
        
        for (int i = 0; i < light_set.length; i++) {
            if (i % 2 == 0) {
                light_set[i] = new Light(i, Color.GREEN);
            } else {
                light_set[i] = new Light(i, Color.RED);
            }
        }
        
        /*
        lightset = new Light[numOfIntersection][4];
        for (int i = 0; i < lightset.length; i++) {
            for (int k = 0; k < lightset[0].length; k++) {
                if (i % 2 == 0) {
                    lightset[k][i] = new Light(i, Color.GREEN);
                } else {
                    lightset[k][i] = new Light(i, Color.RED);
                }
                lightset[k][i].setChangeTimes(LIGHTCYCLER, LIGHTCYCLEG, LIGHTCYCLEY);

            }
            lightset[i].setChangeTimes(LIGHTCYCLER, LIGHTCYCLEG, LIGHTCYCLEY);
        }


*/   }
        
    public Light[] getLight_Set() {
        return light_set;
    }
   
    public void runCycleUnit() {
        for (Light light_set1 : light_set) {
            light_set1.runCycleUnit();
        }
    }
    /*
    public void runCycle() throws InterruptedException {
        while (true) {       
            for (int i = 0; i < lightset.length; i++) {
                for (int k = 0; k < lightset[0].length; k++) {
                    lightset[i][k].runCycleUnit();
                }
            }
            time += time + lightset[0][0].getTimeIncrement();// time increment
            // There should be some statement that controls the end of this cycle. 
            //MJ This is where you come in. actionListener will have to take care of this later.

        }
        // There should be some statement that control the end of this cycle. 
        //MJ This is where you come in. One of the actionListener will have to take care of this later.
    }
*/
}

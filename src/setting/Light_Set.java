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
    Light2[] lightset;

    static double time = 0;

    /**
     * Because each light in an intersection is following a certain light cycle,
     * they should have a time schedule according to each other Please change it
     * later on so that this class can cover all the light (not only 1
     * intersection).
     *
     * @param x
     * @param y
     */
    public Light_Set(double x, double y) {
        xvalue = x;
        yvalue = y;
        lightset = new Light2[4];

        for (int i = 0; i < lightset.length; i++) {
            if (i % 2 == 0) {
                lightset[i] = new Light2(i, Color.GREEN);
            } else {
                lightset[i] = new Light2(i, Color.RED);
            }
            lightset[i].setChangeTimes(LIGHTCYCLER, LIGHTCYCLEG, LIGHTCYCLEY);
        }

    }

    public void runCycleUnit() throws InterruptedException {
        for (int i = 0; i < lightset.length; i++) {
            lightset[i].runCycleUnit();
        }
        // There should be some statement that control the end of this cycle. 
        //MJ This is where you come in. One of the actionListener will have to take care of this later.
    }
}

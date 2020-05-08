/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.awt.Color;
import java.util.Arrays;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author Christine
 */
public class Light_Set2 implements TCSConstant {

    double[] position;
    Light2[] light_set;

    static double time = 0;

    /**
     *      Mimics a 6 lane by 6 lane intersection, with 3 lanes for each direction (left turn lane, middle through 
     * lane, and right turn lane) 
     *      The middle through lane and the right turn lane on either side of the intersection vertically run first, 
     * then left turn lanes on either side vertically, then the through and right lanes on either side horizontally, 
     * and finally, left turn lanes on either side horizontally
     *  
     *      Because each light in an intersection is following a certain light cycle, they have a time schedule 
     * according to each other, with left turn lanes given a shorter green light, given they cover only two lanes 
     * per light object rather than four.
     *
     * @param position
     */
    
    public Light_Set2(double[] position) {
        this.position = position;
        
        //each light covers two dircections
        //light_set[0] covers 4 lanes total: through lanes and right turn lanes, on either side vertically
        //light_set[1] covers 2 lanes: left turn lanes, on either side vertically
        //light_set[2] covers 4 lanes total: through lanes and right turn lanes, on either side horizontally
        //light_set[3] covers 2 lanes total: left turn lanes, on either sied horizontally
        light_set = new Light2[4];

        //assigning light starting colors
        light_set[0] = new Light2(Color.GREEN, false, false);
        light_set[1] = new Light2(Color.RED, false, true);
        light_set[2] = new Light2(Color.RED, true, false);
        light_set[3] = new Light2(Color.RED, true, true);

        //red light cycle time needs to calculated individually for each light
        double light_cyc_red;
        
        //  left_turn_dec determines how short the green light for left turn lane will be 
        //compared to the green light for through and right lanes
        //  set to 1/2 beacuse the left turn light only covers one lane from each side, 
        //while the through and right light covers two lanes from each side
        double left_turn_dec = 1.0 / 2.0;
        
        for (Light2 light : light_set) {
            if (light.isLeftTurnLight()) {
                //calculating and setting change times for left turn lights
                light_cyc_red = LIGHTCYCLEG * (2 + left_turn_dec)
                        + LIGHTCYCLEY * 3.0 + LIGHTCYCDEADTIME * 4.0;
                light.setChangeTimes(light_cyc_red, LIGHTCYCLEG * left_turn_dec, LIGHTCYCLEY);
                
            } else {
                //calculating and setting change times for through and right lights
                light_cyc_red = LIGHTCYCLEG * (1 + left_turn_dec * 2.0)
                        + LIGHTCYCLEY * 3.0 + LIGHTCYCDEADTIME * 4.0;
                light.setChangeTimes(light_cyc_red, LIGHTCYCLEG, LIGHTCYCLEY);
                
            }
        }
        
        light_set[0].setCycleTime(light_set[0].getChangeTimes()[0]);
        light_set[1].setCycleTime(light_set[0].getChangeTimes()[0] + LIGHTCYCLEG * (2 + left_turn_dec) 
                + 3.0 * (LIGHTCYCLEY + LIGHTCYCDEADTIME));
        light_set[2].setCycleTime(light_set[0].getChangeTimes()[0] + LIGHTCYCLEG * (1 + left_turn_dec) 
                + 2.0 * (LIGHTCYCLEY + LIGHTCYCDEADTIME));
        light_set[3].setCycleTime(light_set[0].getChangeTimes()[0] + LIGHTCYCLEG + LIGHTCYCLEY + LIGHTCYCDEADTIME);
        
    }

    //must be called before running cycle for the first time
    public void startLightSetCycle(){
        for (Light2 light_set1 : light_set) {
            light_set1.startCycle();
        }
    }
    
    public void endLightSetCycle(){
        for (Light2 light_set1 : light_set) {
            light_set1.endCycle();
        }
    }
    
    public Light2[] getLight_Set() {
        return light_set;
    }
    
    public void runCycleUnit() {
        for (Light2 light_set1 : light_set) {
            light_set1.runCycleUnit();
        }
    }

}

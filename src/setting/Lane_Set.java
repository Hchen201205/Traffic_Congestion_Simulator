/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import traffic_congestion_simulator.TCSConstant;


/**
 *
 * @author chenhanxi
 */
public class Lane_Set implements TCSConstant{
    
    Lane2[] lane_set;
    
    // Creates a lane.
    public Lane_Set(boolean automated, int[] x_value, int[] y_value, int[]length, int[] width, int[] direction, int numOfLane, Light_Set light_set) {
        lane_set = new Lane2[numOfLane];
        if (light_set != null) {
            for (int i = 0; i < lane_set.length; i++) {
                lane_set[i] = new Lane2(automated, x_value[i], y_value[i], length[i], width[i], direction[i], light_set.getLight_Set()[i]);
            }
        } else {
            for (int i = 0; i < lane_set.length; i++) {
                lane_set[i] = new Lane2(automated, x_value[i], y_value[i], length[i], width[i], direction[i], null);
            }
        }
    }
    
    //Runs that lane.
    public void runUnit() {
        for (Lane2 lane_set1 : lane_set) {
            lane_set1.runUnit();
        }
    }
}

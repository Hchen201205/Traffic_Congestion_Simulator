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
    
    Lane[] lane_set;
    
    // Creates a lane.
    public Lane_Set(boolean automated, double[][] position_set, double[][]size_set, double[] direction, int numOfLane, Light_Set light_set) {
        lane_set = new Lane[numOfLane];
            for (int i = 0; i < lane_set.length; i++) {
                lane_set[i] = new Lane(automated, position_set[i], size_set[i], direction[i], light_set.getLight_Set()[i]);
            }
    }
    
    //Runs that lane.
    public void runUnit() {
        for (Lane lane_set1 : lane_set) {
            lane_set1.runUnit();
        }
    }
}

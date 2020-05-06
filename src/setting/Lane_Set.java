/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.util.ArrayList;
import traffic_congestion_simulator.TCSConstant;
import vehicle.Vehicle;

/**
 *
 * @author chenhanxi
 */
public class Lane_Set implements TCSConstant {

    Lane[] lane_set;
    
    ArrayList<Vehicle> overflowList;

    // Creates a lane.
    public Lane_Set(double[][] position_set, double[][] size_set, double[] direction, int numOfLane, Light_Set light_set) {
        lane_set = new Lane[numOfLane];
        for (int i = 0; i < lane_set.length; i++) {
            lane_set[i] = new Lane(position_set[i], size_set[i], direction[i], light_set.getLight_Set()[i]);
        }
        overflowList = new ArrayList<>();
    }

    //Runs that lane.
    public void runUnit() {
        for (Lane lane : lane_set) {
            lane.runUnit();
        }
        // Intersection will be taken care in the Lane_Pool class.
    }
    
    public int size() {
        return lane_set.length;
    }
    
    public Lane getLane(int i) {
        return lane_set[i];
    }
}

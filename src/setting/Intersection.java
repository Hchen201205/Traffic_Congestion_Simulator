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
//Creates a intersection situation.
public class Intersection {
    Lane[] lane_set;
    double[] position;
    double[] size;
    ArrayList<Vehicle> carList;
    public Intersection(Lane[] lane_set, double[] position, double[] size) {
        this.lane_set = lane_set;
        this.position = position;
        this.size = size;
        this.carList = new ArrayList<>();
    }
    
//Update each car's acceleration unit.
    public void runCycleUnit() {
        for (int i = 0; i < carList.size(); i++) {
            carList.get(i).accelerate(TCSConstant.TIMEINCREMENTS, true);
        }
    }
    
    public void addCar(Vehicle vehicle) {
        carList.add(vehicle);
    }
    
}

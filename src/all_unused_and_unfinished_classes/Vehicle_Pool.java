/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package all_unused_and_unfinished_classes;

import java.util.ArrayList;
import vehicle.Vehicle;

/**
 *
 * @author chenhanxi
 */
public class Vehicle_Pool {
    
    ArrayList<Vehicle> inLane;
    ArrayList<Vehicle> outLane; // Intersection;

    public Vehicle_Pool() {
        inLane = new ArrayList<>();
        outLane = new ArrayList<>();
    }

    public void shift(boolean toInLane, Vehicle vehicle) {
        if (toInLane) {
            inLane.add(vehicle);
            outLane.remove(vehicle);
        } else {
            outLane.add(vehicle);
            inLane.remove(vehicle);
        }
    }
    
    public void shift(boolean toInLane, ArrayList<Vehicle> vehicleList) {
        if (toInLane) {
            inLane.addAll(vehicleList);
            outLane.removeAll(vehicleList);
        } else {
            outLane.addAll(vehicleList);
            inLane.removeAll(vehicleList);
        }
    }
    
    public ArrayList<Vehicle> getInlane() {
        return inLane;
    }
    
    public ArrayList<Vehicle> getOutlane() {
        return outLane;
    }
}

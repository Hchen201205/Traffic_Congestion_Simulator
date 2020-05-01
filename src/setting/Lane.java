/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.util.ArrayList;
import vehicle.*;
import traffic_congestion_simulator.TCSConstant;

/**
 * One thing to remember in this lane class is that it needs to be queue instead
 * of stack
 *
 * @author chenhanxi
 */
public class Lane {

    ArrayList<Vehicle> carList;// a list of all the cars on the road.

    boolean automated; // Do I need this?

    int x_value;

    int y_value;

    int length;

    int width;

    int direction;
    
    protected final double buffer = 3;
    
    int line;

    // Testing point is a poitn which you can test whether a car is out of bound or not.
    // In our simulation, the only way a car can be out of bound is when it has run through this lane.
    int testingpoint;

    public Lane(boolean automated, int x_value, int y_value, int length, int width, int direction) {
        carList = new ArrayList<>();
        this.automated = automated;
        // Both x and y are defining the center position of the lane.
        this.x_value = x_value;
        this.y_value = y_value;
        this.length = length;
        this.width = width;
        this.direction = direction; //0 = 'n', 1 = 's', 2 = 'e' or 3 = 'w'
        switch (direction) {
            case 0:
                testingpoint = y_value - length / 2;
                break;
            case 1:
                testingpoint = y_value + length / 2;
                break;
            case 2:
                testingpoint = x_value + length / 2;
                break;
            case 3:
                testingpoint = x_value - length / 2;
                break;
        }
        // I need to fix the car class based on this. That the length will always be the length and the width will always be the width. It's the direction that dominate.
    }

    // This will report the lane status.
    public double checkLaneStatus() {
        double excessDistance = length;
        for (int i = 0; i < carList.size(); i++) {
            excessDistance -= carList.get(i).getSize()[0];
            if (i >= 1) {
                excessDistance -= carList.get(i).getSafetyDistance();
            }
        }
        return excessDistance;
    }

    // This will check whether the car can make it to the other end or not.
    // Using this method, you are assuming an excessDistance variable is passed along from the simulation class to this specific lane.
    // Kevin, for now, please assume that this class is completed. It will tell you how many spots there are left in the lane ahead of this one.
    public int checkSpotLeft(double excessDistance) {
        int spotLeft = 0;
        for (int i = 0; i < carList.size(); i++) {
        }
        return spotLeft;
    }
    
    public void addCar(Vehicle car) {
        carList.add(car);
    }

    public void addCar(int[] position, int[] size) {
        // I still have to add this part.
    }

    public void removeCar(int index) {
        carList.remove(index);
    }

    // This will run for one millisecond and update carList
    public void run() {
        for (int i = 0; i < carList.size(); i++) {
            carList.get(i).accelerateUnit(true, false);
        }
        updateCarList();
        // You also need to think how you are going to transport these cars into other lanes if any.
        // It's time to introduce a car pool class...
    }

    /**
     * Slow is the method for yellow light
     * Kevin.Please have some thoughts on that. We can discuss together but I need you to at least start it.
 Essentially, you will be using some of the methods that I've already set up in this class. Please choose them wisely.
     * @param spot_left 
     * @param length 
     */
    public void lineCheck(int spot_left, int length){
        if(length > line){
            for (int i = 0; i < carList.size(); i++) {
            carList.get(i).accelerateUnit(false, true);
            }
        }
        else {
            for (int i = 0; i < spot_left; i++) {
            
        }
        for (int i = 0; i < carList.size(); i++) {
            carList.get(i).accelerateUnit(false, true);
            
            }
        }
    }
    
    public void slow(int spot_left) {
        //int increment;
        for (int i = 0; i < spot_left; i++) {
            
        }
        for (int i = 0; i < carList.size(); i++) {
            carList.get(i).accelerateUnit(false, true);
            
        }
    }
    public void updateCarList() {
        for (int i = 0; i < carList.size(); i++) {
            switch (direction) {
                case 0:
                    if (carList.get(i).getPosition()[1] <= testingpoint) {
                        carList.remove(i);
                    }
                    break;
                case 1:
                    if (carList.get(i).getPosition()[1] >= testingpoint) {
                        carList.remove(i);
                    }
                    break;
                case 2:
                    if (carList.get(i).getPosition()[0] >= testingpoint) {
                        carList.remove(i);
                    }
                    break;
                case 3:
                    if (carList.get(i).getPosition()[0] <= testingpoint) {
                        carList.remove(i);
                    }
                    break;
            }
        }
    }
    
    // You need to first measure the distance and then decide whether you want to move forward or back up or neither.
    public void redStop(){
        for (int i = 0; i < carList.size(); i++) {
            carList.get(i).accelerateUnit(true, false);
        }
        updateCarList();
    }
    
    // This is a bug. Please fix.
    public void distanceAdjust(){
        for (int i = 0; i < buffer; i++) {
            carList.get(i).accelerateUnit(true, false);
        }
    }

}
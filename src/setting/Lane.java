/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.awt.Color;
import java.util.ArrayList;
import vehicle.*;
import traffic_congestion_simulator.TCSConstant;
import static traffic_congestion_simulator.TCSConstant.ROUNDEDDECPOS;

/**
 * One thing to remember in this lane class is that it needs to be queue instead
 * of stack
 *
 * @author chenhanxi
 */
public class Lane {

    ArrayList<Vehicle> carList;// a list of all the cars on the road.

    double[] position; // 0 is x, 1 is y

    double[] size; // 0 is length, 1 is width

    double direction; //  angle

    Light light;

    boolean overflow;

    ArrayList<Vehicle> overflowVehicles;

    public Lane(double[] position, double[] size, double direction, Light light) {
        carList = new ArrayList<>();
        // Both x and y are defining the center position of the lane.
        this.position = position;
        this.size = size;
        this.direction = direction; // angle

        // I need to fix the car class based on this. That the length will always be the length and the width will always be the width. It's the direction that dominate.
        this.light = light;

        overflow = false;

        overflowVehicles = new ArrayList<>();
    }

    public double rounder(double num) {
        num = num * Math.pow(10, ROUNDEDDECPOS);
        num = Math.round(num);
        return num / Math.pow(10, ROUNDEDDECPOS);
    }

    /**
     * This function will run this lane for one unit time.
     */
    public void runUnit() {
        if (light == null || light.getColor().equals(Color.GREEN)) {
            green();
        } else if (light.getColor().equals(Color.RED)) {
            // Use Red Procedure.
        } else {
            // Use Yellow PRocedure.
        }
    }

    // This will report the lane status.
    // It's highly recommend that this method don't use with checkSpotLeft at the same time because the target these methods are serving for are not the same.
    public double checkLaneStatus() {
        double excessDistance = size[0];
        for (int i = 0; i < carList.size(); i++) {
            excessDistance -= carList.get(i).getSize()[0];
            if (i >= 1) {
                excessDistance -= carList.get(i).getBuffer();
            }
        }
        return excessDistance;
    }

    // This will check whether the car can make it to the other end or not.
    // Using this method, you are assuming an excessDistance variable is passed along from the simulation class to this specific lane.
    // Kevin, This class is completed and you can use it. It will tell you how many spots there are left in the lane ahead of this one.
    public int checkSpotLeft(Lane2 lane2, double time) {
        double excessDistance = lane2.checkLaneStatus();
        int spotLeft = 0;
        for (int i = 0; i < carList.size(); i++) {
            Vehicle car = carList.get(i);
            excessDistance -= car.getBuffer() + car.getSize()[0]; // Buffer + length;
            if (excessDistance < 0) {
                break;

                // Here I need to check whether the car can make it...
            }
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
    public void green() {
        for (int i = 0; i < carList.size(); i++) {
            if (carList.get(i).getReactionTime() == 0) {
                carList.get(i).accelerate(TCSConstant.TIMEINCREMENTS, true);
            } else {
                carList.get(i).reduceReactionTimeUnit();
                break;
            }

        }
        updateCarList();
    }

    /**
     * Slow is the method for yellow light Kevin. Please have some thoughts on
     * that. We can discuss together but I need you to at least start it.
     * Essentially, you will be using some of the methods that I've already set
     * up in this class. Please choose them wisely.
     *
     * @param spot_left
     */
    public void yellow(double excessDistance, Lane2 lane2) {
        int spotLeft = checkSpotLeft(lane2, excessDistance);
        for (int i = 0; i < carList.size(); i++) {
            Vehicle v = carList.get(i);
            if ((v.getAutomated() && spotLeft > 0 && estimate distance) || !v.getAutomated()) {
                v.accelerate(TCSConstant.TIMEINCREMENTS, true);
            } else {
                v.accelerate(TCSConstant.TIMEINCREMENTS, false);
            }
        }
    }

    public void red() {
        carList.get(i).
        for (int i = 1; i < carList.size(); i++) {
            
        }
    }
    
    public void updateCarList() {
        for (int i = 0; i < carList.size(); i++) {
            double[] frontPos = carList.get(i).getCarFrontPos();
            // Distance formula
            double distance = Math.sqrt(Math.pow(frontPos[0] - position[0], 2) + Math.pow(frontPos[1] - position[1], 2));
            if (distance > (1 / 2 * size[0])) {

                // Remember to check this tomorrow.
                overflowVehicles.add(carList.get(i));
                carList.remove(i);
                overflow = true;
            } else {
                break;
            }
        }
    }

    public boolean haveLight() {
        return light != null;
    }

    public boolean getOverFlow() {
        return overflow;
    }

    public ArrayList<Vehicle> getOverFlowList() {
        return overflowVehicles;
    }

    public void removeOverFlow() {
        overflow = false;
        overflowVehicles.clear();
    }

    public double getDirection() {
        return direction;
    }

    public double[][] getPoints() {
        double[][] points = new double[4][2];
        double v1 = direction;
        for (int i = 0; i < 2; i++) {
            double v2 = v1 + 90;
            for (int j = 0; j < 2; j++) {
                points[i + j][0] = position[0] + 1 / 2 * size[0] * Math.cos(Math.toRadians(v1)) + 1 / 2 * size[1] * Math.cos(Math.toRadians(v2));
                points[i + j][1] = position[1] + 1 / 2 * size[0] * Math.sin(Math.toRadians(v1)) + 1 / 2 * size[1] * Math.sin(Math.toRadians(v2));
                v2 -= 180;

            }
            v1 -= 180;
        }
        return points;
    }
}

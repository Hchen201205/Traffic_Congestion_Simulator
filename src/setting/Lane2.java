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

/**
 * One thing to remember in this lane class is that it needs to be queue instead
 * of stack
 *
 * @author chenhanxi
 */
public class Lane2 {

    ArrayList<Vehicle2> carList;// a list of all the cars on the road.

    boolean automated; // Do I need this?

    int x_value;

    int y_value;

    int length;

    int width;

    int direction;

    Light light;

    // Testing point is a poitn which you can test whether a car is out of bound or not.
    // In our simulation, the only way a car can be out of bound is when it has run through this lane.
    int testingpoint;

    public Lane2(boolean automated, int x_value, int y_value, int length, int width, int direction, Light light) {
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
        this.light = light;
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
        double excessDistance = length;
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
            Vehicle2 car = carList.get(i);
            excessDistance -= car.getBuffer() + car.getSize()[0]; // Buffer + length;
            if (excessDistance < 0) {
                break;

                // Here I need to check whether the car can make it...
            }
        }
        return spotLeft;
    }

    public void addCar(Vehicle2 car) {
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
        // You also need to think how you are going to transport these cars into other lanes if any.
        // It's time to introduce a car pool class...
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
        if (automated) {
            int spotLeft = checkSpotLeft(lane2, excessDistance);
            for (int i = 0; i < spotLeft; i++) {

            }
            for (int i = 0; i < carList.size(); i++) {
                // wait for implementation

            }
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

}

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
public class Lane2 {

    ArrayList<Vehicle> carList;// a list of all the cars on the road.

    boolean automated; // Do I need this?

    boolean occupied;

    int x_value;

    int y_value;

    int length;

    int width;

    int direction;

    int testingpoint;

    public Lane2(boolean automated, int x_value, int y_value, int length, int width, int direction) {
        carList = new ArrayList<>();
        this.automated = automated;
        occupied = false;
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
    }

    public boolean checkLaneStatus() {
        return occupied;
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

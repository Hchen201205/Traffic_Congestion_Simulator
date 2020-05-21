/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import vehicle.*;
import static traffic_congestion_simulator.TCSConstant.ROUNDEDDECPOS;

/**
 * Lane is a class that will: 1. Run the Vehicle objects on a lane based on the
 * Light object on this lane. This class can: 1. Run all Vehicle objects by one
 * increment 2. Place or remove Vehicle when specific method is called.
 *
 * @author chenhanxi
 */
public class Lane {

    ArrayList<Vehicle> carList; // A list of all the cars on the road.

    double[] position; // The position of a Lane object, position[0] will be its x-value and position[1] will be its y-value

    double[] frontPos; // The front position of a Lane object, which is located on the middle of its width but on the top of its length same with its direction.

    double[] size; // The size of a Lane object, size[0] will be its length and size[1] will be its width

    double direction; // The direction, in degree, of a Lane object.

    Light light; // a Light object that direct the Vehicle on a Lane.

    //boolean overflow; // A indicator that will be used when multiple Lane_Set are declared. Not supported
    //ArrayList<Vehicle> overflowVehicles; // An array of Vehicle that move out from the Lane. Supposedly work with overflow. Not supported.
    char reset; // reset will help the Lane when Light is changed. It will prevent Lane class to repeatly implement some one-time operation. R = Red; G = Green; Y = Yellow;

    public Lane(double[] position, double[] size, double direction, Light light) {
        carList = new ArrayList<>();
        // The position, size, and direction shouldn't be changing.
        this.position = position;
        this.size = size;
        this.direction = direction;

        this.light = light;

        //overflow = false;
        //overflowVehicles = new ArrayList<>();
        //The car's dicretion is given in degrees, so the cars are able to turn.
        frontPos = new double[2];
        // The orientation of the map is x is bigger when going right, and y is bigger when going down.
        frontPos[0] = position[0] + 1 / 2.0 * size[0] * rounder(Math.cos(Math.toRadians(direction)));
        frontPos[1] = position[1] - 1 / 2.0 * size[0] * rounder(Math.sin(Math.toRadians(direction)));

        // Determine the initial light.
        if (light.getColor().equals(Color.RED)) {
            reset = 'R';
        } else if (light.getColor().equals(Color.GREEN)) {
            reset = 'G';
        } else {
            reset = 'Y';
        }
    }

    /**
     * Rounder is a method that is the same across all classes. It will ensure
     * the value is in ROUNDEDDECPOS decimal places.
     *
     * @param num
     * @return
     */
    public double rounder(double num) {
        num = num * Math.pow(10, ROUNDEDDECPOS);
        num = Math.round(num);
        return num / Math.pow(10, ROUNDEDDECPOS);
    }

    /**
     * runUnit will run this lane for one unit time. It will hear from the Light
     * object about the color, and will: 1. accelerate the Vehicle objects when
     * the light is green or when there is no light (output Lane). 2. stop and
     * reset Vehicle objects when the light is either yellow or red.
     */
    public void runUnit() {
        if (light == null || light.getColor().equals(Color.GREEN)) {
            if (reset != 'G') {
                reset = 'G';
            }
            green();
        } else if (light.getColor().equals(Color.RED)) {
            // Run the methods that only need to run once. Like setting Vehicles to their position.
            if (reset != 'R') {
                reset = 'R';
                setCars();
                // whenever there is a red light, a new round of reaction time will be generated.
                for (int i = 0; i < carList.size(); i++) {
                    carList.get(i).genRandReactionTime();
                }
            }
        } else {
            if (reset != 'Y') {
                reset = 'Y';
                for (Vehicle v : carList) {
                    if (v.getSpeed()[0] != 0 || v.getSpeed()[1] != 0) {
                        v.setSpeed(new double[2]);
                    }
                }

            }

            /* Yellow Light Attempt
            if (reset != 'Y') {
                reset = 'Y';
                // Do something here.
                double[] stackPos = frontPos.clone();
                for (int i = 0; i < carList.size(); i++) {
                    Vehicle c = carList.get(i);
                    c.genRandReactionTime();
                    if (((i > 0 && !carList.get(i - 1).isAccelerating()) || i == 0) && c.getDecelerateToStopRate(frontPos) <= c.getDeceleration_rate() * 2) {
                        c.setAccelerating(false);
                        c.setDestination(stackPos);
                    }
                }
            }
            yellow();
             */// This was my attempt to get the yellow light. Not working.
        }
        updateCarList();
    }

    /**
     * addCar is a method that will take in a boolean automated variable and the
     * number of cars. It will then create the Vehicle objects, add them into
     * carList, and reposition them.
     *
     * @param automated
     * @param numOfCar
     * @return
     */
    public int addCar(boolean automated, int numOfCar) {
        // stackPos is a xy value position that will keep update when a new Vehicle is added.
        // It will start from the front position of the Lane and add the buffer and length of each car on that direction.
        double[] stackPos = frontPos.clone();
        for (int i = 0; i < carList.size(); i++) {
            if (i > 0) {
                stackPos[0] = stackPos[0] - (rounder(Math.cos(Math.toRadians(direction)) * (carList.get(i).getBuffer())));
                stackPos[1] = stackPos[1] + (rounder(Math.sin(Math.toRadians(direction)) * (carList.get(i).getBuffer())));
            }
            stackPos[0] = stackPos[0] - (rounder(Math.cos(Math.toRadians(direction)) * (carList.get(i).getSize()[0])));
            stackPos[1] = stackPos[1] + (rounder(Math.sin(Math.toRadians(direction)) * (carList.get(i).getSize()[0])));
        }

        // It will count the Vehicle that is added and return it.
        int newAdd = 0;
        Vehicle v;
        if (automated) {
            v = new AutomatedCar(stackPos, direction);

        } else {
            v = new NormalCar(stackPos, direction);
        }
        stackPos[0] = stackPos[0] - (rounder(Math.cos(Math.toRadians(direction)) * (v.getBuffer())));
        stackPos[1] = stackPos[1] + (rounder(Math.sin(Math.toRadians(direction)) * (v.getBuffer())));
        double distance = Math.sqrt(Math.pow(stackPos[0] - position[0], 2) + Math.pow(stackPos[1] - position[1], 2));
        // Whenever the scakPos is out of the radius of the lane, it will stop adding Vehicle.
        while (distance <= (1 / 2.0 * size[0]) && newAdd < numOfCar) {
            carList.add(v);
            newAdd++;
            stackPos[0] = stackPos[0] - (rounder(Math.cos(Math.toRadians(direction)) * (v.getSize()[0])));
            stackPos[1] = stackPos[1] + (rounder(Math.sin(Math.toRadians(direction)) * (v.getSize()[0])));
            if (automated) {
                v = new AutomatedCar(stackPos, direction);

            } else {
                v = new NormalCar(stackPos, direction);
            }
            stackPos[0] = stackPos[0] - (rounder(Math.cos(Math.toRadians(direction)) * (v.getBuffer())));
            stackPos[1] = stackPos[1] + (rounder(Math.sin(Math.toRadians(direction)) * (v.getBuffer())));
            distance = Math.sqrt(Math.pow(stackPos[0] - position[0], 2) + Math.pow(stackPos[1] - position[1], 2));
        }
        setCars();
        return newAdd;
    }

    /**
     * removeCar will take in an int parameter index. It will remove the car on
     * that index.
     *
     * @param index
     */
    public void removeCar(int index) {
        carList.remove(index);
    }

    /**
     * getColor() is an accessor that will get the color of the Light object.
     *
     * @return
     */
    public Color getColor() {
        return light.getColor();
    }

    /**
     * green is a method that will be responsible for Vehicle's movement for one
     * increment of unit second when a green light is reached.
     */
    public void green() {
        for (int i = 0; i < carList.size(); i++) {
            Vehicle c = carList.get(i);
            if (c.getReactionTime() <= 0) {
                // Here have an if-else statement to account for the time when two cars are too close.
                if (i > 0 && !c.distanceCheck(carList.get(i - 1))) {
                    c.accelerate(false);
                } else if (Math.sqrt(Math.pow(c.getSpeed()[0], 2) + Math.pow(c.getSpeed()[1], 2)) < c.getSpeedLimit()) {
                    c.accelerate(true);

                } else {
                    c.travelWithConstantSpeed();
                }
                // if the reactiontime of a Vehicle hasn't reach, the run will be ended.
            } else {
                c.reduceReactionTimeUnit();
                break;
            }

        }
    }

    /**
     * setCars will reset all the Vehicle objects to appropriate position on the
     * Lane. There needs to be a buffer region in between two Vehicles.
     */
    public void setCars() {

        if (carList.size() > 0) {
            carList.get(0).setPosition(frontPos);
            double[] destination = new double[2];
            for (int i = 1; i < carList.size(); i++) {
                destination[0] = carList.get(i - 1).getPosition()[0] - (rounder(Math.cos(Math.toRadians(direction)) * (carList.get(i - 1).getSize()[0] + carList.get(i).getBuffer())));

                destination[1] = carList.get(i - 1).getPosition()[1] + (rounder(Math.sin(Math.toRadians(direction)) * (carList.get(i - 1).getSize()[0] + carList.get(i).getBuffer())));

                carList.get(i).setPosition(destination);
            }
        }
    }

    /**
     * updateCarList will check whether a Vehicle is in the radius of the Lane.
     * If it is not in, the method will remove the Vehicle from its carList.
     */
    public void updateCarList() {
        for (int i = 0; i < carList.size(); i++) {
            // Distance formula
            double distance = Math.sqrt(Math.pow(carList.get(i).getPosition()[0] - position[0], 2) + Math.pow(carList.get(i).getPosition()[1] - position[1], 2));
            if (distance > (1 / 2.0 * size[0])) {
                //overflowVehicles.add(carList.get(i)); Not supported
                carList.remove(i--);

                //overflow = true; Not Supported
            } else {
                break;
            }
        }
    }

    /**
     * Unrelated to this simulation haveLight will check whether the Lane has a
     * light
     *
     * @return
     */
    public boolean haveLight() {
        return light != null;
    }

    /**
     * getDirection is an accessor method that will return the direction of this
     * Lane.
     *
     * @return
     */
    public double getDirection() {
        return direction;
    }

    /**
     * Unrelated to this simulation getCarPos is a method for testing that will
     * print out all the Vehicle's current position
     *
     * @return
     */
    public String getCarPos() {
        String result = "";
        for (int i = 0; i < carList.size(); i++) {
            result += "|" + Arrays.toString(carList.get(i).getPosition()) + "|";
        }
        return result;
    }

    /**
     * Unrelated to this simulation getCarPos is a method for testing that will
     * print out all the Vehicle's current speed.
     *
     * @return
     */
    public String getCarSpeed() {
        String result = "";
        for (int i = 0; i < carList.size(); i++) {
            result += "|" + Arrays.toString(carList.get(i).getSpeed()) + "|";
        }
        return result;
    }

    public String getCarReaction() {
        String result = "";
        for (int i = 0; i < carList.size(); i++) {
            result += "|" + carList.get(i).getReactionTime() + "|";
        }
        return result;
    }
    
    /**
     * Unrelated to this simulation getPosition is an accessor method that will
     * return the position of this Lane
     *
     * @return
     */
    public double[] getPosition() {
        return position;
    }

    /**
     * getCarListSizes is an accessor method that will return the size of the
     * carList.
     *
     * @return
     */
    public int getCarListSizes() {
        return carList.size();
    }

    /**
     * Unrelated to this simulation. getSize is an accessor method that will
     * return the size of this Lane
     *
     * @return
     */
    public double[] getSize() {
        return size;
    }

    /* Not supported.
    yellow is a method that will direct the Vehicles to either slow down or speed up when a yellow light is on.
    public void yellow() {
        double[] stackPos = frontPos.clone();
        for (int i = 0; i < carList.size(); i++) {
            if (i > 0) {
                stackPos[0] = stackPos[0] - (rounder(Math.cos(Math.toRadians(direction)) * (carList.get(i - 1).getSize()[0] + carList.get(i).getBuffer())));
                stackPos[1] = stackPos[1] + (rounder(Math.sin(Math.toRadians(direction)) * (carList.get(i - 1).getSize()[0] + carList.get(i).getBuffer())));
            }

        }
    }
     */
 /* Not supported
    checkLaneStatus(), supposedly, will report the excess space of this Lane object.
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
     */
 /* Not supported.
    It will output how many spots there are left in the chosen Lane object.
    It will help the automatedCar to decide whether it should move forward or stop when a yellow light is on.
    public static int checkSpotLeft(Lane lane, double time) {
        double excessDistance = lane.checkLaneStatus();
        int spotLeft = 0;
        for (int i = 0; i < carList.size(); i++) {
            Vehicle car = carList.get(i);
            excessDistance -= car.getBuffer() + car.getSize()[0]; // Buffer + length;
            if (excessDistance < 0) {
                break;

            }
        }
        return spotLeft;
    }
     */
 /* Not supported
    These methods will help the Lane_Set to get cars that are moving out of the Lane
    and transport them into the next Lane.
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
     */
 /* Not supported
    Suposedly, red will help the Vehicle to adjust their relative position.
    public void red() {
        double[] destination = new double[2];
        // Wait for position to be switched.
        destination[0] = frontPos[0] - (carList.get(1).getSize()[0] * rounder(Math.abs(Math.cos(Math.toRadians(direction)))));
        destination[1] = frontPos[1] - (carList.get(1).getSize()[1] * rounder(Math.abs(Math.cos(Math.toRadians(direction)))));
        carList.get(1).setPosition(destination);

        for (int i = 1; i < carList.size(); i++) {
            destination[0] = carList.get(i - 1).getPosition()[0] - (rounder(Math.abs(Math.cos(Math.toRadians(direction))) * (1 / 2 * carList.get(i - 1).getSize()[0] - carList.get(i).getSafetyDistance() - 1 / 2 * carList.get(i).getSize()[0])));
            destination[1] = carList.get(i - 1).getPosition()[1] - (rounder(Math.abs(Math.cos(Math.toRadians(direction))) * (1 / 2 * carList.get(i - 1).getSize()[1] - carList.get(i).getSafetyDistance() - 1 / 2 * carList.get(i).getSize()[1])));
            if (carList.get(i).isAutomated()) {
                carList.get(i).setPosition(position);
            } else {
                // This is where the safety distance + random comes in.
            }
        }
    }
     */
}

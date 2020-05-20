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

    double[] frontPos;

    double[] size; // 0 is length, 1 is width

    double direction; //  angle

    Light light;

    boolean overflow;

    ArrayList<Vehicle> overflowVehicles;

    char reset; // R = Red; G = Green; Y = Yellow;

    int capacity;

    public Lane(double[] position, double[] size, double direction, Light light) {
        carList = new ArrayList<>();
        // Both x and y are defining the center position of the lane.
        this.position = position;
        this.size = size;
        this.direction = direction; // angle

        this.light = light;

        overflow = false;

        overflowVehicles = new ArrayList<>();
        
        //The car's dicretion is given in degrees, so the cars are able to turn.
        frontPos = new double[2];
        frontPos[0] = position[0] + 1 / 2.0 * size[0] * rounder(Math.cos(Math.toRadians(direction)));
        frontPos[1] = position[1] - 1 / 2.0 * size[0] * rounder(Math.sin(Math.toRadians(direction)));
        if (light.getColor().equals(Color.RED)) {
            reset = 'R';
        } else if (light.getColor().equals(Color.GREEN)) {
            reset = 'G';
        } else {
            reset = 'Y';
        }
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
            if (reset != 'G') {
                reset = 'G';
            }
            green();
        } else if (light.getColor().equals(Color.RED)) {
            if (reset != 'R') {
                reset = 'R';
                setCars();
            }
        } else {
            if (reset != 'Y') {
                reset = 'Y';
                yellow();
                for (Vehicle v : carList) {
                    if (v.getSpeed()[0] != 0 || v.getSpeed()[1] != 0) {
                        v.setSpeed(new double[2]);
                    }
                }

            }

            /*
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

    // This will report the lane status.
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

    // It will output how many spots there are left in the lane ahead of the current one.
    public int checkSpotLeft(Lane lane, double time) {
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

    public void addCar(Vehicle car) {
        carList.add(car);
        setCars();
    }

    // Done
    public int addCar(boolean automated, int numOfCar) {
        double[] stackPos = frontPos.clone();
        for (int i = 0; i < carList.size(); i++) {
            if (i > 0) {
                stackPos[0] = stackPos[0] - (rounder(Math.cos(Math.toRadians(direction)) * (carList.get(i).getBuffer())));
                stackPos[1] = stackPos[1] + (rounder(Math.sin(Math.toRadians(direction)) * (carList.get(i).getBuffer())));
            }
            stackPos[0] = stackPos[0] - (rounder(Math.cos(Math.toRadians(direction)) * (carList.get(i).getSize()[0])));
            stackPos[1] = stackPos[1] + (rounder(Math.sin(Math.toRadians(direction)) * (carList.get(i).getSize()[0])));
        }
        
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

    public void removeCar(int index) {
        carList.remove(index);
    }

    public Color getColor() {
        return light.getColor();
    }
    
    // This will run for one millisecond and update carList
    public void green() {
        for (int i = 0; i < carList.size(); i++) {
            Vehicle c = carList.get(i);
            if (c.getReactionTime() <= 0) {
                // Here have an if-else statement to account for the time when two cars are too close.
                if (i > 0 && !c.distanceCheck(carList.get(i - 1))) {
                    // two reaction time will happen here.
                    c.accelerate(false);
                } else if (Math.sqrt(Math.pow(c.getSpeed()[0], 2) + Math.pow(c.getSpeed()[1], 2)) < c.getSpeedLimit()) {
                    c.accelerate(true);

                } else {
                    c.travelWithConstantSpeed();
                }
            } else {
                c.reduceReactionTimeUnit();
                break;
            }

        }
    }

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

    // This method will take care of setting up the lane.
    // Debugged
    public void setCars() {

        if (carList.size() > 0) {
            carList.get(0).setPosition(frontPos);
            double[] destination = new double[2];
            for (int i = 1; i < carList.size(); i++) {
                destination[0] = carList.get(i - 1).getPosition()[0] - (rounder(Math.cos(Math.toRadians(direction)) * (carList.get(i - 1).getSize()[0] + carList.get(i).getBuffer())));
                // I change this one to + instead of minus because of our system.

                destination[1] = carList.get(i - 1).getPosition()[1] + (rounder(Math.sin(Math.toRadians(direction)) * (carList.get(i - 1).getSize()[0] + carList.get(i).getBuffer())));

                carList.get(i).setPosition(destination);
            }
        }
    }

    public void setCars2() {
        double[] carpos = {1000, 1000};
        carList.get(0).setPosition(carpos);
        /*
        if (carList.size() > 0) {
            double[] carPos = frontPos.clone();
            carList.get(0).setPosition(carPos);
        }
         */
    }

    public void setCarsSpecial() {
        if (carList.size() > 0) {
            carList.get(0).setPosition(frontPos);
            double[] destination = new double[2];
            for (int i = 1; i < carList.size(); i++) {
                destination[0] = carList.get(i - 1).getPosition()[0] - (rounder(Math.cos(Math.toRadians(direction)) * (carList.get(i - 1).getSize()[0] + carList.get(i).getBuffer())));

                destination[1] = carList.get(i - 1).getPosition()[1] + (rounder(Math.sin(Math.toRadians(direction)) * (carList.get(i - 1).getSize()[0] + carList.get(i).getBuffer())));

                carList.get(i).setPosition(destination);
            }
            for (int i = 0; i < carList.size(); i++) {
                double[] tempPos = carList.get(i).getCenterPos().clone();
                tempPos[0] = tempPos[0] - (rounder(Math.cos(Math.toRadians(direction)) * carList.get(i).getSize()[0] * 1 / 2));
                tempPos[1] = tempPos[1] + (rounder(Math.sin(Math.toRadians(direction)) * carList.get(i).getSize()[0] * 1 / 2));
                carList.get(i).setPosition(tempPos);
            }
        }
    }

    public void updateCarList() {
        for (int i = 0; i < carList.size(); i++) {
            // Distance formula
            double distance = Math.sqrt(Math.pow(carList.get(i).getPosition()[0] - position[0], 2) + Math.pow(carList.get(i).getPosition()[1] - position[1], 2));
            if (distance > (1 / 2.0 * size[0])) {
                overflowVehicles.add(carList.get(i));
                carList.remove(i--);

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

    public String getCarPos() {
        String result = "";
        for (int i = 0; i < carList.size(); i++) {
            result += "|" + Arrays.toString(carList.get(i).getPosition()) + "|";
        }
        return result;
    }

    public String getCarSpeed() {
        String result = "";
        for (int i = 0; i < carList.size(); i++) {
            result += "|" + Arrays.toString(carList.get(i).getSpeed()) + "|";
        }
        return result;
    }
    
    public double[] getPosition() {

        return position;
    }

    public int getCarListSizes() {
        return carList.size();
    }
    
    public double[] getSize() {
        return size;
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

    public void yellow() {
        double[] stackPos = frontPos.clone();
        for (int i = 0; i < carList.size(); i++) {
            if (i > 0) {
                stackPos[0] = stackPos[0] - (rounder(Math.cos(Math.toRadians(direction)) * (carList.get(i - 1).getSize()[0] + carList.get(i).getBuffer())));
                stackPos[1] = stackPos[1] + (rounder(Math.sin(Math.toRadians(direction)) * (carList.get(i - 1).getSize()[0] + carList.get(i).getBuffer())));
            }

            // I probably will need this line.
            //System.out.print(carList.get(i).getDeceleration_rate(stackPos));
        }
    }
     
    public static void main(String[] args) {

        double[] position = {135, 0};
        double[] size = {270, 10};

        double direction = 270;
        System.out.println("hi");

        double[] carpos = {10, 5};

        Light l = new Light(Color.GREEN, true, false);
        l.setChangeTimes(53, 10, 3);
        l.setCycleTime(53);

        Lane lane = new Lane(position, size, direction, l);

        lane.addCar(true, 6);
        System.out.println(lane.getCarPos());
        /*
        for (int i = 0; i < 30; i++) {
            Vehicle c = new NormalCar(carpos, direction);
            carpos[0]--;
            lane.addCar(c);
            System.out.println(Arrays.toString(c.getPosition()));
        }
         */
        //lane.setCars();

        l.startCycle();
        for (int i = 0; i < 500; i++) {
            System.out.println(l.getColorString() + l.getTimePassed());
            //l.runCycleUnit();
            if (i % 2 == 0) {
                String output = "";

                for (Vehicle car : lane.carList) {

                    /*
                    output += String.format("%7f, %7f; %7f, %7f; %7f\t\t", car.rounder(car.getCenterPos()[0]),
                            car.rounder(car.getCenterPos()[1]), car.getSize()[0], car.getSize()[1], car.rounder(car.getDirection()));
                     */
                    output += String.format("Speed: %s Direction: %s Position: %s\t\t", Arrays.toString(car.getSpeed()), car.getDirection(), Arrays.toString(car.getPosition()));
                }
                System.out.println(output);

            }
            lane.green();
            //lane.runUnit();

            /*
            System.out.println("safety: " + c2.getSafetyDistance());
            System.out.println(c.getReactionTime() + " : " + c2.getReactionTime());
            System.out.println(Arrays.toString(c.getSpeed()) + " : " + Arrays.toString(c.getPosition()));
            System.out.println(Arrays.toString(c2.getSpeed()) + " : " + Arrays.toString(c2.getPosition()));
             */
        }

    }
}

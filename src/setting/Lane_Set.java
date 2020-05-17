/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
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
    public Lane_Set(double[][] position_set, double[][] size_set, double[] direction, int numOfLane, LightSet light_set, int[] lightseq) {

        lane_set = new Lane[numOfLane];
        for (int i = 0; i < lane_set.length; i++) {
            lane_set[i] = new Lane(position_set[i], size_set[i], direction[i / 3], light_set.getLight_Set()[lightseq[i]]);
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

    public int getSize() {
        return lane_set.length;
    }

    public Lane getLane(int i) {
        return lane_set[i];
    }
    
    public int getCars() {
        int numOfCars = 0;
        for (int i = 0; i < lane_set.length; i++) {
            numOfCars += lane_set[i].carList.size();
        }
        return numOfCars;
    }

    public static void main(String[] args) {
        double[][] size_set = new double[12][2];
        for (double[] size_set1 : size_set) {
            size_set1[0] = 270;
            size_set1[1] = 10;
        }
        double[][] position_set = {{275, 135}, {285, 135}, {295, 135},
        {465, 275}, {465, 285}, {465, 295},
        {325, 465}, {315, 465}, {305, 465},
        {135, 325}, {135, 315}, {135, 305}};

        double[] direction = {270, 180, 90, 0};

        int[] lightseq = {0, 0, 1, 2, 2, 3, 0, 0, 1, 2, 2, 3};

        int numOfLane = 12;

        double[] lightPos = {310, 310};

        LightSet lightset2 = new LightSet(lightPos);
        lightset2.startLightSetCycle();

        Lane_Set ls = new Lane_Set(position_set, size_set, direction, numOfLane, lightset2, lightseq);

        int testingCarNumber = 216;
        int runTime = 0;
        for (int i = 0; i < ls.getSize(); i++) {
            testingCarNumber -= ls.getLane(i).addCar(true, testingCarNumber);
            System.out.println(ls.getLane(i).getCarPos());
            System.out.printf("Lane %d ahs direction %f", i, ls.getLane(i).getDirection());
        }
        while (testingCarNumber > 0 || ls.getCars() > 0) {
            System.out.println(runTime + " : " + lightset2.toString());
            lightset2.runCycleUnit();
            ls.runUnit();
            runTime++;
            for (int i = 0; i < ls.getSize(); i++) {
                if (ls.getLane(i).getColor().equals(Color.RED)) {
                    testingCarNumber -= ls.getLane(i).addCar(true, testingCarNumber);
                }
                else if (ls.getLane(i).getColor().equals(Color.GREEN)) {
                    System.out.printf("Lane %d is Green now, it has %d car\n", i, ls.getLane(i).carList.size());
                    System.out.println(ls.getLane(i).getCarPos());
                    System.out.println(ls.getLane(i).getCarSpeed());
                    
                }
            }
            
        }

        System.out.println("TestingCarNumber is:" + testingCarNumber);
        System.out.println("Use time is:" + runTime);

    }
}

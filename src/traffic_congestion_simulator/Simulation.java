/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import setting.Lane_Set;
import setting.LightSet;
//hii
public class Simulation implements TCSConstant {

    Lane_Set laneset;

    LightSet lightset;

    int runtime;
    
    int[] testingCarNumbers;

    public Simulation() {

        lightset = new LightSet(LIGHTPOSITION);
        lightset.startLightSetCycle();

        laneset = new Lane_Set(LANEPOSITION, LANESIZE, LANEDIRECTION, LANEPOSITION.length, lightset, LIGHTSEQUENCE);
        
        testingCarNumbers = new int[LANEPOSITION.length];
        
        runtime = 0;
    }

    // This is for graph 2
    public int run(boolean automated, int[] carsPerLane) {
        // You have an array that will each account for its own cars.
        testingCarNumbers = carsPerLane.clone();
        runtime = 0;
        
        // This will add the cars in.
        for (int i = 0; i < laneset.getSize(); i++) {
            testingCarNumbers[i] = testingCarNumbers[i] - laneset.getLane(i).addCar(automated, testingCarNumbers[i]);
        }
        
        
        // THis will run the car.
        while (!checkFinished() || laneset.getCars() > 0) {
            lightset.runCycleUnit();
            laneset.runUnit();
            runtime++;
            for (int i = 0; i < laneset.getSize(); i++) {
                if (laneset.getLane(i).getColor().equals(Color.RED)) {
                    testingCarNumbers[i] -= laneset.getLane(i).addCar(true, testingCarNumbers[i]);
                } else if (laneset.getLane(i).getColor().equals(Color.GREEN)) {
                    
                    // You can delete this line if necessary.
                    //System.out.printf("Lane %d is Green now, it has %d car\n", laneset.getLane(i).getCarListSizes(), laneset.getCars());

                }
            }

        }

        return runtime;
    }
    
    private boolean checkFinished() {
        for (int i = 0; i < testingCarNumbers.length; i++) {
            if (testingCarNumbers[i] > 0) {
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
        Simulation s = new Simulation();
        
        // You need to input twelve numbers here.
        // In the first graph, You can try to make put any numbers, 
        //make sure you have each lane with equal number of cars EXCEPT those you want to be zero.
        // For the second graph, all the numbers have to be the same.
        
        
        /*int[] carsPerLane = {x, x, x, 
                            x, 50, 50, 
                             50, 50, 50, 
                             50, 50, 50};
*/
        //I'll change the numbers in the carsPerLane array ranging from 20 - 120 
        //using a nested loop
        //since the no.2 graph will be # car v. time
        double timeOutput = 0;
        for (int i = 20; i < 120; i++) {
            int [] carsPerLane = new int[12];//set number of lanes; creating new array as each loop finishes
            for (int j = 0; j < 12; j++) {
                carsPerLane[j] = i; //inputing number cars in each lane 
            }
            for (int z = 0; z < 100; z++) { 
                timeOutput = timeOutput + ((s.run(true, carsPerLane))* (TIMEINCREMENTS));
            }
            double timeOutputAverage = timeOutput/100;
            System.out.printf("# CarsPL: %d ; time: %f",i, timeOutputAverage);
        }
        //I'll be looping 100 times to get a more accurate number of the running time 
        /*
        for (int i = 0; i < 100; i++) {
            timeOutput = timeOutput + ((s.run(true, carsPerLane))* (TIMEINCREMENTS));
        }*/
        
        
        
        // This means that you input 54 cars to each of the twelve lanes.
        // This is like the no.2 graph I ask you to do. Just do this first.
        //System.out.println(s.run(true, carsPerLane));
    }
}

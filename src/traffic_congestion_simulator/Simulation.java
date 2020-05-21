/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;

import java.awt.Color;
import setting.Lane_Set;
import setting.LightSet;

/**
 * Simulation is a class that will create Lane_Set object. It will also be in
 * charge of running Lane_Set. It is in charge of returning data.
 *
 * @author chenhanxi
 */
public class Simulation implements TCSConstant {

    Lane_Set laneset;

    LightSet lightset;

    int runtime; // Count the runtime with one increment of unit second as a unit.

    int[] testingCarNumbers; // The number of cars that will be put in each Lane.

    public Simulation() {

        lightset = new LightSet(LIGHTPOSITION);
        lightset.startLightSetCycle();

        laneset = new Lane_Set(LANEPOSITION, LANESIZE, LANEDIRECTION, LANEPOSITION.length, lightset, LIGHTSEQUENCE);

        testingCarNumbers = new int[LANEPOSITION.length];

        runtime = 0;
    }

    /**
     * run is a method that will take in a boolean variable (automated) and an
     * array of integer for the number of Vehicle objects in each Lane.
     *
     * @param automated
     * @param carsPerLane
     * @return
     */
    public int run(boolean automated, int[] carsPerLane) {
        // You have an array that will each account for its own cars.
        testingCarNumbers = carsPerLane.clone();
        runtime = 0;

        // Vehicles are added.
        for (int i = 0; i < laneset.getSize(); i++) {
            testingCarNumbers[i] = testingCarNumbers[i] - laneset.getLane(i).addCar(automated, testingCarNumbers[i]);
        }

        // Whenever there are Vehicles not added or there are Vehicles still running.
        while (!checkFinished() || laneset.getCars() > 0) {
            lightset.runCycleUnit();
            laneset.runUnit();
            runtime++;
            for (int i = 0; i < laneset.getSize(); i++) {
                // Whenever the Light is red, new Vehicles are added until no testing car are available for that Lane.
                if (laneset.getLane(i).getColor().equals(Color.RED)) {
                    testingCarNumbers[i] -= laneset.getLane(i).addCar(automated, testingCarNumbers[i]);
                }
                /* Testing method
                else if (laneset.getLane(i).getColor().equals(Color.GREEN)) {
                    //System.out.printf("Lane %d is Green now, it has %d car\n", i, laneset.getLane(i).getCarListSizes());

                }*/
            }
        }
        return runtime;
    }

    /**
     * checkFinished is a method that will see if there is any more Vehicles needed to add.
     * @return 
     */
    private boolean checkFinished() {
        for (int i = 0; i < testingCarNumbers.length; i++) {
            if (testingCarNumbers[i] > 0) {
                return false;
            }
        }
        return true;
    }

    /* Testing method
    public static void main(String[] args) {
        Simulation s = new Simulation();
        int[] testingCarPerLane = {59, 668, 5,
                                   0, 3, 11,
                                   5, 662, 22,
                                   13, 2, 14};

        System.out.println(s.run(false, testingCarPerLane));
    }
*/
}

/*/*
/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;


import static traffic_congestion_simulator.TCSConstant.TIMEINCREMENTS;

/**
 * Traffic_Congestion_Simulator serves as the main of the program.
 *
 * @author MJ
 */
public class Traffic_Congestion_Simulator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Simulation s = new Simulation();

        // For testing, all the numbers have to be the same.
        //int[] carsPerLane = {30, 30, 30, 
        //30, 30, 30, 
        //30, 30, 30, 
        //30, 30, 30};
        //I'll change the numbers in the carsPerLane array ranging from 20 - 120 
        //using a nested loop
        //since the no.2 graph will be # car v. time
        //System.out.println((s.run(false, carsPerLane))*(TIMEINCREMENTS));
        boolean automated = false;
        double timeOutput = 0;
        for (int i = 20; i < 160; i = i + 10) {
            int[] carsPerLane = new int[12];//set number of lanes; creating new array as each loop finishes
            for (int j = 0; j < 12; j++) {
                carsPerLane[j] = i; //inputing number cars in each lane 
            }
            for (int z = 0; z < 10; z++) { //change this the run boolean to false if wanting normal car data
                timeOutput = timeOutput + ((s.run(automated, carsPerLane)) * (TIMEINCREMENTS));
            }
            double timeOutputAverage = timeOutput / 10;
            System.out.printf("%d   ;    %f\n", i, timeOutputAverage);

        }

    }

}

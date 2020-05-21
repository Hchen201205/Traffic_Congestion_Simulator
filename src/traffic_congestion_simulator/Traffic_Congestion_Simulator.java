/*/*
/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;

import setting.LightTesting;
import vehicle.VehicleTesting;

/**
 *
 * @author chenhanxi
 */
public class Traffic_Congestion_Simulator {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        //VehicleTesting test = new VehicleTesting();
        //test.graphDataTesting();
        //LightTesting test2 = new LightTesting()
        Simulation s = new Simulation();
        int[] carsPerLane = {20, 20, 20, 
                            20, 20, 20, 
                             20, 20, 20, 
                             20, 20, 20};
        
        /*int[] carsPerLane = {30, 30, 30, 
                            30, 30, 30, 
                             30, 30, 30, 
                             30, 30, 30};     */   
        //System.out.println((s.run(false, carsPerLane))*0.05);
        double timeOutput = 0;
        for (int z = 0; z < 15; z++) {
            timeOutput = timeOutput + ((s.run(true, carsPerLane)));
        }
        double timeOutputAverage = timeOutput / 15;
        System.out.printf("%d   ;    %.3f\n", 20, timeOutputAverage);
        
    }

    
}

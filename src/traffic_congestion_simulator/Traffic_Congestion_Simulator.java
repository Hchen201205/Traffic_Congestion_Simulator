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
        VehicleTesting test = new VehicleTesting();
        test.graphDataTesting();
        //LightTesting test2 = new LightTesting();
    }
    
}

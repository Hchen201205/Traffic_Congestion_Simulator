/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;

import java.awt.Color;
import setting.Lane;
import setting.Light;
import vehicle.AutomatedCar;
import vehicle.NormalCar;
import vehicle.Vehicle;

/**
 *
 * @author chenhanxi
 */
public class Simulation2 {
    public Simulation2(boolean automated) {
        
        double[] position = {140, 310};
        double[] size = {280, 10};
        double direction = 0;
        System.out.println("hi");
        Light light = new Light(direction, Color.GREEN);

        light.startCycle();

        Lane test = new Lane(position, size, direction, light);
        light.setChangeTimes(10, 7, 2);

        for (int i = 0; i < 47; i++) {
            double[] carpos = {0, 5};
            double[] carsize = {3, 2};

            Vehicle c;
            if (automated) {
                c = new AutomatedCar(carpos, carsize, direction);
            } else {
                c = new NormalCar(carpos, carsize, direction);
            }
            
            c.updateSafetyDistance();
            test.addCar(c);
            //System.out.println(c.getAcceleration_rate());
        }
        test.setCars();
        System.out.println(test.getCarPos());
        for (int i = 0; i < 500; i++) {
            light.runCycleUnit();
        }
        System.out.println(test.getCarPos());
        for (int i = 0; i < 800; i++) {
            System.out.println(i + "round");
            light.runCycleUnit();
            test.runUnit();
            System.out.println(test.getCarPos());
        }

    }
    
    public static void main(String[] args) {
        boolean automated = true;
        Simulation2 test = new Simulation2(automated);
    }
}

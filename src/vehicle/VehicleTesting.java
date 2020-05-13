/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.Arrays;
import java.util.HashMap;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author Christine
 */
public class VehicleTesting implements TCSConstant{

    public static void accelerateTesting() {

    }

    public static void turnTesting() {
        double[] size = {2, 1};
        double[][] pos = {{2, 5}, {2, 0.5}, {5.5, 0}, {7.5, 0}, {8, 3.5}, {8, 5.5}, {4.5, 6}, {2.5, 6}};
        double[][] des = {{9, 12}, {2.5, 0}, {2, 3.5}, {8, 0.5}, {4.5, 0}, {7.5, 6}, {8, 2.5}, {2, 5.5}};

        HashMap<Double, String> facing = new HashMap<Double, String>();
        facing.put(0.0, "right");
        facing.put(90.0, "up");
        facing.put(180.0, "left");
        facing.put(270.0, "down");

        for (int i = 0; i < 8; i++) {
            double direction = 0;
            direction += ((int) ((i) / 2.0)) * 90;
            AutomatedCar car = new AutomatedCar(pos[i], size, direction);

            int turn;
            if (i % 2 == 0) {
                System.out.println("Left turn facing " + facing.get(direction) + ": ");
                turn = 90;
            } else {
                System.out.println("Right turn facing " + facing.get(direction) + ": ");
                turn = -90;
            }
            System.out.println("----------------------------------------");

            System.out.println("Initial Position: " + Arrays.toString(car.position));
            car.turn(turn, des[i], true);
            System.out.println("Turn radius: " + car.turn_radius);

            System.out.println("\nTurning: ");
            double count = 0;
            while (car.is_turning) {
                count++;
                car.turn(turn, des[i], true);
                if (count % 1 == 0) {
                    System.out.println("Position: [" + car.rounder(car.position[0])
                           + ", " + car.rounder(car.position[1]) + "]");
                    System.out.println("Direction: " + car.rounder(car.direction));
                }
            }
            System.out.println("\nTime increments: " + count);
            System.out.println("Final Direction: " + car.direction);
            System.out.println("Final Position: " + Arrays.toString(car.position));
            System.out.println("\n\n\n\n\n\n");

        }
    }

    public void graphDataTesting() {
        double[] size = {2, 1};
        double[] pos1 = {2, 5};
        double[] des1 = {9, 12};
        AutomatedCar car1 = new AutomatedCar(pos1, size, 0);
        
        double[] pos2 = {2, 3};
        AutomatedCar car2 = new AutomatedCar(pos2, size, 0);
        
        double[] pos3 = {14, 9};
        AutomatedCar car3 = new AutomatedCar(pos3, size, 180);
        
        double[] pos4 = {14, 7};
        double[] des4 = {7,0};
        AutomatedCar car4 = new AutomatedCar(pos4, size, 180);
        
        for (int i = 0; i < 60; i++) {
            if (i > 55){
                System.out.println(car1.rounder(car1.getCenterPos()[0]) + ", " + car1.rounder(car1.getCenterPos()[1])
                    + ", " + car1.rounder(car1.direction) + "\t\t" + car1.rounder(car2.getCenterPos()[0])
                    + ", " + car1.rounder(car2.getCenterPos()[1]) + ", " + car1.rounder(car2.direction) + 
                    "\t\t" + car1.rounder(car3.getCenterPos()[0]) + ", " + car1.rounder(car3.getCenterPos()[1])
                    + ", " + car1.rounder(car3.direction) + 
                    "\t\t" + car1.rounder(car4.getCenterPos()[0]) + ", " + car1.rounder(car4.getCenterPos()[1])
                    + ", " + car1.rounder(car4.direction));
            } else {
            car2.accelerate(true);
            car3.accelerate(true);
            System.out.println(car1.rounder(car1.getCenterPos()[0]) + ", " + car1.rounder(car1.getCenterPos()[1])
                    + ", " + car1.rounder(car1.direction) + "\t\t" + car1.rounder(car2.getCenterPos()[0])
                    + ", " + car1.rounder(car2.getCenterPos()[1]) + ", " + car1.rounder(car2.direction) + 
                    "\t\t" + car1.rounder(car3.getCenterPos()[0]) + ", " + car1.rounder(car3.getCenterPos()[1])
                    + ", " + car1.rounder(car3.direction) + 
                    "\t\t" + car1.rounder(car4.getCenterPos()[0]) + ", " + car1.rounder(car4.getCenterPos()[1])
                    + ", " + car1.rounder(car4.direction));
            }
        }
        
        car1.turn(90, des1, true);
        car4.turn(90, des4, true);
        while (car1.is_turning) {
            car1.turn(90, des1, true);
            car4.turn(90, des4, true);
            /*
            System.out.println(car.rounder(car.getCenterPos()[0]) + ", " + car.rounder(car.getCenterPos()[1])
                    + ", " + car.rounder(car.direction) + "\t\t" + car.rounder(car2.getCenterPos()[0])
                    + ", " + car.rounder(car2.getCenterPos()[1]) + ", " + car.rounder(car2.direction));
            */
            System.out.println(car1.rounder(car1.getCenterPos()[0]) + ", " + car1.rounder(car1.getCenterPos()[1])
                    + ", " + car1.rounder(car1.direction) + "\t\t" + car1.rounder(car2.getCenterPos()[0])
                    + ", " + car1.rounder(car2.getCenterPos()[1]) + ", " + car1.rounder(car2.direction) + 
                    "\t\t" + car1.rounder(car3.getCenterPos()[0]) + ", " + car1.rounder(car3.getCenterPos()[1])
                    + ", " + car1.rounder(car3.direction) + 
                    "\t\t" + car1.rounder(car4.getCenterPos()[0]) + ", " + car1.rounder(car4.getCenterPos()[1])
                    + ", " + car1.rounder(car4.direction));
        }
        
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author Christine
 */
public class VehicleTesting implements TCSConstant {

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
            AutomatedCar car = new AutomatedCar(pos[i], direction);

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
        double left = 5;
        double lower = 5;
        double lane = LSIZE[1];
        ArrayList<Vehicle> cars = new ArrayList<>();

        double[] pos1 = {left, lower + 2.5 * lane};
        double[] des1 = {left + 3.5 * lane, lower + 6 * lane};
        AutomatedCar car1 = new AutomatedCar(pos1, 0);
        cars.add(car1);

        double[] pos2 = {left, lower + 1.5 * lane};
        AutomatedCar car2 = new AutomatedCar(pos2, 0);
        cars.add(car2);

        double[] pos3 = {left + 6 * lane, lower + 4.5 * lane};
        AutomatedCar car3 = new AutomatedCar(pos3, 180);
        cars.add(car3);

        double[] pos4 = {left + 6 * lane, lower + 3.5 * lane};
        double[] des4 = {left + 2.5 * lane, lower};
        AutomatedCar car4 = new AutomatedCar(pos4, 180);
        cars.add(car4);

        int i;
        for (i = 0; i < 70; i++) {
            if (i > 65) {
                output(cars, i);
            } else {
                car2.accelerate(true);
                car3.accelerate(true);
                output(cars, i);
            }
        }

        car1.turn(90, des1, true);
        car4.turn(90, des4, true);
        while (car1.is_turning || car4.is_turning) {
            i++;
            if (!car1.isTurning()) {
                car4.turn(90, des4, true);
                car1.accelerate(true);
            } else if (!car4.isTurning()) {
                car1.turn(90, des1, true);
                car4.accelerate(true);
            } else {
                car1.turn(90, des1, true);
                car4.turn(90, des4, true);
            }
            output(cars, i);
        }

        for (int j = 0; j < 10; j++) {
            car1.accelerate(true);
            car4.accelerate(true);
            output(cars, j);
        }

    }

    public void output(ArrayList<Vehicle> cars, int i) {
        String output = "";
        if (i % 2 == 0) {
            for (Vehicle car : cars) {
                output += String.format("%7f, %7f; %7f, %7f; %7f\t\t", car.rounder(car.getCenterPos()[0]),
                        car.rounder(car.getCenterPos()[1]), car.size[0], car.size[1], car.rounder(car.direction));
            }
            System.out.println(output);
        }
    }

}

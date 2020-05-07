/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Christine
 */
public class VehicleTesting {

    public static void accelerateTesting() {

    }

    public static void turnTesting() {
        double[] size = {2, 1};
        double[][] pos = {{1, 2.5}, {1, 0.5}, {5.5, -1}, {7.5, -1}, {9, 3.5}, {9, 5.5}, {4.5, 7}, {2.5, 7}};
        double[][] des = {{5.5, 6}, {2.5, 0}, {2,  3.5}, {8,  0.5}, {4.5, 0}, {7.5, 6}, {8, 2.5}, {2, 5.5}};

        HashMap<Integer, String> facing = new HashMap<Integer, String>();
        facing.put(0, "right");
        facing.put(90, "up");
        facing.put(180, "left");
        facing.put(270, "down");

        for (int i = 0; i < 8; i++) {
            int direction = 0;
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
                if (count % 10 == 0) {
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
}

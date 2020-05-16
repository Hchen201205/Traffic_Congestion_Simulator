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

    public VehicleTesting() {
        System.out.println("TESTING");
    }

    public void accelerateTesting() {
        double left = 5;
        double lower = 5;
        double lane = LSIZE[1];
        ArrayList<Vehicle> cars = new ArrayList<>();

        double[] pos1 = {left, lower + 1.5 * lane};
        AutomatedCar car1 = new AutomatedCar(pos1, 0);
        cars.add(car1);

        double[] pos2 = {left + 6 * lane, lower + 4.5 * lane};
        AutomatedCar car2 = new AutomatedCar(pos2, 180);
        cars.add(car2);

        for (int i = 0; i < 20; i++) {
            car1.accelerate(true);
            car2.accelerate(true);
            output(cars, i);
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
        double new_pos = car1.getPosition()[0] - car1.size[0] / 2.0;
        car1.changeXPosition(new_pos);
        cars.add(car1);

        double[] pos2 = {left, lower + 1.5 * lane};
        AutomatedCar car2 = new AutomatedCar(pos2, 0);
        double new_pos2 = car2.getPosition()[0] - car2.size[0] / 2.0;
        car2.changeXPosition(new_pos2);
        cars.add(car2);

        double[] pos3 = {left, lower + 0.5 * lane};
        double[] des3 = {left + 0.5 * lane, lower};
        AutomatedCar car3 = new AutomatedCar(pos3, 0);
        double new_pos3 = car3.getPosition()[0] - car3.size[0] / 2.0;
        car3.changeXPosition(new_pos3);
        cars.add(car3);

        double[] pos4 = {left + 6 * lane, lower + 3.5 * lane};
        double[] des4 = {left + 2.5 * lane, lower};
        AutomatedCar car4 = new AutomatedCar(pos4, 180);
        double new_pos4 = car4.getPosition()[0] + car4.size[0] / 2.0;
        car4.changeXPosition(new_pos4);
        cars.add(car4);

        double[] pos5 = {left + 6 * lane, lower + 4.5 * lane};
        AutomatedCar car5 = new AutomatedCar(pos5, 180);
        double new_pos5 = car5.getPosition()[0] + car5.size[0] / 2.0;
        car5.changeXPosition(new_pos5);
        cars.add(car5);

        double[] pos6 = {left + 6 * lane, lower + 5.5 * lane};
        double[] des6 = {left + 5.5 * lane, lower + 6 * lane};
        AutomatedCar car6 = new AutomatedCar(pos6, 180);
        double new_pos6 = car6.getPosition()[0] + car6.size[0] / 2.0;
        car6.changeXPosition(new_pos6);
        cars.add(car6);

        //first light
        for (int k = 0; k < 10; k++) {
            output(cars, k);
        }

        int i;

        boolean track1 = true;
        boolean track2 = true;
        for (i = 0; i < 100; i++) {
            if (i > 85) {
                output(cars, i);
            } else {
                car2.accelerate(true);
                car5.accelerate(true);
                if ((car3.rounder(car3.position[0]) >= left && track1)
                        || (car6.rounder(car6.position[0]) <= (left + lane * 6) && track2)) {
                    if (car3.rounder(car3.position[0]) >= left) {
                        track1 = false;
                        car3.turn(-90, des3, true);
                    } else {
                        car3.accelerate(true);
                    }
                    if (car6.rounder(car6.position[0]) <= left + lane * 6) {
                        track2 = false;
                        car6.turn(-90, des6, true);
                    } else {
                        car6.accelerate(true);
                    }
                    output(cars, i);
                } else if ((car3.is_turning || car6.is_turning) && !track1 && !track2) {
                    if (!car3.isTurning()) {
                        car6.turn(-90, des6, true);
                        car3.accelerate(true);
                    } else if (!car6.isTurning()) {
                        car3.turn(-90, des3, true);
                        car6.accelerate(true);
                    } else {
                        car3.turn(-90, des3, true);
                        car6.turn(-90, des6, true);
                    }
                    output(cars, i);
                } else {
                    if (car3.isTurning()) {
                        car3.turn(-90, des3, true);
                        car6.accelerate(true);
                    } else if (car6.isTurning()) {
                        car6.turn(-90, des6, true);
                        car3.accelerate(true);
                    } else {
                        car3.accelerate(true);
                        car6.accelerate(true);
                    }
                    output(cars, i);
                }
            }
        }

        while (car1.position[0] <= left || car4.position[0] >= (left + lane * 6)) {
            //System.out.println("I ran");
            i++;
            if (Math.round(car1.position[0]) < left) {
                car1.accelerate(true);
            } else {
                car1.turn(90, des1, true);
            }
            if (Math.round((car4.position[0])) > left + lane * 6) {
                car4.accelerate(true);
            } else {
                car4.turn(90, des4, true);
            }
            output(cars, i);
        }

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

        for (int j = i; j < i + 20; j++) {
            car1.accelerate(true);
            car4.accelerate(true);
            output(cars, j);
        }

    }

    public void output(ArrayList<Vehicle> cars, int i) {
        String output = "";
        if (i % 1 == 0) {
            for (Vehicle car : cars) {
                output += String.format("%7f, %7f; %7f, %7f; %7f\t\t", car.rounder(car.getCenterPos()[0]),
                        car.rounder(car.getCenterPos()[1]), car.size[0], car.size[1], car.rounder(car.direction));
            }
            System.out.println(output);
        }
    }

}

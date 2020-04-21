/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import vehicle.*;
import java.util.ArrayList;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author KY
 */
public class Lane implements TCSConstant {

    ArrayList<Car> carList;// a list of all the cars on the road.

    boolean automated;

    boolean occupied;
    
    boolean one; // it's for testing

    public Lane(boolean automated) {
        carList = new ArrayList<>();
        this.automated = automated;
        occupied = false;
    }

    public boolean roadIsFull() {
        return occupied;

    }

    public void addCar(Car car) {
        carList.add(car);
    }
    
    public void addCar(double[] position, double[] size) {
        if (automated) {
            if (carList.isEmpty()) {
                Automated_Car a = new Automated_Car(position, size, 'r');
                a.getPosition();
                carList.add(new Automated_Car(position, size, 'r'));
            } else {
                Car car = carList.get(carList.size() - 1);
                if ((car.getPosition()[0] - car.getSafetyDistance() - (car.getSize()[0] - TESTSIZEX) / 2) > 0 
                        && (car.getPosition()[1] - car.getSafetyDistance() - (car.getSize()[1] - TESTSIZEY) / 2) > 0) {
                    carList.add(new Automated_Car(position, size, 'r'));
                }
            }
        }
    }

    public void deleteCar(int index) {
        carList.remove(index);
    }
    
    public void detectOutOfBoundsCar() {
        for (int i = 0; i < carList.size(); i++) {
            double[] pos = carList.get(i).getPosition();
            if (pos[0] > DRAWINGWIDTH || pos[0] < 0 ||pos[1] > DRAWINGHEIGHT || pos[1] < 0) {
                // do something
            }
        }
    }
    public void runCar() {
        for (int i = 0; i < carList.size(); i++) {
             if(speed == speedllimit){
                 return false;
             }
             else {
                  accelerate(acceleration, time_increments);
             }
             if(Distance == buffer){
                 accelerate(0 - acceleration, time_increments);
             }
             else{
                 accelerate(acceleration, time_increments);
             }
             if(color == 1){
                 accelerate(0 - acceleration, time_increments);
             }
             if(color == 0){
                 accelerate(acceleration, time_increments);
             }
        }
    }
    
    public boolean check_line(){
        if(car.position == line.position){ // we need a line variable 
            return true;
        }
        else{
            return false;
        }
    }
    
    public void Yellow_speedup(){
        if(true){
            accelerate(acceleration, time_increments);
        }
        else{
            accelerate(0 - acceleration, time_increments);
        }
    }
    
    public void Next_car(){
        accelerate(0 - acceleration, time_increments);
    }
    
    public void Green(){// starts to accelerate
        accelerate(acceleration, time_increments);
    }
    
    public void Yellow(){// starts to deccelerate
        accelerate(0 - acceleration, time_increments);
    }
    
    public void Red(){// stops 
            speed = 0;
            acceleration = 0;
        }
    
    public void Safe_Distance (){//distance between cars, no crash
        if(distance < 1){
            accelerate(0 - acceleration, time_increments);
        }
    }
    
    public int Distance_Check (){// Calculates the distance between the car and the car in front of it
        distance = initial_distance + (velocity1 - velocity2)*time;
    } return distance;
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.concurrent.TimeUnit;
import setting.Lane;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author Christine
 */
public abstract class Vehicle implements  TCSConstant {
    protected double[] speed = new double[2];//{speed left/right, speed up/down} in m/s (always positive)               
    protected double[] position;             //Position as the upper corner of vehicle, in m
    protected double[] size;                 //{length, width} in m
    protected double acceleration_rate;      //in m/s^2
    protected double decceleration_rate;     //in m/s^2
    protected int direction;                 //0 = 'n', 1 = 's', 2 = 'e' or 3 = 'w'          
    protected double speed_limit;            //in m/s, set for AutomatedCar, randomized for NormalCar
    protected double safety_distance;        //distance needed for car to safely deccelerate to 0
    protected double time_moving;            //total time vehicle has been moving, helpful for testing/checking
    
    protected boolean is_accelerating;       //true if accelerate method is running
    protected boolean is_turning;            //true if car is turning in intersection
    protected boolean at_limit_line;         //no implementation yet
    protected final double buffer = BUFFER;  //gap between cars when stopped, in m
    protected final double time_increments = TIMEINCREMENTS; //milliseconds 
    
    //simple acceleration function, updates position, speed, saftey_distance, time_moving
    public abstract void accelerate(double time, double acceleration) throws InterruptedException;
    
    //calculates and assigns new saftey distance based on current speed
    public abstract void updateSafetyDistance();
    
    public abstract void accelerateToSpeed(double speed) throws InterruptedException;
    
    public abstract void accelerateToSpeedLimit() throws InterruptedException;
    
    public abstract void deccelerateToSpeed(double speed) throws InterruptedException;
    
    public abstract void deccelerateToStop() throws InterruptedException;
    
    //vehicle will travel a certain distance to a stop, accelerating to speed limit 
    //until it needs to begin deccelerating based on saftey distance 
    public abstract void travelDistanceToStop(double distance) throws InterruptedException;
    
    //calculates accleration rate based on vehicle size
    public abstract void setAccelerationRate();
    
    //calculates deccleration rate based on vehicle size
    public abstract void setDeccelerationRate();
    
    //no implementation yet
    public abstract void turn(char dirrection);
    
    //distance between front bumper of vehicle to back bumper of front car plus the buffer
    public abstract double findDistanceFromFrontVehicle(Vehicle front_car);
    
    //no implementation yet
    //returns distance needed to reach the limit line of the lane (begining of intersection)
    public abstract double findDistanceFromLimitLine (Lane lane);
    
    //returns exact time needed to deccelerate to stop
    public abstract double timeToStop();
    
    //total number of increments needed to deccelerate to stop
    public abstract int incrementsToStop();
    
    //returns exact time needed to accelerate to speed_limit
    public abstract double timeToSpeedLimit();
    
    //total number of increments needed to accelerate to speed_limit
    public abstract int incrementsToSpeedLimit();
    
    //returns true vehicle is facing east or west
    public boolean isTravelingHorizontal(){
        if (direction == '2' || direction == '3'){
            return true;
        } 
        return false;
    }
    

    
    //Basic getters and setters:
    
    public boolean isAccelerating() {
        return is_accelerating;
    }
    
    public boolean isTurning(){
        //no implementation yet
        return is_turning;
    }
    
    //returns char value for direction ('n' = north, 's' =  south, etc.)
    public char getDirection(){
        switch(direction) {
            case 0:
                return 'n';
            case 1:
                return 's';
            case 2:
                return 'e';
            default: 
                return 'w';
        }
    }
    
    public double[] getPosition() {
        return position;
    }

    //the position component that is changing
    public double getDirectionalPos(){
        if (direction == 'l' || direction == 'r'){
            return this.position[0];
        } 
        return this.position[1];
    }
    
    //the speed component that is changing
    public double getDirectionalSpeed(){
        if (direction == 'l' || direction == 'r'){
            return this.speed[0];
        } 
        return this.speed[1];
    }
    
    public double getSafetyDistance() {
        return safety_distance;
    }

    public double[] getSize() {
        return size;
    }
    
    public double getTimeMoving(){
        return time_moving;
    }
}

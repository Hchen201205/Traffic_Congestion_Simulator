/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.concurrent.TimeUnit;
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
                                            
    protected double safety_distance;        //distance needed for car to safely deccelerate to 0
    protected double time_moving;            //total time vehicle has been moving 
    protected boolean is_accelerating;       //true if accelerate method is running
    
    protected final double buffer = 3;       //to be added onto safety distance (gap between cars when stopped, in m)
    protected final double speed_limit = 18; //in m/s
    protected final double time_increments = TIMEINCREMENTS; //milliseconds 
    
    //simple acceleration function, updates position, speed, saftey_distance, time_moving, and sleeps for time_increments
    public abstract void accelerate(double time, double acceleration) throws InterruptedException;
    
    public abstract void accelerateUnit(boolean accelerating, boolean decelerating);
    
    public abstract void updateSafetyDistance();
    
    public abstract void accelerateToSpeed(double speed) throws InterruptedException;
    
    public abstract void accelerateToSpeedLimit() throws InterruptedException;
    
    public abstract void deccelerateToSpeed(double speed) throws InterruptedException;
    
    public abstract void deccelerateToStop() throws InterruptedException;
    
    //vehicle will travel a certain distance to a stop, accelerating to speed limit 
    //until it needs to begin deccelerating based on saftey distance 
    public abstract void travelDistanceToStop(double distance) throws InterruptedException;
    
    //no implementation yet
    public abstract void turn(char dirrection);
    
    //distance between front bumper of vehicle to back bumper of front car plus the buffer
    public abstract double getDistanceFromFrontVehicle(Vehicle front_car);
    
    public abstract double getDistanceFromLimitLine ();
    
    public abstract double timeToStop ();
    
    public abstract int incrementsToStop();
    
    public abstract double timeToSpeedLimit();
    
    public abstract int incrementsToSpeedLimit();
    
    //Getters and Setters:
    
    public boolean isAccelerating() {
        return is_accelerating;
    }
    
    // I update the direction part so that it can be consistent.
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
    
    public boolean isTravelingHorizontal(){
        if (direction == 'l' || direction == 'r'){
            return true;
        } 
        return false;
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
    
    // If you want to delete this. Please let me know...
    public double getBuffer() {
        return buffer;
    }
}

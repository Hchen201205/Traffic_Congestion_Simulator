/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import setting.Lane;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author Christine
 */
public abstract class Vehicle2 implements  TCSConstant {
    protected double[] speed = new double[2];//{speed left/right, speed up/down} in m/s (always positive)               
    protected double[] position;             //Position as the upper corner of vehicle, in m
    protected double[] size;                 //{length, width} in m
    protected double acceleration_rate;      //in m/s^2
    protected double deceleration_rate;      //in m/s^2
    protected int direction;                 //degrees, 0° -> right, 90° -> up, etc.     
    protected double speed_limit;            //in m/s, set for AutomatedCar, randomized for NormalCar
    protected double safety_distance;        //distance needed for car to safely decelerate to 0
    protected double time_moving;            //total time vehicle has been moving, helpful for testing/checking
    protected double reaction_time;
    
    protected boolean is_accelerating;       //true if accelerate method is running
    protected boolean is_turning;            //true if car is turning in intersection
    
    protected Random rand = new Random(100); // Instead of initialize random in each car class, it can be created here.
    
    protected final double buffer = BUFFER;  //gap between cars when stopped, in m
    protected final int rounded_dec_pos = ROUNDEDDECPOS;     //the decimal position accuracy of functions
    protected final double time_increments = TIMEINCREMENTS; //milliseconds 
    
    
    //Abstract functions:
    
    //simple acceleration function, updates position, speed, saftey_distance, time_moving
    public abstract void accelerate(double time, boolean accelerate);
    
    //calculates and assigns new saftey distance based on current speed
    public abstract void updateSafetyDistance();
    
    
    
    //The following functions are now obsolete with the way Vehicle classes function
    //But the logic is still helpful and might be able to be reused in other classes
    //See AutomatedCar for fleshed out functions and code
    
    /*
    
    public abstract void accelerateToSpeed(double speed) throws InterruptedException;
    
    public abstract void accelerateToSpeedLimit() throws InterruptedException;
    
    public abstract void decelerateToSpeed(double speed) throws InterruptedException;
    
    public abstract void decelerateToStop() throws InterruptedException;
    
    //vehicle will travel a certain distance to a stop, accelerating to speed limit 
    //until it needs to begin decelerating based on saftey distance 
    public abstract void travelDistanceToStop(double distance) throws InterruptedException;
    */
    
    
    
    //assigns each car a random acceleration rate
    public abstract void genRandAcceleration();
    
    //assigns each car a random deceleration rate
    public abstract void genRandDeceleration();
    
    public abstract void genRandReactionTime();
    
    //no implementation yet
    public abstract void turn(int dirrection);
    
    //distance between front bumper of vehicle to back bumper of front car plus the buffer
    public abstract double getDistanceFromFrontVehicle(Vehicle2 front_car);
    
    //no implementation yet
    //for use when car is turning
    public abstract double getDistanceFromTurningVehicle(Vehicle2 front_car);
    
    //no implementation yet
    //returns distance needed to reach the limit line of the lane (begining of intersection)
    public abstract double getDistanceFromLimitLine (Lane lane);
    
    
    //Non-abstract functions:
    
    //returns true vehicle is facing east or west
    public boolean isTravelingHorizontal(){
        if (direction == 0 || direction == 180){
            return true;
        } 
        return false;
    }
    
    public boolean isTravelingVertical(){
        if (direction == 90 || direction == 270){
            return true;
        } 
        return false;
    }
    
    public double rounder (double num){
        num = num * Math.pow(10, rounded_dec_pos);
        num = Math.round(num);
        return num / Math.pow(10, rounded_dec_pos);
    }

    
    //Basic getters
    
    public boolean isAccelerating() {
        return is_accelerating;
    }
    
    public boolean isTurning(){
        return is_turning;
    }
    
    public boolean isStopped(){
        if (this.getDirectionalSpeed() == 0){
            return true;
        }
        return false;
    }
    /*
    public boolean isAtLimitLine(){
        //no implementation yet
        double limit_line_pos = ;
        if (this.getDirectionalPos() == limit_line_pos){
            return true;
        }
            return false;    
    }
    */
    public int getDirection(){
        return direction;
    }
    //returns char value for direction
    //'!' means car is not travelling in one of the cardinal directions
    public char getCharDirection(){
        switch(direction) {
            case 0:
                return 'r';
            case 90:
                return 'u';
            case 180:
                return 'l';
            case 270: 
                return 'd';
            default:
                return '!';
        }
    }
    
    public double[] getPosition() {
        return position;
    }
    
    //the position component that is changing
    public double getDirectionalPos(){
        if (direction ==  0|| direction == 180){
            return this.position[0];
        } else if (direction == 90 || direction == 270){
            return this.position[1];
        }
        //leaving this in as an error message in case
        //might delete later
        System.out.println("This vehicle is not facing a cardinal direction");
        return 0;
    }
    
    //the speed component that is changing
    public double getDirectionalSpeed(){
        if (direction ==  0|| direction == 180){
            return this.speed[0];
        } else if (direction == 90 || direction == 270){
            return this.speed[1];
        }
        //leaving this in as an error message in case
        //might delete later
        System.out.println("This vehicle is not traveling in a cardinal direction");
        return 0;
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
    
    public double getBuffer() {
        return buffer;
    }
    
    public int getRoundedDecPos(){
        return rounded_dec_pos;
    }
    
    public double getReactionTime() {
        return reaction_time;
    }
    
    public void reduceReactionTimeUnit() {
        reaction_time -= TIMEINCREMENTS;
    }
}

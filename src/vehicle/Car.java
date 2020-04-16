/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Christine
 */
public abstract class Car {
    protected double[] speed = new double[2];
    protected double[] position;
    //Position as the center of the car, in m
    protected double[] size;
    protected double acceleration_rate;
    protected double decceleration_rate;
    //in m/s^2
    protected char direction;
    //r for right, l for left, d for down, u for up
    protected double safety_distance;
    protected boolean accelerating;
    protected final double buffer = 3;
    //adds onto safety distance (gap between cars when stopped, in m)
    protected final double speedLimit = 18;
    //in m/s
    public abstract void accelerate(long time) throws InterruptedException;
    
    public abstract void updateSafetyDistance();
    
    public abstract void accelerateToSpeedLimit(long time_increments) throws InterruptedException;
        
    public boolean isAccelerating() {
        return accelerating;
    }
    
    public double getDistance(Car car2) {
        return Math.sqrt(Math.pow(position[0] + car2.getPosition()[0], 2) + Math.pow(position[1] + car2.getPosition()[1], 2));
    }
    
    public char getDirection(){
        return direction;
    }
    
    public double[] getPosition() {
        return position;
    }

    public double getSafetyDistance() {
        return safety_distance;
    }

    public double[] getSize() {
        return size;
    }
}

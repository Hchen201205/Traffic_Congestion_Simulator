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
public class NormalCar extends Vehicle implements TCSConstant {

    // Don't need reaction_time as a variable in normal car. It now is a shared variable in vehicle.
    //protected double reaction_time;       //constantly updating based on fixed random range and mean
    protected double reaction_time_mean;  //randomly generated once, then fixed
    protected double acceleration_mean;   //randomly generated once, then fixed
    protected double deceleration_mean;  //randomly generated once, then fixed
    protected double safety_distance_min;

    public NormalCar(double[] position, double[] size, int direction) {
        rand = new Random();
        speed[0] = 0;
        speed[1] = 0;
        safety_distance_min = 0;
        safety_distance = 0;
        time_moving = 0;
        is_turning = false;

        this.genReactionTimeMean();
        this.genAccelerationMean();
        this.genDecelerationMean();
        this.genRandAcceleration();
        this.genRandDeceleration();
        this.genRandReactionTime();

        this.position = position;
        this.size = size;
        this.direction = direction;
    }

    //Next 4 methods are mean generators
    //randomly generates a mean value for each changing variable
    //may mess with variance
    public void genReactionTimeMean() {
        reaction_time_mean = rand.nextGaussian() * 0.3 + REACTIONTIMEAVG;
    }

    public void genAccelerationMean() {
        acceleration_mean = rand.nextGaussian() * ACCELERATIONAVG / 8 + ACCELERATIONAVG;
    }

    public void genDecelerationMean() {
        deceleration_mean = rand.nextGaussian() * DECELERATIONAVG / 8 + DECELERATIONAVG;
    }

    //may mess with the variance for the next three methods
    //will asign a new rand acceleration based on mean, 
    public void genRandAcceleration() {
        acceleration_rate = rand.nextGaussian() * acceleration_mean / 5
                + acceleration_mean;
        acceleration_rate = this.rounder(acceleration_rate);
    }

    //will asign a new rand deceleration based on mean
    public void genRandDeceleration() {
        deceleration_rate = rand.nextGaussian() * deceleration_mean / 5
                + deceleration_mean;
        deceleration_rate = this.rounder(deceleration_rate);
    }

    //will asign a new rand reaction time based on mean
    public void genRandReactionTime() {
        reaction_time = rand.nextGaussian() * 0.1 + reaction_time_mean;
        reaction_time = this.rounder(reaction_time);
    }

    //generates the minimum value that saftey_distance can randomly generate to
    public void genSafetyDistanceMin() {
        safety_distance_min = Math.pow(this.getDirectionalSpeed(), 2)
                / (2 * -deceleration_rate);
    }

    //will asign a new rand safety_distance each time it is called
    public void updateSafetyDistance() {
        if (this.is_turning) {
            this.genSafetyDistanceMin();
            //safety_distance will always be at least the minimum distance to deccelerate to stop
            safety_distance = Math.abs(rand.nextGaussian() * safety_distance_min / 7)
                    + safety_distance_min;
            safety_distance = this.rounder(safety_distance);
        } else {
            //safety distance while turning
            //not implemented yet
        }

    }
    
    //Calculates the acceleration of individual car.
    public void accelerate(double time, boolean accelerate) {
        //reaction time randomizes each time it is used to begin accelerating from stop
        //actual delay from reaction time must be handled in an outside class
        if (this.isStopped()) {
            this.genRandReactionTime();
        }
        double acceleration;
        if (accelerate) {
            acceleration = acceleration_rate;
        } else {
            acceleration = deceleration_rate;
        }

        is_accelerating = true;

        double deltaPosX = speed[0] * time + 1.0 / 2 * acceleration * time * time * Math.cos(Math.toRadians(direction));
        deltaPosX = rounder(deltaPosX);
        position[0] = deltaPosX;
        speed[0] += rounder(acceleration * time * Math.cos(Math.toRadians(direction)));
        double deltaPosY = speed[1] * time + 1.0 / 2 * acceleration * time * time * Math.sin(Math.toRadians(direction));
        deltaPosY = rounder(deltaPosY);
        position[1] = deltaPosY;
        speed[1] += rounder(acceleration * time * Math.sin(Math.toRadians(direction)));

        /*
        if (this.isTravelingHorizontal()){
            double deltaPosX = speed[0] * time + 1.0 / 2 * acceleration * time * time;
            deltaPosX = this.rounder(deltaPosX, this.rounded_dec_pos);
            position[0] += deltaPosX * Math.cos(Math.toRadians(direction));
            speed[0] += this.rounder(acceleration * time, this.rounded_dec_pos);
            
        } else if (this.isTravelingVertical()) {
            double deltaPosY = speed[1] * time + 1.0 / 2 * acceleration * time * time;
            deltaPosY = this.rounder(deltaPosY, this.rounded_dec_pos);
            position[1] += deltaPosY * Math.sin(Math.toRadians(direction));
            speed[1] += this.rounder(acceleration * time, this.rounded_dec_pos);
            
        } else {
            //error message just in case
            //might delete later
            System.out.println("Error: accelerate called while car is not facing"
                    + " a cardinal direction");
        }
         */
        updateSafetyDistance();
        time_moving += time;
        is_accelerating = false;
    }

    public void turn(int direction) {
        //no implementation yet
    }

    //Updates the distance between cars.
    public double getDistanceFromFrontVehicle(Vehicle front_car) {
        if (front_car.isTravelingHorizontal()) {
            return Math.abs(this.position[0] - front_car.position[0])
                    - front_car.size[0] + buffer;
        }
        return Math.abs(this.position[1] - front_car.position[1])
                - front_car.size[0] + buffer;
    }

    /*
    public double getDistanceFromTurningVehicle(Vehicle2 front_car){
        //no implementation yet
        
        return distance;
    }
     */
 /*
    public double getDistanceFromLimitLine (Lane lane){
        //not implemented yet
        
        return distance;
    }
     */
    //Getters
    public double getReactionTime() {
        return reaction_time;
    }

    public double getReactionTimeMean() {
        return reaction_time_mean;
    }

    public double getAccelerationMean() {
        return acceleration_mean;
    }

    public double getDecelerationMean() {
        return deceleration_mean;
    }

    @Override
    public double getDistanceFromTurningVehicle(Vehicle front_car) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getDistanceFromLimitLine(Lane lane) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

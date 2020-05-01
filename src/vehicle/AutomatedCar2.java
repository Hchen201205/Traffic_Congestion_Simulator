/*
 * See abstract parent class for method/variable explanations
 */
package vehicle;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import setting.Lane;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author Christine
 */
public class AutomatedCar2 extends Vehicle2 implements TCSConstant {

    public AutomatedCar2(double[] position, double[] size, int direction) {
        speed[0] = 0;
        speed[1] = 0;
        safety_distance = 0;
        speed_limit = 18;
        time_moving = 0;
        is_turning = false;
        reaction_time = 0;

        //may change/dump these two methods later
        this.genRandAcceleration();
        this.genRandDeceleration();

        this.position = position;
        this.size = size;
        this.direction = direction;
    }

    //changes saftey distance
    public void accelerate(double time, boolean accelerate) {
        //sleep handled in seperate class
        //long sleep_time = (long) (time * 1000);
        double acceleration;
        if (accelerate) {
            acceleration = acceleration_rate;
        } else {
            acceleration = deceleration_rate;
        }
        
        // I'm going to need it soon.
        is_accelerating = true;

        // I updated it.
        double deltaPosX = speed[0] * time + 1.0 / 2 * acceleration * time * time * Math.cos(Math.toRadians(direction));
        deltaPosX = this.rounder(deltaPosX, rounded_dec_pos);
        position[0] = deltaPosX;
        speed[0] += this.rounder(acceleration * time * Math.cos(Math.toRadians(direction)), rounded_dec_pos);
        double deltaPosY = speed[1] * time + 1.0 / 2 * acceleration * time * time * Math.sin(Math.toRadians(direction));
        deltaPosY = this.rounder(deltaPosY, rounded_dec_pos);
        position[1] = deltaPosY;
        speed[1] += this.rounder(acceleration * time * Math.sin(Math.toRadians(direction)), rounded_dec_pos);

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
            System.out.println("Error: accelerate called while car is not facing a cardinal direction");
        }
         */
        updateSafetyDistance();
        //Sleep handled in seperate class
        //TimeUnit.MICROSECONDS.sleep(sleep_time);
        time_moving += time;
        is_accelerating = false;
    }

    public void genRandReactionTime() {
        reaction_time = 0;
    }
    
    public void updateSafetyDistance() {
        if (this.is_turning) {
            safety_distance = Math.pow(this.getDirectionalSpeed(), 2) / (2 * -deceleration_rate);
        } else {
            //safety distance while turning
            //not implemented yet
        }
    }

    //Obsolete functions, explained in abstract Vehicle class
    //still might find a use for code/logic in other classes
    /*
    public void accelerateToSpeed(double speed) throws InterruptedException {
        double speed_dir = this.getDirectionalSpeed();
        while (speed_dir < speed) {
            accelerate(time_increments, acceleration_rate);
            speed_dir = this.getDirectionalSpeed();
        }
    }

    public void decelerateToSpeed(double speed) throws InterruptedException {
        double speed_dir = this.getDirectionalSpeed();
        while (speed_dir > speed) {
            accelerate(time_increments, deceleration_rate);
            speed_dir = this.getDirectionalSpeed();
        }
    }

    public void accelerateToSpeedLimit() throws InterruptedException {
        accelerateToSpeed(speed_limit);
    }

    public void decelerateToStop() throws InterruptedException {
        decelerateToSpeed(0);
    }

    public void travelDistanceToStop(double distance) throws InterruptedException {
        double initial_pos = this.getDirectionalPos();
        while (distance > safety_distance) {
            if (this.getDirectionalSpeed() < speed_limit) {
                accelerate(time_increments, acceleration_rate);
            } else {
                accelerate(time_increments, 0);
            }
            distance -= Math.abs(initial_pos - this.getDirectionalPos());
            initial_pos = this.getDirectionalPos();
        }
        this.decelerateToStop();
    }
     */
    /**
     * This class will serve as an estimation method. It will estimate the
     * distance of the car can travel in the next something seconds. And will
     * report the time.
     *
     */
    public double estimateDistance(int direction, int time) {

        return 0;
    }

    //Randomly generating acceleration and deceleration functions:
    //randomly generated from gaussian distribution of average values
    //may change variance if necessary
    public void genRandAcceleration() {
        acceleration_rate = rand.nextGaussian() * ACCELERATIONAVGMAX / 10 + ACCELERATIONAVGMAX;
    }

    ;
    
    //deceleration relys on generated acceleration to avoid unrealistic/conflicting rates
    public void genRandDeceleration() {
        double scaled_dec_avg_max = DECELERATIONAVGMAX / ACCELERATIONAVGMAX * acceleration_rate;
        deceleration_rate = rand.nextGaussian() * scaled_dec_avg_max / 10 + scaled_dec_avg_max;
    }

    ;
    
    
    
    public void turn(int direction) {
        //no implementation yet
    }

    //front bumper of car to back bumper of front car plus buffer
    public double getDistanceFromFrontVehicle(Vehicle2 front_car) {
        if (front_car.isTravelingHorizontal()) {
            return Math.abs(this.position[0] - front_car.position[0]) - front_car.size[0] + buffer;
        }
        return Math.abs(this.position[1] - front_car.position[1]) - front_car.size[0] + buffer;
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
    //returns exact time needed to decelerate to stop
    public double timeToStop() {
        double speed = this.getDirectionalSpeed();
        double time = -speed / this.deceleration_rate;
        return time;
    }

    //total number of increments needed to decelerate to stop
    public int incrementsToStop() {
        double increments = this.timeToStop() / time_increments;
        return (int) Math.floor(increments);
    }

    //returns exact time needed to accelerate to speed_limit
    public double timeToSpeedLimit() {
        double speed = this.getDirectionalSpeed();
        double time = -(speed_limit - speed) / this.deceleration_rate;
        return time;
    }

    //total number of increments needed to accelerate to speed_limit
    public int incrementsToSpeedLimit() {
        double increments = this.timeToSpeedLimit() / time_increments;
        return (int) Math.floor(increments);
    }

    @Override
    public double getDistanceFromTurningVehicle(Vehicle2 front_car) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getDistanceFromLimitLine(Lane lane) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

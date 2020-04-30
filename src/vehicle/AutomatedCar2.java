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
public class AutomatedCar2 extends Vehicle implements TCSConstant{
    protected Random rand;
    
    public AutomatedCar2(double[] position, double[] size, int direction) {
        speed[0] = 0;
        speed[1] = 0;
        safety_distance = 0;
        speed_limit = 18;
        time_moving = 0;
        
        //may change/dump these two methods later
        this.setAccelerationRate();
        this.setDecelerationRate();
        
        this.position = position;
        this.size = size;
        this.direction = direction;
    }

    //changes saftey distance
    public void accelerate(double time, double acceleration) throws InterruptedException {
        //sleep handled in seperate class
        //long sleep_time = (long) (time * 1000);
        is_accelerating = true;
        switch (direction) {
            case 0:
                double deltaPosX = speed[0] * time + 1.0 / 2 * acceleration * time * time;
                position[0] += deltaPosX;
                speed[0] += acceleration * time;
                break;
            case 180:
                deltaPosX = speed[0] * time + 1.0 / 2 * acceleration * time * time;
                position[0] -= deltaPosX;
                speed[0] += acceleration * time;
                break;
            case 90:
                double deltaPosY = speed[1] * time + 1.0 / 2 * acceleration * time * time;
                position[1] += deltaPosY;
                speed[1] += acceleration * time;
                break;
            case 270:
                deltaPosY = speed[1] * time + 1.0 / 2 * acceleration * time * time;
                position[1] -= deltaPosY;
                speed[1] += acceleration * time;
                break;
            //error message just in case
            //might delete later
            default:
                System.out.println("Error: accelerate called while car is not facing a cardinal direction");
                return;
        }
        updateSafetyDistance();
        //Sleep handled in seperate class
        //TimeUnit.MICROSECONDS.sleep(sleep_time);
        time_moving += time;
        is_accelerating = false;
    }

    public void updateSafetyDistance() {
        safety_distance = Math.pow(this.getDirectionalSpeed(), 2) / (2 * -deceleration_rate);
    }

    
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

    /**
     * This class will serve as an estimation method. It will estimate the
     * distance of the car can travel in the next something seconds. And will
     * report the time.
     *
     */
    public double estimateDistance(int direction, int time) {
        
        return 0;
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

    //Randomly generating acceleration and deceleration functions:
    //randomly generated from gaussian distribution of average values
    //may change variance if necessary
    public void setAccelerationRate(){
        acceleration_rate = rand.nextGaussian()*ACCELERATIONAVGMAX/10 + ACCELERATIONAVGMAX;
    };
    
    //deceleration relys on generated acceleration to avoid unrealistic/conflicting rates
    public void setDecelerationRate(){
        double scaled_dec_avg_max = DECELERATIONAVGMAX/ACCELERATIONAVGMAX * acceleration_rate;
        deceleration_rate = rand.nextGaussian()*scaled_dec_avg_max/10 + scaled_dec_avg_max;
    };
    
    
    
    public void turn(int direction) {
        
    }

    //front bumper of car to back bumper of front car plus buffer
    public double getDistanceFromFrontVehicle(Vehicle front_car) {
        if (front_car.isTravelingHorizontal()) {
            return Math.abs(this.position[0] - front_car.position[0]) - front_car.size[0] + buffer;
        }
        return Math.abs(this.position[1] - front_car.position[1]) - front_car.size[0] + buffer;
    }
    
    public double getDistanceFromLimitLine (Lane lane){
        //not implemented yet, coming soon to a java class near you
        
        return distance;
    }
    
    public double timeToStop (){
        double speed = this.getDirectionalSpeed();
        double time = - speed / this.deceleration_rate;
        return time;
    }
    
    public int incrementsToStop(){
        double increments = this.timeToStop() / time_increments;
        return (int) Math.floor(increments);
    }
    
    public double timeToSpeedLimit(){
        double speed = this.getDirectionalSpeed();
        double time = - (speed_limit - speed) / this.deceleration_rate;
        return time;
    }
    
    public int incrementsToSpeedLimit(){
        double increments = this.timeToSpeedLimit() / time_increments;
        return (int) Math.floor(increments);
    }

}

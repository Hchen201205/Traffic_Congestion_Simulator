/*
 * See abstract parent class for method/variable explanations
 */
package vehicle;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import setting.Lane;

/**
 *
 * @author Christine
 */
public class AutomatedCar extends Vehicle {

    public AutomatedCar(double[] position, double[] size, int direction) {
        speed[0] = 0;
        speed[1] = 0;
        safety_distance = 0;
        speed_limit = 18;
        time_moving = 0;
        
        //may change/dump these two methods later
        this.setAccelerationRate();
        this.setDeccelerationRate();
        
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
            case 2:
                double deltaPosX = speed[0] * time + 1.0 / 2 * acceleration * time * time;
                position[0] += deltaPosX;
                speed[0] += acceleration * time;
                break;
            case 3:
                deltaPosX = speed[0] * time + 1.0 / 2 * acceleration * time * time;
                position[0] -= deltaPosX;
                speed[0] += acceleration * time;
                break;
            case 0:
                double deltaPosY = speed[1] * time + 1.0 / 2 * acceleration * time * time;
                position[1] += deltaPosY;
                speed[1] += acceleration * time;
                break;
            case 1:
                deltaPosY = speed[1] * time + 1.0 / 2 * acceleration * time * time;
                position[1] -= deltaPosY;
                speed[1] += acceleration * time;
                break;
        }
        updateSafetyDistance();
        //Sleep handled in seperate class
        //TimeUnit.MICROSECONDS.sleep(sleep_time);
        time_moving += time;
        is_accelerating = false;
    }

    public void updateSafetyDistance() {
        safety_distance = Math.pow(this.getDirectionalSpeed(), 2) / (2 * -decceleration_rate);
    }

    
    public void accelerateToSpeed(double speed) throws InterruptedException {
        double speed_dir = this.getDirectionalSpeed();
        while (speed_dir < speed) {
            accelerate(time_increments, acceleration_rate);
            speed_dir = this.getDirectionalSpeed();
        }
    }

    public void deccelerateToSpeed(double speed) throws InterruptedException {
        double speed_dir = this.getDirectionalSpeed();
        while (speed_dir > speed) {
            accelerate(time_increments, decceleration_rate);
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

    public void deccelerateToStop() throws InterruptedException {
        deccelerateToSpeed(0);
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
        this.deccelerateToStop();
    }

    public void setAccelerationRate(){
        //not sure if these methods will make thing too complicated or not
        //no implementation yet
        
        acceleration_rate = 3;
    };
    
    public void setDeccelerationRate(){
        //no implementation yet
        
        decceleration_rate = -4;
    };
    
    public void turn(char dirrection) {
        //no implementaion yet
    }

    //front bumper of car to back bumper of front car plus buffer
    public double getDistanceFromFrontVehicle(Vehicle front_car) {
        if (front_car.isTravelingHorizontal()) {
            return Math.abs(this.position[0] - front_car.position[0]) - front_car.size[0] + buffer;
        }
        return Math.abs(this.position[1] - front_car.position[1]) - front_car.size[0] + buffer;
    }
    
    public double getDistanceFromLimitLine (Lane lane){
        //not implemented yet, coming soon
        return distance;
    }
    
    public double timeToStop (){
        double speed = this.getDirectionalSpeed();
        double time = - speed / this.decceleration_rate;
        return time;
    }
    
    public int incrementsToStop(){
        double increments = this.timeToStop() / time_increments;
        return (int) Math.floor(increments);
    }
    
    public double timeToSpeedLimit(){
        double speed = this.getDirectionalSpeed();
        double time = - (speed_limit - speed) / this.decceleration_rate;
        return time;
    }
    
    public int incrementsToSpeedLimit(){
        double increments = this.timeToSpeedLimit() / time_increments;
        return (int) Math.floor(increments);
    }

}

/*
 * See abstract parent class for method/variable explanations
 */
package vehicle;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Christine
 */
public class AutomatedCar extends Vehicle{
    public AutomatedCar(double[] position, double[] size, char direction) {
        speed[0] = 0;
        speed[1] = 0;
        acceleration_rate = 6;
        decceleration_rate = -9;
        safety_distance = buffer;
        this.position = position;
        this.size = size;
        this.direction = direction;
        this.time_moving = 0;
    }

    //changes saftey distance, sleeps
    public void accelerate(double time, double acceleration) throws InterruptedException {
        long sleep_time = (long) (time * 1000);
        is_accelerating = true;
        switch (direction){
            case 'r':
                double deltaPosX = speed[0] * time + 1.0 / 2 * acceleration * time * time;
                position[0] += deltaPosX;
                speed[0] += acceleration*time;
                break;
            case 'l':
                deltaPosX = speed[0] * time + 1.0 / 2 * acceleration * time * time;
                position[0] -= deltaPosX;
                speed[0] += acceleration*time;
                break;
            case 'd':
                double deltaPosY = speed[1] * time + 1.0 / 2 * acceleration * time * time;
                position[1] += deltaPosY;
                speed[1] += acceleration*time;
                break;
            case 'u':
                deltaPosY = speed[1] * time + 1.0 / 2 * acceleration * time * time;
                position[1] -= deltaPosY;
                speed[1] += acceleration*time;
                break;
        }
        updateSafetyDistance();
        TimeUnit.MICROSECONDS.sleep(sleep_time);
        time_moving += time;
        is_accelerating = false;
    }

    public void updateSafetyDistance() {
        safety_distance = Math.pow(this.getDirectionalSpeed(), 2) / (2 * -decceleration_rate);
    }

    public void accelerateToSpeed(double speed) throws InterruptedException {
        double speed_dir = this.getDirectionalSpeed();
        while (speed_dir < speed){
            accelerate(time_increments, acceleration_rate);
            speed_dir = this.getDirectionalSpeed();
        }
    }
    
    public void deccelerateToSpeed(double speed) throws InterruptedException{
        double speed_dir = this.getDirectionalSpeed();
        while (speed_dir > speed){
            accelerate(time_increments, decceleration_rate);
            speed_dir = this.getDirectionalSpeed();
        }
    }
    
    public void accelerateToSpeedLimit() throws InterruptedException {
        accelerateToSpeed(speed_limit);
    }
    
    public void deccelerateToStop() throws InterruptedException {
        deccelerateToSpeed(0);
    }
    
    public void travelDistanceToStop(double distance) throws InterruptedException{
        double initial_pos = this.getDirectionalPos();
        while(distance > safety_distance){
            if (this.getDirectionalSpeed() < speed_limit){
                accelerate(time_increments, acceleration_rate);
            } else {
                accelerate(time_increments, 0);
            }
            distance -= Math.abs(initial_pos - this.getDirectionalPos());
            initial_pos = this.getDirectionalPos();
        }
        this.deccelerateToStop();
    }
    
    public void turn(char dirrection){
        //no implementaion yet
    }
    
    //front bumper of car to back bumper of front car plus buffer
    public double getDistanceFromFrontVehicle(Vehicle front_car) {
        if (front_car.isTravelingHorizontal()){
            return Math.abs(this.position[0] - front_car.position[0]) - front_car.size[0] + buffer;
        } 
        return Math.abs(this.position[1] - front_car.position[1]) - front_car.size[0] + buffer;
    }
    
}


/*
 * See abstract parent class for method/variable explanations
 */
package vehicle;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Christine
 */
public class AutomatedCar extends Vehicle {

    public AutomatedCar(double[] position, double[] size, int direction) {
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

    /**
     * This is a new method I add. I add it because we are now measuring the
     * movement for every millisecond unit. So why not have a method that only
     * accelerate for one unit and keep updating. Also, I didn't add the sleep
     * method in here because the light class will take the sleep. So you can
     * think of: light will run for a unit second first and then the car will
     * accelerate for one unit second. Or maybe this is not right... Maybe I
     * should add sleep in the simulation class.
     *
     * @param accelerating
     * @param deccelerating
     */
    public void accelerateUnit(boolean accelerating, boolean deccelerating) {
        int time = 1;
        if (accelerating || deccelerating) {
            double acceleration = 0;
            is_accelerating = true;
            if (accelerating) {
                acceleration = acceleration_rate;
            } else if (deccelerating) {
                acceleration = decceleration_rate;
            }
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
            time_moving += time;
            is_accelerating = false;
        }

    }

    //changes saftey distance, sleeps
    public void accelerate(double time, double acceleration) throws InterruptedException {
        long sleep_time = (long) (time * 1000);
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
        TimeUnit.MICROSECONDS.sleep(sleep_time);
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
     * This class will serve as an estimation method.It will estimate the
     * distance of the car can travel in the next something seconds. And will
     * report the time.
     *
     * @return
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

}

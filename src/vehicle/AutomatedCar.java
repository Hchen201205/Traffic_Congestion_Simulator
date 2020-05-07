/*
 * See abstract parent class for method/variable explanations
 */
package vehicle;

import java.util.Arrays;
import setting.Lane;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author Christine
 */
//Mimics cars with automated acceleration
public class AutomatedCar extends Vehicle implements TCSConstant {

    //Creates a Automated car.
    public AutomatedCar(double[] position, double[] size, int direction) {
        speed[0] = 0;
        speed[1] = 0;
        safety_distance = 0;
        speed_limit = 18;
        time_moving = 0;
        is_turning = false;
        reaction_time = 0;
        turning_acceleration = 0;
        turn_radius = 0;
        turning_velocity = 0;
        turn_initial_position = new double[2];
        turn_initial_direction = 0;

        //may change/dump these two methods later
        this.genRandAcceleration();
        this.genRandDeceleration();

        this.position = position;
        this.size = size;
        this.direction = direction;
    }

    public void accelerate(double time_increment, boolean accelerate) {
        double acceleration;
        if (accelerate) {
            acceleration = acceleration_rate;
            this.genRandAcceleration();
        } else {
            acceleration = deceleration_rate;
            this.genRandDeceleration();
        }

        // I'm going to need it soon.
        is_accelerating = true;

        // I updated it.
        double deltaPosX = (speed[0] * time_increment + 1.0 / 2 * acceleration * time_increment * time_increment)
                * Math.abs(Math.cos(Math.toRadians(direction)));
        deltaPosX = this.rounder(deltaPosX);
        position[0] += deltaPosX;
        speed[0] += this.rounder(acceleration * time_increment * Math.abs(Math.cos(Math.toRadians(direction))));

        double deltaPosY = (speed[1] * time_increment + 1.0 / 2 * acceleration * time_increment * time_increment)
                * Math.abs(Math.sin(Math.toRadians(direction)));
        deltaPosY = this.rounder(deltaPosY);
        position[1] += deltaPosY;
        speed[1] += this.rounder(acceleration * time_increment * Math.abs(Math.sin(Math.toRadians(direction))));

        if (speed[0]*Math.abs(Math.cos(Math.toRadians(direction))) < 0 || 
            speed[1] * Math.abs(Math.sin(Math.toRadians(direction))) < 0){
            speed[0] = 0;
            speed[1] = 1;
        } 
        
        updateSafetyDistance();
        time_moving += time_increment;
        is_accelerating = false;

    }
    
    //car will travel for one increment without changing speed
    public void travelWithConstantSpeed(){
        double acceleration_rate = this.acceleration_rate;
        this.acceleration_rate = 0;
        accelerate(this.time_increments, true);
        this.acceleration_rate = acceleration_rate;
    }

    //For automated car there is no reaction time.
    public void genRandReactionTime() {
        reaction_time = 0;
    }

    //Updates car safe distances, so the car will avoid accident.
    public void updateSafetyDistance() {
        if (this.is_turning) {
            safety_distance = Math.pow(this.getDirectionalSpeed(), 2) / (2 * -deceleration_rate);
        } else {
            //safety distance while turning
            //not implemented yet
        }
    }

    //Randomly generating acceleration and deceleration functions:
    //randomly generated from gaussian distribution of average values
    //may change variance if necessary
    public void genRandAcceleration() {
        acceleration_rate = Math.abs(rand.nextGaussian() * ACCELERATIONAVGMAX / 10 + ACCELERATIONAVGMAX);
        acceleration_rate = this.rounder(acceleration_rate);
    }

    //deceleration relys on generated acceleration to avoid unrealistic/conflicting rates
    public void genRandDeceleration() {
        double scaled_dec_avg_max = DECELERATIONAVGMAX / ACCELERATIONAVGMAX * acceleration_rate;
        deceleration_rate = rand.nextGaussian() * scaled_dec_avg_max / 10 + scaled_dec_avg_max;
        deceleration_rate = this.rounder(deceleration_rate);
    }

    //front bumper of car to back bumper of front car plus buffer
    public double getDistanceFromFrontVehicle(Vehicle front_car) {
        if (front_car.isTravelingHorizontal()) {
            return Math.abs(this.position[0] - front_car.position[0])
                    - this.size[0] / 2 - front_car.size[0] / 2 + buffer;
        }
        return Math.abs(this.position[1] - front_car.position[1])
                - this.size[1] / 2 - front_car.size[1] / 2 + buffer;
    }
    
    public void setTurningConstants(double[] destination, boolean accelerate) {
        //slighty dimished acceleration rate to more realistically model a turn
        //also acceleration is assumed to be constant
        if (accelerate){
            turning_acceleration = 3.0 / 4.0 * this.acceleration_rate;
        } else {
            turning_acceleration = 3.0 / 4.0 * this.deceleration_rate;
        }
        

        turning_velocity = this.getDirectionalSpeed() * 3.0 / 4.0;

        //radius of quarter circle that is being used to model the turn
        turn_radius = Math.abs(destination[0] - this.position[0]);
        
        //initial values stored so turn() can be called many times and edit Vehicle vairables
        turn_initial_position[0] = this.position[0];
        turn_initial_position[1] = this.position[1];
        turn_initial_direction = this.direction;

    }

    public void turn(int direction, double[] destination, boolean accelerate) {
        //  this will run one time when turn() is first called, sets the constants 
        //and positions car halfway in the intersection to prepare to begin turn
        if (!this.is_turning) {
            if (this.isTravelingHorizontal()) {
                position[0] += size[0] * Math.cos(Math.toRadians(this.direction)) / 2;
            } else {
                position[1] += size[0] * Math.sin(Math.toRadians(this.direction)) / 2;
            }

            this.setTurningConstants(destination, accelerate);
            //for testing:
            //System.out.println("I ran. Position: " + Arrays.toString(position));
        }
        is_turning = true;

        //the change of angle is used to model acceleration
        //the change is calculated based on one time increment of turning
        turning_velocity += turning_acceleration * time_increments;
        if (turning_velocity < 0){
            turning_velocity = 0;
        }
        
        double angle_increment = turning_velocity * time_increments / turn_radius;
        //testing:
        //System.out.println("Angle inc: " + Math.toDegrees(angle_increment));
        
        //keeps track of how far car has travelled in the intersection
        double[] turn_pos = {Math.abs(Math.abs(position[0]) - Math.abs(turn_initial_position[0])),
            Math.abs(Math.abs(position[1]) - Math.abs(turn_initial_position[1]))};

        //starting facing left or right
        if (this.turn_initial_direction == 0 || this.turn_initial_direction == 180) {
            //  keeps track of what reference angle should be used for each position component
            //and whether each position component should be positively or negatively incremented
            int neg_or_pos = (int) (Math.cos(Math.toRadians(this.turn_initial_direction))
                    * Math.sin(Math.toRadians(this.turn_initial_direction + direction)));
            
            //changes dirrection of car based on calculated incremental angle change
            this.direction += neg_or_pos * Math.toDegrees(angle_increment);
            
            //updates position components for one time increment of turning
            position[0] += Math.cos(Math.toRadians(this.turn_initial_direction))
                    * (turn_radius * Math.abs(Math.cos(Math.toRadians(this.direction - 
                    neg_or_pos * 90))) - turn_pos[0]);

            position[1] += Math.sin(Math.toRadians(this.turn_initial_direction + direction))*(turn_radius - turn_pos[1] - Math.abs(turn_radius
                    * Math.sin(Math.toRadians(this.direction - neg_or_pos * 90))));
        
        //starting facing up or down
        } else {
            int neg_or_pos = (int) (-1*Math.cos(Math.toRadians(this.turn_initial_direction + direction))
                    * Math.sin(Math.toRadians(this.turn_initial_direction)));
            
            this.direction += neg_or_pos * Math.toDegrees(angle_increment);
            
            position[0] += Math.cos(Math.toRadians(this.turn_initial_direction + direction))*(turn_radius - turn_pos[0] - Math.abs(turn_radius
                    * Math.cos(Math.toRadians(this.direction - neg_or_pos * 90))));
            
            position[1] += Math.sin(Math.toRadians(this.turn_initial_direction))
                    * (turn_radius * Math.abs(Math.sin(Math.toRadians(this.direction - 
                    neg_or_pos * 90))) - turn_pos[1]);
            
        }

        time_moving += time_increments;
        
        //checks if the car has finished the turn or not
        if (direction == -90 && this.direction <= turn_initial_direction - 90
                || direction == 90 && this.direction >= turn_initial_direction + 90) {
            
            //rounds angle down/up to be exactly 0, 90, 180, or 270
            if (this.direction >= 360) {
                this.direction = 0;
            } else if (this.direction < -1) {
                this.direction = 270;
            } else {
                this.direction = turn_initial_direction + direction;
            }
            
            //  moves car slightly forward in whatever direction it is facing so 
            //the whole car is past limit line (with back tires on it)
            if (this.isTravelingHorizontal()) {
                position[0] = destination[0] + size[0] * Math.cos(Math.toRadians(this.direction)) / 2;
                position[1] = destination[1];
            } else {
                position[1] = destination[1] + size[0] * Math.sin(Math.toRadians(this.direction)) / 2;
                position[0] = destination[0];
            }
            
            is_turning = false;    
        } 
            
    }

    //runs a turn increment with no acceleration
    //car will continue along turn for one increment with whatever speed it currently has
    public void turnWithConstantSpeed(int direction, double[] destination){
        double turning_acceleration = this.turning_acceleration;
        double acceleration_rate = this.acceleration_rate;
        this.turning_acceleration = 0;
        this.acceleration_rate = 0;

        turn(direction, destination, true);
        
        this.turning_acceleration = turning_acceleration;
        this.acceleration_rate = acceleration_rate;
    }
    
    /*
    public double getDistanceFromTurningVehicle(Vehicle2 front_car){
        //no implementation yet
        
        return distance;
    }
     
 
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
    public double getDistanceFromTurningVehicle(Vehicle front_car) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getDistanceFromLimitLine(Lane lane) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.Arrays;
import java.util.Random;
import setting.Lane;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author Christine
 */
//Mimics cars with human drivers
public class NormalCar extends Vehicle implements TCSConstant {

    // Don't need reaction_time as a variable in normal car. It now is a shared variable in vehicle.
    //protected double reaction_time;       //constantly updating based on fixed random range and mean
    protected double reaction_time_mean;  //randomly generated once, then fixed
    protected double acceleration_mean;   //randomly generated once, then fixed
    protected double deceleration_mean;  //randomly generated once, then fixed
    protected double safety_distance_min;
    protected double turn_safety_angle_min;

    public NormalCar(double[] position, double[] size, double direction) {
        rand = new Random();
        speed[0] = 0;
        speed[1] = 0;
        //created a method to generate random speed limit so it is consistent with other variables
        //speed_limit = AUTOMATEDFINALVELOCITY + rand.nextGaussian();
        safety_distance_min = 0;
        safety_distance = 0;
        time_moving = 0;
        is_turning = false;
        turning_acceleration = 0;
        turning_acceleration = 0;
        turn_radius = 0;
        turning_velocity = 0;
        turn_initial_position = new double[2];
        turn_initial_direction = 0;
        turn_safety_angle_min = 0;
        turn_safety_angle = 0;

        this.genReactionTimeMean();
        this.genAccelerationMean();
        this.genDecelerationMean();

        this.genRandAcceleration();
        this.genRandDeceleration();
        this.genRandReactionTime();
        this.genRandSpeedLimit();

        this.position = position;
        this.size = size;
        this.direction = direction;
    }

    //Next 4 methods are mean generators
    //randomly generates a mean value for each changing variable
    //may mess with variance
    public void genReactionTimeMean() {
        reaction_time_mean = rand.nextGaussian() * REACTIONTIMEAVG / 8.0 + REACTIONTIMEAVG;
    }

    public void genAccelerationMean() {
        acceleration_mean = rand.nextGaussian() * ACCELERATIONAVG / 8.0 + ACCELERATIONAVG;
    }

    public void genDecelerationMean() {
        deceleration_mean = rand.nextGaussian() * DECELERATIONAVG / 8.0 + DECELERATIONAVG;
    }

    //may mess with the variance for the next three methods
    //will asign a new rand acceleration based on mean, 
    @Override
    public void genRandAcceleration() {
        acceleration_rate = rand.nextGaussian() * acceleration_mean / 5.0
                + acceleration_mean;
        acceleration_rate = this.rounder(acceleration_rate);
    }

    //will asign a new rand deceleration based on mean
    @Override
    public void genRandDeceleration() {
        deceleration_rate = rand.nextGaussian() * deceleration_mean / 5.0
                + deceleration_mean;
        deceleration_rate = this.rounder(deceleration_rate);
    }

    //will asign a new rand reaction time based on mean
    @Override
    public void genRandReactionTime() {
        reaction_time = rand.nextGaussian() * 0.1 + reaction_time_mean;
        reaction_time = this.rounder(reaction_time);
        System.out.println("This reaction time is" + reaction_time);
    }

    public void genRandSpeedLimit() {
        speed_limit = rand.nextGaussian() * AUTOMATEDFINALVELOCITY / 9.0 + AUTOMATEDFINALVELOCITY;
        //System.out.println(speed_limit);
    }

    //generates the minimum value that saftey_distance can randomly generate to
    public void genSafetyDistanceMin() {
        safety_distance_min = Math.pow(this.getDirectionalSpeed(), 2)
                / (2 * -deceleration_rate);
    }

    //will asign a new rand safety_distance each time it is called
    @Override
    public void updateSafetyDistance() {
        if (this.is_turning) {
            //safety distance while turning
            //not implemented yet

        } else {
            this.genSafetyDistanceMin();
            //safety_distance will always be at least the minimum distance to deccelerate to stop
            safety_distance = Math.abs(rand.nextGaussian() * safety_distance_min / 7.0)
                    + safety_distance_min;
            safety_distance = this.rounder(safety_distance);
        }

    }

    @Override
    public void accelerate(boolean accelerate) {
        //reaction time randomizes each time it is used to begin accelerating from stop
        //actual delay and ranomization reaction time must be handled in an outside class

        double acceleration;
        if (accelerate) {
            acceleration = acceleration_rate;
        } else {
            acceleration = deceleration_rate;
        }

        is_accelerating = true;

        position[0] += speed[0] * TCSConstant.TIMEINCREMENTS
                + 1.0 / 2 * acceleration * TCSConstant.TIMEINCREMENTS * TCSConstant.TIMEINCREMENTS * Math.cos(Math.toRadians(direction));
        position[0] = this.rounder(position[0]);
        speed[0] += acceleration * TCSConstant.TIMEINCREMENTS * Math.abs(Math.cos(Math.toRadians(direction)));
        speed[0] = this.rounder(speed[0]);

        position[1] += speed[1] * TCSConstant.TIMEINCREMENTS
                + 1.0 / 2 * acceleration * TCSConstant.TIMEINCREMENTS * TCSConstant.TIMEINCREMENTS * Math.sin(Math.toRadians(direction));
        position[1] = this.rounder(position[1]);
        speed[1] += acceleration * TCSConstant.TIMEINCREMENTS * Math.abs(Math.sin(Math.toRadians(direction)));
        speed[1] = this.rounder(speed[1]);

        //just a precaution in case estimating the decelerating to stop gives a negative speed
        if (speed[0] * Math.abs(Math.cos(Math.toRadians(direction))) < 0
                || speed[1] * Math.abs(Math.sin(Math.toRadians(direction))) < 0) {
            speed[0] = 0;
            speed[1] = 1;
        }

        updateSafetyDistance();
        time_moving += TCSConstant.TIMEINCREMENTS;
        is_accelerating = false;
    }

    @Override
    public void setTurningConstants(double[] destination, boolean accelerate) {
        //slighty dimished acceleration rate to more realistically model a turn
        //also acceleration is assumed to be constant
        if (accelerate) {
            turning_acceleration = 2.0 / 3.0 * this.acceleration_rate;
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

    @Override
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
        if (turning_velocity < 0) {
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
                    * (turn_radius * Math.abs(Math.cos(Math.toRadians(this.direction
                            - neg_or_pos * 90))) - turn_pos[0]);

            position[1] += Math.sin(Math.toRadians(this.turn_initial_direction + direction)) * (turn_radius - turn_pos[1] - Math.abs(turn_radius
                    * Math.sin(Math.toRadians(this.direction - neg_or_pos * 90))));

            //starting facing up or down
        } else {
            int neg_or_pos = (int) (-1 * Math.cos(Math.toRadians(this.turn_initial_direction + direction))
                    * Math.sin(Math.toRadians(this.turn_initial_direction)));

            this.direction += neg_or_pos * Math.toDegrees(angle_increment);

            position[0] += Math.cos(Math.toRadians(this.turn_initial_direction + direction)) * (turn_radius - turn_pos[0] - Math.abs(turn_radius
                    * Math.cos(Math.toRadians(this.direction - neg_or_pos * 90))));

            position[1] += Math.sin(Math.toRadians(this.turn_initial_direction))
                    * (turn_radius * Math.abs(Math.sin(Math.toRadians(this.direction
                            - neg_or_pos * 90))) - turn_pos[1]);

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

    public double getTurnSafetyAngleMin() {

        return turn_safety_angle_min;
    }

    @Override
    public void updateTurnSafetyAngle() {

    }

    //Getters
    @Override
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
    public double[] estimateBreakingPoint(double x_value, double y_value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] estimateBreakingPoint(Vehicle v) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getAutomated() {
        return false;
    }

    @Override
    public void decelerate(double[] pos) {
        // what kind of random variable do I want...
        double ax = -this.rounder(Math.pow(speed[0], 2) / (2 * pos[0] - position[0]) + rand.nextGaussian() / 10);
        double ay = -this.rounder(Math.pow(speed[1], 2) / (2 * pos[1] - position[1]) + rand.nextGaussian() / 10);

        double deltaPosX = (speed[0] * TCSConstant.TIMEINCREMENTS + 1.0 / 2 * ax * TCSConstant.TIMEINCREMENTS * TCSConstant.TIMEINCREMENTS);
        position[0] += deltaPosX;
        position[0] = this.rounder(position[0]);
        speed[0] += this.rounder(ax * TCSConstant.TIMEINCREMENTS);

        double deltaPosY = (speed[1] * TCSConstant.TIMEINCREMENTS + 1.0 / 2 * ax * TCSConstant.TIMEINCREMENTS * TCSConstant.TIMEINCREMENTS);
        position[1] += deltaPosY;
        position[1] = this.rounder(position[1]);
        speed[1] += this.rounder(ay * TCSConstant.TIMEINCREMENTS);
        updateSafetyDistance();
        time_moving += TCSConstant.TIMEINCREMENTS;
    }

}

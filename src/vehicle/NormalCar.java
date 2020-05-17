/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.Arrays;
import java.util.Random;
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

    public NormalCar(double[] position, double direction) {
        rand = new Random();
        speed[0] = 0;
        speed[1] = 0;
        //created a method to generate random speed limit so it is consistent with other variables
        //speed_limit = AUTOMATEDFINALVELOCITY + rand.nextGaussian();
        safety_distance_min = 0;
        safety_distance = 0;
        time_moving = 0;
        is_turning = false;
        turn_tangential_acceleration = 0;
        turn_tangential_acceleration = 0;
        turn_tangential_deceleration = 0;
        turn_radius = 0;
        turn_tangential_speed = 0;
        turn_initial_position = new double[2];
        turn_initial_direction = 0;
        turn_safety_angle_min = 0;
        turn_safety_angle = 0;
        size = new double[2];

        this.genRandSize();
        this.genReactionTimeMean();
        this.genAccelerationMean();
        this.genDecelerationMean();

        this.genRandAcceleration();
        this.genRandDeceleration();
        this.genRandReactionTime();
        this.genRandSpeedLimit();

        this.position = position.clone();
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
            this.genSafetyDistanceMin();
            //safety_distance will always be at least the minimum distance to deccelerate to stop
            safety_distance = Math.abs(rand.nextGaussian() * safety_distance_min / 13.0)
                    + safety_distance_min;
            safety_distance = this.rounder(safety_distance);
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

        position[0] += (speed[0] * TCSConstant.TIMEINCREMENTS + 1.0 / 2 * acceleration 
                * TCSConstant.TIMEINCREMENTS * TCSConstant.TIMEINCREMENTS) * Math.cos(Math.toRadians(direction));
        position[0] = this.rounder(position[0]);
        speed[0] += acceleration * TCSConstant.TIMEINCREMENTS * Math.abs(Math.cos(Math.toRadians(direction)));
        speed[0] = this.rounder(speed[0]);

        position[1] += (speed[1] * TCSConstant.TIMEINCREMENTS + 1.0 / 2 * acceleration 
                * TCSConstant.TIMEINCREMENTS * TCSConstant.TIMEINCREMENTS) * Math.sin(Math.toRadians(direction));;
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
    public void setTurningConstants(double[] destination) {
        //slighty dimished acceleration/deceleration rate to more realistically model a turn
        //also they are assumed to be constant
        turn_tangential_acceleration = 2.0 / 3.0 * this.acceleration_rate;
        turn_tangential_deceleration = 2.0 / 3.0 * this.deceleration_rate;

        turn_tangential_speed = this.getDirectionalSpeed() * 3.0 / 4.0;

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
            this.setTurningConstants(destination);
            //for testing:
            //System.out.println("I ran. Position: " + Arrays.toString(position));
        }
        is_turning = true;

        double turning_acceleration_value = turn_tangential_acceleration;
        if (!accelerate) {
            turning_acceleration_value = turn_tangential_deceleration;
        }
        
        //the change of angle is used to model acceleration
        //the change is calculated based on one time increment of turning
        turn_tangential_speed += turning_acceleration_value * time_increment;
        if (turn_tangential_speed < 0) {
            turn_tangential_speed = 0;
        }

        double angle_increment = turn_tangential_speed * time_increment / turn_radius;
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

        time_moving += time_increment;
        updateTurnSafetyAngle();
        
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
                position[0] = destination[0] + size[0] * Math.cos(Math.toRadians(this.direction));
                position[1] = destination[1];
                speed[0] = turn_tangential_speed;
                speed[1] = 0;
            } else {
                position[1] = destination[1] + size[0] * Math.sin(Math.toRadians(this.direction));
                position[0] = destination[0];
                speed[1] = turn_tangential_speed;
                speed[0] = 0;
            }

            is_turning = false;
            
            //when turn is called for the last time, it switches to treating the car as traveling straight
            updateSafetyDistance();
        }

    }

    public void genTurnSafetyAngleMin() {
        double angular_deceleration = this.turn_tangential_deceleration / this.turn_radius; 
        double angular_speed = this.turn_tangential_speed / this.turn_radius;
        this.turn_safety_angle_min = Math.pow(angular_speed, 2) / (-2 * angular_deceleration);
    }

    @Override
    public void updateTurnSafetyAngle() {
            this.genTurnSafetyAngleMin();
            //turn_safety_angle will always be at least the minimum angle to deccelerate to stop
            turn_safety_angle = Math.abs(rand.nextGaussian() * turn_safety_angle_min / 7.0)
                    + turn_safety_angle_min;
            turn_safety_angle = this.rounder(turn_safety_angle);
    }
    
    @Override
    public void decelerateToStop(double[] pos) {
        double ax_avg = -Math.pow(speed[0], 2) / (2 * pos[0] - position[0]);
        double ax = this.rounder(rand.nextGaussian()* ax_avg / 10.0 + ax_avg);
        
        double ay_avg = -Math.pow(speed[1], 2) / (2 * pos[1] - position[1]);
        double ay = this.rounder(rand.nextGaussian()* ay_avg / 10.0 + ay_avg);
        
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
    public boolean isAutomated() {
        return false;
    }

    
    
    
    @Override
    public double[] estimateBrakingPoint(double x_value, double y_value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] estimateBrakingPoint(Vehicle v) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}

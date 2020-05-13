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
public abstract class Vehicle implements TCSConstant {

    protected double[] speed = new double[2];//{speed left/right, speed up/down} in m/s (always positive)               
    protected double[] position;             //Position as the top of the car but half of the , in m
    protected double[] size;                 //{length, width} in m
    protected double[] breakingPoint;

    protected double acceleration_rate;      //in m/s^2
    protected double deceleration_rate;      //in m/s^2
    protected double direction;              //degrees, 0° -> right, 90° -> up, etc. Should always be positive     
    protected double speed_limit;            //in m/s, set for AutomatedCar, randomized for NormalCar
    protected double safety_distance;        //distance needed for car to safely decelerate to 0
    protected double time_moving;            //total time vehicle has been moving, helpful for testing/checking
    protected double reaction_time;          //time it takes vehicle to begin accelerating from stop

    protected boolean is_accelerating;       //true if accelerate method is running
    protected boolean is_turning;            //true if car is turning in intersection

    //temporary turning constants (only called before a car starts a turn)
    protected double turning_acceleration;   //acceleration value used only for turning
    protected double turn_radius;            //radius of quarter circle modeling turn
    protected double turning_velocity;       //calculated from turning_acceleration value
    protected double[] turn_initial_position;//position before turn (with center at limit line)
    protected double turn_initial_direction; //direction car was facing before turn
    protected double turn_safety_angle;

    protected Random rand = new Random(100); // Instead of initializing random in each car class, it can be created here.

    protected final double buffer = BUFFER;  //gap between cars when stopped, in m
    protected final int rounded_dec_pos = ROUNDEDDECPOS;     //the decimal position accuracy of functions
    protected final double time_increments = TIMEINCREMENTS; //milliseconds 

    //Abstract functions:
    //simple acceleration function, updates position, speed, saftey_distance, time_moving
    //accelerate = true to accelerate & accelerate = false to decelerate
    public abstract void accelerate(boolean accelerate);

    public abstract void decelerate(double[] pos);

    public abstract boolean getAutomated();
    
    //calculates and assigns new saftey distance based on current speed
    public abstract void updateSafetyDistance();

    //assigns each car a random acceleration rate
    public abstract void genRandAcceleration();

    //assigns each car a random deceleration rate
    public abstract void genRandDeceleration();

    public abstract void genRandReactionTime();

    public abstract double[] estimateBreakingPoint(double x_value, double y_value);

    public abstract double[] estimateBreakingPoint(Vehicle v);

    //used when turn() is first called, sets starting constants so the function 
    //can be called on many times and edit Vehicle vairables
    public abstract void setTurningConstants(double[] destination, boolean accelerate);

    //      - Direction is 90 for left turn and -90 for right turn
    //      - Destination is the middle of the limit line of the lane the car should turn to 
    //      - Accounts for the back half of the car still being in the intersection 
    // after the car has reached the destination by moving car forward till back 
    // is past the limit line
    //      - Only use method once car has front tires at limit line
    //      - When the car begins turn, is_turning will be set to true and once the car 
    // has finished the turn, is_turning will be set to false
    //accelerate = true to accelerate & accelerate = false to decelerate
    public abstract void turn(int direction, double[] destination, boolean accelerate);
    
    public abstract void updateTurnSafetyAngle();
    //Non-abstract functions:
    
    //this will make graphing in matlab much simpler
    public double[] getLeftBottomCornerPos(){
        double[] pos = new double[2];
        double dist = Math.sqrt(Math.pow(size[0], 2.0) + Math.pow(size[1], 2.0));
        double angle = direction + Math.toDegrees(Math.atan((size[1]/2.0)/size[0]));
        pos[0] = position[0] - dist * Math.cos(Math.toRadians(angle));
        pos[1] = position[1] - dist * Math.sin(Math.toRadians(angle));
        return pos;
    }
    
    public double[] getCenterPos(){
        double[] pos = new double[2];
        pos[0] = position[0] - size[0] / 2.0 * Math.cos(Math.toRadians(direction));
        pos[1] = position[1] - size[0] / 2.0 * Math.sin(Math.toRadians(direction));
        return pos;
    }

        //distance from front bumper of car to back bumper of front car plus buffer
    public double getDistanceFromFrontVehicle(Vehicle front_car) {
        if (front_car.isTravelingHorizontal()) {
            return Math.abs(this.position[0] - front_car.position[0])
                    - front_car.size[0] + buffer;
        }
        return Math.abs(this.position[1] - front_car.position[1])
                - front_car.size[1] + buffer;
    }
    
    //car will travel for one increment at it's speed limit value with no acceleration
    public void travelWithConstantSpeed(){
        double acceleration = this.acceleration_rate;
        this.acceleration_rate = 0;
        speed[0] = rounder(speed_limit * Math.cos(Math.toRadians(direction)));
        speed[1] = rounder(speed_limit * Math.sin(Math.toRadians(direction)));
        accelerate(true);

        this.acceleration_rate = acceleration;
    };
    
    //runs a turn increment with no acceleration
    //car will continue along turn for one increment with whatever speed it currently has
    public void turnWithConstantSpeed(int direction, double[] destination) {
        double turning_acceleration = this.turning_acceleration;
        double acceleration_rate = this.acceleration_rate;
        this.turning_acceleration = 0;
        this.acceleration_rate = 0;

        turn(direction, destination, true);

        this.turning_acceleration = turning_acceleration;
        this.acceleration_rate = acceleration_rate;
    }
    
    //no implementation yet
    //for use when car is turning
    public double getAngleFromTurningVehicle(Vehicle front_car){
        
    };

    
        
    /*
    public void move(double direction) {
        if (this.direction == direction) {
            if (Math.sqrt(Math.pow(speed[0], 2) + Math.pow(speed[1], 2)) < speed_limit) {
                accelerate(true);
            } else {
                travelWithConstantSpeed();
            }
        }
    }*/

    //returns true vehicle is facing east or west
    public boolean isTravelingHorizontal() {
        if (direction == 0 || direction == 180) {
            return true;
        }
        return false;
    }

    /* Once we have turning. I will implement this method.
    public void genRandDestination() {
        
    }
     */
    public boolean isTravelingVertical() {
        if (direction == 90 || direction == 270) {
            return true;
        }
        return false;
    }

    public double rounder(double num) {
        num = num * Math.pow(10, rounded_dec_pos);
        num = Math.round(num);
        return num / Math.pow(10, rounded_dec_pos);
    }

    public boolean isAccelerating() {
        return is_accelerating;
    }

    public boolean isTurning() {
        return is_turning;
    }

    public boolean isStopped() {
        if (this.getDirectionalSpeed() == 0) {
            return true;
        }
        return false;
    }

    public double getDirection() {
        return direction;
    }

    //returns char value for direction
    //'!' means car is not travelling in one of the cardinal directions
    public char getCharDirection() {
        switch ((int) direction) {
            case 0:
                return 'r';
            case 90:
                return 'u';
            case 180:
                return 'l';
            case 270:
                return 'd';
            default:
                return '!';
        }
    }

    public double[] getPosition() {
        return position;
    }

    public double getSpeedLimit() {
        return speed_limit;
    }
    
    //the position component that is changing
    public double getDirectionalPos() {
        if (direction == 0 || direction == 180) {
            return this.position[0];
        } else if (direction == 90 || direction == 270) {
            return this.position[1];
        }
        //leaving this in as an error message in case
        //might delete later
        System.out.println("This vehicle is not facing a cardinal direction");
        return 0;
    }

    //the speed component that is changing
    public double getDirectionalSpeed() {
        if (direction == 0 || direction == 180) {
            return this.speed[0];
        } else if (direction == 90 || direction == 270) {
            return this.speed[1];
        }
        //leaving this in as an error message in case
        //might delete later
        System.out.println("This vehicle is not traveling in a cardinal direction");
        return 0;
    }

    public double getSafetyDistance() {
        return safety_distance;
    }

    public double[] getSize() {
        return size;
    }

    public double[] getSpeed() {
        return speed;
    }

    public double getAcceleration_rate() {
        return acceleration_rate;
    }

    public double getTimeMoving() {
        return time_moving;
    }

    public double getBuffer() {
        return buffer;
    }

    public int getRoundedDecPos() {
        return rounded_dec_pos;
    }

    public double getReactionTime() {
        return reaction_time;
    }

    public void reduceReactionTimeUnit() {
        reaction_time -= TIMEINCREMENTS;
    }

    public void setPosition(double[] position) {
        this.position[0] = position[0];
        this.position[1] = position[1];
    }

    //The following functions are now obsolete with the way Vehicle classes function
    //But the logic is still helpful and might be able to be reused in other classes
    //See AutomatedCar for fleshed out functions and code
    /*
    
    public abstract void accelerateToSpeed(double speed) throws InterruptedException;
    
    public abstract void accelerateToSpeedLimit() throws InterruptedException;
    
    public abstract void decelerateToSpeed(double speed) throws InterruptedException;
    
    public abstract void decelerateToStop() throws InterruptedException;
    
    //vehicle will travel a certain distance to a stop, accelerating to speed limit 
    //until it needs to begin decelerating based on saftey distance 
    public abstract void travelDistanceToStop(double distance) throws InterruptedException;
     */
}

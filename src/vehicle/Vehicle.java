/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.Random;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author Christine
 */
public abstract class Vehicle implements TCSConstant {

    protected double[] speed = new double[2];//{speed left/right, speed up/down} in m/s (always positive)               
    protected double[] position;             //Position as center of front of car, in m
    protected double[] size;                 //{length, width} in m
    protected double[] braking_point;
    protected double[] des = new double[2];

    protected double acceleration_rate;      //in m/s^2
    protected double deceleration_rate;      //in m/s^2
    protected double direction;              //degrees, 0° -> right, 90° -> up, etc. Should always >= 0 and < 360  
    protected double speed_limit;            //in m/s, set for AutomatedCar, randomized for NormalCar
    protected double safety_distance;        //distance needed for car to decelerate to stop
    protected double time_moving;            //total time vehicle has been moving, helpful for testing/checking
    protected double reaction_time;          //time it takes vehicle to begin accelerating from stop

    protected boolean is_accelerating;       //true if accelerate method is running
    protected boolean is_turning;            //true if car is turning in intersection

    //turning constants (called before a car starts a turn)
    protected double turn_tangential_acceleration;   //acceleration value used only for turning
    protected double turn_tangential_deceleration;   //deceleration value used only for turning
    protected double turn_radius;            //radius of quarter circle modeling turn
    protected double turn_tangential_speed;  //used to calculate angle increment
    protected double[] turn_initial_position;//position before turn
    protected double turn_initial_direction; //direction car was facing before turn
    protected double turn_safety_angle;      //angle needed for car to decelerate to stop while turning

    protected static Random rand = new Random(100); // Instead of initializing random in each car class, it can be created here.

    protected final double buffer = BUFFER;  //gap between cars when stopped, in m
    protected final int rounded_dec_pos = ROUNDEDDECPOS;     //the decimal position accuracy of functions
    protected final double time_increment = TIMEINCREMENTS; //milliseconds 

    //Abstract functions:
    //true for AutomatedCar, false for NormalCar
    public abstract boolean isAutomated();

    //simple acceleration function, based on time_increment 
    //updates position, speed, saftey_distance, time_moving
    //accelerate = true to accelerate & accelerate = false to decelerate
    public abstract void accelerate(boolean accelerate);

    //calculates deceleration rate needed for car to come to stop at pos
    //based on time_increment
    public abstract void decelerateToStop(double[] pos);

    //calculates and assigns new saftey distance based on current speed
    public abstract void updateSafetyDistance();

    //assigns each car a random acceleration rate
    public abstract void genRandAcceleration();

    //assigns each car a random deceleration rate
    public abstract void genRandDeceleration();

    //random for NormalCar, 0 for AutomatedCar
    public abstract void genRandReactionTime();

    public abstract double[] estimateBrakingPoint(double x_value, double y_value);

    public abstract double[] estimateBrakingPoint(Vehicle front_vehicle);

    //used when turn() is first called, sets starting constants so the function 
    //can be called on many times and edit Vehicle vairables
    public abstract void setTurningConstants(double[] destination);

    //  Direction is 90 for left turn and -90 for right turn
    //  Destination is the middle of the limit line of the lane the car should turn to 
    //  Only use method once car has front tires at limit line
    //  When the car begins turn, is_turning will be set to true and turn constants 
    //will be set and once the car has finished the turn, is_turning will be set to false
    //  accelerate = true to accelerate & accelerate = false to decelerate
    public abstract void turn(int direction, double[] destination, boolean accelerate);

    //calculates and assigns new saftey angle based on current speed while turning
    public abstract void updateTurnSafetyAngle();

    //Non-abstract functions:
    //this will make graphing in matlab much simpler
    public double[] getLeftBottomCornerPos() {
        double[] pos = new double[2];
        double dist = Math.sqrt(Math.pow(size[0], 2.0) + Math.pow(size[1], 2.0));
        double angle = direction + Math.toDegrees(Math.atan((size[1] / 2.0) / size[0]));
        pos[0] = position[0] - dist * Math.cos(Math.toRadians(angle));
        pos[1] = position[1] - dist * Math.sin(Math.toRadians(angle));
        return pos;
    }

    public double[] getCenterPos() {
        double[] pos = new double[2];
        pos[0] = position[0] - size[0] / 2.0 * Math.cos(Math.toRadians(direction));
        pos[1] = position[1] - size[0] / 2.0 * Math.sin(Math.toRadians(direction));
        return pos;
    }

    public void genRandSize() {
        size[0] = this.rounder(rand.nextGaussian() * LENGTHAVG / 25.0 + LENGTHAVG);
        if (size[0] < LENGTHMIN) {
            size[0] = LENGTHMIN;
        }

        size[1] = rand.nextGaussian() * WIDTHAVG / 25.0 + WIDTHAVG;
        if (size[1] > WIDTHMAX) {
            size[1] = WIDTHMAX;
        } else if (size[1] < WIDTHMIN) {
            size[1] = WIDTHMIN;
        }

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

    //Checks distance between car and the car in front of it. Straight only.
    public boolean distanceCheck(Vehicle front_vehicle) {
        double[] point = new double[2];
        point[0] = front_vehicle.getPosition()[0] - front_vehicle.getSize()[0] * Math.cos(Math.toRadians(front_vehicle.direction));
        point[1] = front_vehicle.getPosition()[1] - front_vehicle.getSize()[1] * Math.sin(Math.toRadians(front_vehicle.direction));
        double distance = Math.sqrt(Math.pow(point[0] - position[0], 2) + Math.pow(point[1] - position[1], 2));
        if (distance < safety_distance) {
            return false;
        } else {
            return true;
        }
    }

    //car will travel for one increment at it's speed limit value with no acceleration
    public void travelWithConstantSpeed() {
        double acceleration = this.acceleration_rate;
        this.acceleration_rate = 0;
        speed[0] = rounder(speed_limit * Math.cos(Math.toRadians(direction)));
        speed[1] = rounder(speed_limit * Math.sin(Math.toRadians(direction)));
        accelerate(true);

        this.acceleration_rate = acceleration;
    }

    ;
    
    //runs a turn increment with no acceleration
    //car will continue along turn for one increment with whatever speed it currently has
    public void turnWithConstantSpeed(int direction, double[] destination) {
        double turning_acceleration = this.turn_tangential_acceleration;
        double acceleration_rate = this.acceleration_rate;
        this.turn_tangential_acceleration = 0;
        this.acceleration_rate = 0;

        turn(direction, destination, true);

        this.turn_tangential_acceleration = turning_acceleration;
        this.acceleration_rate = acceleration_rate;
    }

    //for use when car is turning
    public double getAngleFromTurningFrontVehicle(Vehicle front_car) {
        if (front_car.is_turning) {
            return Math.abs(front_car.direction - this.direction);
        }
        return 0;
    }

    ;

    public void changePosition(double[] pos) {
        this.position = pos;
    }

    public void changeXPosition(double pos_x) {
        this.position[0] = pos_x;
    }

    public void changeYPosition(double pos_y) {
        this.position[1] = pos_y;
    }

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

    public void setAccelerating(boolean is_accelerating) {
        this.is_accelerating = is_accelerating;
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

    public double getDeceleration_rate() {
        return deceleration_rate;
    }

    public double getDeceleration_rate(double[] destination) {
        double ax, ay;
        if (destination[0] != position[0]) {
            ax = -Math.pow(speed[0], 2) / (2 * destination[0] - position[0]);
        } else {
            ax = 0;
        }
        if (destination[1] != position[1]) {
            ay = -Math.pow(speed[1], 2) / (2 * destination[1] - position[1]);
        } else {
            ay = 0;
        }

        return Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2));
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

    public void setDestination(double[] stackPos) {
        des = stackPos.clone();
    }

    public double[] getDestination() {
        return des;
    }

    public void setSpeed(double[] speed) {
        this.speed = speed.clone();
    }
}

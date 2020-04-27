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
public class NormalCar extends Vehicle implements  TCSConstant{
    protected double reaction_time;       //constantly updating based on fixed random range and mean
    protected double reaction_time_mean;  //randomly generated once, then fixed
    protected double acceleration_mean;   //randomly generated once, then fixed
    protected double decceleration_mean;  //randomly generated once, then fixed
    protected Random rand;
    
    public NormalCar(double[] position, double[] size, int direction) {
        rand = new Random();
        speed[0] = 0;
        speed[1] = 0;
        safety_distance = 0;
        time_moving = 0;
        
        this.genRandMeans();
        this.genRandAcceleration();
        this.genRandDecceleration();
        this.genRandReactionTime();
        
        this.position = position;
        this.size = size;
        this.direction = direction;
    }
    
    //randomly generates a mean value for each changing variable
    public void genRandMeans(){
        reaction_time_mean = rand.nextGaussian()*0.3 + REACTIONTIMEAVG;
        
        //need to calculate decceleration and acceleration rates based onn size
        //I will do some research to find averages to generate a function from
        
    }
    
    //may mess with the variance for the next three methods
    //will asign a new rand acceleration based on mean, 
    public void genRandAcceleration(){
        acceleration_rate = rand.nextGaussian()*acceleration_mean/5 + acceleration_mean;
    }
    
    //will asign a new rand decceleration based on mean
    public void genRandDecceleration(){
        decceleration_rate = rand.nextGaussian()*decceleration_mean/5 + decceleration_mean;
    }
    
    //will asign a new rand reaction time based on mean
    public void genRandReactionTime(){
        reaction_time = rand.nextGaussian()*0.1 + reaction_time_mean;
    }
    
    
    
    

    public void accelerate(double time, double acceleration) throws InterruptedException {
        
    }

    public void updateSafetyDistance() {
        
    }

    public void turn(int direction) {
        //no implementation yet
    }


    public double getDistanceFromFrontVehicle(Vehicle front_car) {
        if (front_car.isTravelingHorizontal()) {
            return Math.abs(this.position[0] - front_car.position[0]) - front_car.size[0] + buffer;
        }
        return Math.abs(this.position[1] - front_car.position[1]) - front_car.size[0] + buffer;
    }

    public double getDistanceFromLimitLine(Lane lane) {
        //no implementation yet
        return distance;
    }
    
    
    //Getters
    
    public double getReactionTime(){
        return reaction_time;
    }
    
    public double getReactionTimeMean(){
        return reaction_time_mean;
    }
    
    public double getAccelerationMean(){
        return acceleration_mean;
    }
    
    public double getDeccelerationMean(){
        return decceleration_mean;
    }


}

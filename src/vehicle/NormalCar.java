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
    protected double deceleration_mean;  //randomly generated once, then fixed
    
    public NormalCar(double[] position, double[] size, int direction) {
        rand = new Random();
        speed[0] = 0;
        speed[1] = 0;
        safety_distance = 0;
        time_moving = 0;
        
        this.genRandMeans();
        this.genRandAcceleration();
        this.genRandDeceleration();
        this.genRandReactionTime();
        
        this.position = position;
        this.size = size;
        this.direction = direction;
    }
    
    //randomly generates a mean value for each changing variable
    public void genRandMeans(){
        reaction_time_mean = rand.nextGaussian()*0.3 + REACTIONTIMEAVG;
        acceleration_mean = rand.nextGaussian()*ACCELERATIONAVG/8 + ACCELERATIONAVG;
        deceleration_mean = rand.nextGaussian()*DECELERATIONAVG/8 + DECELERATIONAVG;

    }
    
    //may mess with the variance for the next three methods
    //will asign a new rand acceleration based on mean, 
    public void genRandAcceleration(){
        acceleration_rate = rand.nextGaussian()*acceleration_mean/5 + acceleration_mean;
    }
    
    //will asign a new rand deceleration based on mean
    public void genRandDeceleration(){
        deceleration_rate = rand.nextGaussian()*deceleration_mean/5 + deceleration_mean;
    }
    
    //will asign a new rand reaction time based on mean
    public void genRandReactionTime(){
        reaction_time = rand.nextGaussian()*0.1 + reaction_time_mean;
    }
    
    
    
    

    public void accelerate(double time, double acceleration) throws InterruptedException {
        //reaction time randomizes each time it is used to begin accelerating from stop
        //actual delay from reaction time must be handled in an outside class
        if (this.isStopped()){
            this.genRandReactionTime();
        }
        
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
        time_moving += time;
        is_accelerating = false;
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
    
    public double getDecelerationMean(){
        return deceleration_mean;
    }

    @Override
    public void accelerateToSpeed(double speed) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accelerateToSpeedLimit() throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void decelerateToSpeed(double speed) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void decelerateToStop() throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void travelDistanceToStop(double distance) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAccelerationRate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDecelerationRate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double timeToStop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int incrementsToStop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double timeToSpeedLimit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int incrementsToSpeedLimit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}

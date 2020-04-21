/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Christine
 */
public class Normal_Car extends Vehicle{
    private long reaction_time;
    
    public Normal_Car(double[] position, double[] size, char direction) {
        speed[0] = 0;
        speed[1] = 0;
        acceleration_rate = 6;
        decceleration_rate = 9;
        safety_distance = buffer;
        reaction_time = 2;
        this.position = position;
        this.size = size;
        this.direction = direction;
    }

    public void accelerate(long time) throws InterruptedException {
        accelerating = true;
        double rand_acceleration_rate = randGenerator(acceleration_rate*0.9, acceleration_rate);
        switch (direction){
            case 'r':
                double deltaPosX = speed[0] * time + 1.0 / 2 * rand_acceleration_rate * time * time;
                position[0] += deltaPosX;
                speed[0] += rand_acceleration_rate*time;
                break;
            case 'l':
                deltaPosX = speed[0] * time + 1.0 / 2 * rand_acceleration_rate * time * time;
                position[0] -= deltaPosX;
                speed[0] += rand_acceleration_rate*time;
                break;
            case 'd':
                double deltaPosY = speed[1] * time + 1.0 / 2 * rand_acceleration_rate * time * time;
                position[1] += deltaPosY;
                speed[1] += rand_acceleration_rate*time;
                break;
            case 'u':
                deltaPosY = speed[1] * time + 1.0 / 2 * rand_acceleration_rate * time * time;
                position[1] -= deltaPosY;
                speed[1] += rand_acceleration_rate*time;
                break;
        }
        updateSafetyDistance();
        TimeUnit.MILLISECONDS.sleep(time);
        accelerating = false;
    }
    
    public void accelerateFromRest(long time) throws InterruptedException{
        long rand_time = (long) randGenerator(reaction_time*0.9, reaction_time*1.1);
        accelerate(time);
    }

    public void updateSafetyDistance() {
        safety_distance = speed[0] * speed[0] / (2 * decceleration_rate) + buffer;
    }

    public void accelerateToSpeedLimit(long time_increments) throws InterruptedException {
        double speed_dir;
        if (direction == 'l' || direction == 'r'){
            speed_dir = speed[0];
        } else {
            speed_dir = speed[1];
        }
        while (speed_dir < speedLimit){
            accelerate(time_increments);
        }
    }
    
    public double randGenerator(double min, double max){
        return min + (max-min)*Math.random();
    }
    
}

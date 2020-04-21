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
public class Automated_Car extends Car{
    public Automated_Car(double[] position, double[] size, char direction) {
        speed[0] = 0;
        speed[1] = 0;
        acceleration_rate = 6;
        decceleration_rate = 9;
        safety_distance = buffer;
        this.position = position;
        this.size = size;
        this.direction = direction;
    }

    public void accelerate(long time) throws InterruptedException {
        accelerating = true;
        switch (direction){
            case 'r':
                double deltaPosX = speed[0] * time + 1.0 / 2 * acceleration_rate * time * time;
                position[0] += deltaPosX;
                speed[0] += acceleration_rate*time;
                break;
            case 'l':
                deltaPosX = speed[0] * time + 1.0 / 2 * acceleration_rate * time * time;
                position[0] -= deltaPosX;
                speed[0] += acceleration_rate*time;
                break;
            case 'd':
                double deltaPosY = speed[1] * time + 1.0 / 2 * acceleration_rate * time * time;
                position[1] += deltaPosY;
                speed[1] += acceleration_rate*time;
                break;
            case 'u':
                deltaPosY = speed[1] * time + 1.0 / 2 * acceleration_rate * time * time;
                position[1] -= deltaPosY;
                speed[1] += acceleration_rate*time;
                break;
        }
        updateSafetyDistance();
        TimeUnit.MILLISECONDS.sleep(time);
        accelerating = false;
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

}


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
public class NormalCar extends Vehicle{
    private double reaction_time;
    
    public NormalCar(double[] position, double[] size, int direction) {
        speed[0] = 0;
        speed[1] = 0;
        safety_distance = 0;
        time_moving = 0;
        
        this.genRandAccelerationRate();
        this.genRandDecclerationRate();
        this.genRandReactionTime();
        
        this.position = position;
        this.size = size;
        this.direction = direction;
    }
    

    
}

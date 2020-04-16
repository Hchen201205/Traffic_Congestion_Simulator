/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Christine
 */
public class Light {
    boolean start;
    private int[] changing_time;
    private int color;

    public Light() {
        start = false;
        color = 0;
        changing_time = new int[3];
    }

    public void setSchangeTimes(int rtg, int gty, int ytr) {
        changing_time[0] = rtg;
        changing_time[1] = gty;
        changing_time[2] = ytr;
    }

    public int[] getChangeTimes() {
        return changing_time;
    }
    
    public int getColor(){
        return color;
    }
    
    public void changeColor(){
        if (color == 2){
            color = 0;
        } else {
            color++;
        }
    }
    
    public void startCycle() throws InterruptedException{
        start = true;
        while (start){
            TimeUnit.SECONDS.sleep(changing_time[color]);
            changeColor();
        }
    }
    
    public void endCycle() {
        start = false;
    }

}



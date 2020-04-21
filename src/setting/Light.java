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
    char direction;                //'n' 's' 'e' or 'w'
    private int[] change_times;    //array of light cycle timing between color changes (milliseconds)
    private int color;             //0 = red, 1 = green, 2 = yellow
    private double time_remaining; //time until next color change
    
    private final double time_increments = 0.1;
            
    public Light(char direction) {
        this.direction = direction;
        start = false;
        color = 0;
        change_times = new int[3];
        time_remaining = 0;
    }
    
    //runs light in real time based on change times
    public void startCycle() throws InterruptedException{
        start = true;
        while (start){
            for (double i = 0; i < change_times[color]; i += time_increments) {
                time_remaining = change_times[color] - i;
                long sleep_time = (long) (time_increments * 1000);
                TimeUnit.MICROSECONDS.sleep(sleep_time);
            }           
            changeColor();
        }
    }
    
    public void endCycle() {
        start = false;
    }
    
    public void changeColor(){
        if (color == 2){
            color = 0;
        } else {
            color++;
        }
    }
    
    //Getters and Setters
    
    //setting cycle times
    public void setChangeTimes(int rtg, int gty, int ytr) {
        change_times[0] = rtg;
        change_times[1] = gty;
        change_times[2] = ytr;
    }

    public int[] getChangeTimes() {
        return change_times;
    }
    
    //returns string color value
    public String getColor(){
        if (color == 0){
            return "red";
        } else if (color == 1){
            return "green";
        } 
        return "yellow";
    }
    
    //returns int color value
    public int getColorInt(){
        return color;
    }
    
    public double getTimeRemaining(){
        return time_remaining;
    }
}
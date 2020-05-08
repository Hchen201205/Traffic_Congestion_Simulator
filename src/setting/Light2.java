/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.awt.Color;
import java.util.Arrays;
import traffic_congestion_simulator.TCSConstant;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Christine
 */
public class Light2 implements TCSConstant {

    boolean start;
    private double[] change_times;   //array of light cycle timing between color changes (milliseconds)
    private Color color;
    private double cycle_time;      //time that has passed in this light's current cycle;
                                    //resets after each complete cycle

    private boolean is_horizontal;      //if light is assigned to lanes facing left and right
    private boolean is_left_turn_light; //if light is assignedned to lanes facing up and down

    //  Add a color so that we can set which color the light is starting on
    //  Since lights need to be offset by specific calculated times, the cycle_time 
    //will no longer be assigned until setCycleTime is called
    public Light2(Color color, boolean horizontal, boolean left_turn) {
        this.color = this.color = color;
        this.is_horizontal = horizontal;
        this.is_left_turn_light = left_turn;
        start = false;
        change_times = new double[3];
    }

    
    /**
     * This is a method that will change the boolean start into true.
     */
    public void startCycle() {
        start = true;
    }

    //Runs a single increment of traffic light cycle.
    public void runCycleUnit() {
        if (start) {
            cycle_time += TIMEINCREMENTS;
            changeColor();

            //Need this line. Java has a rounding error without it...
            cycle_time = Math.round(cycle_time * Math.pow(10, ROUNDEDDECPOS)) / Math.pow(10, ROUNDEDDECPOS);

        }

    }

    public void endCycle() {
        start = false;
    }

    public void changeColor() {
        if (cycle_time == change_times[0]) {
            color = Color.GREEN;
        } else if (cycle_time == change_times[0] + change_times[1]) {
            color = Color.YELLOW;
        } else if (cycle_time == change_times[0] + change_times[1] + change_times[2]) {
            color = Color.RED;
            cycle_time = 0;
        }
    }

    //Getter and setters:
    
    //for properly offsetting lights from eachother when cycle starts
    public void setCycleTime(double time) {
        cycle_time = time;
        if (cycle_time > change_times[0] + change_times[1] + change_times[2]) {
            cycle_time -= change_times[0] + change_times[1] + change_times[2];
        } else if (cycle_time < 0) {
            cycle_time += change_times[0] + change_times[1] + change_times[2];
        }
    }

    public boolean getStart() {
        return start;
    }

    //setting cycle times
    public void setChangeTimes(double rtg, double gty, double ytr) {
        change_times[0] = rtg;
        change_times[1] = gty;
        change_times[2] = ytr;
    }

    public double[] getChangeTimes() {
        return change_times;
    }

    public String getColorString() {
        if (color.equals(Color.RED)) {
            return "Red";
        } else if (color.equals(Color.GREEN)) {
            return "Green";
        } else if (color.equals(Color.YELLOW)) {
            return "Yellow";
        } else {
            System.out.println("\n\n\tThis is the Light, there's something wrong with the color secion\n\n"); // I'm leaving a testing method here in case anything happens
            return "I don't know what this color is.";
        }
    }

    public Color getColor() {
        return color;
    }

    public boolean isHorizontal() {
        return this.is_horizontal;
    }

    public boolean isLeftTurnLight() {
        return this.is_left_turn_light;
    }

    public double getTimePassed() {
        return cycle_time;
    }

}

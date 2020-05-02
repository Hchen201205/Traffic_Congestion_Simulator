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
public class Light implements TCSConstant {

    boolean start;
    int direction;                //0 = 'n', 1 = 's', 2 = 'e' or 3 = 'w'
    private int[] change_times;   //array of light cycle timing between color changes (milliseconds)
    private Color color;
    private double time_passed;   // time that has passed in this cycle;
    
    private final int rounded_dec_pos = ROUNDEDDECPOS;
    
    //Creates a traffic light.
    public Light(int direction) {
        this.direction = direction;
        start = false;
        color = Color.RED;
        change_times = new int[3];
    }

    //Add a color so that we can set which color the light is starting on
    public Light(int direction, Color color) {
        this.direction = direction;
        start = false;
        this.color = color; // red -> 0; green -> 0 + red interval; yellow -> 0 + red interval + yellow interval;
        time_passed = 0;
        if (color == Color.GREEN) {
            time_passed = TCSConstant.LIGHTCYCLER;
        } else if (color == Color.YELLOW) {
            time_passed = TCSConstant.LIGHTCYCLER + TCSConstant.LIGHTCYCLEG;
        }
        change_times = new int[3];
    }

    //runs light in real time based on change times
    /**
     * This is a method that will change the boolean start into true.
     */
    public void startCycle() {
        start = true;
    }

    //A cycle of traffic light.
    public void runCycleUnit() {
        if (start) {
            time_passed += TIMEINCREMENTS;
            /* I'm deciding to not have these two lines. The reason for that is the light we are considering is not a whole. It's just a single light. 
        I will probably have this sleep in simulation class.
        long sleep_time = (long) (time_increments * 1000);
        TimeUnit.MICROSECONDS.sleep(sleep_time);
             */
            changeColor();

            // You need this line. Java has a rounding error if you don't include this.
            time_passed = Math.round(time_passed * Math.pow(10, rounded_dec_pos) / Math.pow(10, rounded_dec_pos));

        }

    }

    public void endCycle() {
        start = false;
    }

    public void changeColor() {
        if (time_passed == change_times[0]) {
            color = Color.GREEN;
        } else if (time_passed == change_times[0] + change_times[1]) {
            color = Color.YELLOW;
        } else if (time_passed == change_times[0] + change_times[1] + change_times[2]) {
            color = Color.RED;
            time_passed = 0;
        }
    }

    //Getters and Setters
    public boolean getStart() {
        return start;
    }

    //setting cycle times
    public void setChangeTimes(int rtg, int gty, int ytr) {
        change_times[0] = rtg;
        change_times[1] = gty;
        change_times[2] = ytr;
    }

    public int[] getChangeTimes() {
        return change_times;
    }

    public Color getColor() {
        return color;
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

    public char getDirection() {
        switch (direction) {
            case 0:
                return 'n';
            case 1:
                return 's';
            case 2:
                return 'e';
            default:
                return 'w';

        }
    }

    public double getTimePassed() {
        return time_passed;
    }

    /**
     * This is the testing code. Try it out. I believe there is no more
     * bug. When you confirm it just delete the following.
     *
     * @param arg
     */
    /*
    public static void main(String[] arg) {
        Light testing = new Light(1, Color.GREEN);
        System.out.println(testing.getColorString());
        System.out.println(testing.getDirection());
        testing.setChangeTimes(LIGHTCYCLER, LIGHTCYCLEG, LIGHTCYCLEY);
        System.out.println(Arrays.toString(testing.getChangeTimes()));
        System.out.println(testing.getTimePassed());
        testing.startCycle();
        for (int i = 0; i < 8000; i++) {
            testing.runCycleUnit();
            System.out.println(testing.getTimePassed());
        }
        System.out.println(testing.getColorString());
    }
     */
}

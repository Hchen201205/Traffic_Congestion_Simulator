/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.awt.Color;
import traffic_congestion_simulator.TCSConstant;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Christine
 */
public class Light2 {

    boolean start;
    int direction;                //0 = 'n', 1 = 's', 2 = 'e' or 3 = 'w'
    private int[] change_times;    //array of light cycle timing between color changes (milliseconds)
    private Color color;
    //private int color;             //0 = red, 1 = green, 2 = yellow
    //private double time_remaining; //time until next color change
    private double time_passed; // time that has pass in this cycle;

    private final double time_increments = 0.1;

    public Light2(int direction) {
        this.direction = direction;
        start = false;
        color = Color.RED;
        change_times = new int[3];
    }

    //Add a color so that we can set which color the light is starting on
    public Light2(int direction, Color color) {
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
        //time_remaining = 0;
    }

    //runs light in real time based on change times
    /**
     * This is a method that will change the boolean start into true.
     */
    public void startCycle() {
        start = true;
    }

    
    public void runCycleUnit() throws InterruptedException {
        time_passed += time_increments;
        long sleep_time = (long) (time_increments * 1000);
        TimeUnit.MICROSECONDS.sleep(sleep_time);
        changeColor();
    }

    public void endCycle() {
        start = false;
    }

    /**
     * This is the method that will return the start value. May be deleted
     * later.
     *
     * @return
     */
    public boolean getStart() {
        return start;
    }

    public void changeColor() {
        if (time_passed == change_times[0]) {
            color = Color.GREEN;
        } else if (time_passed == change_times[0] + change_times[1]) {
            color = Color.YELLOW;
        } else if (time_passed == change_times[0] + change_times[1] + change_times[2])  {
            color = Color.RED;
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
            System.out.println("\n\n\tThis is the Light, there's something wrong with the color secion\n\n"); // I'm leaving a testing method hear in case of anything happen.
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
    
    
    /**
     * This is a method that will run the cycle based on the boolean start value
     *
     * @throws InterruptedException
     */
    /*
    public void runCycle() throws InterruptedException {
        while (start) {
            for (double i = 0; i < change_times[color]; i += time_increments) {
                runCycleUnit(i);
            }
            changeColor();
        }
    }

    public void runCycleUnit(double total_increment) throws InterruptedException {
        time_remaining = change_times[color] - total_increment;
        long sleep_time = (long) (time_increments * 1000);
        TimeUnit.MICROSECONDS.sleep(sleep_time);
    }*/
    /*
    //returns string color value
    public String getColor() {
        if (color == 0) {
            return "red";
        } else if (color == 1) {
            return "green";
        }
        return "yellow";
    }*/

    /*
    //returns int color value
    public int getColorInt() {
        return color;
    }
*/
    /*
    public double getTimeRemaining() {
        return time_remaining;
    }
     */
    /*
    public void changeColor() {
        if (color == 2) {
            color = 0;
        } else {
            color++;
        }
    }
*/
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;

/**
 * TSConstant interface will act as a database.
 * It stores all the contents that this simulation needs.
 *
 * @author chenhanxi
 */
public interface TCSConstant {

    public final double TIMEINCREMENTS = 0.05; // Milliseconds in unit second

    public final int ROUNDEDDECPOS = 4; // Decimal Places

    //Vehicle classes constants
    public final double BUFFER = 1; // The space between two Vehicles when stop

    public final double LENGTHAVG = 4.0;

    public final double LENGTHMIN = 3.4;

    public final double WIDTHAVG = 1.8;

    public final double WIDTHMIN = 1.75;

    public final double WIDTHMAX = 2.4;

    public final double ACCELERATIONAVG = 3.6;

    public final double ACCELERATIONAVGMAX = 4.2;

    public final double DECELERATIONAVG = -2.2;

    public final double DECELERATIONAVGMAX = -3.2;

    public final double REACTIONTIMEAVG = 1;

    public final double AUTOMATEDFINALVELOCITY = 10.0;

    public final int FRAMEWIDTH = 600;

    public final int FRAMEHEIGHT = 600;

    public final int DRAWINGWIDTH = 400;

    public final int DRAWINGHEIGHT = 400;

    /*
    public final int LANELENGTH = 150;
    
    public final int LANEKWIDTH = 150;
     */
    // Need to modify
    public final int STARTINGPOSX = 0;

    public final int STARTINGPOSY = 0;

    public final int TESTSIZEX = 50;

    public final int TESTSIZEY = 30;

    public final int LIGHTPOSX = 150;

    public final int LIGHTPOSY = 150;

    // This value can be changed.
    public final double LIGHTCYCLEG = 20;

    // This value can be changed.
    public final double LIGHTCYCLEY = 3;

    public final double LIGHTCYCDEADTIME = 1;

    // We will fix this later.
    public final double[] LIGHTPOSITION = {310, 310};

    public final double[][] LANEPOSITION = {{275, 135}, {285, 135}, {295, 135},
    {465, 275}, {465, 285}, {465, 295},
    {325, 465}, {315, 465}, {305, 465},
    {135, 325}, {135, 315}, {135, 305}};

    // I Do this so that the size of each lane can be manipulated.
    public final double[][] LANESIZE = {{270, 10}, {270, 10}, {270, 10}, {270, 10}, {270, 10}, {270, 10}, {270, 10}, {270, 10}, {270, 10}, {270, 10}, {270, 10}, {270, 10}};

    public final double[] LANEDIRECTION = {270, 180, 90, 0};
    
    public final int[] LIGHTSEQUENCE = {0, 0, 1, 2, 2, 3, 0, 0, 1, 2, 2, 3};

    public final double[] LSIZE = {40, 3.5};

    public final int[] NUMOFLANE = {}; // NUMOFLANE can also be an indicator of number of light at a specific intersection.

    public final int NUMOFINTERSECTION = 0;

}

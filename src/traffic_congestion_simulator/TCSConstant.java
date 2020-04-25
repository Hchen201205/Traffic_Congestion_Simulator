/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;

/**
 * TSConstant interface will act as a database.
 * It contains all the numbers the 
 *
 * @author chenhanxi
 */
public interface TCSConstant {
    
    public final double TIMEINCREMENTS = 0.0001; //milliseconds
    
    public final int FRAMEWIDTH = 1000;
    
    public final int FRAMEHEIGHT = 500;
    
    public final int DRAWINGWIDTH = 400;
    
    public final int DRAWINGHEIGHT = 400;
    
    public final int LANELENGTH = 150;
    
    public final int LANEKWIDTH = 150;
    
    // Need to modify
    public final int STARTINGPOSX = 0;
    
    public final int STARTINGPOSY = 0;
    
    public final int TESTSIZEX = 50;
    
    public final int TESTSIZEY = 30;
    
    public final int LIGHTGREENTOYELLOW = 25;
    
    public final int LIGHTYELLOWTORED = 10;
    
    public final int LIGHTREDTOGREEN = 40;
    
    public final int LIGHTPOSX = 150;
    
    public final int LIGHTPOSY = 150;
    
    // This value will be changed.
    public final int LIGHTCYCLER = 0;
    
    // This value will be changed.
    public final int LIGHTCYCLEG = 0;
    
    // This value will be changed.
    public final int LIGHTCYCLEY = 0;
    
}

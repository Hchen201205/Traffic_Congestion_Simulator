/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;

import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author ymango
 */
public class Canvas extends JComponent{
    public void paint(Graphics g){
        g.drawRect(50,200,450,400);//normal car 
        g.drawRect(700,200, 450, 400);//cruise controlled car
    }
    public void carsPaint(Graphics g){
    //this method will have all the cars drawn 
    //these cars will have all the information from car class and light class
    }
    public void lanesPaint(Graphics g){
    //this method will have all the lanes drawn 
    // these lanes will be not changed since we are focusing on only one region for now
    }
    
    public void trafficLightPaint(Graphics g){
        //this method will draw all the traffic lights and extends to light class
    }
}


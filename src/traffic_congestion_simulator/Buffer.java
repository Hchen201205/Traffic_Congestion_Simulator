/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;

/**
 *
 * @author chenhanxi
 */
public class Buffer {
    private static int state = 1; // 1 for Light, 2 for Lane.
    
    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
}


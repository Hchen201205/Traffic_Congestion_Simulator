/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import traffic_congestion_simulator.Buffer;
import traffic_congestion_simulator.TCSConstant;

/**
 *
 * @author chenhanxi
 */
public class Lane_Pool implements Runnable {

    Lane_Set[] lanepool;

    final Buffer shared;

    //Creates lanes segments.
    public Lane_Pool(boolean automated, Light_Pool lightpool, Buffer shared) {
        // + 1 for the lane that is exporting cars.
        lanepool = new Lane_Set[TCSConstant.NUMOFINTERSECTION + 1];

        for (int i = 0; i < lanepool.length; i++) {
            lanepool[i] = new Lane_Set(automated, TCSConstant.LANEXVALUE[i], TCSConstant.LANEYVALUE[i], TCSConstant.LANELENGTH[i], TCSConstant.LANEWIDTH[i], TCSConstant.LANEDIRECTION[i], TCSConstant.NUMOFLANE[i], lightpool.getLight_Set(i));
        }

        this.shared = shared;
    }

    private void runUnit() {
        for (Lane_Set laneset : lanepool) {
            laneset.runUnit();
        }
    }

    //Updates lane status.
    @Override
    public void run() {
        while (true) {
            synchronized (shared) {
                if (shared.getState() != 2) {
                    try {
                        shared.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    System.out.println("Lane increment by 1 unit second.");
                    runUnit();

                    // This is where you sleep
                    Thread.sleep(1);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                shared.setState(1);
                shared.notifyAll();
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import traffic_congestion_simulator.Buffer;
import traffic_congestion_simulator.TCSConstant;
import vehicle.Vehicle;
import vehicle.Vehicle_Pool;

/**
 *
 * @author chenhanxi
 */
//Creates the conditions that all cars have to obey.
public class Lane_Pool implements Runnable {

    Lane_Set[] lanepool;

    Vehicle_Pool vpool;

    final Buffer shared;

    //Creates lanes segments.
    public Lane_Pool(boolean automated, Light_Pool lightpool, Buffer shared) {
        // + 1 for the lane that is exporting cars.
        lanepool = new Lane_Set[TCSConstant.NUMOFINTERSECTION + 1];

        vpool = new Vehicle_Pool();

        for (int i = 0; i < lanepool.length; i++) {
            lanepool[i] = new Lane_Set(TCSConstant.LANEPOSITION[i], TCSConstant.LANESIZE[i], TCSConstant.LANEDIRECTION[i], TCSConstant.NUMOFLANE[i], lightpool.getLight_Set(i));
        }

        this.shared = shared;
    }

    private void runUnit() {
        for (Lane_Set laneset : lanepool) {
            laneset.runUnit();
            for (int i = 0; i < laneset.size(); i++) {
                Lane lane = laneset.getLane(i);

                // This will make sure any cars getting out from the lane will be accounted.
                if (lane.haveLight() && lane.getOverFlow()) {
                    vpool.shift(false, lane.getOverFlowList());
                    lane.removeOverFlow();
                }
            }
        }

        // This will solve the intersection problem.
        for (Lane_Set laneset : lanepool) {
            for (int i = 0; i < laneset.size(); i++) {
                // This will make sure any cars getting out from the lane will be accounted.
                Lane lane = laneset.getLane(i);
                if (lane.haveLight() && lane.getOverFlow()) {
                    vpool.shift(false, lane.getOverFlowList());
                    lane.removeOverFlow();
                }
                
                // This will make sure any cars getting into a lane is accounted.
                for (int j = 0; j < vpool.getOutlane().size(); j++) {
                    if (inLane(lane, vpool.getOutlane().get(j))) {
                        vpool.shift(true, vpool.getOutlane().get(j));
                        lane.addCar(vpool.getOutlane().get(j));
                    }
                }
            }
        }

    }

    // Check whether the point of the front end of the vehicle is in a lane.
    private boolean inLane(Lane lane, Vehicle vehicle) {
        double[][] lanePoints = lane.getPoints();
        double[] vpos = vehicle.getPosition();

        int i;
        int j;
        boolean result = false;
        for (i = 0, j = lanePoints.length - 1; i < lanePoints.length; j = i++) {
            if ((lanePoints[i][1] > vpos[1]) != (lanePoints[j][1] > vpos[1])
                    && (vpos[0] < (lanePoints[j][0] - lanePoints[i][0]) * (vpos[0] - lanePoints[i][1]) / (lanePoints[j][1] - lanePoints[i][1]) + lanePoints[i][0])) {
                result = !result;
            }
        }
        return result;
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

    class Point {

        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package all_unused_and_unfinished_classes;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import setting.Light;
import setting.Lane;
import traffic_congestion_simulator.TCSConstant;

/**
 * Simulation class will be the class taking care of the simulation of the
 * program. It will utilize Road class and Light class because a traffic is
 * essentially formed by roads and lights. It will perform the simulation when a
 * run method is called. It will act as a thread for the control class. Because
 * of this, it will have a run() function so that it can be considered as a
 * Runnable. The reason for making this a thread class is that the control class
 * can then perform control experiment.
 * This class extends Thread class because it will be Thread and can be paused whenever the user wants to see an instance of simulation.
 * The reason why this class does not implement Runnable is because Runnable does not have pause and resume method. It only has ready and running state.
 * However, it will implement all the lights as Runnable.
 * Eventually, whenever the stop method of Thread class of a Simulation object is called, this instance will be terminated.
 *
 *
 * @author chenhanxi
 */
public class Simulation3 extends Thread implements TCSConstant {

    /**
     * light instance variable will be an arrayList because different crossroad
     * has different light pattern. However, because in the simulation light
     * will be used as thread, each crossroad will only need one light and can
     * calculate the relative pattern.
     */
    public ArrayList<Light> lights;

    /**
     * road instance variable will be an arrayList containing multiple Road
     * Object. Essentially, it can help by inputing the position, length, and
     * direction of the road. With this, multiple road classes will form a
     * crossroad or even more complex settings.
     */
    public ArrayList<Lane> roads;

    public ExecutorService service;

    public final Lock addingVehicleLock = new ReentrantLock();

    public final Condition canAdd = addingVehicleLock.newCondition();
    public final Condition canRun = addingVehicleLock.newCondition();

    public boolean full = false;
    public boolean empty = false;
    public boolean automated;

    public static int carAmount;

    // Both are assumptions
    public final double[] startingPos = {0, 0};
    public final double[] size = {TESTSIZEX, TESTSIZEY};

    /**
     * Simulation() is a constructor.When it is called, a version of
     * construction will be created. This is beneficial for compare experiment.
     * Simulation() will initialize and input Objects into lights and roads
     * ArrayList.
     *
     * @param automated
     */
    public Simulation3(boolean automated) {
        service = Executors.newCachedThreadPool();

        lights = new ArrayList<>();
        //car_set = new ArrayList<>();
        roads = new ArrayList<>();

        /**
         * The constructor will then add lights in by accessing the static Map
         * in the Data class. It will also use both the Data class in a similar
         * fashion and automated boolean value to add Road into roads.
         */
    }

    /**
     * run() is an override method from Runnable Interface. It will be the
     * execution function for this class as a Thread. In run(), a few steps will
     * be executed: 1. The function will execute all the Light objects in lights
     * ArrayList. 2. It will then perform simulation by accessing the functions
     * in Road class. 3. Whenever any light turns, this function, while running,
     * will make adjustment by calling different functions in the specific Road
     * Object.
     */
    @Override
    public void run() {
        /*!!!
        for (Light light : lights) {
            service.execute(light);
        }
        */
        while (true) {
            for (Lane road : roads) {
                /*
            If the light in that road section is green.
            Do something.
            If not.
            Do other things.
                 */

            }
        }

        // 
    }

    /**
     * addCars is a modifier. It will be called by the control class.
     * Essentially, when it is called, it will receive an int value that states
     * the number of cars the user wants to put in. It then will add those cars
     * one by one. Each car will be generated at the end of a Road and will add
     * into the Road class.
     *
     * @param amount
     * @param automated
     * @throws InterruptedException
     */
    public void addCars(int amount) throws InterruptedException {

        // Need Implementation
        /* The following code will implement the Road class that I created for testing purpose.
           Initially, I attempted to use only one Road for demonstration purpose. Now, as I know that there can be multiple Road, these codes can no longer be used.
        addingVehicleLock.lock();
        try {
            while (full) {
                System.out.println("Traffic road is full, car adder is waiting.");
                canAdd.await();
            }
            road.addCar(startingPos, size);
            empty = false;
            canRun.signalAll();
        } finally {
            addingVehicleLock.unlock();
        }
         */
    }

    /**
     * Accessor function
     * @return 
     */
    public ArrayList<Lane> getRoad() {
        return roads;
    }
    
    /**
     * Accessor function
     * @return 
     */
    public ArrayList<Light> getLight() {
        return lights;
    }
    
    /**
     * calculateDensity is a function that will access the specific Road in roads.
     * It will then count the number of cars (n) in that Road and the length of that road (d).
     * It will then return a double using n / d.
     * @param index
     * @return 
     */
    public double calculateDensity(int index) {
        return 0; // Need implementation
    }
    
    
    
    
    
    /* The following code were all the attempts I made without knowing what other classes will be exactly like.
    public void runCars() throws InterruptedException {
        /*The following code is used by the Road class that I created for testing purpose.
        addingVehicleLock.lock();
        try {
            while (empty) {
                System.out.println("The road is empty. Car runner is waiting.");
                canRun.await();
            }
            road.runCar();
            // Wait for security distance.
            canAdd.signalAll();
        } finally {
            addingVehicleLock.lock();
        }
    }

    /*
    public void runSimulation() {
        while (true) {
            car_set.parallelStream().forEach(car_pair -> {

            });
            for (int i = 0; i < car_set.size(); i++) {
                if (car_set.get(i)[0] == null && light.getLight() == Color.GREEN) {
                    car_set.parallelStream().forEach(car_pair -> {
                        car_pair.identify();
                    });
                }
            }
        }

    }
     */
 /*
    public void runOneTime() {
        for (int i = 0; i <) {

        }
    }

    public int getSize() {
        return car_set.size();
    }
     */
 /*
        if (automated && amount > 0) {
            for (int i = 0; i < amount; i++) {
                double[] position = new
            }
        } else if (amount > 0) {
            Car[] temp = new Car[2];
            temp[1] = new Normal_Car();
            car_set.add(temp);
            for (int i = 1; i < amount; i++) {
                Car[] temp2 = new Car[2];
                // Previous car;
                temp2[0] = car_set.get(i - 1)[1];
                temp2[1] = new Normal_Car();
                car_set.add(temp2);
            }
        }*/
 /*
    @Override
    public void run() {
        System.out.println("1");
        try {
            Thread.sleep(3000);
            System.out.println("2");
        } catch (InterruptedException ex) {
            Logger.getLogger(Simulation3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import java.util.Arrays;

/**
 *
 * @author Christine
 */
public class VehicleTesting {
    public VehicleTesting(){
        //Turning:
        turnTesting();
    }
    
    public static void turnTesting(){
        //left turn facing right
        double[] size = {2,1};
        double[] pos1 = {1,2.5};
        double[] des1 = {5.5,6};
        
        AutomatedCar test1 = new AutomatedCar(pos1, size, 0);
        test1.turn(90, des1);
        System.out.println("Turn radius: " + test1.turn_radius);
        
        double count = 0;
        while (test1.is_turning) {
            count++;
            test1.turn(90, des1);
            if(count%10 == 0){
            System.out.println("Position: " + Arrays.toString(test1.position));
            System.out.println("Dirrection: " + test1.direction);
            }
        }
        System.out.println(count);
        System.out.println("Final Direction: " + test1.direction);
        System.out.println("Final Position: " + Arrays.toString(test1.position));
        System.out.println("\n\n\n\n\n\n");
        
        //right turn facing right 
        double[] pos2 = {1,0.5};
        double[] des2 = {2.5, 0};
        
        AutomatedCar test2 = new AutomatedCar(pos2, size, 0);
        test2.turn(-90, des2);
        System.out.println("Turn radius: " + test2.turn_radius);
        
        count = 0;
        while (test2.is_turning) {
            count++;
            test2.turn(-90, des2);
            if(count%10 == 0){
            System.out.println("Position: " + Arrays.toString(test2.position));
            System.out.println("Dirrection: " + test2.direction);
            }
        }
        System.out.println(count);
        System.out.println("Final Direction: " + test2.direction);
        System.out.println("Final Position: " + Arrays.toString(test2.position));
        System.out.println("\n\n\n\n\n\n");
        
        //left turn facing left
        double[] pos3 = {9,3.5};
        double[] des3 = {4.5, 0};
        
        AutomatedCar test3 = new AutomatedCar(pos3, size, 180);
        test3.turn(90, des3);
        System.out.println("Turn radius: " + test3.turn_radius);
        
        count = 0;
        while (test3.is_turning) {
            count++;
            test3.turn(90, des3);
            if(count%10 == 0){
            System.out.println("Position: " + Arrays.toString(test3.position));
            System.out.println("Dirrection: " + test3.direction);
            }
        }
        System.out.println(count);
        System.out.println("Final Direction: " + test3.direction);
        System.out.println("Final Position: " + Arrays.toString(test3.position));
        System.out.println("\n\n\n\n\n\n");
        
        //right turn facing left
        double[] pos4 = {9,5.5};
        double[] des4 = {7.5, 6};
        
        AutomatedCar test4 = new AutomatedCar(pos4, size, 180);
        test4.turn(-90, des4);
        System.out.println("Turn radius: " + test4.turn_radius);
        
        count = 0;
        while (test4.is_turning) {
            count++;
            test4.turn(-90, des4);
            if(count%10 == 0){
            System.out.println("Position: " + Arrays.toString(test4.position));
            System.out.println("Dirrection: " + test4.direction);
            }
        }
        System.out.println(count);
        System.out.println("Final Direction: " + test4.direction);
        System.out.println("Final Position: " + Arrays.toString(test4.position));
        System.out.println("\n\n\n\n\n\n");
        
        //left turn facing up
        double[] pos5 = {5.5,-1};
        double[] des5 = {2, 3.5};
        
        AutomatedCar test5 = new AutomatedCar(pos5, size, 90);
        test5.turn(90, des5);
        System.out.println("Turn radius: " + test5.turn_radius);
        
        count = 0;
        while (test5.is_turning) {
            count++;
            test5.turn(90, des5);
            if(count%10 == 0){
            System.out.println("Position: " + Arrays.toString(test5.position));
            System.out.println("Dirrection: " + test5.direction);
            }
        }
        System.out.println(count);
        System.out.println("Final Direction: " + test5.direction);
        System.out.println("Final Position: " + Arrays.toString(test5.position));
        System.out.println("\n\n\n\n\n\n");
    
        //right turn facing up
        double[] pos6 = {7.5,-1};
        double[] des6 = {8, 0.5};
        
        AutomatedCar test6 = new AutomatedCar(pos6, size, 90);
        test6.turn(-90, des6);
        System.out.println("Turn radius: " + test6.turn_radius);
        
        count = 0;
        while (test6.is_turning) {
            count++;
            test6.turn(-90, des6);
            if(count%10 == 0){
            System.out.println("Position: " + Arrays.toString(test6.position));
            System.out.println("Dirrection: " + test6.direction);
            }
        }
        System.out.println(count);
        System.out.println("Final Direction: " + test6.direction);
        System.out.println("Final Position: " + Arrays.toString(test6.position));
        System.out.println("\n\n\n\n\n\n");
        
        //left turn facing down
        double[] pos7 = {4.5,7};
        double[] des7 = {8, 2.5};
        
        AutomatedCar test7 = new AutomatedCar(pos7, size, 270);
        test7.turn(90, des7);
        System.out.println("Turn radius: " + test7.turn_radius);
        
        count = 0;
        while (test7.is_turning) {
            count++;
            test7.turn(90, des7);
            if(count%10 == 0){
            System.out.println("Position: " + Arrays.toString(test7.position));
            System.out.println("Dirrection: " + test7.direction);
            }
        }
        System.out.println(count);
        System.out.println("Final Direction: " + test7.direction);
        System.out.println("Final Position: " + Arrays.toString(test7.position));
        System.out.println("\n\n\n\n\n\n");
        
        //right turn facing down
        double[] pos8 = {2.5,7};
        double[] des8 = {2, 5.5};
        
        AutomatedCar test8 = new AutomatedCar(pos8, size, 270);
        test8.turn(-90, des8);
        System.out.println("Turn radius: " + test8.turn_radius);
        
        count = 0;
        while (test8.is_turning) {
            count++;
            test8.turn(-90, des8);
            if(count%10 == 0){
            System.out.println("Position: " + Arrays.toString(test8.position));
            System.out.println("Dirrection: " + test8.direction);
            }
        }
        System.out.println(count);
        System.out.println("Final Direction: " + test8.direction);
        System.out.println("Final Position: " + Arrays.toString(test8.position));
        System.out.println("\n\n\n\n\n\n");
    }
}

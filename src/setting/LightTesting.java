/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import java.util.Arrays;

/**
 *
 * @author Christine
 */
public class LightTesting {

    public LightTesting() {
        double[] pos = {1, 1};
        Light_Set2 test = new Light_Set2(pos);
        for (int i = 0; i < 4; i++) {
            System.out.println("Light " + (i + 1) + " change times: " + Arrays.toString(test.getLight_Set()[i].getChangeTimes()));
        }

        test.startLightSetCycle();

        System.out.println("     Light 1\t|   Light 2\t|   Light 3\t|   Light 4");
        System.out.println("-------------------------------------------------------");

        for (int i = 0; i < 200000; i++) {
            if (i % 10 == 0) {
                System.out.printf("     %s\t|     %s\t|     %s\t|     %s\n",
                        test.getLight_Set()[0].getColorString(), test.getLight_Set()[1].getColorString(),
                        test.getLight_Set()[2].getColorString(), test.getLight_Set()[3].getColorString());
                /*
                System.out.printf("%f     |     %f     |     %f     |     %f     \n", 
                        test.getLight_Set()[0].getTimePassed(), test.getLight_Set()[1].getTimePassed(),
                        test.getLight_Set()[2].getTimePassed(), test.getLight_Set()[3].getTimePassed());
                 */
            }
            test.runCycleUnit();

        }

    }

    public static void main(String[] args) {
        LightTesting l = new LightTesting();
    }
}

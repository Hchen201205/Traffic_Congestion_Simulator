/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic_congestion_simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
import vehicle.*;
import setting.*;

/**
 *
 * @author chenhanxi
 */
public class Simulation2 extends JPanel implements TCSConstant {

    private static final long serialVersionUID = 1L;

    private boolean automated;

    private Lane lane;

    private Light light;

    public Simulation2(boolean automated, int numOfCar) {
        this.automated = automated;
        double[] position = {0, 0};
        double[] size = {270, 10};

        double direction = 0;
        System.out.println("hi");

        double[] carpos = {10, 5};

        light = new Light(Color.GREEN, true, false);
        lane = new Lane(position, size, direction, light);
        for (int i = 0; i < numOfCar; i++) {
            Vehicle c;
            if (automated) {
                c = new AutomatedCar(carpos, direction);
            } else {
                c = new NormalCar(carpos, direction);
            }
            carpos[0]--;
            lane.addCar(c);
            System.out.println(Arrays.toString(c.getPosition()));
        }
        lane.setCars();
    }

    @Override
    public void paint(Graphics g) {
        paintSetting(g);
    }

    public void paintSetting(Graphics g) {
        /*
        AffineTransform at = new AffineTransform();
        at.setToTranslation(80, 20);
        Shape r = new Rectangle2D.Double(lane.getPosition()[0], lane.getPosition()[1], lane.getSize()[0], lane.getSize()[1]);
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;

        g2d.draw(r);
*/
    }

    public Lane getLane() {
        return lane;
    }

    public Light getLight() {
        return light;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Traffic Congestion Simulator");
        f.setVisible(true);
        f.setSize(600, 600);
        f.setResizable(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Simulation2 s2 = new Simulation2(true, 6);
        f.add(s2);
        s2.paint(s2.getGraphics());
    }
}

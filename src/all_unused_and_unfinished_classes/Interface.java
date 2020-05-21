/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package all_unused_and_unfinished_classes;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import java.awt.Component;
import javax.swing.JLabel;
/**
 *
 * @author ymango
 */
public class Interface extends JFrame{

    /**
     * @param args the command line arguments
     */
    //one of the cars can be user controlled? 
    public static void main(String[] args) {
        // TODO code application logic here
        int height, width; 
        width = 10000;
        height = 700;
        JFrame frame = new JFrame("Interface");//Frame of the interface
        frame.setSize(width,height);//sizing frame
        
        //JPanel container = new JPanel();
        //container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));//boxing panels
       
        JButton resetButton = new JButton("reset");
        resetButton.setBounds(80, 5, 50, 20);
        //JPanel resetPanel = new JPanel();
        //resetPanel.add(resetButton);
        
        //container.add(resetPanel); 
        resetButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e ){
                System.out.println("reset done");
                //if a button is clicked, then this method will reset the whole simulation 
            }
        });
        
        JButton startButton = new JButton("start");
        startButton.setBounds(10,5,50,20);        
        startButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e ){
                System.out.println("Start");
                //if a button is clicked, then this method will start the simulation
            }
        });
        
        
        JButton stopButton = new JButton("stop");
        stopButton.setBounds(150,5,50,20);
        //container.add(finishButton,BorderLayout.PAGE_END);
        
        stopButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e ){
                System.out.println("stopped");
                //if a button is clicked, then this method will stop the simulation
            }
        });

        JLabel l = new JLabel("Normal"); //title for left rectangle that will include the today's road simulation
        l.setText("Normal");
        l.getText();
        
        JLabel r = new JLabel("New Method"); //title for right rectangle that will include the our new method
        r.setText("New Method");
        r.getText();
        
        //here is the code for user controlled simulation buttons
        
        JButton seeDataButton = new JButton("see data");
        seeDataButton.setBounds(1000,30,70,20);
        seeDataButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e ){
                System.out.println("data seen");
                //if button clicked, data will be shown in another frame or window
            }
        });
        
        JButton speedControlButton = new JButton("speed");
        speedControlButton.setBounds(1000,70,70,20);
        speedControlButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e ){
                System.out.println("which car would you want to change the speed?");
                //if a button is clicked, then the simulation will stop  and ask the user to 
                //select which car to change the speed 
                // then as the user enters the car number, this method allows change in speed of a car 
                //and then the simulation will resume with the changed speed 
                // the user can then see how the cruise control allows reduction in traffic 
                
            }
        });
        
        JButton lightButton = new JButton("light");
        lightButton.setBounds(1000,110, 70,20);
        lightButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e ){
                System.out.println("Enter red (for stop), orange (for entering stop stage), green (for go):");
                //if a button is clicked, then the simulation will stop and ask the user to
                //enter a string out of these: red, orange, green
                //then the simulation will resume after the user enters the string with the changed traffic lights
            }
        });
        
        frame.add(stopButton);
        frame.add(startButton);
        frame.add(resetButton);
        frame.add(seeDataButton);
        frame.add(speedControlButton);
        frame.add(lightButton);
        frame.add(l);
        frame.add(r);
        frame.getContentPane().add(new Canvas());
        
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
}

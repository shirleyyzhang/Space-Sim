/**
 File Name: SpaceRunner.java
 @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 Date: January 17, 2022
 Description: This class contains the main method to run the Space simulation
 */
import javax.swing.*;

@SuppressWarnings("serial")
public class SpaceRunner extends JFrame{

    /**
     * main:
     * to utilize SpaceRunner
     *
     * @param args
     */
    public static void main(String[] args) {
        new SpaceRunner();
    }

    /**
     * SpaceRunner:
     * Creates a new SpaceRunner to run simulation
     */
    public SpaceRunner() {
        this.add(new Space());
        this.setTitle("Space Simulation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
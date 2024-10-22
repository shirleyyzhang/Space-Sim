/**
 File Name: Space.java
 @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 Date: January 17, 2022
 Description: This class represents a Space simulation
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class Space extends JPanel implements Runnable, ActionListener {

    /* FIELDS */

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static final int DELAY = 2;
    private BodyManager bodyManager;
    private Thread simulator;
    private boolean running;

    private FileManager fileManager;
    private UIManager buttonManager;
    private ParticleManager particleManager;

    /* ACCESSORS & MUTATORS */

    /**
     * @return if space simulation is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @return associated file manager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param running state of space simulation (pause or run)
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * @param fileManager to store files
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @return screen dimensions
     */
    public Dimension getScreenSize() {
        return screenSize;
    }

    /**
     * @return screen width
     */
    public static int getScreenWidth() {
        return screenWidth;
    }

    /**
     * @return screen height
     */
    public static int getScreenHeight() {
        return screenHeight;
    }

    /**
     * @return delay
     */
    public static int getDelay() {
        return DELAY;
    }

    /**
     * @return body manager to store and manage bodies
     */
    public BodyManager getBodyManager() {
        return bodyManager;
    }

    /**
     * @return simulator
     */
    public Thread getSimulator() {
        return simulator;
    }

    /**
     * @param screenSize screen size to set
     */
    public void setScreenSize(Dimension screenSize) {
        this.screenSize = screenSize;
    }

    /**
     * @param screenWidth screen width to set
     */
    public static void setScreenWidth(int screenWidth) {
        Space.screenWidth = screenWidth;
    }

    /**
     * @param screenHeight height to set
     */
    public static void setScreenHeight(int screenHeight) {
        Space.screenHeight = screenHeight;
    }

    /**
     * @param bodyManager body manager to set
     */
    public void setBodyManager(BodyManager bodyManager) {
        this.bodyManager = bodyManager;
    }

    /**
     * @param simulator to set
     */
    public void setSimulator(Thread simulator) {
        this.simulator = simulator;
    }

    /* CONSTRUCTOR */

    /**
     * Space:
     * Constructor to create Space simulation
     */
    public Space() {
        running = false;
        screenWidth = (int) screenSize.getWidth();
        screenHeight = (int) screenSize.getHeight();
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setLayout(null);

        particleManager = new ParticleManager();
        bodyManager = new BodyManager(particleManager);
        fileManager = new FileManager(new String[] {"file 1"}, bodyManager);
        buttonManager = new UIManager(this, bodyManager, fileManager);
    }

    /**
     * addNotify:
     * Creates and starts new simulation
     */
    @Override
    public void addNotify() {
        super.addNotify();

        simulator = new Thread(this);
        simulator.start();
    }

    /**
     * paintComponent:
     * Paints a component of simulation using given graphics
     *
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //TODO: move the drawing to another class if possible to organize it
        bodyManager.drawBodies(g);
        particleManager.drawParticles(g);
    }

    /**
     * loop:
     * Loops through bodies and particles to update according to simulation interactions
     */
    private void loop() {
        // Point point = MouseInfo.getPointerInfo().getLocation();
        // Vec vec = new Vec(point.getX(),point.getY());
        // objects.get(0).pos = vec;

        if(running) {
            bodyManager.updateBodies();
            particleManager.updateParticles();
        }
    }

    /**
     * run:
     * Runs space simulation
     */
    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            loop();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {

                String msg = String.format("Thread interrupted: %s", e.getMessage());

                JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
            }

            beforeTime = System.currentTimeMillis();
        }
    }

    /**
     * actionPerformed:
     * Implements action listener
     *
     * @param e ActionEvent such as mouse click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action == null)
            action = "";
        buttonManager.buttonFunction(action);

    }

    /**
     * clearSim:
     * Clears bodies in simulation
     */
    public void clearSim() {
        bodyManager.setBodies(new ArrayList<Body>());
        bodyManager.setNumBodies(0);
    }
}

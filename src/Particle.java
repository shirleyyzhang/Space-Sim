/**
 File Name: Particle.java
 @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 Date: January 17, 2022
 Description: This class represents a Particle and stores all corresponding information.
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Particle {

    /* FIELDS */
    private Vec pos; // instance field: position of Particle
    private Vec vel; // instance field: velocity of Particle
    private double timeLeft; // instance field: time left before destruction
    private double totalTime; // instance field: total time of Particle
    private boolean destroyed; // instance field: boolean indicating if destroyed
    private Color color; // instance field: colour of Particle
    private double radius; // instance field: radius of Particle

    /* ACCESSORS */

    /**
     * @return position
     */
    public Vec getPos() {
        return pos;
    }

    /**
     * @return velocity
     */
    public Vec getVel() {
        return vel;
    }

    /**
     * @return time left before destruction
     */
    public double getTimeLeft() {
        return timeLeft;
    }

    /**
     * @return boolean indicating if destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * @return colour
     */
    public Color getColor() {
        return color;
    }

    /* MUTATORS */

    /**
     * @param pos the position to set
     */
    public void setPos(Vec pos) {
        this.pos = pos;
    }

    /**
     * @param vel the velocity to set
     */
    public void setVel(Vec vel) {
        this.vel = vel;
    }

    /**
     * @param timeLeft the time left to set
     */
    public void setTimeLeft(double timeLeft) {
        this.timeLeft = timeLeft;
    }

    /**
     * @param destroyed the boolean destroyed to set
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * @param color the colour to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /* CONSTRUCTORS */

    /**
     * Particle: Constructor that creates a new Particle object with given fields
     *
     * @param pos      position
     * @param vel      velocity
     * @param timeLeft
     * @param color
     */
    public Particle(Vec pos, Vec vel, double timeLeft, Color color, double radius) {
        this.pos = pos;
        this.vel = vel;
        this.timeLeft = timeLeft;
        this.totalTime = timeLeft;
        this.color = color;
        this.radius = radius;
    }

    /* METHODS */

    /**
     * draw:
     * Draws particles using given graphics
     *
     * @param g Graphics
     */
    public void draw(Graphics g) {
        float alpha = (float) (timeLeft / totalTime);
        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        ((Graphics2D) g).setComposite(alcom);

        g.setColor(color);
        g.fillOval((int) (pos.getX() - radius), (int) (pos.getY() - radius), (int) radius * 2, (int) radius * 2);
    }

}
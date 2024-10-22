/**
 File Name: Star.java
 @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 Date: January 17, 2022
 Description: This class represents a Star celestial body and stores all corresponding information.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;

public class Star extends Circle {

    /* FIELDS */
    private static final int MAX_PLANETS = 20; // class field (constant): maximum number of Planets
    private int numPlanets; // instance field: number of Planets orbiting this Star
    protected Planet[] planets = new Planet[MAX_PLANETS]; // instance field: array of Planets orbiting this Star

    /* ACCESSORS */

    /**
     * @return maximum number of planets
     */
    public int getMAX_PLANETS() {
        return MAX_PLANETS;
    }

    /**
     * @return number of Planets orbiting
     */
    public int getNumPlanets() {
        return numPlanets;
    }

    /**
     * @return array of Planets orbiting
     */
    public Planet[] getPlanets() {
        return planets;
    }

    /* MUTATORS */

    /**
     * @param planets array of Planets orbiting
     */
    public void setPlanets(Planet[] planets) {
        this.planets = planets;
        numPlanets = planets.length;
    }

    /* CONSTRUCTORS */

    /**
     * Star: Constructor that creates a new Star object with all given fields
     *
     * @param name
     * @param position
     * @param mass
     * @param isStatic
     * @param resolveCollisions
     * @param velocity
     * @param age
     * @param angle
     * @param angularVelocity
     * @param radius
     */
    public Star(String name, Vec position, double mass, boolean isStatic, boolean resolveCollisions, Vec velocity,
                int age, double angle, double angularVelocity, double radius) {
        super(name, position, mass, isStatic, resolveCollisions, velocity, age, angle, angularVelocity, radius);
        numPlanets = 0;

    }

    /**
     * Star: Constructor that creates a new Star object with necessary given fields
     *
     * @param name
     * @param mass
     * @param age
     * @param radius
     */
    public Star(String name, double mass, int age, double radius) {
        super(name,
                new Vec(MouseInfo.getPointerInfo().getLocation().getX(),
                        MouseInfo.getPointerInfo().getLocation().getY()),
                mass, false, true, new Vec(0, 0), age, 0, 0, radius);
        numPlanets = 0;
    }

    /* METHODS */

    /**
     * compareTo: Compares this and other Star by age
     *
     * @param other Star to compare to
     * @return difference between this and other Star's ages
     */
    public int compareTo(Star other) {
        return age - other.age;
    }

    /**
     * draw: Draws Star simulation with given Graphics
     *
     * @param g Graphics
     */
    public void draw(Graphics g) { // temp draw method
        g.setColor(new Color(255,204,0));
        g.fillOval((int) (pos.getX() - radius), (int) (pos.getY() - radius), (int) radius * 2, (int) radius * 2);
    }

    /**
     * onCollide: Manages consequences of Star collision
     *
     * @param col CollisionResolver for collision
     */
    public void onCollide(CollisionResolver col, BodyManager bm, ParticleManager pm) {
        Body thisStar;
        Body other;
        if (col.getBody1() instanceof Star && col.getBody1().equals(this)) {
            thisStar = col.getBody1();
            other = col.getBody2();
        } else {
            thisStar = col.getBody2();
            other = col.getBody1();
        }

        if (other instanceof Star && thisStar.mass < other.mass) {
            other.mass += thisStar.mass;
            thisStar.destroy();
        } else {
            thisStar.mass += other.mass;
            other.destroy();
        }

    }

    /**
     * toString: Organizes field information of implicit Planet object into a String
     *
     * @return organized field information of implicit Planet object as a String
     */
    public String toString() {
        return super.toString() + "\nNumber of Planets:" + numPlanets;
    }

}
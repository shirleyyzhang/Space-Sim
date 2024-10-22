/**
 File Name: Planet.java
 @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 Date: January 17, 2022
 Description: This class represents a Planet celestial body and stores all corresponding information.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;

public class Planet extends Circle {

    /* FIELDS */
    protected int population; // instance field: population of Planet
    protected String orbitStar; // instance field: Star that this Planet orbits
    private int numMoons; // instance field: number of Moons orbiting Planet
    protected Moon[] moons; // instance field: array of Moons orbiting Planet
    protected boolean visited; // instance field: marker for if Planet has been visited in a trip

    private static final int MAX_MOONS = 20; // class field (constant): maximum number of Moons to orbit Planet
    private static final double MAX_IMPULSE = 800; // class field (constant): maximum value for Planet's impulse

    /* ACCESSORS */

    /**
     * @return population
     */
    public int getPopulation() {
        return population;
    }

    /**
     * @return orbit Star
     */
    public String getOrbitStar() {
        return orbitStar;
    }

    /**
     * @return number of Moons
     */
    public int getNumMoons() {
        return numMoons;
    }

    /**
     * @return maximum number of moons allowed to orbit planet
     */
    public static int getMaxMoons() {
        return MAX_MOONS;
    }

    /**
     * @return the maxImpulse
     */
    public static double getMaxImpulse() {
        return MAX_IMPULSE;
    }

    /**
     * @return the array of orbiting moons
     */
    public Moon[] getMoons() {
        return moons;
    }

    /**
     * @return whether planet was visited during a trip
     */
    public boolean isVisited() {
        return visited;
    }

    /* MUTATORS */

    /**
     * @param population the population to set
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * @param orbitStar the orbit Star to set
     */
    public void setOrbitStar(String orbitStar) {
        this.orbitStar = orbitStar;
    }

    /**
     * @param moons the array of Moons to set
     */
    public void setMoons(Moon[] moons) {
        this.moons = moons;
        numMoons = moons.length;
    }

    /**
     * @param visited the boolean to set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /* CONSTRUCTORS */

    /**
     * Planet:
     * Constructor that creates a new Planet object with all given fields
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
     * @param population
     * @param orbitStar
     */
    public Planet(String name, Vec position, double mass, boolean isStatic, boolean resolveCollisions, Vec velocity,
                  int age, double angle, double angularVelocity, double radius, int population, String orbitStar) {

        super(name, position, mass, isStatic, resolveCollisions, velocity, age, angle, angularVelocity, radius);
        this.population = population;
        this.orbitStar = orbitStar;

    }

    /**
     * Planet:
     * Constructor that creates a new Planet object with necessary given fields
     *
     * @param name
     * @param mass
     * @param age
     * @param radius
     * @param orbitStar
     */
    public Planet(String name, double mass, int age, double radius, String orbitStar) {
        super(name, new Vec(MouseInfo.getPointerInfo().getLocation().getX() , MouseInfo.getPointerInfo().getLocation().getY()), mass, false, true, new Vec(0,0), age, 0, 0, radius);
        this.orbitStar = orbitStar;
        numMoons = 0;
    }

    /* METHODS */

    /**
     * onCollide: insert description
     * @param col CollisionResolver
     * @param bm BodyManager
     * @param pm ParticleManager
     */
    @Override
    public void onCollide(CollisionResolver col, BodyManager bm, ParticleManager pm) {
        super.onCollide(col, bm, pm);
        if (col.getMaxImpulse() / this.density > MAX_IMPULSE) {
            this.toAsteroid(bm);
            pm.addParticle(pos,2*Math.sqrt(this.area/Math.PI));
        }
    }

    /**
     * compareTo: Compares this and other Planet by population
     *
     * @param other Planet to compare to
     * @return difference between this and other Planet's populations
     */
    public int compareTo(Planet other) {
        return this.population - other.population;
    }

    /**
     * toString: Organizes field information of implicit Planet object into a String
     *
     * @return organized field information of implicit Planet object as a String
     */
    public String toString() {
        return super.toString() + "\nPopulation: " + population + "\nStar which planet orbits: " + orbitStar
                + "\nNumber of moons: " + numMoons;
    }

    /**
     * draw:
     * Draws Star simulation with given Graphics
     *
     * @param g Graphics
     */
    public void draw(Graphics g) { // temp draw method
        g.setColor(new Color(0,153,0));
        g.fillOval((int) (pos.getX() - radius), (int) (pos.getY() - radius), (int) radius * 2, (int) radius * 2);
    }

}
/**
 File Name: Moon.java
 @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 Date: January 17, 2022
 Description: This class represents a Moon celestial body and stores all corresponding information.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;

public class Moon extends Circle {

    /* FIELDS */

    protected String orbitPlanet; // instance field: Planet that this Moon orbits

    /* ACCESSORS */

    /**
     * @return orbitPlanet
     */
    public String getOrbitPlanet() {
        return orbitPlanet;
    }

    /* MUTATORS */

    /**
     * @param orbitPlanet
     */
    public void setOrbitPlanet(String orbitPlanet) {
        this.orbitPlanet = orbitPlanet;
    }

    /* CONSTRUCTORS */

    /**
     * Moon:
     * Constructor that creates new Moon object with all given fields
     *
     * @param name              the name of the moon
     * @param position          the position of the center of the moon
     * @param mass              the mass of the moon
     * @param isStatic          whether it moves
     * @param resolveCollisions whether it collides with other objects
     * @param velocity          the velocity of the moon
     * @param age               the age of the body
     * @param angle             the angle which the velocity is going in
     * @param angularVelocity   the change of angle over time of the body
     * @param radius            the radius of the circle
     * @param inOrbitPlanet     the planet which the sun orbits
     */
    public Moon(String name, Vec position, double mass, boolean isStatic, boolean resolveCollisions, Vec velocity,
                int age, double angle, double angularVelocity, double radius, String inOrbitPlanet) {

        super(name, position, mass, isStatic, resolveCollisions, velocity, age, angle, angularVelocity, radius);
        orbitPlanet = inOrbitPlanet;

    }

    /**
     * Moon:
     * Constructor that creates new Moon object with necessary given fields
     *
     * @param name
     * @param mass
     * @param age
     * @param radius
     * @param orbitPlanet
     */
    public Moon(String name, double mass, int age, double radius, String orbitPlanet) {
        super(name, new Vec(MouseInfo.getPointerInfo().getLocation().getX() , MouseInfo.getPointerInfo().getLocation().getY()), mass, false, true, new Vec(0,0), age, 0, 0, radius);
        this.orbitPlanet = orbitPlanet;
    }

    /* METHODS */

    /**
     * draw:
     * Draws Moon simulation with given Graphics
     *
     * @param g Graphics
     */
    @Override
    public void draw(Graphics g) { // a temporary draw method
        g.setColor(Color.WHITE);
        g.fillOval((int) (pos.getX() - radius), (int) (pos.getY() - radius), (int) radius * 2, (int) radius * 2);
    }

    /**
     * compareTo: Compares this and other Moon by area
     *
     * @param other Moon to compare to
     * @return difference between this and other Moon's areas
     */
    public double CompareTo(Moon other) {
        return area - other.area;
    }

    /**
     * toString: Organizes field information of implicit Moon object into a String
     *
     * @return organized field information of implicit Moon object as a String
     */
    @Override
    public String toString() {
        return super.toString() + "Planet which moon orbits: " + orbitPlanet;
    }

}
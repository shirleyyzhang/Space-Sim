/**
 * File Name: Circle.java
 * @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 * Date: January 17, 2022
 * Description: This class stores all the information for a circle rigidbody
 */
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
public class Circle extends Body {

    /* FIELDS */
    private final static int MAX_VERTICES = 10;
    private final static int MIN_VERTICES = 5;// for generating polygons
    protected double radius;

    /* ACCESSORS & MUTATORS */

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /* CONSTRUCTORS */

    /**
     * Circle:
     * Constructor that creates a new Circle object with given properties
     *
     * @param name              the name of the circle
     * @param pos               the position of the center of the circle
     * @param mass              the mass of the circle
     * @param isStatic          whether it moves
     * @param resolveCollisions whether it collides with other objects
     * @param vel               the velocity of the polygon
     * @param radius            the radius of the circle
     */
    public Circle(String name, Vec pos, double mass, boolean isStatic, boolean resolveCollisions, Vec vel, int age,
                  double angle, double angularVelocity, double radius) {
        super(name, pos, mass, isStatic, resolveCollisions, vel,age,angle,angularVelocity);
        this.radius = radius;
        this.updateProperties();
    }

    // TODO Create another constructor

    /* METHODS */

    /**
     * draw:
     * Draws the Circle with given graphics
     *
     * @param g Graphics
     */
    @Override
    public void draw(Graphics g) { // a temporary draw method
        g.setColor(Color.RED);
        g.fillOval((int) (pos.getX() - radius), (int) (pos.getY() - radius), (int) radius * 2, (int) radius * 2);
    }

    /**
     * calcArea:
     * calculates area of Circle
     *
     * @return area
     */
    @Override
    protected double calcArea() {
        return radius * radius * Math.PI;
    }

    /**
     * calcMomentOfInertia:
     * calculates moment of inertia (resistance to rotation) for Circle
     *
     * @return moment of inertia
     */
    @Override
    protected double calcMomentOfInertia() {
        return mass * radius * radius / 4;
    }

    /**
     * getMaxMin:
     * retrieves maximum and minimum points of Circle circumference
     *
     * @return maximum and minimum points
     */
    @Override
    public double[] getMaxMin() {
        // [min x, max x, min y, max y]
        return new double[] { pos.getX() - radius, pos.getX() + radius, pos.getY() - radius, pos.getY() + radius };
    }

    /**
     * toString:
     * organizes Circle properties into a String
     *
     * @return String of organized properties
     */
    @Override
    public String toString() {
        return super.toString() + "\nradius: " + radius;
    }

    /**
     * toAsteroid:
     * converts Circle into Asteroid with vertices
     *
     * @param bd associated BodyManager
     */
    public void toAsteroid(BodyManager bd) {
        Random rand = new Random();

        int numVertices = rand.nextInt(MAX_VERTICES-MIN_VERTICES)+MIN_VERTICES;
        Vec[] vertices = ConvexPolygon.randomRegularPolygon(radius,numVertices);
        bd.addAsteroid(null, new Vec(pos), mass, isStatic, resolveCollisions, new Vec(vel), age, angle, angularVelocity, vertices);
        this.destroy();
    }
}
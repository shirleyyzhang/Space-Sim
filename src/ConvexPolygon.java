/**
 * File Name: ConvexPolygon.java
 * @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 * Date: January 17, 2022
 * Description: This class stores all the information for a convex polygon rigidbody
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Random;

public class ConvexPolygon extends Body {

    /* FIELDS */
    private final static double PERCENT_VARIATION = 0.5; // for creating random polygons
    protected Vec[] vertices; // the array off vertices of the polygon

    /* ACCESSORS */

    /**
     * @return the vertices
     */
    public Vec[] getVertices() {
        return vertices;
    }

    /* MUTATORS */

    /**
     * The vertices must be in counterclockwise order.
     *
     * @param vertices the vertices to set
     */
    public void setVertices(Vec[] vertices) {
        this.vertices = vertices;
    }

    /* CONSTRUCTORS */

    /**
     * ConvexPolygon: Constructor that creates a new Convex Polygon
     *
     * @param name              the name of the polygon
     * @param pos               the position of the polygon
     * @param mass              the mass of the polygon
     * @param isStatic          whether it moves
     * @param resolveCollisions whether it collides with other bodies
     * @param vel               the velocity of the polygon
     * @param vertices          an array storing the vertices of the polygon in
     *                          counterclockwise order
     */
    public ConvexPolygon(String name, Vec pos, double mass, boolean isStatic, boolean resolveCollisions, Vec vel,
                         int age, double angle, double angularVelocity, Vec[] vertices) {
        super(name, pos, mass, isStatic, resolveCollisions, vel, age, angle, angularVelocity);
        this.vertices = vertices;
        this.adjustPositionToCenterOfMass();
        this.updateProperties();
    }

    /**
     * adjustPositionToCenterOfMass: adjusts the coordinate to the center of mass of
     * the polygon object
     *
     */
    public void adjustPositionToCenterOfMass() {
        Vec delta = new Vec(0,0);
        for(Vec vertex: vertices) {
            delta.add(vertex);
        }
        delta.div(vertices.length);
        pos.add(delta);
        for(int i=0;i<vertices.length;i++) {
            vertices[i].sub(delta);
        }
    }


    /**
     * rotate: rotates the polygon at a specified angle
     *
     * @param angle the angle at which the poygon will rotate to face
     */
    @Override
    protected void rotate(double angle) {
        super.rotate(angle);
        for (int i = 0; i < this.vertices.length; i++) {
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);
            double xNew = this.vertices[i].getX() * cos - this.vertices[i].getY() * sin;
            double yNew = this.vertices[i].getX() * sin + this.vertices[i].getY() * cos;

            this.vertices[i].setX(xNew);
            this.vertices[i].setY(yNew);
        }
    }

    /**
     * draw: draws the circle object onto the screen
     *
     * @param g the instance variable used from the Graphics Class
     */
    @Override
    public void draw(Graphics g) { // temporary draw method
        Polygon p = new Polygon();
        for (int j = 0; j < vertices.length; j++) {
            p.addPoint((int) (vertices[j].getX() + pos.getX()), (int) (vertices[j].getY() + pos.getY()));
        }
        g.setColor(Color.GRAY);
        g.fillPolygon(p);
    }

    /**
     * calcArea: calculates and returns the area of the circle
     *
     * @return the radius of the circle
     */
    @Override
    protected double calcArea() {
        double area = 0;
        for (int i = 1; i < this.vertices.length - 1; i++) {
            Vec v1 = Vec.getSub(this.vertices[i + 1], this.vertices[0]);
            Vec v2 = Vec.getSub(this.vertices[i], this.vertices[0]);
            area += Vec.cross(v1, v2) / 2.0;
        }
        return Math.abs(area);
    }

    /**
     * calcMomentOfInertia: calculates the body at the moment of inertia
     *
     * @return the inertia at the body's moment of impact
     */
    @Override
    protected double calcMomentOfInertia() {

        double momentOfInertia = 0;

        for (int i = 1; i < this.vertices.length - 1; i++) {

            Vec p1 = this.vertices[0], p2 = this.vertices[i], p3 = this.vertices[i + 1];

            double w = p1.distance(p2);

            double w1 = Math.abs(Vec.getSub(p1, p2).dot(Vec.getSub(p3, p2)) / w);
            double w2 = Math.abs(w - w1);

            double signedTriArea = Vec.cross(Vec.getSub(p3, p1), Vec.getSub(p2, p1)) / 2.0;

            double h = 2.0 * Math.abs(signedTriArea) / w;

            Vec p4 = Vec.getAdd(p2, Vec.getSub(p1, p2).getMult(w1 / w));

            Vec cm1 = new Vec(p2.getX() + p3.getX() + p4.getX(), p2.getY() + p3.getY() + p4.getY());
            Vec cm2 = new Vec(p1.getX() + p3.getX() + p4.getX(), p1.getY() + p3.getY() + p4.getY());

            double I1 = this.density * w1 * h * ((h * h / 4) + (w1 * w1 / 12));
            double I2 = this.density * w2 * h * ((h * h / 4) + (w2 * w2 / 12));

            double m1 = 0.5 * w1 * h * this.density;
            double m2 = 0.5 * w2 * h * this.density;

            double I1cm = I1 - (m1 * Math.pow(cm1.distance(p3), 2));
            double I2cm = I2 - (m2 * Math.pow(cm2.distance(p3), 2));

            double momentOfInertiaPart1 = I1cm + (m1 * cm1.getLengthSq());
            double momentOfInertiaPart2 = I2cm + (m2 * cm2.getLengthSq());

            if (Vec.cross(Vec.getSub(p1, p3), Vec.getSub(p4, p3)) > 0) {
                momentOfInertia += momentOfInertiaPart1;
            } else {
                momentOfInertia -= momentOfInertiaPart1;
            }

            if (Vec.cross(Vec.getSub(p4, p3), Vec.getSub(p2, p3)) > 0) {
                momentOfInertia += momentOfInertiaPart2;
            } else {
                momentOfInertia -= momentOfInertiaPart2;
            }
        }

        return Math.abs(momentOfInertia);
    }

    /**
     * getMaxMin: finds the maximum and minimum points of the circle body
     *
     * @return an array of coordinates with the circle's max and min points
     */
    @Override
    public double[] getMaxMin() {
        double[] arr = { Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE };// [min x, max x, min
        // y, max y]
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].getX() < arr[0]) {
                arr[0] = vertices[i].getX();
            }
            if (vertices[i].getX() > arr[1]) {
                arr[1] = vertices[i].getX();
            }
            if (vertices[i].getY() < arr[2]) {
                arr[2] = vertices[i].getY();
            }
            if (vertices[i].getY() > arr[3]) {
                arr[3] = vertices[i].getY();
            }
        }
        arr[0] += pos.getX();
        arr[1] += pos.getX();
        arr[2] += pos.getY();
        arr[3] += pos.getY();
        return arr;
    }

    /**
     * toString: Organizes Convex Polygon information into a String
     *
     * @return String organized information
     */
    @Override
    public String toString() {
        return super.toString() + "\nnumber of vertices: " + this.vertices.length;
    }

    /**
     * regularPolygon: This method creates an array containing vertices for a
     * regular polygon.
     *
     * @param radius      the radius of the polygon
     * @param numVertices the number of vertices
     * @return an array storing all the vertices
     */
    public static Vec[] regularPolygon(double radius, int numVertices) {
        Vec[] vertices = new Vec[numVertices];
        for (int i = 0; i < numVertices; i++) {
            vertices[i] = new Vec(Math.cos(-i * Math.PI * 2.0 / numVertices) * radius,
                    Math.sin(-i * Math.PI * 2.0 / numVertices) * radius);
        }
        return vertices;
    }

    /**
     * rectangle: This method creates an array containing vertices for a rectangle.
     *
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @return an array storing all the vertices
     */
    public static Vec[] rectangle(int width, int height) {
        Vec[] vertices = new Vec[4]; // 4 vertices in a rectangle

        vertices[0] = new Vec(width / 2, height / 2);
        vertices[1] = new Vec(width / 2, -height / 2);
        vertices[2] = new Vec(-width / 2, -height / 2);
        vertices[3] = new Vec(-width / 2, height / 2);

        return vertices;
    }

    /**
     * randomRegularPolygon: create an array of vertices of a new polygon with
     * random vertices values in the array
     *
     * @param radius      the radius of the polygon
     * @param numVertices the number of vertices the polygon will have
     * @return an array of coordinates with the polygon's vertices
     */
    public static Vec[] randomRegularPolygon(double radius, int numVertices) {
        Vec[] vertices = new Vec[numVertices];
        Random rand = new Random();
        double variation = Math.PI * PERCENT_VARIATION / numVertices;
        for (int i = 0; i < numVertices; i++) {
            double delta = rand.nextDouble() * variation * 2 - variation;
            vertices[i] = new Vec(Math.cos(-i * Math.PI * 2.0 / numVertices + delta) * radius,
                    Math.sin(-i * Math.PI * 2.0 / numVertices + delta) * radius);
        }
        return vertices;
    }
}
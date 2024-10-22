/**
 File Name: Asteroid.java
 @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 Date: January 17, 2022
 Description: This class represents an Asteroid celestial body and stores all corresponding information.
 Contains Triangle class for shattering effect.
 */

import java.awt.Graphics;
import java.util.ArrayList;

public class Asteroid extends ConvexPolygon {

    /* FIELDS */
    private static final double MAX_IMPULSE = 1500; // class field (constant): maximum impulse the asteroid can take
    // before shattering
    private static final double AREA_THRESHOLD = 5000; // class field : minimum possible area the asteroid can have for
    // it to shatter
    private static final int DEFAULT_NUM_VERTICES = 6; // class field : the number of vertices for an default asteroid
    private static final int DEFAULT_RADIUS = 100; // class field: the default radius of an asteroid

    /* CONSTRUCTORS */

    /**
     * Asteroid: Constructor that creates a new Asteroid object with all given
     * fields
     *
     * @param name              the name of the asteroid
     * @param position          the position of the center of the asteroid
     * @param mass              the mass of the asteroid
     * @param isStatic          whether it moves
     * @param resolveCollisions whether it collides with other objects
     * @param velocity          the velocity of the asteroid
     * @param age               the age of the body
     * @param angle             the angle which the velocity is going in
     * @param angularVelocity   the change of angle over time of the body
     * @param vertices          a list of all positions of each vertices of an
     *                          asteroid
     */
    public Asteroid(String name, Vec position, double mass, boolean isStatic, boolean resolveCollisions, Vec velocity,
                    int age, double angle, double angularVelocity, Vec[] vertices) {

        super(name, position, mass, isStatic, resolveCollisions, velocity, age, angle, angularVelocity, vertices);

    }

    /**
     * Asteroid: Constructor that creates a new Asteroid object with necessary given
     * fields
     *
     * @param name   the name of the asteroid
     * @param pos    the position of the asteroid
     * @param mass   the mass of the asteroid
     */
    public Asteroid(String name, Vec pos, double mass, Vec vel) {
        super(name, pos, mass, false, true, vel, 0, 0, 0,
                ConvexPolygon.randomRegularPolygon(DEFAULT_RADIUS, DEFAULT_NUM_VERTICES));
    }

    /* METHODS */

    /**
     * draw: Draws an Asteroid simulation
     *
     * @param g Graphics
     */
    @Override
    public void draw(Graphics g) { // a temporary draw method
        super.draw(g);
    }

    /**
     * compareTo: Compares this and other Asteroid by speed
     *
     * @param other Asteroid to compare to
     * @return difference between this and other Asteroid's speeds
     */
    public double compareTo(Asteroid other) {
        return vel.getLength() - other.vel.getLength();
    }

    /**
     * shatter: Shatters (breaks apart) Asteroid
     *
     * @author Samuel
     * @param bm the BodyManager object
     */
    public void shatter(BodyManager bm) {

        Vec[] vertices = this.getVertices();

        // generate super triangle
        double minx = Double.POSITIVE_INFINITY, miny = Double.POSITIVE_INFINITY;
        double maxx = Double.NEGATIVE_INFINITY, maxy = Double.NEGATIVE_INFINITY;

        for (Vec v : vertices) {
            minx = Math.min(minx, v.getX());
            miny = Math.min(minx, v.getY());
            maxx = Math.max(maxx, v.getX());
            maxy = Math.max(maxx, v.getY());
        }

        double dx = (maxx - minx) * 10;
        double dy = (maxy - miny) * 10;

        Vec v0 = new Vec(minx - dx, miny - dy * 3);
        Vec v1 = new Vec(minx - dx, maxy + dy);
        Vec v2 = new Vec(maxx + dx * 3, maxy + dy);

        Triangle st = new Triangle(v0, v1, v2);

        // create ArrayList of triangles
        ArrayList<Triangle> triangles = new ArrayList<Triangle>();

        // start with the super triangle in the ArrayList
        triangles.add(st);

        // add the vertices one by one
        for (Vec vertex : vertices) {
            triangles = addVertex(vertex, triangles);
        }

        // if the vertices are not part of the super triangle, then it is a fragment
        for (Triangle tri : triangles) {
            if (!(tri.v0.equals(st.v0) || tri.v0.equals(st.v1) || tri.v0.equals(st.v2) || tri.v1.equals(st.v0)
                    || tri.v1.equals(st.v1) || tri.v1.equals(st.v2) || tri.v2.equals(st.v0) || tri.v2.equals(st.v1)
                    || tri.v2.equals(st.v2))) {

                // create new asteroid
                bm.addAsteroid(null, new Vec(pos), Math.abs(density * tri.getArea()), isStatic, resolveCollisions,
                        new Vec(vel), 0, angle, angularVelocity, tri.getVertices());
            }
        }

        // destroy the original asteroid
        this.destroy();
    }

    /**
     * getUniqueEdges: Retrieves unique edges out of an array with edges that are
     * not unique
     *
     * @author Samuel
     * @param edges the ArrayList of edge
     * @return edges a new ArrayList of unique edges
     */
    private static ArrayList<Vec[]> getUniqueEdges(ArrayList<Vec[]> edges) {

        // create new unique edges ArrayList
        ArrayList<Vec[]> uniqueEdges = new ArrayList<Vec[]>();

        // loop through all the edges
        for (int i = 0; i < edges.size(); i++) {

            boolean isUnique = true;

            // loop through all the other edges and if none of them are equal to this, then
            // the edge is unique
            for (int j = 0; j < edges.size() && isUnique; j++) {
                if (i != j && ((edges.get(i)[0].equals(edges.get(j)[0]) && edges.get(i)[1].equals(edges.get(j)[1]))
                        || (edges.get(i)[1].equals(edges.get(j)[0]) && edges.get(i)[0].equals(edges.get(j)[1])))) {

                    isUnique = false;

                }
            }

            // add the unique edge to the ArrayList
            if (isUnique) {
                uniqueEdges.add(edges.get(i));
            }
        }

        return uniqueEdges;
    }

    /**
     * addVertex: adds vertex and updates the ArrayList of triangles
     *
     * @author Samuel
     * @param vertex    the position of the vertiex
     * @param triangles the ArrayList storing all the current triangles
     * @return ArrayList<Triangle> an updated ArrayList of triangles
     */
    private static ArrayList<Triangle> addVertex(Vec vertex, ArrayList<Triangle> triangles) {
        ArrayList<Vec[]> edges = new ArrayList<Vec[]>();

        ArrayList<Triangle> newTriangles = new ArrayList<Triangle>();

        // Remove triangles with circumcircles containing the vertex
        for (Triangle tri : triangles) {
            if (tri.inCircumcircle(vertex)) {
                edges.add(new Vec[] { new Vec(tri.v0), new Vec(tri.v1) });
                edges.add(new Vec[] { new Vec(tri.v1), new Vec(tri.v2) });
                edges.add(new Vec[] { new Vec(tri.v2), new Vec(tri.v0) });
            } else {
                newTriangles.add(tri);
            }
        }

        // Get unique edges
        edges = getUniqueEdges(edges);

        // Create new triangles from the unique edges and new vertex
        for (Vec[] edge : edges) {
            newTriangles.add(new Triangle(edge[0], edge[1], vertex));
        }

        return newTriangles;
    };

    /**
     * onCollide: Shatters Asteroid on collision
     *
     * @param col the CollisionResolver object
     */
    @Override
    public void onCollide(CollisionResolver col, BodyManager bm, ParticleManager pm) {
        super.onCollide(col, bm, pm);

        if (col.getMaxImpulse() / this.density > MAX_IMPULSE && this.area > AREA_THRESHOLD) {
            this.shatter(bm);
            pm.addParticle(pos, 2 * Math.sqrt(this.area / Math.PI));
        }
    }

    /**
     * Triangle: This class represents a Triangle using vectors and stores
     * associated information
     *
     * @author Samuel Ho
     */
    private static class Triangle {

        /* FIELDS */
        private Vec v0; // instance field: first vertex of Triangle
        private Vec v1; // instance field: second vertex of Triangle
        private Vec v2; // instance field: third vertex of Triangle
        private Vec circumcenter; // instance field: circumcenter of Triangle
        private double circumradius; // instance field: circumradius of Triangle

        /* CONSTRUCTOR */

        /**
         * Triangle: Constructor that creates new Triangle object with given vertices
         *
         * @param v0 first vertex
         * @param v1 second vertex
         * @param v2 third vertex
         */
        private Triangle(Vec v0, Vec v1, Vec v2) {
            this.v0 = new Vec(v0);
            this.v1 = new Vec(v1);
            this.v2 = new Vec(v2);

            //calculate circumcenter
            Vec cp = v2.getSub(v0);
            Vec bp = v1.getSub(v0);
            double bx = bp.getX();
            double by = bp.getY();
            double cx = cp.getX();
            double cy = cp.getY();
            double dp = 2 * (bx * cy - by * cx);
            Vec u = new Vec((cp.getY() * bp.getLengthSq() - bp.getY() * cp.getLengthSq()) / dp,
                    (bp.getX() * cp.getLengthSq() - cp.getX() * bp.getLengthSq()) / dp);
            this.circumcenter = u.getAdd(v0);
            this.circumradius = u.getLength();
        }

        /**
         * inCircumcircle: checks if a point is inside the circumcircle
         *
         * @param v the point
         * @return true if it is in the circumcircle and false otherwise
         */
        private boolean inCircumcircle(Vec v) {
            return (this.circumcenter.getSub(v)).getLength() <= this.circumradius;
        }

        /**
         * getArea: Calculates area of Triangle
         *
         * @return area
         */
        private double getArea() {
            return (v1.getX() - v0.getX()) * (v1.getY() + v0.getY()) + (v2.getX() - v1.getX()) * (v2.getY() + v1.getY())
                    + (v0.getX() - v2.getX()) * (v0.getY() + v2.getY());
        }

        /**
         * getVertices: Retrieves vertices of Triangle
         *
         * @return Vec[] array of Vec representing vertices
         */
        private Vec[] getVertices() {
            double sum = getArea();

            //ensures that the vertices are in the right order
            if (sum >= 0) {
                return new Vec[] { new Vec(v0), new Vec(v1), new Vec(v2) };
            } else {
                return new Vec[] { new Vec(v2), new Vec(v1), new Vec(v0) };
            }
        }
    }

}
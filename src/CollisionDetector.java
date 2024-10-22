/**
 * File Name: CollisionDetector.java
 * @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 * Date: January 17, 2022
 * Description: This class is in charge of detecting collisions between two bodies.
 */

import java.util.ArrayList;

public class CollisionDetector {

    /* FIELDS */

    private Body body1, body2;
    private double overlap;
    private Vec normal;
    private ArrayList<Vec> contactPoints;

    /* ACCESSORS & MUTATORS */

    /**
     * @return the obj1
     */
    public Body getObj1() {
        return body1;
    }

    /**
     * @return the obj2
     */
    public Body getObj2() {
        return body2;
    }

    /**
     * @return the overlap
     */
    public double getOverlap() {
        return overlap;
    }

    /**
     * @return the normal
     */
    public Vec getNormal() {
        return normal;
    }

    /**
     * @return the contactPoints
     */
    public ArrayList<Vec> getContactPoints() {
        return contactPoints;
    }

    /* CONSTRUCTORS */

    /**
     * CollisionDetector:
     * Constructor that creates a new Collision Detector
     *
     * @param body1 the first body
     * @param body2 the second body
     */
    public CollisionDetector(Body body1, Body body2) {
        this.body1 = body1;
        this.body2 = body2;
    }

    /* METHODS */

    /**
     * aabb:
     * This method performs an axis aligned bounding box collision test.
     *
     * @return if the bounding boxes of the two bodies are colliding
     */
    private boolean aabb() {
        double[] n1 = body1.getMaxMin();
        double[] n2 = body2.getMaxMin();
        return n1[0] < n2[1] && n1[1] > n2[0] && n1[2] < n2[3] && n1[3] > n2[2];
    }

    /**
     * checkCollision:
     * This method checks for collisions between two bodies. In the process, it also
     * stores the collision normals, overlap, and contact points.
     *
     * @return if the two bodies are colliding
     */
    public boolean checkCollision() {

        // do a quick aabb bounding box collision test to improve efficiency
        if (!aabb()) {
            return false;
        }

        if (body1 instanceof Circle && body2 instanceof ConvexPolygon) {
            // swap
            Body temp = body1;
            body1 = body2;
            body2 = temp;
        }

        // handle circle and circle collisions
        if (body1 instanceof Circle && body2 instanceof Circle && checkCollisionCircleCircle()) {
            return true;
        }

        // handle polygon and circle collisions
        if (body1 instanceof ConvexPolygon && body2 instanceof Circle && checkCollisionPolygonCircle()) {
            collisionPointsPolygonCircle();
            return true;
        }

        // handle polygon and polygon collisions
        if (body1 instanceof ConvexPolygon && body2 instanceof ConvexPolygon && checkCollisionPolygonPolygon()) {
            collisionPointsPolygonPolygon();
            if (this.contactPoints == null) {
                return false;
            }
            return true;
        }

        return false;
    }

    /**
     * checkCollisionPolygonPolygon:
     * This method performs a separating axis test on two convex polygons. It also
     * stores the collision normal and overlap in the process.
     *
     * @return if the two convex polygons are colliding
     */
    private boolean checkCollisionPolygonPolygon() {

        // initialize temporary variables
        double overlap = Double.MAX_VALUE;
        Vec collisionNormal = null;
        ConvexPolygon c1 = (ConvexPolygon) body1;
        ConvexPolygon c2 = (ConvexPolygon) body2;

        // store each polygon's axes into an array
        Vec[] axes1 = getAxesPolygon(c1);
        Vec[] axes2 = getAxesPolygon(c2);

        // loop over the the first set of axes
        for (int i = 0; i < axes1.length; i++) {

            Vec axis = axes1[i];

            // project both shapes onto the axis
            double[] p1 = projection(c1, axis);
            double[] p2 = projection(c2, axis);

            // if the projection of the bodies overlap then they are not colliding
            if ((p1[1] < p2[0] || p2[1] < p1[0])) { // max greater than min
                return false;
            }

            // get the overlap
            double o = Math.min(p2[1] - p1[0], p1[1] - p2[0]);

            // check if it is the minimum
            if (o < overlap) {
                overlap = o;
                collisionNormal = axis;
            }
        }

        // loop over the the second of axes
        for (int i = 0; i < axes2.length; i++) {

            Vec axis = axes2[i];

            // project both shapes onto the axis
            double[] p1 = projection(c1, axis);
            double[] p2 = projection(c2, axis);

            // if the projection of the bodies overlap then they are not colliding
            if ((p1[1] < p2[0] || p2[1] < p1[0])) { // max greater than min
                return false;
            }

            // get the overlap
            double o = Math.min(p2[1] - p1[0], p1[1] - p2[0]);

            // check if it is the minimum
            if (o < overlap) {
                overlap = o;
                collisionNormal = axis;
            }
        }

        // store the normal and overlap
        this.overlap = overlap;
        this.normal = collisionNormal;

        // do one last check to make sure the normal is pointing in the right direction
        Vec dir = body1.getPos().getSub(body2.getPos());

        // flip the normal if it is not
        if (dir.dot(this.normal) > 0) {

            this.normal.reverse();
        }
        this.normal.normalize();

        return true;
    }

    /**
     * checkCollisionPolygonCircle:
     * This method performs a separating axis test on a convex polygon and a circle.
     * It also store the collision normal and overlap in the process. The first body
     * must be a convex polygon and the second body must be a circle.
     *
     * @return if the polygon and the circle are colliding
     */
    private boolean checkCollisionPolygonCircle() {

        // initialize temporary variables
        double overlap = Double.MAX_VALUE;
        Vec smallest = null;
        ConvexPolygon p = (ConvexPolygon) body1;
        Circle c = (Circle) body2;

        // store the polygon's axes into an array
        Vec[] axes = getAxesPolygon(p);

        // loop over the array of axes
        for (int i = 0; i < axes.length; i++) {

            Vec axis = axes[i];

            // project both shapes onto the axis
            double[] p1 = projection(p, axis);
            double[] p2 = projection(c, axis);

            // if the projection of the bodies overlap then they are not colliding
            if ((p1[1] < p2[0] || p2[1] < p1[0])) {// max greater than min
                return false;
            }

            // get the overlap
            double o = Math.min(p2[1] - p1[0], p1[1] - p2[0]);

            // check if it is the minimum
            if (o < overlap) {
                overlap = o;
                smallest = axis;
            }

        }

        // do one last test with a vector pointing from the center of the circle to
        // the closest vertex on the polygon
        Vec axis = getAxisCircle(p, c.getPos());

        // project both shapes onto the axis
        double[] p1 = projection(p, axis);
        double[] p2 = projection(c, axis);

        // if the projection of the bodies overlap then they are not colliding
        if ((p1[1] < p2[0] || p2[1] < p1[0])) {// max greater than min
            return false;
        }

        // get the overlap
        double o = Math.min(p2[1] - p1[0], p1[1] - p2[0]);

        // check if it is the minimum
        if (o < overlap) {
            overlap = o;
            smallest = axis;
        }

        // store the normal and overlap
        this.normal = smallest;
        this.overlap = overlap;

        // do one last check to make sure the normal is pointing in the right direction
        Vec dir = p.getPos().getSub(c.getPos());

        // flip the normal if it is not
        if (dir.dot(this.normal) > 0) {
            this.normal.reverse();
        }
        this.normal.normalize();

        return true;
    }

    /**
     * checkCollisionCircleCircle:
     * This method checks if two circles are colliding or not. The collision normal,
     * overlap, and contact points are stored.
     *
     * @return if the two circles are colliding
     */
    private boolean checkCollisionCircleCircle() {

        // initialize temporary variables
        Circle c1 = (Circle) body1;
        Circle c2 = (Circle) body2;
        Vec diff = c2.getPos().getSub(c1.getPos());
        double radii = c1.getRadius() + c2.getRadius();

        // store the overlap between the two circles
        this.overlap = diff.getLength() - radii;

        // if the circles are overlapping then they are colliding
        if (this.overlap < 0) {

            // store the collision normal
            diff.normalize();
            this.normal = diff;

            // store the contact points
            this.contactPoints = new ArrayList<Vec>();
            Vec cp1 = c1.getPos().getAdd(diff.getMult(c1.getRadius()));
            Vec cp2 = c2.getPos().getSub(diff.getMult(c2.getRadius()));
            this.contactPoints.add(cp1);
            this.contactPoints.add(cp2);

            return true;
        }

        return false;
    }

    /**
     * projection:
     * This method projects a convex polygon onto an axis.
     *
     * @param convexPolygon the convex polygon to be projected
     * @param axis          the axis which the polygon is projected on
     * @return the maximum and minimum values stored in an array [max,min]
     */
    private static double[] projection(ConvexPolygon convexPolygon, Vec axis) {

        // initialize temporary variables
        Vec[] vertices = convexPolygon.getVertices();
        double min = axis.dot(Vec.getAdd(vertices[0], convexPolygon.getPos()));
        double max = min;

        // loop through all the vertices in the polygon
        for (int i = 1; i < vertices.length; i++) {

            // project vertices onto axis
            double p = axis.dot(Vec.getAdd(vertices[i], convexPolygon.getPos()));

            // checks if it is the maximum or minimum
            if (p < min) {
                min = p;
            } else if (p > max) {
                max = p;
            }
        }

        return new double[] { min, max };
    }

    /**
     * projection:
     * This method projects a circle onto an axis
     *
     * @param circle the circle to be projected
     * @param axis   the axis which the circle is projected on
     * @return the maximum and minimum values stored in an array [max,min]
     */
    private static double[] projection(Circle circle, Vec axis) {

        // initialize temporary variables
        Vec dirRadius = axis.getMult(circle.getRadius());

        // find the ends of the circle parallel to the axis.
        Vec r1 = circle.getPos().getAdd(dirRadius);
        Vec r2 = circle.getPos().getSub(dirRadius);

        // project each ends of the circle onto the axis
        double min = axis.dot(r1);
        double max = axis.dot(r2);

        // check if the the minimum is actually the minimum or other way around
        if (min > max) {
            // swap
            double temp = max;
            max = min;
            min = temp;
        }

        return new double[] { min, max };
    }

    /**
     * getAxesPolygon:
     * This method gets all the normals of a convex polygon
     *
     * @param convexPolygon a convex polygon
     * @return all the axis of the convex polygon stored in a vector array
     */
    private static Vec[] getAxesPolygon(ConvexPolygon convexPolygon) {

        // initialize temporary variables
        Vec[] vertices = convexPolygon.getVertices();
        Vec[] axes = new Vec[vertices.length];

        // loop over the vertices
        for (int i = 0; i < vertices.length; i++) {

            // get two adjacent vertices
            Vec p1 = vertices[i];
            Vec p2 = vertices[(i + 1) % vertices.length];

            // subtract the two to get the edge vector
            Vec edge = Vec.getSub(p1, p2);

            // get the perpendicular vector
            Vec normal = edge.getPerp();
            normal.normalize();
            axes[i] = normal;
        }

        return axes;
    }

    /**
     * getAxisCircle:
     * This method gets the vector pointing from the center of the circle to the
     * closest vertex on the polygon
     *
     * @param convexPolygon the convex polygon which we are testing for collision
     *                      with the circle
     * @param circleCenter  the center of the circle
     * @return the axis
     */
    private static Vec getAxisCircle(ConvexPolygon convexPolygon, Vec circleCenter) {

        // initialize temporary variables
        Vec[] vertices = convexPolygon.getVertices();
        Vec closestVertex = vertices[0].getAdd(convexPolygon.getPos());

        // loop through all the vertices
        for (int i = 1; i < vertices.length; i++) {
            Vec vertex = vertices[i].getAdd(convexPolygon.getPos());

            // if this vertex is closer, then set the closest vertex to this vertex
            if (vertex.distanceSq(circleCenter) < closestVertex.distanceSq(circleCenter)) {
                closestVertex = vertex;
            }
        }

        return closestVertex.getSub(circleCenter).getNormalized();
    }

    /**
     * collisionPointsPolygonCircle:
     * This method finds all the contact points in a convex polygon and circle
     * collision.
     */
    private void collisionPointsPolygonCircle() {

        // initialize temporary variables
        ConvexPolygon p = (ConvexPolygon) body1;
        Circle c = (Circle) body2;
        Vec[] vertices = p.getVertices();
        Vec closestPoint = null;

        // loop through all the vertices of the convex polygon
        for (int i = 0; i < vertices.length; i++) {

            // get the position of two adjacent vertices
            Vec v1 = vertices[i].getAdd(p.getPos());
            Vec v2 = vertices[(i + 1) % vertices.length].getAdd(p.getPos());

            // find the closest point on this edge to the center of the circle
            Vec point = closestPointOnSegment(c.getPos(), v1, v2);

            // check if it is the closest point among all the edges
            if (closestPoint == null || c.getPos().distanceSq(point) < c.getPos().distanceSq(closestPoint)) {
                closestPoint = point;
            }
        }

        // store the contact point
        this.contactPoints = new ArrayList<Vec>();
        this.contactPoints.add(closestPoint);
    }

    /**
     * collisionPointsPolygonPolygon:
     * This method finds all contact points in a convex polygon and convex polygon
     * collision. It uses an clipping algorithm to maximize efficiency.
     *
     * @see <a href=
     *      "https://dyn4j.org/2011/11/contact-points-using-clipping/">Clipping
     *      Algorithm Explanation</a>
     */
    private void collisionPointsPolygonPolygon() {

        // find the edges closest to the collision
        Vec[] e1 = getSignificantEdges((ConvexPolygon) body1, normal);
        Vec[] e2 = getSignificantEdges((ConvexPolygon) body2, normal.getReversed());

        // determine which is the reference edge and which is the incident edge
        // the reference edge is the shorter one when projected onto the normal
        Vec[] ref, inc;
        if (Math.abs(Vec.getSub(e1[2], e1[1]).dot(normal)) <= Math.abs(Vec.getSub(e2[2], e2[1]).dot(normal))) {
            ref = e1;
            inc = e2;
        } else {
            ref = e2;
            inc = e1;
        }

        // determine the edge vector
        Vec refv = Vec.getSub(ref[2], ref[1]);
        refv.normalize();

        // project the first reference point onto this edge vector
        double o1 = refv.dot(ref[1]);

        // clip the incident edge by the first vertex of the reference edge
        ArrayList<Vec> cp = clip(inc[1], inc[2], refv, o1);

        // if two points are not left, then the clipping operation has failed
        if (cp.size() < 2)
            return;

        // project the second reference point onto the reference edge vector
        double o2 = refv.dot(ref[2]);

        // clip the what is left of the incident edge by the second vertex of the
        // reference edge in the OTHER direction
        cp = clip(cp.get(0), cp.get(1), refv.getReversed(), -o2);

        // if two points are not left, then the clipping operation has failed
        if (cp.size() < 2)
            return;

        // compute the reference edge normal
        Vec refNorm = refv.getPerp().getReversed();

        // project a vertex of the reference edge onto this normal
        double max = refNorm.dot(ref[0]);

        // if any of these points are past this threshold, the they are removed
        if (refNorm.dot(cp.get(1)) - max < 0.0) {
            cp.remove(cp.get(1));
        }
        if (refNorm.dot(cp.get(0)) - max < 0.0) {
            cp.remove(cp.get(0));
        }

        this.contactPoints = cp;
    }

    /**
     * closestPointOnSegment:
     * This method finds the point on a a segment closest to another point
     *
     * @param p a point
     * @param a one end of the segment
     * @param b the other end of the segment
     * @return the closest point on the segment to the other point.
     */
    private static Vec closestPointOnSegment(Vec p, Vec a, Vec b) {

        // find the edge vectors
        Vec ab = b.getSub(a);
        Vec ap = p.getSub(a);

        // project ap onto ab it and normalize it
        double proj = ab.dot(ap) / ab.getLengthSq();

        // check if the closest point is either ends of the segment
        if (proj <= 0) {
            return a;
        }

        if (proj >= 1) {
            return b;
        }

        return a.getAdd(ab.getMult(proj));
    }

    /**
     * getSignificantEdges:
     * This method finds the significant edges closest to the center of collision
     * used in the clipping algorithm.
     *
     * @param convexPolygon a convex polygon
     * @param n             the collision normal
     * @return an array of vectors storing the vertices of the significant edge
     *         [v1,v2]
     */
    private static Vec[] getSignificantEdges(ConvexPolygon convexPolygon, Vec n) {

        // initialize temporary variables
        double max = Double.NEGATIVE_INFINITY;
        Vec[] vertices = convexPolygon.getVertices();
        int index = 0;

        // find the farthest vertex in the polygon along the separation normal
        int c = vertices.length;
        for (int i = 0; i < c; i++) {
            double projection = n.dot(Vec.getAdd(vertices[i], convexPolygon.getPos()));
            if (projection > max) {
                max = projection;
                index = i;
            }
        }

        // find the edge that is most perpendicular to the separation normal
        Vec v = Vec.getAdd(convexPolygon.getPos(), vertices[index]);
        Vec v1 = Vec.getAdd(convexPolygon.getPos(), vertices[index + 1 == vertices.length ? 0 : index + 1]);
        Vec v0 = Vec.getAdd(convexPolygon.getPos(), vertices[index == 0 ? vertices.length - 1 : index - 1]);

        // Find edge vectors
        Vec l = Vec.getSub(v, v1);
        Vec r = Vec.getSub(v, v0);
        l.normalize();
        r.normalize();

        // the edge that is most perpendicular to n will have
        // a dot product closer to zero
        if (r.dot(n) <= l.dot(n)) {
            // return the edge vector, maintaining the direction
            return new Vec[] { v, v0, v };
        } else {
            // return the edge vector, maintaining the direction
            return new Vec[] { v, v, v1 };
        }
    }

    /**
     * clip:
     * This method determines which points are a part of the collision. It finds the
     * projection of two points onto the normal and determines if it is out of
     * bounds compared with the other projection.
     *
     * @param v1 the vertex of one end of the segment
     * @param v2 the vertex of the other end of the segment
     * @param n  the normal vector
     * @param o  the magnitude of the other projection
     * @return possible collision points
     */
    private static ArrayList<Vec> clip(Vec v1, Vec v2, Vec n, double o) {

        // initialize array of collision points
        ArrayList<Vec> cp = new ArrayList<Vec>();

        // projects each vertex onto the normal and subtracts the other projection
        double d1 = n.dot(v1) - o;
        double d2 = n.dot(v2) - o;

        // if either point is past o along n then the point is kept
        if (d1 >= 0.0)
            cp.add(v1);
        if (d2 >= 0.0)
            cp.add(v2);

        // if one of the points above are not valid, then the correct point needs to be
        // found
        if (d1 * d2 < 0.0) {

            // get edge vector
            Vec e = Vec.getSub(v2, v1);

            // compute the scalar for how far the point is along the edge
            double u = d1 / (d1 - d2);

            // multiply the edge vector by the scalar and add it to vertex 1
            e.mult(u);
            e.add(v1);
            cp.add(e);
        }

        return cp;
    }
}
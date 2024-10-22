/**
 * File Name: CollisionResolver.java
 * @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 * Date: January 17, 2022
 * Description: This class is responsible for resolving collisions between two bodies.
 */
import java.util.ArrayList;

public class CollisionResolver {

    /* FIELDS */

    private static final double ALLOWED_PENETRATION = 0.1;
    private static final double BIAS_FACTOR = 0.01;
    private static final double THRESHOLD_BOUNCE_VELOCITY = -0.1;

    private Body body1;
    private Body body2;
    private CollisionDetector col;
    private ArrayList<Vec> contacts;
    private int len;
    private double massNormal;
    private double massTangent;
    private double bias;
    private double pt;
    private double pn;
    private double elasticityBias;
    private double friction;
    private double maxImpulse;

    /* ACCESSORS & MUTATORS */

    /**
     * @return the contacts
     */
    public ArrayList<Vec> getContacts() {
        return contacts;
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(ArrayList<Vec> contacts) {
        this.contacts = contacts;
    }

    /**
     * @return the body1
     */
    public Body getBody1() {
        return body1;
    }

    /**
     * @param body1 the body1 to set
     */
    public void setBody1(Body body1) {
        this.body1 = body1;
    }

    /**
     * @return the body2
     */
    public Body getBody2() {
        return body2;
    }

    /**
     * @param body2 the body2 to set
     */
    public void setBody2(Body body2) {
        this.body2 = body2;
    }

    /**
     * @return the maxImpulse
     */
    public double getMaxImpulse() {
        return maxImpulse;
    }

    /**
     * @param maxImpulse the maxImpulse to set
     */
    public void setMaxImpulse(double maxImpulse) {
        this.maxImpulse = maxImpulse;
    }

    /* CONSTRUCTORS */

    /**
     * CollisionResolver: Constructor that creates new Collision Resolver
     *
     * @param col the collision detector object storing all collision info
     */
    public CollisionResolver(CollisionDetector col) {
        this.body1 = col.getObj1();
        this.body2 = col.getObj2();
        this.col = col;
        this.contacts = col.getContactPoints();
        this.len = this.contacts.size();
        this.pt = 0;
        this.pn = 0;
        this.friction = Math.sqrt(Math.pow(body1.getFriction(), 2) + Math.pow(body2.getFriction(), 2));
        this.maxImpulse = 0;
        // Precompute normal mass, tangent mass, and bias
        // loop through all collision points
        for (int i = 0; i < this.len; i++) {
            Vec contact = this.contacts.get(i);
            // find radius vectors
            Vec ra = Vec.getSub(contact, body1.getPos());
            Vec rb = Vec.getSub(contact, body2.getPos());
            // calculate the mass normal value for the two objects in the collision
            double rna = Vec.dot(ra, col.getNormal());
            double rnb = Vec.dot(rb, col.getNormal());
            double kNormal = body1.getInvMass() + body2.getInvMass()
                    + (Vec.dot(ra, ra) - rna * rna) * body1.getInvInertia()
                    + (Vec.dot(rb, rb) - rnb * rnb) * body2.getInvInertia();
            this.massNormal = 1.0 / kNormal;
            // calculate the mass tangent value for the two objects in the collision
            Vec tangent = Vec.crossProduct(col.getNormal(), 1);
            double rta = Vec.dot(ra, tangent);
            double rtb = Vec.dot(rb, tangent);
            double kTangent = body1.getInvMass() + body2.getInvMass()
                    + (Vec.dot(ra, ra) - rta * rta) * body1.getInvInertia()
                    + (Vec.dot(rb, rb) - rtb * rtb) * body2.getInvInertia();
            this.massTangent = 1.0 / kTangent;
            // the bias factor prevents bodies from sinking
            this.bias = -BIAS_FACTOR * Math.min(0, -col.getOverlap() + ALLOWED_PENETRATION);
            // calculate the elasticity constant
            Vec rv = Vec.getSub(Vec.getAdd(body2.getVel(), Vec.crossProduct(body2.getAngularVelocity(), rb)),
                    Vec.getAdd(body1.getVel(), Vec.crossProduct(body1.getAngularVelocity(), ra)));
            double vn = rv.dot(col.getNormal());
            double e = Math.min(body1.getElasticity(), body2.getElasticity());
            if (vn < THRESHOLD_BOUNCE_VELOCITY) {
                this.elasticityBias = -e * vn;
            } else {
                this.elasticityBias = 0;
            }
        }
    }

    /* METHODS */

    /**
     * applyImpulse: This method applies impulses on both bodies to resolve the
     * collision
     */
    public void applyImpulse() {

        // loop through all collision points
        for (int i = 0; i < this.len; i++) {

            Vec contact = this.contacts.get(i);

            // find the radius vectors
            Vec ra = Vec.getSub(contact, body1.getPos());
            Vec rb = Vec.getSub(contact, body2.getPos());

            // find relative velocity between the two objects
            Vec rv = Vec.getSub(Vec.getAdd(body2.getVel(), Vec.crossProduct(body2.getAngularVelocity(), rb)),
                    Vec.getAdd(body1.getVel(), Vec.crossProduct(body1.getAngularVelocity(), ra)));

            // find velocity along normal
            double vn = rv.dot(col.getNormal());

            // calculate current normal impulse
            double dPn = massNormal * (-vn + bias + this.elasticityBias);

            // save max impulse
            maxImpulse = Math.max(maxImpulse, dPn);

            // store original normal impulse
            double Pn0 = pn;

            // ensure that the normal impulse is greater than 0, so that the bodies are
            // separating
            pn = Math.max(Pn0 + dPn, 0);

            // calculate change in normal impulse
            dPn = pn - Pn0;

            // find normal impulse vector
            Vec Pn = col.getNormal().getMult(dPn);

            // apply normal impulses
            body1.applyImpulse(Pn.getReversed(), ra);
            body2.applyImpulse(Pn, rb);

            // recalculate relative velocity
            rv = Vec.getSub(Vec.getAdd(body2.getVel(), Vec.crossProduct(body2.getAngularVelocity(), rb)),
                    Vec.getAdd(body1.getVel(), Vec.crossProduct(body1.getAngularVelocity(), ra)));

            // find the velocity tangent to the collision
            Vec tangent = Vec.crossProduct(col.getNormal(), 1.0);
            double vt = Vec.dot(rv, tangent);

            // calculate the change in tangential impulse due to friction
            double dPt = massTangent * (-vt);

            // store original tangential impulse
            double Pt0 = pt;

            // calculate friction impulse
            double maxPt = friction * this.pn;

            // clamp the tangential impulse to ensure that bodies don't start increasing in
            // speed
            pt = clamp(Pt0 + dPt, -maxPt, maxPt);

            // calculate the change in tangential impulse
            dPt = pt - Pt0;

            // find tangential impulse vector
            Vec Pt = tangent.getMult(dPt);

            // apply tangential impulses
            body1.applyImpulse(Pt.getReversed(), ra);
            body2.applyImpulse(Pt, rb);
        }
    }

    /**
     * clamp: This method clamps a variable between a specified range.
     *
     * @param val the value of the variable
     * @param min the minimum value
     * @param max the maximum value
     * @return the clamped value of the variable
     */
    private static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

}
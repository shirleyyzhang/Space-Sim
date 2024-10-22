import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

/**
 * File Name: Body.java
 *
 * @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 * Date: January 17, 2022
 * Description: This class represents and stores information for a Body
 */
public abstract class Body {

    /* FIELDS */
    private static final Vec BOUNDARY_X = new Vec(-10000, 10000);
    private static final Vec BOUNDARY_Y = new Vec(-10000, 10000);
    private static final int MAX_ANGLE = 360;
    private static final int MAX_SPEED = 100;
    private static final double MERGE_RADIUS_FACTOR = 1.2;
    private static final double MERGE_TIME = 3000;
    private static final double DEFAULT_FRICTION = 0.3;
    private static final double DEFAULT_ELASTICITY = 0;
    private static double nameCounter = 0;

    protected Vec pos;
    protected double mass;
    protected double invMass;
    protected Vec vel;
    protected double angle;
    protected double angularVelocity;
    protected boolean isStatic;
    protected double inertia;
    protected double density;
    protected double friction;
    protected double invInertia;
    protected double elasticity;
    protected boolean resolveCollisions;
    protected HashMap<Body, Integer> collisions;
    protected String name;
    protected int age;
    protected double area;
    protected boolean destroyed;

    /* ACCESSORS & MUTATORS */

    /**
     * @return the resolveCollisions
     */
    public boolean isResolveCollisions() {
        return resolveCollisions;
    }

    /**
     * @param resolveCollisions the resolveCollisions to set
     */
    public void setResolveCollisions(boolean resolveCollisions) {
        this.resolveCollisions = resolveCollisions;
    }

    /**
     * @return the collisions
     */
    public HashMap<Body, Integer> getCollisions() {
        return collisions;
    }

    /**
     * @param collisions the collisions to set
     */
    public void setCollisions(HashMap<Body, Integer> collisions) {
        this.collisions = collisions;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the area
     */
    public double getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(double area) {
        this.area = area;
    }

    /**
     * @param invMass the invMass to set
     */
    public void setInvMass(double invMass) {
        this.invMass = invMass;
    }

    /**
     * @param inertia the inertia to set
     */
    public void setInertia(double inertia) {
        this.inertia = inertia;
    }

    /**
     * @param density the density to set
     */
    public void setDensity(double density) {
        this.density = density;
    }

    /**
     * @param invInertia the invInertia to set
     */
    public void setInvInertia(double invInertia) {
        this.invInertia = invInertia;
    }

    /**
     * @return the destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * @param destroyed the destroyed to set
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * @return the pos
     */
    public Vec getPos() {
        return pos;
    }

    /**
     * @param pos the pos to set
     */
    public void setPos(Vec pos) {
        this.pos = pos;
    }

    /**
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(double mass) {
        this.mass = mass;
        this.updateProperties();
    }

    /**
     * @return the invMass
     */
    public double getInvMass() {
        return invMass;
    }

    /**
     * @return the vel
     */
    public Vec getVel() {
        return vel;
    }

    /**
     * @param vel the vel to set
     */
    public void setVel(Vec vel) {
        this.vel = vel;
    }

    /**
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @param angle the angle to set
     */
    public void setAngle(double angle) {
        this.rotate(angle - this.angle);
    }

    /**
     * @return the angularVelocity
     */
    public double getAngularVelocity() {
        return angularVelocity;
    }

    /**
     * @param angularVelocity the angularVelocity to set
     */
    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    /**
     * @return the isStatic
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * @param isStatic the isStatic to set
     */
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        if (!this.isStatic) {
            this.invMass = 1.0 / this.mass;
            this.invInertia = 1.0 / this.inertia;
        }
    }

    /**
     * @return the inertia
     */
    public double getInertia() {
        return inertia;
    }

    /**
     * @return the inverse inertia
     */
    public double getInvInertia() {
        return invInertia;
    }

    /**
     * @return the density
     */
    public double getDensity() {
        return density;
    }

    /**
     * @return the friction
     */
    public double getFriction() {
        return friction;
    }

    /**
     * @param friction the friction to set
     */
    public void setFriction(double friction) {
        this.friction = friction;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the elasticity
     */
    public double getElasticity() {
        return elasticity;
    }

    /**
     * @param elasticity the elasticity to set
     */
    public void setElasticity(double elasticity) {
        this.elasticity = elasticity;
    }

    /* CONSTRUCTORS */

    /**
     * Body: Constructor that creates a new Body with given fields
     *
     * @param name              the name of the body
     * @param pos               the position of the body
     * @param mass              the mass of the body
     * @param isStatic          whether the body moves or not
     * @param resolveCollisions whether the body will collide with other bodies
     * @param vel               the velocity of the polygon
     * @param angularVelocity   the angular velocity of the body
     * @param angle             the angle of the body
     * @param age               the age of the body
     */
    public Body(String name, Vec pos, double mass, boolean isStatic, boolean resolveCollisions, Vec vel, int age,
                double angle, double angularVelocity) {
        this.pos = pos;
        this.mass = mass;
        this.vel = vel;
        this.angle = angle;
        this.angularVelocity = angularVelocity;
        this.isStatic = isStatic;
        this.friction = DEFAULT_FRICTION;
        if (name != null) {
            this.name = name;
        } else {
            this.name = "Object " + nameCounter;
            nameCounter++;
        }
        this.elasticity = DEFAULT_ELASTICITY;
        this.age = age;
        this.resolveCollisions = resolveCollisions;
        this.destroyed = false;
        this.collisions = new HashMap<Body, Integer>();
    }

    /* METHODS */

    /**
     * rotate: Rotates the body.
     *
     * @param angle the angle in degrees
     */
    protected void rotate(double angle) {
        this.angle += Math.toDegrees(angle);
        if (this.angle >= MAX_ANGLE) {
            this.angle -= MAX_ANGLE;
        } else if (this.angle < 0) {
            this.angle += MAX_ANGLE;
        }
    }

    /**
     * calcArea: Calculates the area of the body.
     *
     * @return the area of this body
     */
    protected abstract double calcArea();

    /**
     * calcMomentOfInertia: Calculates the moment of inertia of this body.
     *
     * @return the moment of inertia
     */
    protected abstract double calcMomentOfInertia();

    /**
     * getMaxMin: This method returns the maximum and minimum x/y values of this
     * body.
     *
     * @return an array storing [min x, max x, min y, max y]
     */
    public abstract double[] getMaxMin();

    /**
     * draw: Draws the body onto the screen.
     *
     * @param g the graphics component
     */
    public abstract void draw(Graphics g);

    /**
     * applyImpulse: Applies an impulse on the body.
     *
     * @param impulse the impulse vector
     * @param radius  the radius vector
     */
    public void applyImpulse(Vec impulse, Vec radius) {
        if (!isStatic) {
            vel.add(impulse.getMult(invMass));
            angularVelocity += 1.0 / inertia * Vec.cross(radius, impulse);
            angularVelocity = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, angularVelocity));
            vel = new Vec(Math.max(-MAX_SPEED, Math.min(MAX_SPEED, vel.getX())),
                    Math.max(-MAX_SPEED, Math.min(MAX_SPEED, vel.getY())));
        }
    }

    /**
     * move: Responsible for moving the body.
     *
     * @param bm associated body manager
     * @param pm associated particle manager
     */
    public void move(BodyManager bm, ParticleManager pm) {
        // don't update if it is destroyed
        if (this.isDestroyed()) {
            return;
        }

        // move bodies if they are not static
        if (!isStatic) {
            if (!vel.isNan()) {
                pos.add(vel);
            }
            if (!Double.isNaN(angularVelocity)) {
                rotate(angularVelocity);
            }
        } else {
            vel = new Vec(0, 0);
            angularVelocity = 0;
        }

        // destroy bodies if they travel too far
        if (this.pos.getX() < BOUNDARY_X.getX() || this.pos.getX() > BOUNDARY_X.getY()
                || this.pos.getY() < BOUNDARY_Y.getX() || this.pos.getY() > BOUNDARY_Y.getY()) {
            this.destroy();
        }

        // merge bodies if two bodies have been touching for long enough
        for (Map.Entry<Body, Integer> ent : collisions.entrySet()) {
            if (ent.getValue() > MERGE_TIME && !ent.getKey().isDestroyed()) {
                merge(ent.getKey(), bm, pm);
            }
        }
    }

    /**
     * updateProperties: Updates the properties of a body.
     */
    protected void updateProperties() {
        this.invMass = 1.0 / this.mass;
        this.area = this.calcArea();
        this.density = this.mass / this.area;
        this.inertia = this.calcMomentOfInertia();
        this.invInertia = 1.0 / this.inertia;
        if (isStatic) {
            this.invMass = 0;
            this.invInertia = 0;
        }
    }

    /**
     * This method is called whenever a body collides with another body
     *
     * @param col the CollisionResolver object
     * @param bm  the BodyManager object
     * @param pm  the ParticleManager object
     */
    public void onCollide(CollisionResolver col, BodyManager bm, ParticleManager pm) {
    }

    /**
     * destroy: destroys this Body
     */
    public void destroy() {
        for (Body key : collisions.keySet()) {
            key.collisions.remove(this);
        }
        this.destroyed = true;
    }

    /**
     * distaceTo: Calculates distance from this implicit and given explicit Body
     * using position vectors
     *
     * @param other Body
     * @return distance between
     */
    public double distanceTo(Body other) {
        return this.pos.distance(other.pos);
    }

    /**
     * merge: Merges two bodies and combines it into one body
     *
     * @param otherBody the other body that is merging with this
     * @param bm        BodyManager object
     * @param pm        ParticleManager object
     */
    private void merge(Body otherBody, BodyManager bm, ParticleManager pm) {

        // recalculate values for the planet.
        Vec newVel;
        double newAngularVelocity;
        double combinedMass = this.getMass() + otherBody.getMass();
        double massRatio = otherBody.getMass() / combinedMass;
        Vec newPos = (otherBody.getPos().getSub(this.getPos())).getMult(massRatio).getAdd(this.getPos());
        double newRadius = Math.sqrt((this.getArea() + otherBody.getArea()) / Math.PI) / MERGE_RADIUS_FACTOR;

        // check which body is bigger, and the new planet will inherit the bigger body's
        // properties.
        if (massRatio > 0.5) {
            newVel = new Vec(otherBody.vel);
            newAngularVelocity = otherBody.angularVelocity;
        } else {
            newVel = new Vec(this.vel);
            newAngularVelocity = this.angularVelocity;
        }

        // create planet and particles
        bm.addPlanet(null, newPos, combinedMass, false, true, newVel, 0, 0, newAngularVelocity, newRadius, 0, null);
        pm.addParticle(newPos, 2 * newRadius);

        // destroy bodies
        this.destroy();
        otherBody.destroy();
    }

    /**
     * toString: organizes properties into String
     *
     * @return String organized properties
     */
    @Override
    public String toString() {
        return "\nName: " + name + "\nPosition: " + pos + "\nMass: " + mass + "\nIs Static: " + isStatic
                + "\nResolve Collisions: " + resolveCollisions + "\nVelocity: " + vel + "\nAge: " + age + "\nAngle: "
                + angle + "\nAngular Velocity: " + angularVelocity;
    }

}
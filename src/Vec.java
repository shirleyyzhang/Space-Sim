/**
 * File Name: Vec.java
 * @author: Samuel Ho
 * Date: January 17, 2022 
 * Description: This class represents a Vector and performs necessary Vector calculations.
 */

public class Vec {

    /* FIELDS */
    private double x;	// Instance field: x-coordinate
    private double y;	// Instance field: y-coordinate

    /* ACCESSORS */

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /* MUTATORS */

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /* CONSTRUCTORS */

    /**
     * Vector:
     * Constructor that creates a new Vector object
     */
    public Vec() {
    }

    /**
     * Vector:
     * Constructor that creates a new Vector object with given coordinates
     *
     * @param x
     * @param y
     */
    public Vec(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    /**
     * Vector:
     * Constructor that creates a new Vector object same as given Vector
     *
     * @param v Vector
     */
    public Vec(Vec v) {
        set(v);
    }

    /* METHODS */

    /**
     * set:
     * Sets Vector with given coordinates
     *
     * @param x new x-coordinate
     * @param y new y-coordinate
     */
    public void set(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    /**
     * set:
     * Sets Vector same as given Vector v
     *
     * @param v other Vector
     */
    public void set(Vec v) {
        this.setX(v.getX());
        this.setY(v.getY());
    }

    /**
     * setZero:
     * Sets Vector coordinates as zero
     */
    public void setZero() {
        setX(0);
        setY(0);
    }

    /**
     * getComponents:
     * Retrieves x and y components of this Vector
     *
     * @return x and y components
     */
    public double[] getComponents() {
        return new double[] { getX(), getY() };
    }

    /**
     * getLength:
     * Calculates magnitude of this Vector, calculated using Pythagorean theorem
     *
     * @return magnitude of this Vector
     */
    public double getLength() {
        return Math.sqrt(getX() * getX() + getY() * getY());
    }

    /**
     * getLengthSq:
     * Calculates magnitude squared of this Vector, calculated using Pythagorean theorem
     *
     * @return magnitude squared of this Vector
     */
    public double getLengthSq() {
        return (getX() * getX() + getY() * getY());
    }

    /**
     * distanceSq:
     * Calculates distance squared between this Vector and given other Vector coordinates
     *
     * @param vx other x-coordinate
     * @param vy other y-coordinate
     * @return distance squared between this and other Vector
     */
    public double distanceSq(double vx, double vy) {
        vx -= getX();
        vy -= getY();
        return (vx * vx + vy * vy);
    }

    /**
     * distanceSq:
     * Calculates distance squared between this Vector and given other Vector
     *
     * @param v other Vector
     * @return distance squared between this and other Vector
     */
    public double distanceSq(Vec v) {
        double vx = v.getX() - this.getX();
        double vy = v.getY() - this.getY();
        return (vx * vx + vy * vy);
    }

    /**
     * distance:
     * Calculates distance between this Vector and given other Vector coordinates
     *
     * @param vx other x-coordinate
     * @param vy other y-coordinate
     * @return distance between this and other Vectors
     */
    public double distance(double vx, double vy) {
        vx -= getX();
        vy -= getY();
        return Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * distance:
     * Calculates distance between this Vector and given other Vector
     *
     * @param v other Vector
     * @return distance between this and other Vector
     */
    public double distance(Vec v) {
        double vx = v.getX() - this.getX();
        double vy = v.getY() - this.getY();
        return Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * getAngle:
     * Calculates angle of this Vector with horizontal axis
     *
     * @return angle of this Vector with horizontal axis
     */
    public double getAngle() {
        return Math.atan2(getY(), getX());
    }

    /**
     * normalize:
     * Normalizes (makes unit vector) this Vector
     *
     */
    public void normalize() {
        double magnitude = getLength();
        if (magnitude != 0) {
            setX(getX() / magnitude);
            setY(getY() / magnitude);
        }

    }

    /**
     * getNormalized:
     * Calculates and retrieves normalized (unit) this Vector
     *
     * @return Vec normalized Vector
     */
    public Vec getNormalized() {
        double magnitude = getLength();
        return new Vec(getX() / magnitude, getY() / magnitude);
    }

    /**
     * toCartesian:
     * Converts this Vector to Cartesian form
     *
     * @param magnitude
     * @param angle
     * @return Vec Cartesian Vector
     */
    public static Vec toCartesian(double magnitude, double angle) {
        return new Vec(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    /**
     * add:
     * Adds given Vector to this Vector
     *
     * @param v Vector to add with
     */
    public void add(Vec v) {
        this.setX(this.getX() + v.getX());
        this.setY(this.getY() + v.getY());
    }

    /**
     * add:
     * Adds given Vector coordinates to this Vector coordinates
     *
     * @param vx x-coordinate to add
     * @param vy y-coordinate to add
     */
    public void add(double vx, double vy) {
        this.setX(this.getX() + vx);
        this.setY(this.getY() + vy);
    }

    /**
     * getAdd:
     * Retrieves Vector sum of two given Vectors
     *
     * @param v1 Vector 1 to add
     * @param v2 Vector 2 to add
     * @return Vec Vector sum of two given Vectors
     */
    public static Vec getAdd(Vec v1, Vec v2) {
        return new Vec(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    /**
     * getAdd:
     * Retrieves Vectors sum of this and other given Vector
     *
     * @param v other Vector to add
     * @return Vec Vector sum of this and other
     */
    public Vec getAdd(Vec v) {
        return new Vec(this.getX() + v.getX(), this.getY() + v.getY());
    }

    /**
     * sub:
     * Subtracts this Vector with other Vector
     *
     * @param v Vector to subtract with
     */
    public void sub(Vec v) {
        this.setX(this.getX() - v.getX());
        this.setY(this.getY() - v.getY());
    }

    /**
     * sub:
     * Subtracts this Vector coordinates with other Vector coordinates
     *
     * @param vx x-coordinate to subtract
     * @param vy y-coordinate to subtract
     */
    public void sub(double vx, double vy) {
        this.setX(this.getX() - vx);
        this.setY(this.getY() - vy);
    }

    /**
     * getSub:
     * Retrieves Vector subtraction of two given Vectors
     *
     * @param v1 Vector 1 to subtract
     * @param v2 Vector 2 to subtract
     * @return Vec Vector subtraction of two given Vectors
     */
    public static Vec getSub(Vec v1, Vec v2) {
        return new Vec(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    }

    /**
     * getSub:
     * Retrieves Vectors subtraction of this and other given Vector
     *
     * @param v other Vector to subtract
     * @return Vec Vector subtraction of this and other
     */
    public Vec getSub(Vec v) {
        return new Vec(this.getX() - v.getX(), this.getY() - v.getY());
    }

    /**
     * mult:
     * Multiplies this vector by a given scalar
     *
     * @param scalar to multiply with
     */
    public void mult(double scalar) {
        setX(getX() * scalar);
        setY(getY() * scalar);
    }

    /**
     * getMult:
     * Retrieve multiplication of this Vector by given scalar
     *
     * @param scalar to multiply with
     * @return Vec multiplied Vector
     */
    public Vec getMult(double scalar) {
        return new Vec(getX() * scalar, getY() * scalar);
    }

    /**
     * div:
     * Divides this vector by a given scalar
     *
     * @param scalar to divide by
     */
    public void div(double scalar) {
        setX(getX() / scalar);
        setY(getY() / scalar);
    }

    /**
     * getDiv:
     * Retrieve division of this Vector by given scalar
     *
     * @param scalar to divide by
     * @return Vec divided Vector
     */
    public Vec getDiv(double scalar) {
        return new Vec(getX() / scalar, getY() / scalar);
    }

    /**
     * getPerp:
     * Retrieve perpendicular Vector to this Vector
     *
     * @return Vec perpendicular Vector
     */
    public Vec getPerp() {
        return new Vec(-getY(), getX());
    }

    /**
     * dot:
     * Calculates dot product of this and other given Vector
     *
     * @param v other Vector
     * @return double dot product
     */
    public double dot(Vec v) {
        return (this.getX() * v.getX() + this.getY() * v.getY());
    }

    /**
     * dot:
     * Calculates dot product of this coordinates and given coordinates
     *
     * @param vx other x-coordinate
     * @param vy other y-coordinate
     * @return double dot product
     */
    public double dot(double vx, double vy) {
        return (this.getX() * vx + this.getY() * vy);
    }

    /**
     * dot:
     * Calculates dot product of two given vectors
     *
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return double dot product
     */
    public static double dot(Vec v1, Vec v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    /**
     * cross:
     * Calculates cross of this and other given vector
     *
     * @param v other Vector
     * @return double cross
     */
    public double cross(Vec v) {
        return (this.getX() * v.getY() - this.getY() * v.getX());
    }

    /**
     * cross:
     * Calculates cross of this and other given vector coordinates
     *
     * @param vx
     * @param vy
     * @return double cross
     */
    public double cross(double vx, double vy) {
        return (this.getX() * vy - this.getY() * vx);
    }

    /**
     * cross:
     * Calculates cross of two given vectors
     *
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return double cross
     */
    public static double cross(Vec v1, Vec v2) {
        return (v1.getX() * v2.getY() - v1.getY() * v2.getX());
    }

    /**
     * crossProduct:
     * Calculates Vector cross product of given vector and scalar (order matters)
     *
     * @param a Vector
     * @param s Scalar
     * @return Vec cross product
     */
    public static Vec crossProduct(Vec a, double s) {
        return new Vec(s * a.getY(), -s * a.getX());
    }

    /**
     * crossProduct:
     * Calculates cross product of given scalar and vector (order matters)
     *
     * @param s scalar
     * @param a Vector
     * @return Vec cross product
     */
    public static Vec crossProduct(double s, Vec a) {
        return new Vec(-s * a.getY(), s * a.getX());
    }

    /**
     * project:
     * Projects given Vector on this Vector
     *
     * @param v
     * @return Vector projection
     */
    public double project(Vec v) {
        return (this.dot(v) / this.getLength());
    }

    /**
     * project:
     * Projects given Vector coordinates on this Vector
     *
     * @param vx
     * @param vy
     * @return Vector projection
     */
    public double project(double vx, double vy) {
        return (this.dot(vx, vy) / this.getLength());
    }

    /**
     * project:
     * Projects given Vector 2 on Vector 1
     *
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return Vector projection
     */
    public static double project(Vec v1, Vec v2) {
        return (dot(v1, v2) / v1.getLength());
    }

    /**
     * getProjectedVector:
     * Retrieves Vector projection of given other Vector on this Vector
     *
     * @param v other Vector
     */
    public Vec getProjectedVector(Vec v) {
        return this.getNormalized().getMult(this.dot(v) / this.getLength());
    }

    /**
     * getProjectedVector:
     * Retrieves Vector projection of given other Vector on this Vector
     *
     * @param vx other x-coordinate
     * @param vy other y-coordinate
     * @return Vec vector projection
     */
    public Vec getProjectedVector(double vx, double vy) {
        return this.getNormalized().getMult(this.dot(vx, vy) / this.getLength());
    }

    /**
     * getProjectedVector:
     * Retrieves Vector projection of Vector 2 on Vector 1
     *
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return Vector projection
     */
    public static Vec getProjectedVector(Vec v1, Vec v2) {
        return v1.getNormalized().getMult(Vec.dot(v1, v2) / v1.getLength());
    }

    /**
     * rotateBy:
     * Rotates this Vector by given angle
     *
     * @param angle
     */
    public void rotateBy(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double rx = getX() * cos - getY() * sin;
        setY(getX() * sin + getY() * cos);
        setX(rx);
    }

    /**
     * getRotatedBy:
     * Retrieves this Vector rotated by given angle
     *
     * @param angle
     * @return rotated Vector
     */
    public Vec getRotatedBy(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vec(getX() * cos - getY() * sin, getX() * sin + getY() * cos);
    }

    /**
     * rotateTo:
     * Rotates Vector to given angle
     *
     * @param angle
     */
    public void rotateTo(double angle) {
        set(toCartesian(getLength(), angle));
    }

    /**
     * getRotatedTo:
     * Retrieves this Vector rotated to given angle
     *
     * @param angle
     * @return rotated Vector
     */
    public Vec getRotatedTo(double angle) {
        return toCartesian(getLength(), angle);
    }

    /**
     * reverse:
     * Reverses x and y coordinates
     *
     */
    public void reverse() {
        setX(-getX());
        setY(-getY());
    }

    /**
     * getReversed:
     * Retrieves Vector with reversed (negative) x and y coordinates
     *
     * @return Vec reversed Vector
     */
    public Vec getReversed() {
        return new Vec(-getX(), -getY());
    }

    /**
     * clone:
     * Clones (retrieves copy of) this Vector
     *
     * @return Vec clone Vector
     */
    @Override
    public Vec clone() {
        return new Vec(getX(), getY());
    }

    /**
     * equals:
     * Determines if this Vector is equal to given Object
     *
     * @param obj Object to compare to
     * @return boolean if Vector and given Object are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec) {
            Vec v = (Vec) obj;
            return (getX() == v.getX()) && (getY() == v.getY());
        }
        return false;
    }

    /**
     * toString:
     * Organizes Vector information (coordinates) into a String
     *
     * @return String with organized Vector information
     */
    @Override
    public String toString() {
        return "Vec[" + getX() + ", " + getY() + "]";
    }

    /**
     * isNan:
     * Indicates if Vector coordinates are not a number
     * @return whether vector is not a number
     */
    public boolean isNan() {
        return Double.isNaN(x) || Double.isNaN(y);
    }
}
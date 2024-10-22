import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * File Name: BodyManager.java
 *
 * @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 * Date: January 17, 2022
 * Description: This class represents all celestial bodies and stores all corresponding information.
 */
public class BodyManager {

    /* FIELDS */

    private static final int COLLISION_ITERATIONS = 100;
    private static final double GRAVITY_CONSTANT = 0.05;
    private static final double DISTANCE_THRESHOLD = 100;
    private static final double AREA_THRESHOLD = 3000;

    private int numBodies;
    private ArrayList<Body> bodies;
    private ParticleManager particleManager;

    /* ACCESSORS */

    /**
     * @return collision iterations constant
     */
    public static int getCollisionIterations() {
        return COLLISION_ITERATIONS;
    }

    /**
     * @return gravity constant
     */
    public static double getGravityConstant() {
        return GRAVITY_CONSTANT;
    }

    /**
     * @return distance threshold
     */
    public static double getDistanceThreshold() {
        return DISTANCE_THRESHOLD;
    }

    /**
     * @return area threshold constant
     */
    public static double getAreaThreshold() {
        return AREA_THRESHOLD;
    }

    /**
     * @return number of bodies
     */
    public int getNumBodies() {
        return numBodies;
    }

    /**
     * @return the bodies
     */
    public ArrayList<Body> getBodies() {
        return bodies;
    }

    /**
     * @return associated particle manager
     */
    public ParticleManager getParticleManager() {
        return particleManager;
    }

    /* MUTATORS */

    /**
     * @param numBodies the number of bodies to set
     */
    public void setNumBodies(int numBodies) {
        this.numBodies = numBodies;
    }

    /**
     * @param particleManager the particle manager to set
     */
    public void setParticleManager(ParticleManager particleManager) {
        this.particleManager = particleManager;
    }

    /**
     * @param bodies the bodies to set
     */
    public void setBodies(ArrayList<Body> bodies) {
        this.bodies = bodies;
    }

    /* CONSTRUCTORS */

    /**
     * BodyManager: Creates a new BodyManager class which manages the bodies within
     * the system and their interactions
     *
     * @author Samuel
     * @param particleManager associated particle manager
     */
    public BodyManager(ParticleManager particleManager) {
        numBodies = 0;
        this.bodies = new ArrayList<Body>();
        this.particleManager = particleManager;
    }

    /* METHODS */

    /**
     * drawBodies: this method draws all the bodies
     *
     * @author Samuel
     * @param g the graphics component
     */
    public void drawBodies(Graphics g) {
        for (int i = 0; i < bodies.size(); i++) {
            if (bodies.get(i) != null) {
                bodies.get(i).draw(g);
            }
        }
    }

    /**
     * updateBodies: updates the bodies based off their interactions
     *
     * @author Samuel
     */
    public void updateBodies() {

        // initialize temporary variables storing collision resolvers
        ArrayList<CollisionResolver> cols = new ArrayList<CollisionResolver>();
        int len = bodies.size();

        // check for collisions
        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                Body obj1 = bodies.get(i);
                Body obj2 = bodies.get(j);
                updateCollisions(obj1, obj2, cols);
            }
        }

        // resolve collisions
        for (int j = 0; j < COLLISION_ITERATIONS; j++) {
            for (int i = 0; i < cols.size(); i++) {
                cols.get(i).applyImpulse();
            }
        }

        // call the onCollide methods
        for (CollisionResolver col : cols) {
            if (!col.getBody1().isDestroyed() && !col.getBody2().isDestroyed()) {
                col.getBody1().onCollide(col, this, particleManager);
                col.getBody2().onCollide(col, this, particleManager);
            }
        }

        // move bodies
        for (int i = 0; i < len; i++) {
            bodies.get(i).move(this, particleManager);
        }

        // apply gravity on every body
        updateGravity();

        // remove bodies
        Iterator<Body> b = bodies.iterator();
        while (b.hasNext()) {
            Body obj = b.next();
            if (obj.isDestroyed()) {
                b.remove();
                numBodies--;
            }
        }
    }

    /**
     * draw: Calling this method will draw the body.
     *
     * @author Samuel
     * @param graphics the instance variable used to draw from the Graphics class
     */
    public void draw(Graphics graphics) {
        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).draw(graphics);
        }
    }

    /**
     * updateGravity: Calling this method will apply gravity to the bodies.
     *
     * @author Samuel
     */
    private void updateGravity() {
        int len = bodies.size();

        // loop through every possible combination of bodies
        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                Body a = bodies.get(i);
                Body b = bodies.get(j);

                // calculate distance between two objects
                Vec difference = b.getPos().getSub(a.getPos());
                double lenSq = difference.getLengthSq();

                // ensure that the radius is not too small
                if (lenSq < DISTANCE_THRESHOLD) {
                    lenSq = DISTANCE_THRESHOLD;
                }

                // apply the formula Gm1m2/r^2
                double force = GRAVITY_CONSTANT * a.getMass() * b.getMass() / lenSq;
                Vec impulse = difference.getNormalized().getMult(force);

                // apply impulses
                a.applyImpulse(impulse, new Vec(0, 0));
                b.applyImpulse(impulse.getReversed(), new Vec(0, 0));

            }
        }
    }

    /**
     * updateCollisions: update collisions based on given bodies and collision
     * resolvers
     *
     * @author Samuel
     * @param obj1              the first body
     * @param obj2              the second body
     * @param collisionResolver the collision resolver
     */
    private void updateCollisions(Body obj1, Body obj2, ArrayList<CollisionResolver> collisionResolver) {

        if (obj1.isResolveCollisions() && obj2.isResolveCollisions()) {
            CollisionDetector col = new CollisionDetector(obj1, obj2);

            // check if the two bodies are colliding
            if (col.checkCollision()) {

                // add it to the array of collisions to be resolved
                collisionResolver.add(new CollisionResolver(col));

                // update the HashMap of all the collisions the bodies are colliding with
                if (obj1.getCollisions().putIfAbsent(obj2, 1) != null) {
                    obj1.getCollisions().put(obj2, obj1.getCollisions().get(obj2) + 1);
                }
                if (obj2.getCollisions().putIfAbsent(obj1, 1) != null) {
                    obj2.getCollisions().put(obj1, obj2.getCollisions().get(obj1) + 1);
                }
            } else {
                obj1.getCollisions().remove(obj2);
                obj2.getCollisions().remove(obj1);
            }
        }
    }

    /**
     * addStar: Calling this method will add a star object to the bodyManager.
     *
     * @param name              the name of the body
     * @param pos               the position of the body
     * @param mass              the mass of the body
     * @param isStatic          if the body moves or not
     * @param resolveCollisions does the body resolve collisions with bodies it
     *                          collides with
     * @param vel               the velocity in a direction of vectors
     * @param age               the age of the body
     * @param angle             the angle which the body is moving toward
     * @param angVel            the angular velocity of the body
     * @param rad               the radius of the body
     */
    public void addStar(String name, Vec pos, double mass, boolean isStatic, boolean resolveCollisions, Vec vel,
                        int age, double angle, double angVel, double rad) {
        Star s1 = new Star(name, pos, mass, isStatic, resolveCollisions, vel, age, angle, angVel, rad);
        numBodies++;
        bodies.add(s1);
    }

    /**
     * addStar: adds a new Star object to bodies given some necessary properties
     *
     * @param name
     * @param mass
     * @param age
     * @param radius
     */
    public void addStar(String name, double mass, int age, double radius) {
        Star s1 = new Star(name, mass, age, radius);
        numBodies++;
        bodies.add(s1);
    }

    /**
     * addPlanet: Calling this method will add a planet object to the bodyManager.
     *
     * @param name              the name of the body
     * @param pos               the position of the body
     * @param mass              the mass of the body
     * @param isStatic          if the body moves or not
     * @param resolveCollisions does the body resolve collisions with bodies it
     *                          collides with
     * @param vel               the velocity in a direction of vectors
     * @param age               the age of the body
     * @param angle             the angle which the body is moving toward
     * @param angVel            the angular velocity of the body
     * @param rad               the radius of the body
     * @param population        the population of the planet
     * @param orbitStar         the star which the planet orbits
     */
    public void addPlanet(String name, Vec pos, double mass, boolean isStatic, boolean resolveCollisions, Vec vel,
                          int age, double angle, double angVel, double rad, int population, String orbitStar) {
        Planet p1 = new Planet(name, pos, mass, isStatic, resolveCollisions, vel, age, angle, angVel, rad, population,
                orbitStar);
        numBodies++;
        bodies.add(p1);
    }

    /**
     * addPlanet: adds a new Planet object to bodies given some necessary properties
     *
     * @param name
     * @param mass
     * @param age
     * @param radius
     * @param orbitStar
     */
    public void addPlanet(String name, double mass, int age, double radius, String orbitStar) {
        Planet p1 = new Planet(name, mass, age, radius, orbitStar);
        numBodies++;
        bodies.add(p1);
    }

    /**
     * addMoon: Calling this method will add a moon object to the bodyManager.
     *
     * @param name              the name of the body
     * @param pos               the position of the body
     * @param mass              the mass of the body
     * @param isStatic          if the body moves or not
     * @param resolveCollisions does the body resolve collisions with bodies it
     *                          collides with
     * @param vel               the velocity in a direction of vectors
     * @param age               the age of the body
     * @param angle             the angle which the body is moving toward
     * @param angVel            the angular velocity of the body
     * @param rad               the radius of the body
     * @param orbitStar         the star which the moon orbits
     */
    public void addMoon(String name, Vec pos, double mass, boolean isStatic, boolean resolveCollisions, Vec vel,
                        int age, double angle, double angVel, double rad, String orbitStar) {
        Moon m1 = new Moon(name, pos, mass, isStatic, resolveCollisions, vel, age, angle, angVel, rad, orbitStar);
        numBodies++;
        bodies.add(m1);
    }

    /**
     * addMoon: adds a new Moon object to bodies given some necessary properties
     *
     * @param name
     * @param mass
     * @param age
     * @param radius
     * @param orbitPlanet
     */
    public void addMoon(String name, double mass, int age, double radius, String orbitPlanet) {
        Moon m1 = new Moon(name, mass, age, radius, orbitPlanet);
        numBodies++;
        bodies.add(m1);
    }

    /**
     * addAsteroid: Calling this method will add an asteroid object to the
     * bodyManager.
     *
     * @param name              the name of the body
     * @param pos               the position of the body
     * @param mass              the mass of the body
     * @param isStatic          if the body moves or not
     * @param resolveCollisions does the body resolve collisions with bodies it
     *                          collides with
     * @param vel               the velocity in a direction of vectors
     * @param age               the age of the body
     * @param angle             the angle which the body is moving toward
     * @param angVel            the angular velocity of the body
     * @param verticies         the positions of all the vertices of the asteroid
     *                          body
     */
    public void addAsteroid(String name, Vec pos, double mass, boolean isStatic, boolean resolveCollisions, Vec vel,
                            int age, double angle, double angVel, Vec[] verticies) {
        Asteroid a1 = new Asteroid(name, pos, mass, isStatic, resolveCollisions, vel, age, angle, angVel, verticies);
        numBodies++;
        bodies.add(a1);
    }

    /**
     * addAsteroid: adds a new Asteroid object to bodies given some necessary
     * properties
     *
     * @param name
     * @param mass
     */
    public void addAsteroid(String name, double mass) {
        Asteroid a1 = new Asteroid(name, new Vec(600, 600), mass, new Vec(0, 0));
        numBodies++;
        bodies.add(a1);
    }

    /**
     * displayStats: retrieves statistics of each body within the bodyManager to
     * display
     *
     * @return String simulation statistics
     */
    public String displayStats() {
        String stats = "\nSpace Simulation Statistics:";
        for (int i = 0; i < numBodies; i++) {
            stats += "\n" + bodies.get(i);
        }
        return stats;
    }

    /**
     * supportLife: determines if the planet given is habitable and can support life
     *
     * @param planetName the planet which the method checks
     * @return a boolean either the planet supports or does not support life
     */
    public boolean supportLife(String planetName) {
        if (search(planetName) != null) {
            Planet planet = (Planet) (search(planetName));
            Star nearestStar = (Star) search(planet.orbitStar);
            double minDistance = 100 * nearestStar.radius;
            double maxDistance = 200 * nearestStar.radius;
            double distance = (planet.getPos().getSub(nearestStar.getPos())).getLength();
            if (distance >= minDistance && distance <= maxDistance) {
                return true;
            }
            planet.population = 0;
        }
        return false;
    }

    /**
     * listBodies: retrieves list of the names of all bodies
     *
     * @return String list of bodies
     */
    public String listBodies() {
        String names = "\n\nBody names: ";
        for (int i = 0; i < numBodies; i++) {
            names += "\n" + (bodies.get(i)).name;
        }
        return names;
    }

    /**
     * search: looks for a Body object given the name of the body
     *
     * @param inBody the name of the body which you are searching for.
     * @return the body object that the user searches for
     */
    public Body search(String inBody) {
        // uses sequential search algorithm
        for (int i = 0; i < numBodies; i++) {
            if (bodies.get(i).name.equals(inBody))
                return bodies.get(i);
        }
        return null;
    }

    /**
     * sort: uses bubble sort to sort the bodies
     */
    public void sort() {
        // uses bubble sorting algorithm
        boolean sorted = false;
        for (int i = numBodies - 1; i > 0 && !sorted; i++) {
            sorted = true;
            for (int j = 0; j < i; j++) {
                if (bodies.get(j - 1).area > bodies.get(j).area) {
                    sorted = false;
                    Body temp = bodies.get(j);
                    bodies.add(j, bodies.get(j - 1));
                    bodies.add(j - 1, temp);
                }
            }
        }
    }

    /**
     * averageArea: finds the average area of the bodies
     *
     * @return the average area of the bodies
     */
    public double averageArea() {
        double sumArea = 0;
        for (int i = 0; i < numBodies; i++) {
            sumArea += bodies.get(i).area;
        }
        return sumArea / numBodies;
    }

    /**
     * averageAge: finds the average age of the bodies
     *
     * @return the average age of the bodies
     */
    public double averageAge() {
        double sumAge = 0;
        for (int i = 0; i < numBodies; i++) {
            sumAge += bodies.get(i).age;
        }
        return sumAge / numBodies;
    }

    /**
     * bodiesOlderThan: finds all the bodies which are older than the specified age
     *
     * @param minAge the minimum age of the body
     * @return the average area of the bodies
     */
    public String bodiesOlderThan(int minAge) {
        String oldBodies = "";
        for (int i = 0; i < numBodies; i++) {
            if (bodies.get(i).age > minAge) {
                oldBodies += bodies.get(i).name + " " + bodies.get(i).age + " yrs\n";
            }
        }
        return oldBodies;
    }

    /**
     * moonsOf: finds all the moons of the specified planet
     *
     * @param planetID the moons which orbit the planet
     */
    public void moonsOf(String planetID) {
        Moon[] moons = new Moon[20];
        int numMoons = 0;
        for (int i = 0; i < numBodies; i++) {
            if (bodies.get(i) instanceof Moon && ((Moon) bodies.get(i)).orbitPlanet.equals(planetID)
                    && numMoons <= 20) {
                moons[numMoons] = (Moon) bodies.get(i);
                numMoons++;
            }
        }
        Planet planet = (Planet) search(planetID);
        if (search(planetID) instanceof Planet) {
            planet.moons = moons;
        }
    }

    /**
     * planetsOf: lists the planets that orbit a star
     *
     * @param starID the star which the planets orbit around
     */
    public void planetsOf(String starID) {
        Planet[] planets = new Planet[20]; // TODO: constant for 20? Or another way to initialize array
        int numPlanets = 0;
        for (int i = 0; i < numBodies; i++) {
            if (bodies.get(i) instanceof Moon && ((Moon) bodies.get(i)).orbitPlanet.equals(starID)
                    && numPlanets <= 20) {
                planets[numPlanets] = (Planet) bodies.get(i);
                numPlanets++;
            }
        }
        Star star = (Star) search(starID);
        if (search(starID) instanceof Star) {
            star.setPlanets(planets);
        }
    }

    /**
     * largestMoon: finds the largest Moon object
     *
     * @return the largest Moon object
     */
    public Moon largestMoon() {
        Moon largest = null;
        for (int i = 0; i < numBodies; i++) {
            if (bodies.get(i) instanceof Moon) {
                Moon b = (Moon) bodies.get(i);
                if (largest == null || b.CompareTo(largest) > 0) {
                    largest = b;
                }
            }
        }
        return largest;

    }

    /**
     * mostPopPlanet: finds the most populated Planet object
     *
     * @return the most populated Planet object
     */
    public Planet mostPopPlanet() {
        Planet mostPopulated = null;

        for (int i = 0; i < numBodies; i++) {
            if (bodies.get(i) instanceof Planet) {
                Planet p = (Planet) bodies.get(i);
                if (mostPopulated == null || p.compareTo(mostPopulated) > 0) {
                    mostPopulated = p;
                }
            }
        }

        return mostPopulated;
    }

    /**
     * oldestStar: finds the oldest Star object
     *
     * @return the oldest Star object
     */
    public Star oldestStar() {
        Star oldest = null;
        for (int i = 0; i < numBodies; i++) {
            if (bodies.get(i) instanceof Star) {
                Star s = (Star) bodies.get(i);
                if (oldest == null || s.compareTo(oldest) > 0) {
                    oldest = s;
                }
            }
        }
        return oldest;
    }

    /**
     * fastestAsteroid: finds the fastest Asteroid object
     *
     * @return the fastestAsteroid object
     */
    public Asteroid fastestAsteroid() {
        Asteroid fastest = null;
        for (int i = 0; i < numBodies; i++) {
            if (bodies.get(i) instanceof Asteroid) {
                Asteroid a = (Asteroid) bodies.get(i);
                if (fastest == null || a.compareTo(fastest) > 0) {
                    fastest = a;
                }
            }
        }
        return fastest;
    }

    /**
     * shortestPath: Uses recursion to find shortest path from planet 1 to
     * destination, without traveling more than given maximum distance between two
     * planets
     *
     * @param planet1
     * @param destination
     * @param maxDistance
     * @return ShortestPath object containing total distance and path (planets
     *         visited in order)
     */
    public PlanetPath findShortestPath(String planet1, String destination, double maxDistance) {

        boolean found = false;
        Planet currPlanet = null;
        Planet nextPlanet = null;

        // Identifying current Planet
        for (int i = 0; i < numBodies; i++) {
            if (planet1.equalsIgnoreCase(bodies.get(i).name)) {
                currPlanet = (Planet) bodies.get(i);
                currPlanet.setVisited(true);
                found = true;
                System.out.println(currPlanet.name);
            }
        }

        if (found) {
            // Base case: destination reached
            if (planet1.equalsIgnoreCase(destination)) {
                return new PlanetPath(0, planet1);
            }

            // If destination not reached, identify next planet and find distance from next
            // planet to destination
            nextPlanet = closestTo(currPlanet);
            nextPlanet.setVisited(true);
            double distGone = currPlanet.distanceTo(nextPlanet);
            if (distGone < maxDistance) {
                // check if closest planet is within max distance
                if(findShortestPath(nextPlanet.name, destination, maxDistance)!=null)
                    return findShortestPath(nextPlanet.name, destination, maxDistance).addToPath(distGone, planet1);
            }
        }
        return null;

    }

    /**
     * closestTo: Finds other Planet closest to given Planet
     *
     * @param p1 given Planet
     * @return closest Planet
     */
    private Planet closestTo(Planet p1) {
        Planet p = null;
        if (p1 != null) {
            double shortestDist = -1;
            for (int i = 0; i < numBodies; i++) {
                Body b = bodies.get(i);
                if (!b.equals(p1) && b instanceof Planet && (shortestDist == -1 || p1.distanceTo(b) < shortestDist)) {
                    p = (Planet) b;
                    if (p.isVisited() == false) {
                        shortestDist = p1.distanceTo(p);
                    }
                }
            }
        }
        return p;
    }

    /**
     * resetPlanetVisits: Resets visited marker on planets
     */
    public void resetPlanetVisits() {
        for (int i = 0; i < numBodies; i++) {
            if (bodies.get(i) instanceof Planet) {
                ((Planet) (bodies.get(i))).setVisited(false);
            }
        }
    }

    /**
     * PlanetPath: This class represents a path between planets
     *
     * @author Susie Choi
     */
    private static class PlanetPath {

        /* FIELDS */
        private double distance; // instance field: total distance travelled
        private String planetsVisited; // instance field: list planets visited in order

        /* CONSTRUCTOR */

        /**
         * Path: Constructor that creates a new Path object to store distance traveled
         * and planets visited
         *
         * @param distance
         * @param planetsVisited containing planets visited
         */
        private PlanetPath(double distance, String planetsVisited) {
            this.distance = distance;
            this.planetsVisited = planetsVisited;
        }

        /**
         * addToPath: Adds distance traveled to a given planet to the path
         *
         * @param distance
         * @param planet
         * @return
         */
        private PlanetPath addToPath(double distance, String planet) {
            this.distance += distance;
            this.planetsVisited = planet + " " + this.planetsVisited;
            return new PlanetPath(this.distance, this.planetsVisited);
        }

        /**
         * toString: Organizes properties into a String
         *
         * @return String with organized properties
         */
        public String toString() {
            return "\n\nTotal Distance: " + distance + "\n\nPath: " + planetsVisited;
        }

    }

}
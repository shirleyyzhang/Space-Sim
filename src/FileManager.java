/**
 File Name: FileManager.java
 @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 Date: January 17, 2022
 Description: This class manages files associated with bodies.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {

    /* FIELDS */

    private String[] files;		// instance field: names of files
    private static int numFiles;		// class field: number of files
    private BodyManager bodyManager;	// instance field: bodyManager to manage bodies
    private ArrayList<Body> bodies;		// instance field: bodies to manage
    private final int MAX_NUM_FILES = 30;	// class field (constant): maximum number of files to manage

    /* ACCESSORS */

    /**
     * @return names of files
     */
    public String[] getFiles() {
        return files;
    }

    /**
     * @return number of files
     */
    public int getNumFiles() {
        return numFiles;
    }

    /**
     * @return maximum number of files to manage
     */
    public int getMAX_NUM_FILES() {
        return MAX_NUM_FILES;
    }

    /**
     * @return bodyManager
     */
    public BodyManager getBodyManager() {
        return bodyManager;
    }

    /**
     * @return bodies
     */
    public ArrayList<Body> getBodies() {
        return bodies;
    }

    /* MUTATORS */

    /**
     * @param files
     */
    public void setFiles(String[] files) {
        this.files = files;
    }

    /**
     * @param bodyManager
     */
    public void setBodyManager(BodyManager bodyManager) {
        this.bodyManager = bodyManager;
    }

    /**
     * @param bodies
     */
    public void setBodies(ArrayList<Body> bodies) {
        this.bodies = bodies;
    }


    /* CONSTRUCTORS */

    /**
     * FileManager:
     * Constructor that creates a new FileManager object
     *
     * @param input the names of all files which contain information for bodies
     * @param bodyManager to manage bodies in files
     */
    public FileManager(String[] input, BodyManager bodyManager) {

        files = new String[MAX_NUM_FILES];
        files[0] = "sample";

        numFiles = 0;

        for (int i = 0; i < numFiles; i++) {
            files[i] = input[i];
        }

        this.bodyManager = bodyManager;
        bodies = this.bodyManager.getBodies();
    }

    /* METHODS */

    /**
     * listFiles:
     * Retrives list of file names according to index number
     *
     * @return list of file names with corresponding ID
     */
    public String listFiles() {
        String sFiles = "\n\nFILES:";
        for(int i=0; i<numFiles; i++) {
            sFiles += "\n"+i+") "+files[i];
        }
        return sFiles;
    }

    /**
     * addFile:
     * Adds given file name to array of file names
     *
     * @param fileName
     */
    public void addFile(String fileName) {
        files[numFiles] = fileName;
        System.out.println(files[numFiles]);
        numFiles++;
    }

    /**
     * loadFile:
     * Load information from file at given index
     *
     * @param input index
     */
    public void loadFile(int input) {
        File file = new File(files[input]);
        System.out.println(input);
        System.out.println(files[input]);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader in = new BufferedReader(fileReader);
            int numBodies = Integer.parseInt(in.readLine());

            String typeBody;
            String name;
            String stringVec;
            Vec vector;
            double mass;
            String inBoolean;
            boolean isStatic;
            boolean resolveCollisions;
            Vec velocity;
            int age;
            double angle;
            double angularVelocity;
            double radius;
            int population;
            String orbit;
            Vec[] vertices;

            for (int i = 0; i < numBodies; i++) {
                String[] stringVectorArray;
                double[] doubleVectorArray;

                typeBody = in.readLine();
                name = in.readLine();

//				System.out.println(name);

                stringVec = in.readLine();
                stringVectorArray = stringVec.split(",");
                doubleVectorArray = Arrays.stream(stringVectorArray).mapToDouble(Double::parseDouble).toArray();
                vector = new Vec(doubleVectorArray[0], doubleVectorArray[1]);

                mass = Double.parseDouble(in.readLine());

                inBoolean = in.readLine();
                if (inBoolean.equals("T")) {
                    isStatic = true;
                } else {
                    isStatic = false;
                }

                inBoolean = in.readLine();
                if (inBoolean.equals("T")) {
                    resolveCollisions = true;
                } else {
                    resolveCollisions = false;
                }

                stringVec = in.readLine();
                stringVectorArray = stringVec.split(",");
                doubleVectorArray = Arrays.stream(stringVectorArray).mapToDouble(Double::parseDouble).toArray();
                velocity = new Vec(doubleVectorArray[0], doubleVectorArray[1]);

                age = Integer.parseInt(in.readLine());
                angle = Double.parseDouble(in.readLine());
                angularVelocity = Double.parseDouble(in.readLine());

                if (typeBody.equals("Star")) {

                    radius = Double.parseDouble(in.readLine());

                    bodyManager.addStar(name, vector, mass, isStatic, resolveCollisions, velocity, age, angle,
                            angularVelocity, radius);

                } else if (typeBody.equals("Planet")) {

                    radius = Double.parseDouble(in.readLine());
                    population = Integer.parseInt(in.readLine());
                    orbit = in.readLine();

                    bodyManager.addPlanet(name, vector, mass, isStatic, resolveCollisions, velocity, age, angle,
                            angularVelocity, radius, population, orbit);

                } else if (typeBody.equals("Moon")) {

                    radius = Double.parseDouble(in.readLine());
                    orbit = in.readLine();

                    bodyManager.addMoon(name, vector, mass, isStatic, resolveCollisions, velocity, age, angle,
                            angularVelocity, radius, orbit);

                } else if (typeBody.equals("Asteroid")) {
                    stringVec = in.readLine();
                    stringVectorArray = stringVec.split(",");
                    doubleVectorArray = Arrays.stream(stringVectorArray).mapToDouble(Double::parseDouble).toArray();
                    vertices = new Vec[(doubleVectorArray.length / 2)];
                    int k = 0;

                    for (int j = 0; j < vertices.length; j++) {
                        vertices[j] = new Vec(doubleVectorArray[k], doubleVectorArray[k + 1]);

                        k = k + 2;
                    }

                    bodyManager.addAsteroid(name, vector, mass, isStatic, resolveCollisions, velocity, age, angle,
                            angularVelocity, vertices);

                }
            }

            System.out.println("load");

            in.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /**
     * saveFile:
     * Save information to file at given index
     *
     * @param input index
     */
    public void saveFile(int input) {
        File file = new File(files[input]);

        try {
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter out = new BufferedWriter(fileWriter);

            int numOfBodies = bodies.size();
            String typeBody;
            String name;
            String stringPosition;
            Vec vector;
            double mass;
            String stringIsStatic;
            String stringResolveCollisions;
            String stringVelocity;
            int age;
            double angle;
            double angularVelocity;
            double radius;
            int population;
            String orbit;
            String stringVertices;
            Vec[] vertices;

            out.write(numOfBodies);
            out.newLine();

            for (int i = 0; i < numOfBodies; i++) {

                Body currentBody = bodies.get(i);

                if (currentBody instanceof Star) {
                    typeBody = "Star";
                    name = currentBody.getName();

                    vector = currentBody.getPos();
                    stringPosition = "" + vector.getX() + "," + vector.getY();

                    mass = currentBody.getMass();

                    if (currentBody.isStatic()) {
                        stringIsStatic = "T";
                    } else {
                        stringIsStatic = "F";
                    }

                    if (currentBody.isResolveCollisions()) {
                        stringResolveCollisions = "T";
                    } else {
                        stringResolveCollisions = "F";
                    }

                    vector = currentBody.getVel();
                    stringVelocity = "" + vector.getX() + "," + vector.getY();

                    age = currentBody.getAge();
                    angle = currentBody.getAngle();
                    angularVelocity = currentBody.getAngularVelocity();
                    radius = ((Circle) currentBody).getRadius();

                    out.write(typeBody);
                    out.newLine();
                    out.write(name);
                    out.newLine();
                    out.write(stringPosition);
                    out.newLine();
                    out.write(mass + "");
                    out.newLine();
                    out.write(stringIsStatic);
                    out.newLine();
                    out.write(stringResolveCollisions);
                    out.newLine();
                    out.write(stringVelocity);
                    out.newLine();
                    out.write("" + age);
                    out.newLine();
                    out.write("" + angle);
                    out.newLine();
                    out.write("" + angularVelocity);
                    out.newLine();
                    out.write("" + radius);
                    out.newLine();

                } else if (currentBody instanceof Planet) {
                    typeBody = "Planet";
                    name = currentBody.getName();

                    vector = currentBody.getPos();
                    stringPosition = "" + vector.getX() + "," + vector.getY();

                    mass = currentBody.getMass();

                    if (currentBody.isStatic()) {
                        stringIsStatic = "T";
                    } else {
                        stringIsStatic = "F";
                    }

                    if (currentBody.isResolveCollisions()) {
                        stringResolveCollisions = "T";
                    } else {
                        stringResolveCollisions = "F";
                    }

                    vector = currentBody.getVel();
                    stringVelocity = "" + vector.getX() + "," + vector.getY();

                    age = currentBody.getAge();
                    angle = currentBody.getAngle();
                    angularVelocity = currentBody.getAngularVelocity();
                    radius = ((Circle) currentBody).getRadius();
                    population = ((Planet) currentBody).getPopulation();
                    orbit = ((Planet) currentBody).getOrbitStar();

                    out.write(typeBody);
                    out.newLine();
                    out.write(name);
                    out.newLine();
                    out.write(stringPosition);
                    out.newLine();
                    out.write(mass + "");
                    out.newLine();
                    out.write(stringIsStatic);
                    out.newLine();
                    out.write(stringResolveCollisions);
                    out.newLine();
                    out.write(stringVelocity);
                    out.newLine();
                    out.write("" + age);
                    out.newLine();
                    out.write("" + angle);
                    out.newLine();
                    out.write("" + angularVelocity);
                    out.newLine();
                    out.write("" + radius);
                    out.newLine();
                    out.write("" + population);
                    out.newLine();
                    out.write(orbit);

                } else if (currentBody instanceof Moon) {
                    typeBody = "Moon";
                    name = currentBody.getName();

                    vector = currentBody.getPos();
                    stringPosition = "" + vector.getX() + "," + vector.getY();

                    mass = currentBody.getMass();

                    if (currentBody.isStatic()) {
                        stringIsStatic = "T";
                    } else {
                        stringIsStatic = "F";
                    }

                    if (currentBody.isResolveCollisions()) {
                        stringResolveCollisions = "T";
                    } else {
                        stringResolveCollisions = "F";
                    }

                    vector = currentBody.getVel();
                    stringVelocity = "" + vector.getX() + "," + vector.getY();

                    age = currentBody.getAge();
                    angle = currentBody.getAngle();
                    angularVelocity = currentBody.getAngularVelocity();
                    radius = ((Circle) currentBody).getRadius();
                    orbit = ((Moon) currentBody).getOrbitPlanet();

                    out.write(typeBody);
                    out.newLine();
                    out.write(name);
                    out.newLine();
                    out.write(stringPosition);
                    out.newLine();
                    out.write(mass + "");
                    out.newLine();
                    out.write(stringIsStatic);
                    out.newLine();
                    out.write(stringResolveCollisions);
                    out.newLine();
                    out.write(stringVelocity);
                    out.newLine();
                    out.write("" + age);
                    out.newLine();
                    out.write("" + angle);
                    out.newLine();
                    out.write("" + angularVelocity);
                    out.newLine();
                    out.write("" + radius);
                    out.newLine();
                    out.write(orbit);

                } else if (currentBody instanceof Asteroid) {
                    typeBody = "Asteroid";
                    name = currentBody.getName();

                    vector = currentBody.getPos();
                    stringPosition = "" + vector.getX() + "," + vector.getY();

                    mass = currentBody.getMass();

                    if (currentBody.isStatic()) {
                        stringIsStatic = "T";
                    } else {
                        stringIsStatic = "F";
                    }

                    if (currentBody.isResolveCollisions()) {
                        stringResolveCollisions = "T";
                    } else {
                        stringResolveCollisions = "F";
                    }

                    vector = currentBody.getVel();
                    stringVelocity = "" + vector.getX() + "," + vector.getY();

                    age = currentBody.getAge();
                    angle = currentBody.getAngle();
                    angularVelocity = currentBody.getAngularVelocity();
                    vertices = ((ConvexPolygon) currentBody).getVertices();
                    stringVertices = "";

                    for (int j = 0; j < vertices.length; j++) {
                        if (j != 0) {
                            stringVertices = stringVertices + ",";
                        }

                        stringVertices = stringVertices + vertices[j].getX() + "," + vertices[j].getY();
                    }

                    out.write(typeBody);
                    out.newLine();
                    out.write(name);
                    out.newLine();
                    out.write(stringPosition);
                    out.newLine();
                    out.write(mass + "");
                    out.newLine();
                    out.write(stringIsStatic);
                    out.newLine();
                    out.write(stringResolveCollisions);
                    out.newLine();
                    out.write(stringVelocity);
                    out.newLine();
                    out.write("" + age);
                    out.newLine();
                    out.write("" + angle);
                    out.newLine();
                    out.write("" + angularVelocity);
                    out.newLine();

                }
            }

            out.close();
            fileWriter.close();

        } catch (IOException e) {
            System.out.println("Error");
        }
    }
}
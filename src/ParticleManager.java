/**
 File Name: ParticleManager.java
 @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 Date: January 17, 2022
 Description: This class represents manages Particles.
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ParticleManager {

    /* FIELDS */
    private ArrayList<Particle> particles; // instance field: particles to manage
    private static final double PARTICLE_SPEED = 0.2;
    private static final int PARTICLE_LIFE = 1000;
    private static final int NUM_PARTICLE_SPAWN = 10;

    /* ACCESSORS */

    /**
     * @return particles being managed
     */
    public ArrayList<Particle> getParticles() {
        return particles;
    }

    /* MUTATORS */

    /**
     * @param particles the particles to set
     */
    public void setParticles(ArrayList<Particle> particles) {
        this.particles = particles;
    }

    /* CONSTRUCTORS */

    /**
     * ParticleManager: Creates empty default ParticleManager
     */
    public ParticleManager() {
        this.particles = new ArrayList<Particle>();
    }

    /**
     * ParticleManager: Creates ParticleManager to manage given array list of
     * Particles
     *
     * @param particles ArrayList<Particle> particles to manage
     */
    public ParticleManager(ArrayList<Particle> particles) {
        this.particles = particles;
    }

    /* METHODS */

    /**
     * addParticle: Adds a Particle to manage based on given fields
     *
     * @param pos      position
     * @param size     size
     */
    public void addParticle(Vec pos, double size) {
        Random rand = new Random();
        for (int i = 0; i < NUM_PARTICLE_SPAWN; i++) {
            double angle = rand.nextDouble() * Math.PI * 2;
            double magnitude = rand.nextDouble() * PARTICLE_SPEED - PARTICLE_SPEED / 2;
            double radius = rand.nextDouble() * size / 2 + size / 2;
            Particle p = new Particle(pos, new Vec(Math.cos(angle) * magnitude, Math.sin(angle) * magnitude),
                    PARTICLE_LIFE, Color.GRAY, radius);
            particles.add(p);
        }
    }

    // TODO: comment
    public void drawParticles(Graphics g) {
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            if (p != null) {
                p.draw(g);
            }
        }
        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
        ((Graphics2D) g).setComposite(alcom);
    }

    /**
     * updateParticles: updates Particle effects
     */
    public void updateParticles() {

        for (Particle p : particles) {
            p.setPos(p.getPos().getAdd(p.getVel()));
            p.setTimeLeft(p.getTimeLeft() - 1);
            if (p.getTimeLeft() <= 0) {
                p.setDestroyed(true);
            }
        }

        Iterator<Particle> p = particles.iterator();
        while (p.hasNext()) {
            Particle obj = p.next();
            if (obj.isDestroyed()) {
                p.remove();
            }
        }

    }

}
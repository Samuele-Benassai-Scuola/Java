package it.benassai.logic;

import java.util.Random;

public class Horse implements Runnable {

    public static final int MAX_DISTANCE = 100;
    private static int nextId = 0;

    private final int MAX_STEP = 5;
    private final int MAX_DELTA = 2;
    private final Random randomGenerator = new Random();

    private int id;
    private String name;
    private int distance = 0;
    private boolean finished = false;

    public Horse(String name) {
        this.name = name;

        this.id = nextId;
        nextId++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }


    public Horse(boolean finished) {
        this.finished = finished;
    }

    public void resetHorse() {
        finished = false;
        distance = 0;
    }

    public int randomStep() {
        final int baseStep = randomGenerator.nextInt(MAX_STEP + 1);
        final int baseDelta = randomGenerator.nextInt(MAX_DELTA + 1);
        final int deltaSign = randomGenerator.nextInt(2) == 0 ? 1 : -1;

        return baseStep + baseDelta * deltaSign;
    }

    public boolean step(int stepDistance) {
        // TODO: finish this
    }
    
}

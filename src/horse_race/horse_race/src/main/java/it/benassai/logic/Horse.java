package it.benassai.logic;

import java.util.Random;

public class Horse implements Runnable {

    public static final int MAX_DISTANCE = 100;
    private static int nextId = 0;

    private final int MIN_WAIT_TIME = 100;
    private final int MAX_WAIT_TIME = 1000;

    private final int MAX_STEP = 5;
    private final int MAX_DELTA = 2;
    private final Random randomGenerator = new Random();

    private int id;
    private String name;
    private int distance = 0;
    private int time = 0;
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

    public int getTime() {
        return time;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void reset() {
        finished = false;
        distance = 0;
        time = 0;
    }

    public int randomStepDistance() {
        final int baseStep = randomGenerator.nextInt(MAX_STEP + 1);
        final int baseDelta = randomGenerator.nextInt(MAX_DELTA + 1);
        final int deltaSign = randomGenerator.nextInt(2) == 0 ? 1 : -1;

        return baseStep + baseDelta * deltaSign;
    }

    public boolean randomStep() {
        return step(randomStepDistance());
    }

    public boolean step(int stepDistance) {
        if(finished)
            return false;

        distance += stepDistance;
        checkFinished();

        return true;
    }

    public boolean checkFinished() {
        if(finished)
            return true;
        
        if(distance >= MAX_DISTANCE) {
            distance = MAX_DISTANCE;
            finished = true;
        }

        return finished;
    }

    @Override
    public void run() {
        reset();

        final long start = System.currentTimeMillis();

        try {
            while(!finished) {
                Thread.sleep(randomGenerator.nextInt(MIN_WAIT_TIME, MAX_WAIT_TIME));
                randomStep();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        final long end = System.currentTimeMillis();

        time = (int) (end - start);
    }
    
}

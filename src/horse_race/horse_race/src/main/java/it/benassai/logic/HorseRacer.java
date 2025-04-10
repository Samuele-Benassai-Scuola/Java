package it.benassai.logic;

import java.util.ArrayList;
import java.util.List;

public class HorseRacer implements Runnable {

    private List<Horse> horses;
    private List<Horse> rank;
    private boolean racing = false;
    

    public HorseRacer(List<Horse> horses) {
        this.horses = horses;
    }

    public List<Horse> getHorses() {
        return horses;
    }

    public List<Horse> getRank() {
        return rank;
    }

    public boolean isRacing() {
        return racing;
    }

    @Override
    public void run() {
        racing = true;

        final List<Thread> threads = new ArrayList<>();

        for (final Horse horse : horses) {
            threads.add(new Thread(horse));
        }

        try {
            for (final Thread thread : threads) {
                thread.start();
            }
    
            for (final Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        rank = horses;
        rank.sort((x, y) -> {
            return x.getTime() - y.getTime();
        });

        racing = false;
    }
    
}

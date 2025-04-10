package it.benassai.logic;

import java.util.ArrayList;
import java.util.List;

public class HorseRaceLogic {

    public static final int HORSE_NUMBER = 5;

    private List<Horse> horses;
    private HorseRacer racer;
    private List<Horse> rank;

    public HorseRaceLogic(List<String> horseNames) {
        initHorses(horseNames);
        racer = new HorseRacer(horses);
    }

    private void initHorses(List<String> names) {
        horses = new ArrayList<>();

        for (int i = 0; i < HORSE_NUMBER; i++)
            horses.add(new Horse(names.get(i)));
    }

    public void race() {
        if(racer.isRacing())
            return;

        final Thread race = new Thread(racer);
        final Thread raceChecker = new Thread(new HorseRaceChecker(this, racer));

        raceChecker.setDaemon(true);

        raceChecker.start();
        race.start();
    }

    public void updateResult() {
        rank = racer.getRank();
    }

    public List<Horse> getHorses() {
        return List.copyOf(horses);
    }

    public List<Horse> getRank() {
        return List.copyOf(rank);
    }
    
}

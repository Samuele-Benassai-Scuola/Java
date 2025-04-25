package it.benassai.logic;

import java.util.ArrayList;
import java.util.List;

public class HorseRaceLogic {

    public static final int HORSE_NUMBER = 5;

    private HorseBet bet;

    private List<Horse> horses;
    private HorseRacer racer;
    private List<Horse> rank;

    private static HorseRaceLogic instance;

    public synchronized static HorseRaceLogic getInstance() {
        if (instance == null)
            instance = new HorseRaceLogic();
        
        return instance;
    }

    private HorseRaceLogic() {
        initHorses(HorseNames.NAMES);
        racer = new HorseRacer(horses);
        bet = new HorseBet();
    }

    private void initHorses(List<String> names) {
        horses = new ArrayList<>();

        for (int i = 0; i < HORSE_NUMBER; i++)
            horses.add(new Horse(names.get(i)));
    }

    public Horse getHorseByName(String name) {
        for (final Horse horse : horses)
            if (horse.getName().equals(name))
                return horse;
        return null;
    }

    public void resetAll() {
        initHorses(HorseNames.NAMES);
        racer = new HorseRacer(horses);
        rank = null;
        bet = new HorseBet();
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
        return horses;
    }

    public List<Horse> getRank() {
        return rank;
    }

    public HorseBet getBet() {
        return bet;
    }
    
}

package it.benassai.logic;

public class HorseRaceChecker implements Runnable {

    private HorseRaceLogic raceLogic;
    private HorseRacer racer;

    public HorseRaceChecker(HorseRaceLogic raceLogic, HorseRacer racer) {
        this.raceLogic = raceLogic;
        this.racer = racer;
    }

    @Override
    public void run() {
        while(!racer.isRacing());
        while(racer.isRacing());

        raceLogic.updateResult();
    }
    
}

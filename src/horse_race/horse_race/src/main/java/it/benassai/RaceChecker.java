package it.benassai;

import it.benassai.logic.Horse;
import it.benassai.logic.HorseRaceLogic;
import javafx.scene.control.ProgressBar;

public class RaceChecker implements Runnable {

    private RaceController raceController;
    private HorseRaceLogic horseRaceLogic = HorseRaceLogic.getInstance();

    public RaceChecker(RaceController raceController) {
        this.raceController = raceController;
    }

    @Override
    public void run() {
        while (horseRaceLogic.getRank() == null || horseRaceLogic.getRank().size() < HorseRaceLogic.HORSE_NUMBER)
            for (final ProgressBar horseBar : raceController.getHorseBars()) {
                final String name = raceController.getHorseName(horseBar);
                horseBar.setProgress((double) horseRaceLogic.getHorseByName(name).getDistance() / Horse.MAX_DISTANCE);
            }
        System.out.println("a");
        raceController.onRaceFinished();
    }
    
}

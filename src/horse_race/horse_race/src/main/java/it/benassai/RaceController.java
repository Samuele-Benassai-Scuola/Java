package it.benassai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.benassai.logic.Horse;
import it.benassai.logic.HorseRaceLogic;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RaceController {



    private static final String SEPARATOR = ":";

    private List<ProgressBar> horseBars = new ArrayList<>();

    @FXML
    private StackPane horseBarsContainer;
    @FXML
    private StackPane rankContainer;

    @FXML
    private void initialize() {
        initHorseBars();
        startRace();
    }

    private void initHorseBars() {
        horseBarsContainer.getChildren().add(createHorseBars());
    }

    private VBox createHorseBars() {
        final VBox result = new VBox();
        result.setAlignment(Pos.CENTER);

        for (final Horse horse : HorseRaceLogic.getInstance().getHorses()) {
            final HBox container = new HBox();
            container.setAlignment(Pos.CENTER);

            final Label horseLabel = new Label(horse.getName());

            final ProgressBar horseBar = new ProgressBar(0);
            horseBar.setId("HorseBar" + SEPARATOR + horseLabel.getText());

            horseBars.add(horseBar);

            container.getChildren().addAll(horseLabel, horseBar);
            result.getChildren().add(container);
        }

        return result;
    }

    private void startRace() {
        final Thread raceChecker = new Thread(new RaceChecker(this));
        raceChecker.setDaemon(true);

        raceChecker.start();
        HorseRaceLogic.getInstance().race();
    }

    public void onRaceFinished() {
        initRank();
    }

    private void initRank() {
        final Label title = new Label("Rank:");
        title.setAlignment(Pos.CENTER);

        rankContainer.getChildren().add(title);

        for (final Horse horse : HorseRaceLogic.getInstance().getRank()) {
            final HBox container = new HBox();
            container.setAlignment(Pos.CENTER);

            final Label name = new Label(horse.getName());
            final Label time = new Label("" + horse.getTime());

            name.setAlignment(Pos.CENTER);
            time.setAlignment(Pos.CENTER);

            container.getChildren().addAll(name, time);
            rankContainer.getChildren().add(container);
        }
    }

    @FXML
    private void switchToBet() throws IOException {
        App.setRoot("bet");
    }

    public String getHorseName(ProgressBar horseBar) {
        final String[] args = horseBar.getId().split(SEPARATOR);

        if (!args[0].equals("HorseBar"))
            return null;
        
        return args[1];
    }

    public List<ProgressBar> getHorseBars() {
        return horseBars;
    }

}
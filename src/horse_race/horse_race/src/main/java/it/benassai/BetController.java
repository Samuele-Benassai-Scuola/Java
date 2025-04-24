package it.benassai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.benassai.logic.Horse;
import it.benassai.logic.HorseRaceLogic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BetController {

    private static final String SEPARATOR = ":";

    private List<CheckBox> horseOptions = new ArrayList<>();

    @FXML
    private StackPane horseOptionsContainer;
    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        initHorseOptions();
    }

    private void initHorseOptions() {
        horseOptionsContainer.getChildren().add(createHorseOptions());
    }

    private VBox createHorseOptions() {
        final VBox result = new VBox();

        result.setAlignment(Pos.CENTER);

        for (final Horse horse : HorseRaceLogic.getInstance().getHorses()) {
            final CheckBox checkbox = new CheckBox();

            checkbox.setText(horse.getName());
            checkbox.setId("HorseOption" + SEPARATOR + checkbox.getText());

            checkbox.setOnAction((event) -> {
                selectHorseOption(event);
            });

            result.getChildren().add(checkbox);
            horseOptions.add(checkbox);
        }

        return result;
    }

    @FXML
    private void selectHorseOption(ActionEvent event) {
        final CheckBox horseOption = (CheckBox) event.getSource();
        final String[] args = horseOption.getId().split(SEPARATOR);

        if (!args[0].equals("HorseOption"))
            return;
        
        final String option = args[1];

        for (final CheckBox checkbox : horseOptions)
            checkbox.setSelected(false);
        
        horseOption.setSelected(true);
        HorseRaceLogic.getInstance().getBet().setName(option);
    }

    @FXML
    private void switchToRace() throws IOException {
        if (HorseRaceLogic.getInstance().getBet().getName() == null) {
            error("You haven't chosen any bet.");
            return;
        }

        App.setRoot("race");
    }

    @FXML
    private void error(String msg) {
        errorLabel.setText(msg);
    }
    
}

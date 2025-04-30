package it.benassai.logic.enums;

import java.util.Arrays;
import java.util.List;

public enum GameStage {
    
    SETUP,
    PLAYS,
    END;

    public GameStage next() {
        final List<GameStage> values = Arrays.asList(GameStage.values());

        if(this == values.getLast())
            return this;

        final int index = values.indexOf(this);
        return values.get(index + 1);
    }

}

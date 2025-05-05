package it.benassai.battleship_spring.logic.exceptions;

import it.benassai.battleship_spring.logic.enums.GameStage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WrongGameStageException extends RuntimeException {

    GameStage actualStage;

    public WrongGameStageException(GameStage actualStage) {
        this.actualStage = actualStage;
    }

    public WrongGameStageException(String message, GameStage actualStage) {
        super(message);
        this.actualStage = actualStage;
    }

    public WrongGameStageException(Throwable cause, GameStage actualStage) {
        super(cause);
        this.actualStage = actualStage;
    }

    public WrongGameStageException(String message, Throwable cause, GameStage actualStage) {
        super(message, cause);
        this.actualStage = actualStage;
    }

    public WrongGameStageException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace, GameStage actualStage) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.actualStage = actualStage;
    }

}

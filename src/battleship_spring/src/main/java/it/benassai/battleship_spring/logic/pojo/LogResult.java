package it.benassai.battleship_spring.logic.pojo;

import it.benassai.battleship_spring.logic.enums.Operation;
import lombok.Data;

@Data
public abstract class LogResult {
    
    final private int gameId;
    final private Operation opType;

}

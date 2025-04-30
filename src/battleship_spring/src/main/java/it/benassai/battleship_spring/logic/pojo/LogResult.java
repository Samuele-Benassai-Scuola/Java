package it.benassai.logic.pojo;

import it.benassai.logic.enums.Operation;
import lombok.Data;

@Data
public abstract class LogResult {
    
    final private int gameId;
    final private Operation opType;

}

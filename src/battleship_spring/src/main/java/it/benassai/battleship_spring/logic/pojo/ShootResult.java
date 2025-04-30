package it.benassai.logic.pojo;

import it.benassai.logic.Position;
import it.benassai.logic.enums.Operation;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ShootResult extends LogResult {
    
    final private String target;
    final private Position pos;
    final private boolean hit;
    final private boolean sunk;
    final private boolean gameFinished;

    public ShootResult(int gameId, String target, Position pos, boolean hit, boolean sunk, boolean gameFinished) {
        super(gameId, Operation.SHOOT);
        this.target = target;
        this.pos = pos;
        this.hit = hit;
        this.sunk = sunk;
        this.gameFinished = gameFinished;
    }

}

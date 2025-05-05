package it.benassai.battleship_spring.logic.pojo;

import it.benassai.battleship_spring.logic.Position;
import it.benassai.battleship_spring.logic.enums.Operation;
import it.benassai.battleship_spring.logic.enums.Orientation;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class PlaceResult extends LogResult {
    
    final private String owner;
    final private Position head;
    final private int size;
    final private Orientation orientation;

    public PlaceResult(int gameId, String owner, Position head, int size, Orientation orientation) {
        super(gameId, Operation.PLACE);
        this.owner = owner;
        this.head = head;
        this.size = size;
        this.orientation = orientation;
    }
    
}

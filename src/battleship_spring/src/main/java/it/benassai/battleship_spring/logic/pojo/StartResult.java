package it.benassai.logic.pojo;

import it.benassai.logic.enums.Operation;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class StartResult extends LogResult {
    
    final String owner1;
    final String owner2;
    public StartResult(int gameId, String owner1, String owner2) {
        super(gameId, Operation.START);
        this.owner1 = owner1;
        this.owner2 = owner2;
    }

    

}

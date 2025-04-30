package it.benassai.logic;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode.Exclude;

@Getter
@Setter
public class Tile extends Position {
    
    @Exclude
    private boolean hit;

    public Tile(int x, int y) {
        super(x, y);
        this.hit = false;
    }

    public Tile(Position pos) {
        super(pos);
        this.hit = false;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

}

package it.benassai.logic;

import lombok.Data;

@Data
public class Position implements Comparable<Position> {
    
    private int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Position addX(int dx) {
        return new Position(this.x + dx, this.y);
    }

    public Position addY(int dy) {
        return new Position(this.x, this.y + dy);
    }

    public Position add(int dx, int dy) {
        return this.addX(dx).addY(dy);
    }

    @Override
    public int compareTo(Position o) {
        if(this.x != o.x)
            return this.x - o.x;
        
        return this.y - o.y;
    }

    

}

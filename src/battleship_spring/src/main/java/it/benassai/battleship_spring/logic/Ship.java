package it.benassai.battleship_spring.logic;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import it.benassai.battleship_spring.logic.enums.Orientation;
import lombok.Getter;

@Getter
public class Ship {
    
    private List<Tile> tiles;
    private int size;
    private Orientation orientation;
    private boolean sunk;

    public Ship(Position head, int size, Orientation orientation) throws IllegalArgumentException {
        if(head.getX() < 0 || head.getY() < 0)
            throw new IllegalArgumentException("Invalid head.");
        if(size <= 0)
            throw new IllegalArgumentException("Invalid size.");
        if(orientation == null)
            throw new IllegalArgumentException("Invalid orientation.");
        
        this.size = size;
        this.orientation = orientation;
        this.sunk = false;
        this.initTiles(head);
    }

    public void initialize() {
        this.sunk = false;
        this.initTiles(this.getHead());
    }

    private void initTiles(Position head) {
        tiles = new ArrayList<>();

        final int dx = orientation == Orientation.HORIZONTAL ? 1 : 0;
        final int dy = orientation == Orientation.VERTICAL ? 1 : 0;

        for(int i = 0; i < size; i++)
            tiles.add(new Tile(head.add(dx * i, dy * i)));
    }

    private boolean calcSunk() {
        sunk = tiles
            .stream()
            .filter(tile -> !tile.isHit())
            .collect(Collectors.toList())
            .isEmpty();
        
        return sunk;
    }

    public boolean contains(Position pos) {
        return tiles.contains(pos);
    }

    public boolean contains(int x, int y) {
        return contains(new Position(x, y));
    }

    public boolean hit(int x, int y) {
        return hit(new Position(x, y));
    }

    public boolean hit(Position pos) {
        for(Tile tile : tiles) {
            if(tile.equals(pos)) {
                if(tile.isHit())
                    return false;
                else {
                    tile.setHit(true);
                    calcSunk();
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<Tile> getHeadAndTail() {
        List<Tile> result = new ArrayList<>();

        result.add(tiles.getFirst());
        result.add(tiles.getLast());

        return result;
    }

    public Tile getHead() {
        return tiles.getFirst();
    }

    public Tile getTail() {
        return tiles.getLast();
    }

    public boolean intersects(Ship other) {
        for (final Position otherPos : other.getTiles()) {
            for (final Position thisPos : this.getTiles()) {
                if (otherPos.equals(thisPos)) {
                    return true;
                }
            }
        }

        return false;
    }
    

}

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
        final Tile headThis = this.getHead();
        final Tile tailThis = this.getTail();
        final Tile headOther = other.getHead();
        final Tile tailOther = other.getTail();

        if(headThis.equals(headOther))
            return true;

        if(this.orientation == other.orientation) {

            if(headThis.getX() != headOther.getX() && headThis.getX() != headOther.getX())
                return false;

            boolean mainCheck = false;
            boolean zeroCheck = false;

            // mainCheck tries to see if they intersect, and so does zeroCheck
            // *---* #----#
            // If there's a case like the top one, the two checks are the same
            // *---#~~*----#
            // If there's a case like the top one, the two checks are different
            //
            // zeroCheck is always the same, except if one head/tail coincides
            // the only case where this check won't work is if the two are size 1
            // and coincide (already checked when the heads are compared)

            if(headThis.compareTo(tailOther) > 0 ^ tailThis.compareTo(headOther) > 0)
                mainCheck = true;
            if(headThis.compareTo(tailOther) < 0 ^ tailThis.compareTo(headOther) < 0)
                zeroCheck = true;
            
            if(mainCheck != zeroCheck)
                return true;
            
            return mainCheck;
        }
        else {
            // i is the intersection of the lines of the two ships
            // if it is included in both ships, then they intersect
            // it is included if the value changing is between the head and tail

            final int ix = this.orientation == Orientation.VERTICAL ? headThis.getX() : headOther.getX();
            final int iy = this.orientation == Orientation.HORIZONTAL ? headThis.getY() : headOther.getY();

            final boolean thisContains = this.orientation == Orientation.HORIZONTAL
                ? headThis.getX() <= ix && ix <= tailThis.getX()
                : headThis.getY() <= iy && iy <= tailThis.getY();
            
            final boolean otherContains = other.orientation == Orientation.HORIZONTAL
                ? headOther.getX() <= ix && ix <= tailOther.getX()
                : headOther.getY() <= iy && iy <= tailOther.getY();


            return thisContains && otherContains;
        }
    }

}

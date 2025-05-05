package it.benassai.battleship_spring.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import it.benassai.battleship_spring.logic.enums.GameStage;
import it.benassai.battleship_spring.logic.exceptions.AlreadyPutException;
import it.benassai.battleship_spring.logic.exceptions.AlreadyShotException;
import it.benassai.battleship_spring.logic.exceptions.CollisionException;
import it.benassai.battleship_spring.logic.exceptions.WrongGameStageException;
import it.benassai.battleship_spring.logic.pojo.PlaceResult;
import it.benassai.battleship_spring.logic.pojo.ShootResult;
import lombok.Getter;


@Getter
public class BattleShipBoard {

    public static final int SIZE = 10;
    public static final Integer[] SHIP_SIZES = new Integer[] {1, 1, 2, 2, 2, 3, 3, 3, 4};

    private int gameId;
    private String owner;
    private List<Ship> ships;
    private List<Tile> allShots;
    private Map<Integer, Integer> shipsNotPut;
    private GameStage gameStage;

    public BattleShipBoard(int gameId, String owner) {
        this.owner = owner;
        this.gameId = gameId;
        this.ships = new ArrayList<>();
        this.allShots = new ArrayList<>();
        this.shipsNotPut = shipSizesToMap();
        this.gameStage = GameStage.values()[0];
    }

    private Map<Integer, Integer> shipSizesToMap() {
        final Map<Integer, Integer> result = new HashMap<>();

        for(int val : SHIP_SIZES) {
            if(!result.containsKey(val))
                result.put(val, 0);
            
            result.put(val, 1 + result.get(val));
        }

        return result;
    }

    public boolean areShipsSunk() {
        return ships
            .stream()
            .filter(ship -> !ship.isSunk())
            .collect(Collectors.toList())
            .isEmpty();
    }

    public Ship getShipByPosition(Position pos) {
        for(final Ship ship : ships)
            if(ship.contains(pos))
                return ship;
        return null;
    }
    
    private boolean toPlaysStage() throws WrongGameStageException {
        if(gameStage != GameStage.SETUP)
            throw new WrongGameStageException(gameStage);
        
        gameStage = gameStage.next();
        return true;
    }

    private boolean toEndStage() throws WrongGameStageException {
        if(gameStage != GameStage.PLAYS)
            throw new WrongGameStageException(gameStage);
        
        gameStage = gameStage.next();
        return true;
    }

    public boolean checkInBoard(Position pos) {
        final boolean x = 0 <= pos.getX() && pos.getX() < SIZE;
        final boolean y = 0 <= pos.getY() && pos.getY() < SIZE;

        return x && y;
    }

    private int checkShipShot(Position pos) {
        for(int i = 0; i < ships.size(); i++) {
            if(ships.get(i).hit(pos))
                return i;
        }
        return -1;
    }

    public Set<Integer> getRemainingSizes() {
        return shipsNotPut.keySet();
    } 

    public ShootResult shoot(Position pos) throws WrongGameStageException, IllegalArgumentException, AlreadyShotException {
        if(gameStage != GameStage.PLAYS)
            throw new WrongGameStageException(gameStage);
        if(pos == null)
            throw new IllegalArgumentException("Position null.");
        if(!checkInBoard(pos))
            throw new IllegalArgumentException("Position out of bounds.");
        if(allShots.contains(pos))
            throw new AlreadyShotException("The tile was already shot.");
        
        final int indexShip = checkShipShot(pos);
        final boolean hit = indexShip > -1;
        if(hit)
            ships.get(indexShip).hit(pos);
        final boolean sunk = hit ? ships.get(indexShip).isSunk() : false;
        final boolean finished = areShipsSunk();

        final Tile shot = new Tile(pos);
        shot.setHit(hit);

        allShots.add(shot);

        if(finished)
            toEndStage();
        
        return new ShootResult(gameId, owner, pos, hit, sunk, finished);
    }

    public PlaceResult place(Ship ship) throws WrongGameStageException, IllegalArgumentException, 
    AlreadyPutException, CollisionException {
        if(gameStage != GameStage.SETUP)
            throw new WrongGameStageException(gameStage);
        if(ship == null)
            throw new IllegalArgumentException("Ship null.");
        if(!checkInBoard(ship.getHead()))
            throw new IllegalArgumentException("Ship out of bounds.");
        if(!checkInBoard(ship.getTail()))
            throw new IllegalArgumentException("Ship out of bounds.");
        if(!shipsNotPut.containsKey(ship.getSize()))
            throw new AlreadyPutException();
        
        ship.initialize();

        for(Ship s : ships)
            if(ship.intersects(s))
                throw new CollisionException("Ships intersected.");

        ships.add(ship);
        if(shipsNotPut.get(ship.getSize()) == 1)
            shipsNotPut.remove(ship.getSize());
        else
            shipsNotPut.put(ship.getSize(), -1 + shipsNotPut.get(ship.getSize()));

        if(shipsNotPut.size() == 0)
            toPlaysStage();

        return new PlaceResult(gameId, owner, ship.getHead(), ship.getSize(), ship.getOrientation());
    }
    
}

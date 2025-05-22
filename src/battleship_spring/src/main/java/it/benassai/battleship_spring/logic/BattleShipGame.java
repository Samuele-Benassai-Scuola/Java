package it.benassai.battleship_spring.logic;

import java.util.List;
import java.util.stream.Collectors;

import it.benassai.battleship_spring.logic.enums.GameStage;
import it.benassai.battleship_spring.logic.enums.Operation;
import it.benassai.battleship_spring.logic.exceptions.AlreadyPutException;
import it.benassai.battleship_spring.logic.exceptions.AlreadyShotException;
import it.benassai.battleship_spring.logic.exceptions.CollisionException;
import it.benassai.battleship_spring.logic.exceptions.WrongGameStageException;
import it.benassai.battleship_spring.logic.pojo.LogResult;
import it.benassai.battleship_spring.logic.pojo.PlaceResult;
import it.benassai.battleship_spring.logic.pojo.ShootResult;
import it.benassai.battleship_spring.logic.pojo.StartResult;
import lombok.Getter;

@Getter
public class BattleShipGame {
    
    private static int nextId = 0;
    private int id;
    private String[] owners = new String[2];
    private BattleShipBoard[] boards = new BattleShipBoard[2];
    private int currentOwnerIndex;
    private int winnerOwnerIndex;
    private GameStage gameStage;

    public BattleShipGame(String owner0, String owner1) {
        if(owner0 == null || owner1 == null)
            throw new IllegalArgumentException("Owner[s] null.");
        if(owner0.equals("") || owner1.equals(""))
            throw new IllegalArgumentException("Owner[s] empty.");
        if(owner0.equals(owner1))
            throw new IllegalArgumentException("Owners are the same.");
        
        this.id = nextId++;
        this.boards[0] = new BattleShipBoard(this.id, owner0);
        this.boards[1] = new BattleShipBoard(this.id, owner1);
        this.owners[0] = owner0;
        this.owners[1] = owner1;
        this.gameStage = GameStage.values()[0];
        this.winnerOwnerIndex = -1;
        this.currentOwnerIndex = 0;

        Log.getInstance().add(new StartResult(id, owner1, owner1));
    }

    public static BattleShipGame loadGame(int id) throws ClassCastException, IllegalStateException {
        List<LogResult> logs = Log.getInstance()
            .getData()
            .stream()
            .filter(log -> log.getGameId() == id)
            .collect(Collectors.toList());
        
        StartResult start = (StartResult) logs.removeFirst();
        BattleShipGame game = new BattleShipGame(start.getOwner1(), start.getOwner2());

        game.applyLogs(logs);

        return game;
    }

    public String getCurrentOwner() {
        return owners[currentOwnerIndex];
    }

    private void applyLogs(List<LogResult> logs) {
        for(LogResult log : logs) {
            final Operation opType = log.getOpType();

            switch (opType) {
                case PLACE: {
                    PlaceResult res = (PlaceResult) log;
                    place(new Ship(res.getHead(), res.getSize(), res.getOrientation()), res.getOwner());
                }
                case SHOOT: {
                    ShootResult res = (ShootResult) log;
                    shoot(res.getPos());
                }
                default:
                    throw new IllegalStateException();
            }
        }
    }

    private int nextOwnerIndex(int prev) {
        return prev ^ 1;
    }

    public String nextOwner(String owner) {
        final int index = indexOfOwner(owner);

        if(index == -1)
            throw new IllegalArgumentException("Illegal owner.");
        
        return owners[nextOwnerIndex(index)];
    }

    private boolean switchOwner() {
        if(gameStage == GameStage.END)
            return false;
        
        currentOwnerIndex = nextOwnerIndex(currentOwnerIndex);
        return true;
    }

    private boolean allBoardsInStage(GameStage stage) {
        for(BattleShipBoard b : boards)
            if(b.getGameStage() != stage)
                return false;
        return true;
    }

    public boolean checkShipHead(String owner, int x, int y) {
        final List<Position> heads = 
            getBoard(owner)
            .getShips()
            .stream()
            .map(ship -> ship.getHead())
            .collect(Collectors.toList());
        
        return heads.contains(new Position(x, y));
    }

    public Ship getShipFromHead(String owner, int x, int y) {
        final Position pos = new Position(x, y);

        for(Ship ship : getBoard(owner).getShips()) {
            if(ship.getHead().equals(pos))
                return ship;
        }

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

    private int indexOfOwner(String owner) {
        for(int i = 0; i < owners.length; i++)
            if(owners[i].equals(owner))
                return i;
        return -1;
    }

    public String getWinner() {
        if(winnerOwnerIndex < 0)
            throw new WrongGameStageException(gameStage);
        
        return owners[winnerOwnerIndex];
    }

    public BattleShipBoard getBoard(String owner) throws IllegalArgumentException {
        final int index = indexOfOwner(owner);

        if(index < 0)
            throw new IllegalArgumentException("Owner not present.");

        return boards[index];
    }

    public List<Tile> getShotTiles(String owner) throws IllegalArgumentException {
        final int index = indexOfOwner(owner);

        if(index < 0)
            throw new IllegalArgumentException("Owner not present.");

        return boards[index].getAllShots();
    }

    public ShootResult shoot(Position pos) throws WrongGameStageException, IllegalArgumentException, AlreadyShotException {
        if(gameStage != GameStage.PLAYS)
            throw new WrongGameStageException(gameStage);
        
        final int shooter = currentOwnerIndex;
        final int shot = nextOwnerIndex(currentOwnerIndex);

        ShootResult result = boards[shot].shoot(pos);

        if(result.isGameFinished()) {
            toEndStage();
            winnerOwnerIndex = shooter;
        }

        switchOwner();

        Log.getInstance().add(result);
        return result;
    }

    public PlaceResult place(Ship ship, String owner) throws WrongGameStageException, 
    IllegalArgumentException, AlreadyPutException, CollisionException {
        if(gameStage != GameStage.SETUP)
            throw new WrongGameStageException(gameStage);

        final int placer = indexOfOwner(owner);

        PlaceResult result = boards[placer].place(ship);

        if(allBoardsInStage(GameStage.PLAYS))
            toPlaysStage();
        
        Log.getInstance().add(result);
        
        return result;
    }

}

package it.benassai.battleship_spring.logic;

import java.util.List;
import java.util.Random;
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

public class BattleShipGameComputer {
    
    private static final String PLAYER_NAME = "PLAYER";
    private static final String COMPUTER_NAME = "COMPUTER";

    private static int nextId = 0;
    private int id;
    private String[] owners = new String[2];
    private BattleShipBoard[] boards = new BattleShipBoard[2];
    private int currentOwnerIndex;
    private int winnerOwnerIndex;
    private GameStage gameStage;

    public BattleShipGameComputer() {
        this.id = nextId++;
        this.boards[0] = new BattleShipBoard(this.id, PLAYER_NAME);
        this.boards[1] = new BattleShipBoard(this.id, COMPUTER_NAME);
        this.owners[0] = PLAYER_NAME;
        this.owners[1] = COMPUTER_NAME;
        this.gameStage = GameStage.values()[0];
        this.winnerOwnerIndex = -1;
        this.currentOwnerIndex = 0;

        Log.getInstance().add(new StartResult(id, PLAYER_NAME, COMPUTER_NAME));
    }

    public String getCurrentOwner() {
        return owners[currentOwnerIndex];
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

    /*public boolean scheckShipHead(String owner, int x, int y) {
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
    }*/

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

    public BattleShipBoard getPlayerBoard() throws IllegalArgumentException {
        final int index = indexOfOwner(PLAYER_NAME);

        if(index < 0)
            throw new IllegalArgumentException("Owner not present.");

        return boards[index];
    }

    public List<Tile> getComputerBoardHidden() throws IllegalArgumentException {
        final int index = indexOfOwner(COMPUTER_NAME);

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

        computerShoot();

        Log.getInstance().add(result);
        return result;
    }

    private ShootResult computerShoot() throws WrongGameStageException, IllegalArgumentException, AlreadyShotException {
        if(gameStage != GameStage.PLAYS)
            throw new WrongGameStageException(gameStage);

        final Random random = new Random();
        
        final int shooter = currentOwnerIndex;
        final int shot = nextOwnerIndex(currentOwnerIndex);

        Position pos = new Position(random.nextInt(BattleShipBoard.SIZE), random.nextInt(BattleShipBoard.SIZE));
        while (boards[shot].getAllShots().contains(pos)) {
            pos = new Position(random.nextInt(BattleShipBoard.SIZE), random.nextInt(BattleShipBoard.SIZE));
        }

        ShootResult result = boards[shot].shoot(pos);

        if(result.isGameFinished()) {
            toEndStage();
            winnerOwnerIndex = shooter;
        }

        switchOwner();

        Log.getInstance().add(result);
        return result;
    }

    public PlaceResult place(Ship ship) throws WrongGameStageException, 
    IllegalArgumentException, AlreadyPutException, CollisionException {
        if(gameStage != GameStage.SETUP)
            throw new WrongGameStageException(gameStage);

        final int placer = indexOfOwner(PLAYER_NAME);

        PlaceResult result = boards[placer].place(ship);

        if(allBoardsInStage(GameStage.PLAYS))
            toPlaysStage();
        
        Log.getInstance().add(result);
        
        return result;
    }

    private void computerPlace() throws WrongGameStageException, 
    IllegalArgumentException, AlreadyPutException, CollisionException {
        if(gameStage != GameStage.SETUP)
            throw new WrongGameStageException(gameStage);

        final int placer = indexOfOwner(COMPUTER_NAME);
        final BattleShipBoard board = boards[placer];

        while (!board.getRemainingSizes().isEmpty()) {
            // TODO: randomize the ship[s]

            final Ship ship = new Ship(null, null, null);

            final PlaceResult result = boards[placer].place(ship);

            if(allBoardsInStage(GameStage.PLAYS))
                toPlaysStage();
            
            Log.getInstance().add(result);
        }

        
    }
    
}

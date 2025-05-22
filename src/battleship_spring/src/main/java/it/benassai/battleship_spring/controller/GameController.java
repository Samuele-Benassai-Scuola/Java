package it.benassai.battleship_spring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.benassai.battleship_spring.logic.BattleShipBoard;
import it.benassai.battleship_spring.logic.BattleShipGame;
import it.benassai.battleship_spring.logic.Position;
import it.benassai.battleship_spring.logic.Ship;
import it.benassai.battleship_spring.logic.Tile;
import it.benassai.battleship_spring.logic.enums.Orientation;
import it.benassai.battleship_spring.logic.pojo.LogResult;

@RestController
@RequestMapping("/api")
public class GameController {

    private BattleShipGame battleShipGame;

    @DeleteMapping("/reset")
    public void reset() {
        battleShipGame = null;
    }

    @PostMapping("/initialize")
    public void initialize(@RequestBody String[] owners) {
        if (battleShipGame != null) {
            return; 
        }
        if (owners.length != 2) {
            return;
        }

        battleShipGame = new BattleShipGame(owners[0], owners[1]);
    }

    @GetMapping("/gameStage")
    public Map<String, String> getGameStage() {
        final Map<String, String> result = new HashMap<>();

        if (battleShipGame != null) {
            result.put("gameStage", battleShipGame.getGameStage().toString());
        }
        else {
            result.put("gameStage", null);
        }

        return result;
    }

    @GetMapping("/owners")
    public Map<String, String[]> getOwners() {
        final Map<String, String[]> result = new HashMap<>();

        result.put("owners", battleShipGame.getOwners());

        return result;
    }

    @GetMapping("/owners/current")
    public Map<String, String> getCurrentOwner() {
        final Map<String, String> result = new HashMap<>();

        result.put("owner", battleShipGame.getCurrentOwner());

        return result;
    }

    @GetMapping("/winner")
    public Map<String, String> getWinner() {
        final Map<String, String> result = new HashMap<>();

        result.put("winner", battleShipGame.getWinner());

        return result;
    }

    @GetMapping("/board/{owner}")
    public Map<String, List<Position>> getBoard(@PathVariable String owner) {
        final Map<String, List<Position>> result = new HashMap<>();

        final BattleShipBoard board = battleShipGame.getBoard(owner);

        final List<Position> shotPositions = new ArrayList<>();
        final List<List<Position>> shipsPositions = new ArrayList<>();

        for (final Position pos : board.getAllShots()) {
            shotPositions.add(pos);
        }

        for (final Ship ship : board.getShips()) {
            shipsPositions.add(
                ship.getTiles()
                    .stream()
                    .map(tile -> (Position)tile)
                    .collect(Collectors.toList())
                );
        }

        result.put("shot", shotPositions);

        int i = 0;
        for (final List<Position> shipPositions : shipsPositions) {
            i++;
            result.put("ship" + i, shipPositions);
        }

        return result;
    }

    @GetMapping("/board/hidden/{owner}")
    public Map<String, List<Position>> getHiddenBoard(@PathVariable String owner) {
        final Map<String, List<Position>> result = new HashMap<>();

        final BattleShipBoard board = battleShipGame.getBoard(owner);

        final List<Position> hitPositions = new ArrayList<>();
        final List<Position> missPositions = new ArrayList<>();

        for (final Tile tile : board.getAllShots()) {
            if (tile.isHit()) {
                hitPositions.add(tile);
            }
            else {
                missPositions.add(tile);
            }
        }

        result.put("hit", hitPositions);
        result.put("miss", missPositions);

        return result;
    }

    @GetMapping("/remainingShips/{owner}")
    public Map<Integer, Integer> getRemainingShips(@PathVariable String owner) {
        return battleShipGame.getBoard(owner).getShipsNotPut();
    }

    @PostMapping("/place")
    public void place(@RequestBody Map<String, String> body) {
        final int x = Integer.parseInt(body.get("x"));
        final int y = Integer.parseInt(body.get("y"));
        final int size = Integer.parseInt(body.get("size"));
        final String owner = body.get("owner");
        Orientation orientation = null;

        for (final Orientation val : Orientation.values()) {
            if (val.toString().equals(body.get("orientation"))) {
                orientation = val;
                break;
            }
        }

        if (orientation == null) {
            throw new IllegalArgumentException("Orientation not valid.");
        }

        final Ship ship = new Ship(new Position(x, y), size, orientation);

        battleShipGame.place(ship, owner);
    }

    @PostMapping("/shoot")
    public LogResult shoot(@RequestBody Map<String, String> body) {
        final int x = Integer.parseInt(body.get("x"));
        final int y = Integer.parseInt(body.get("y"));

        final LogResult result = battleShipGame.shoot(new Position(x, y));

        return result;
    }
}
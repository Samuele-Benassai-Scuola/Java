package it.benassai.battleship_spring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@RestController
@RequestMapping("/api")
public class GameController {

    private BattleShipGame battleShipGame;

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

    @GetMapping("/board/{owner}")
    public Map<String, List<Position>> getBoard(@PathVariable String owner) {
        final Map<String, List<Position>> result = new HashMap<>();

        final BattleShipBoard board = battleShipGame.getBoard(owner);

        final List<Position> shotPositions = new ArrayList<>();
        final List<Position> shipPositions = new ArrayList<>();

        for (final Position pos : board.getAllShots()) {
            shotPositions.add(pos);
        }

        for (final Ship ship : board.getShips()) {
            for (final Position pos : ship.getTiles()) {
                shipPositions.add(pos);
            }
        }

        result.put("shot", shotPositions);
        result.put("ships", shipPositions);

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
}
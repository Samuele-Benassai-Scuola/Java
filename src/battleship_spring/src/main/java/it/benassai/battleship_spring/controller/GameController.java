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
    public void initialize(@RequestBody String owner1, @RequestBody String owner2) {
        if (battleShipGame != null) {
            return; 
        }

        battleShipGame = new BattleShipGame(owner1, owner2);
    }

    @GetMapping("/gameStage")
    public Map<String, String> getGameStage() {
        final Map<String, String> result = new HashMap<>();

        result.put("gameStage", battleShipGame.getGameStage().toString());

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
    public Map<String, List<Tile>> getHiddenBoard(@PathVariable String owner) {
        final Map<String, List<Tile>> result = new HashMap<>();

        // TODO: put it with separate lists, not Tile but Position

        final BattleShipBoard board = battleShipGame.getBoard(owner);

        result.put("shot", board.getAllShots());

        return result;
    }


}
package it.benassai.battleship_spring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.benassai.battleship_spring.logic.BattleShipGame;

@RestController
@RequestMapping("/api")
public class GameController {

    private BattleShipGame battleShipGame = new BattleShipGame(null, null);

    @GetMapping("/gameStage")
    public Map<String, String> getGameStage() {
        final Map<String, String> result = new HashMap<>();

        result.put("gameStage", battleShipGame.getGameStage().toString());

        return result;
    }


}
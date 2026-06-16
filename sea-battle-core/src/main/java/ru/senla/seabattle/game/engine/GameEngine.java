package ru.senla.seabattle.game.engine;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.game.history.ShotInfo;
import ru.senla.seabattle.player.Player;

public interface GameEngine {

    ShotInfo makeShot(Player attacker,
                      Player defender,
                      Coordinate coordinate);

    boolean isGameOver(Player player);
}

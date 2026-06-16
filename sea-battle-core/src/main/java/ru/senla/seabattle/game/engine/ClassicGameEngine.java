package ru.senla.seabattle.game.engine;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;
import ru.senla.seabattle.game.history.ShotInfo;
import ru.senla.seabattle.player.Player;

public class ClassicGameEngine implements GameEngine {

    @Override
    public ShotInfo makeShot(Player attacker,
                             Player defender,
                             Coordinate coordinate) {
        ShotResult result = defender.getOwnBoard().shoot(coordinate);

        attacker.incrementShotsCount();

        if (result == ShotResult.HIT || result == ShotResult.KILL) {
            attacker.incrementHitsCount();
            attacker.getEnemyViewBoard().markHit(coordinate);
        } else {
            attacker.getEnemyViewBoard().markMiss(coordinate);
        }

        return new ShotInfo(
                coordinate,
                result
        );
    }

    @Override
    public boolean isGameOver(Player player) {
        return player.getOwnBoard().allShipsDestroyed();
    }
}

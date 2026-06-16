package ru.senla.seabattle.game.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;
import ru.senla.seabattle.bot.EasyBotStrategy;
import ru.senla.seabattle.game.history.ShotInfo;
import ru.senla.seabattle.player.BotPlayer;
import ru.senla.seabattle.player.Player;
import ru.senla.seabattle.ship.Ship;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassicGameEngineTest {

    private ClassicGameEngine gameEngine;
    private Player attacker;
    private Player defender;

    @BeforeEach
    void setUp() {
        gameEngine = new ClassicGameEngine();

        attacker = new BotPlayer(
                "Атакующий",
                new EasyBotStrategy(new Random(1))
        );

        defender = new BotPlayer(
                "Защитник",
                new EasyBotStrategy(new Random(2))
        );
    }

    @Test
    void makeShot_shouldReturnMissAndMarkEnemyViewBoard_whenShotMisses() {
        Coordinate coordinate = new Coordinate(0, 0);

        ShotInfo shotInfo = gameEngine.makeShot(
                attacker,
                defender,
                coordinate
        );

        assertEquals(ShotResult.MISS, shotInfo.getResult());
        assertEquals(coordinate, shotInfo.getCoordinate());

        assertEquals(1, attacker.getShotCount());
        assertEquals(0, attacker.getHitsCount());

        assertEquals(
                CellState.MISS,
                attacker.getEnemyViewBoard().getCell(coordinate).getState()
        );

        assertEquals(
                CellState.MISS,
                defender.getOwnBoard().getCell(coordinate).getState()
        );
    }

    @Test
    void makeShot_shouldReturnHitAndUpdateStatistics_whenShipIsHitButNotSunk() {
        Coordinate firstDeck = new Coordinate(0, 0);
        Coordinate secondDeck = new Coordinate(0, 1);

        defender.getOwnBoard().addShip(
                new Ship(Set.of(firstDeck, secondDeck))
        );

        ShotInfo shotInfo = gameEngine.makeShot(
                attacker,
                defender,
                firstDeck
        );

        assertEquals(ShotResult.HIT, shotInfo.getResult());
        assertEquals(firstDeck, shotInfo.getCoordinate());

        assertEquals(1, attacker.getShotCount());
        assertEquals(1, attacker.getHitsCount());

        assertEquals(
                CellState.HIT,
                attacker.getEnemyViewBoard().getCell(firstDeck).getState()
        );

        assertEquals(
                CellState.HIT,
                defender.getOwnBoard().getCell(firstDeck).getState()
        );
    }

    @Test
    void makeShot_shouldReturnKill_whenLastShipDeckIsHit() {
        Coordinate coordinate = new Coordinate(0, 0);

        defender.getOwnBoard().addShip(
                new Ship(Set.of(coordinate))
        );

        ShotInfo shotInfo = gameEngine.makeShot(
                attacker,
                defender,
                coordinate
        );

        assertEquals(ShotResult.KILL, shotInfo.getResult());
        assertEquals(coordinate, shotInfo.getCoordinate());

        assertEquals(1, attacker.getShotCount());
        assertEquals(1, attacker.getHitsCount());

        assertEquals(
                CellState.HIT,
                attacker.getEnemyViewBoard().getCell(coordinate).getState()
        );

        assertEquals(
                CellState.HIT,
                defender.getOwnBoard().getCell(coordinate).getState()
        );
    }

    @Test
    void isGameOver_shouldReturnFalse_whenPlayerHasAliveShip() {
        Coordinate firstDeck = new Coordinate(0, 0);
        Coordinate secondDeck = new Coordinate(0, 1);

        defender.getOwnBoard().addShip(
                new Ship(Set.of(firstDeck, secondDeck))
        );

        gameEngine.makeShot(
                attacker,
                defender,
                firstDeck
        );

        assertFalse(gameEngine.isGameOver(defender));
    }

    @Test
    void isGameOver_shouldReturnTrue_whenAllShipsDestroyed() {
        Coordinate coordinate = new Coordinate(0, 0);

        defender.getOwnBoard().addShip(
                new Ship(Set.of(coordinate))
        );

        gameEngine.makeShot(
                attacker,
                defender,
                coordinate
        );

        assertTrue(gameEngine.isGameOver(defender));
    }
}
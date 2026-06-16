package ru.senla.seabattle.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;
import ru.senla.seabattle.exception.InvalidCoordinateException;
import ru.senla.seabattle.exception.RepeatedShotException;
import ru.senla.seabattle.ship.Ship;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OwnBoardTest {

    private OwnBoard ownBoard;

    @BeforeEach
    void setUp() {
        ownBoard = new OwnBoard();
    }

    @Test
    void addShip_shouldSetShipCellsToShipState() {
        Coordinate coordinate = new Coordinate(0, 0);

        ownBoard.addShip(new Ship(Set.of(coordinate)));

        assertEquals(
                CellState.SHIP,
                ownBoard.getCell(coordinate).getState()
        );
    }

    @Test
    void shoot_shouldReturnMissAndMarkCellAsMiss_whenCellIsEmpty() {
        Coordinate coordinate = new Coordinate(0, 0);

        ShotResult result = ownBoard.shoot(coordinate);

        assertEquals(ShotResult.MISS, result);
        assertEquals(
                CellState.MISS,
                ownBoard.getCell(coordinate).getState()
        );
    }

    @Test
    void shoot_shouldReturnHitAndMarkCellAsHit_whenShipIsHitButNotSunk() {
        Coordinate firstDeck = new Coordinate(0, 0);
        Coordinate secondDeck = new Coordinate(0, 1);

        ownBoard.addShip(new Ship(Set.of(firstDeck, secondDeck)));

        ShotResult result = ownBoard.shoot(firstDeck);

        assertEquals(ShotResult.HIT, result);
        assertEquals(
                CellState.HIT,
                ownBoard.getCell(firstDeck).getState()
        );
    }

    @Test
    void shoot_shouldReturnKill_whenLastShipDeckIsHit() {
        Coordinate coordinate = new Coordinate(0, 0);

        ownBoard.addShip(new Ship(Set.of(coordinate)));

        ShotResult result = ownBoard.shoot(coordinate);

        assertEquals(ShotResult.KILL, result);
        assertEquals(
                CellState.HIT,
                ownBoard.getCell(coordinate).getState()
        );
    }

    @Test
    void shoot_shouldThrowException_whenShotIsRepeatedAfterMiss() {
        Coordinate coordinate = new Coordinate(0, 0);

        ownBoard.shoot(coordinate);

        assertThrows(
                RepeatedShotException.class,
                () -> ownBoard.shoot(coordinate)
        );
    }

    @Test
    void shoot_shouldThrowException_whenShotIsRepeatedAfterHit() {
        Coordinate coordinate = new Coordinate(0, 0);

        ownBoard.addShip(new Ship(Set.of(coordinate)));
        ownBoard.shoot(coordinate);

        assertThrows(
                RepeatedShotException.class,
                () -> ownBoard.shoot(coordinate)
        );
    }

    @Test
    void shoot_shouldThrowException_whenCoordinateIsOutOfBounds() {
        assertThrows(
                InvalidCoordinateException.class,
                () -> ownBoard.shoot(new Coordinate(16, 0))
        );
    }

    @Test
    void allShipsDestroyed_shouldReturnFalse_whenShipIsNotDestroyed() {
        ownBoard.addShip(new Ship(Set.of(
                new Coordinate(0, 0),
                new Coordinate(0, 1)
        )));

        ownBoard.shoot(new Coordinate(0, 0));

        assertFalse(ownBoard.allShipsDestroyed());
    }

    @Test
    void allShipsDestroyed_shouldReturnTrue_whenAllShipsAreDestroyed() {
        ownBoard.addShip(new Ship(Set.of(new Coordinate(0, 0))));

        ownBoard.shoot(new Coordinate(0, 0));

        assertTrue(ownBoard.allShipsDestroyed());
    }

    @Test
    void getShips_shouldReturnCopyOfShipsList() {
        ownBoard.addShip(new Ship(Set.of(new Coordinate(0, 0))));

        ownBoard.getShips().clear();

        assertEquals(1, ownBoard.getShips().size());
    }
}
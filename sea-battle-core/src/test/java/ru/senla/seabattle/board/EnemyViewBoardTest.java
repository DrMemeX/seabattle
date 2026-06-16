package ru.senla.seabattle.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidCoordinateException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnemyViewBoardTest {

    private EnemyViewBoard enemyViewBoard;

    @BeforeEach
    void setUp() {
        enemyViewBoard = new EnemyViewBoard();
    }

    @Test
    void markMiss_shouldSetCellStateToMiss() {
        Coordinate coordinate = new Coordinate(0, 0);

        enemyViewBoard.markMiss(coordinate);

        assertEquals(
                CellState.MISS,
                enemyViewBoard.getCell(coordinate).getState()
        );
    }

    @Test
    void markHit_shouldSetCellStateToHit() {
        Coordinate coordinate = new Coordinate(0, 0);

        enemyViewBoard.markHit(coordinate);

        assertEquals(
                CellState.HIT,
                enemyViewBoard.getCell(coordinate).getState()
        );
    }

    @Test
    void isAvailableForShot_shouldReturnTrue_whenCellIsEmpty() {
        assertTrue(
                enemyViewBoard.isAvailableForShot(new Coordinate(0, 0))
        );
    }

    @Test
    void isAvailableForShot_shouldReturnFalse_whenCellIsMiss() {
        Coordinate coordinate = new Coordinate(0, 0);

        enemyViewBoard.markMiss(coordinate);

        assertFalse(enemyViewBoard.isAvailableForShot(coordinate));
    }

    @Test
    void isAvailableForShot_shouldReturnFalse_whenCellIsHit() {
        Coordinate coordinate = new Coordinate(0, 0);

        enemyViewBoard.markHit(coordinate);

        assertFalse(enemyViewBoard.isAvailableForShot(coordinate));
    }

    @Test
    void getCell_shouldThrowException_whenCoordinateIsOutOfBounds() {
        assertThrows(
                InvalidCoordinateException.class,
                () -> enemyViewBoard.getCell(new Coordinate(16, 0))
        );
    }
}
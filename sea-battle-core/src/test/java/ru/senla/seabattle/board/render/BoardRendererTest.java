package ru.senla.seabattle.board.render;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.ship.Ship;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardRendererTest {

    private BoardRenderer boardRenderer;

    @BeforeEach
    void setUp() {
        boardRenderer = new BoardRenderer();
    }

    @Test
    void renderOwnBoard_shouldShowShipCells() {
        OwnBoard ownBoard = new OwnBoard();

        ownBoard.addShip(new Ship(Set.of(new Coordinate(0, 0))));

        String result = boardRenderer.renderOwnBoard(ownBoard);

        assertTrue(result.contains("#"));
    }

    @Test
    void renderEnemyBoard_shouldHideShipCells() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();

        String result = boardRenderer.renderEnemyBoard(enemyViewBoard);

        assertTrue(result.contains("  1"));
        assertTrue(result.contains("  A"));
    }

    @Test
    void renderEnemyBoard_shouldShowMissAndHitMarks() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();

        enemyViewBoard.markMiss(new Coordinate(0, 0));
        enemyViewBoard.markHit(new Coordinate(0, 1));

        String result = boardRenderer.renderEnemyBoard(enemyViewBoard);

        assertTrue(result.contains("."));
        assertTrue(result.contains("X"));
    }

    @Test
    void renderOwnBoard_shouldContainHeaderAndRowLabels() {
        OwnBoard ownBoard = new OwnBoard();

        String result = boardRenderer.renderOwnBoard(ownBoard);

        assertTrue(result.contains("  1"));
        assertTrue(result.contains(" 16"));
        assertTrue(result.contains("  A"));
        assertTrue(result.contains("  P"));
    }
}
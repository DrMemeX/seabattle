package ru.senla.seabattle.placement.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidPlacementException;
import ru.senla.seabattle.placement.PlacementValidator;
import ru.senla.seabattle.ship.Orientation;
import ru.senla.seabattle.ship.Ship;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultShipPlacementServiceTest {

    private DefaultShipPlacementService shipPlacementService;
    private OwnBoard ownBoard;

    @BeforeEach
    void setUp() {
        PlacementValidator placementValidator = new DefaultPlacementValidator();

        shipPlacementService = new DefaultShipPlacementService(placementValidator);
        ownBoard = new OwnBoard();
    }

    @Test
    void createShip_shouldCreateHorizontalShipWithCorrectCoordinates() {
        Ship ship = shipPlacementService.createShip(
                new Coordinate(0, 0),
                Orientation.HORIZONTAL,
                3
        );

        assertEquals(
                Set.of(
                        new Coordinate(0, 0),
                        new Coordinate(0, 1),
                        new Coordinate(0, 2)
                ),
                ship.getCoordinates()
        );
    }

    @Test
    void createShip_shouldCreateVerticalShipWithCorrectCoordinates() {
        Ship ship = shipPlacementService.createShip(
                new Coordinate(0, 0),
                Orientation.VERTICAL,
                3
        );

        assertEquals(
                Set.of(
                        new Coordinate(0, 0),
                        new Coordinate(1, 0),
                        new Coordinate(2, 0)
                ),
                ship.getCoordinates()
        );
    }

    @Test
    void createShip_shouldThrowException_whenShipSizeIsZero() {
        assertThrows(
                InvalidPlacementException.class,
                () -> shipPlacementService.createShip(
                        new Coordinate(0, 0),
                        Orientation.HORIZONTAL,
                        0
                )
        );
    }

    @Test
    void createShip_shouldThrowException_whenShipSizeIsGreaterThanMax() {
        assertThrows(
                InvalidPlacementException.class,
                () -> shipPlacementService.createShip(
                        new Coordinate(0, 0),
                        Orientation.HORIZONTAL,
                        7
                )
        );
    }

    @Test
    void placeShip_shouldSetShipCellsOnBoard() {
        shipPlacementService.placeShip(
                ownBoard,
                new Coordinate(0, 0),
                Orientation.HORIZONTAL,
                3
        );

        assertEquals(
                CellState.SHIP,
                ownBoard.getCell(new Coordinate(0, 0)).getState()
        );
        assertEquals(
                CellState.SHIP,
                ownBoard.getCell(new Coordinate(0, 1)).getState()
        );
        assertEquals(
                CellState.SHIP,
                ownBoard.getCell(new Coordinate(0, 2)).getState()
        );
    }

    @Test
    void placeShip_shouldThrowException_whenShipTouchesExistingShip() {
        shipPlacementService.placeShip(
                ownBoard,
                new Coordinate(0, 0),
                Orientation.HORIZONTAL,
                1
        );

        assertThrows(
                InvalidPlacementException.class,
                () -> shipPlacementService.placeShip(
                        ownBoard,
                        new Coordinate(1, 1),
                        Orientation.HORIZONTAL,
                        1
                )
        );
    }
}
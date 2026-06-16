package ru.senla.seabattle.placement.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidPlacementException;
import ru.senla.seabattle.ship.Ship;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultPlacementValidatorTest {

    private DefaultPlacementValidator placementValidator;
    private OwnBoard ownBoard;

    @BeforeEach
    void setUp() {
        placementValidator = new DefaultPlacementValidator();
        ownBoard = new OwnBoard();
    }

    @Test
    void validatePlacement_shouldNotThrowException_whenPlacementIsValid() {
        Ship ship = new Ship(Set.of(
                new Coordinate(0, 0),
                new Coordinate(0, 1)
        ));

        assertDoesNotThrow(
                () -> placementValidator.validatePlacement(ownBoard, ship)
        );
    }

    @Test
    void validatePlacement_shouldThrowException_whenShipIsOutOfBounds() {
        Ship ship = new Ship(Set.of(
                new Coordinate(0, 15),
                new Coordinate(0, 16)
        ));

        assertThrows(
                InvalidPlacementException.class,
                () -> placementValidator.validatePlacement(ownBoard, ship)
        );
    }

    @Test
    void validatePlacement_shouldThrowException_whenShipsIntersect() {
        ownBoard.addShip(new Ship(Set.of(
                new Coordinate(0, 0),
                new Coordinate(0, 1)
        )));

        Ship newShip = new Ship(Set.of(
                new Coordinate(0, 1),
                new Coordinate(0, 2)
        ));

        assertThrows(
                InvalidPlacementException.class,
                () -> placementValidator.validatePlacement(ownBoard, newShip)
        );
    }

    @Test
    void validatePlacement_shouldThrowException_whenShipsTouchHorizontally() {
        ownBoard.addShip(new Ship(Set.of(
                new Coordinate(0, 0)
        )));

        Ship newShip = new Ship(Set.of(
                new Coordinate(0, 1)
        ));

        assertThrows(
                InvalidPlacementException.class,
                () -> placementValidator.validatePlacement(ownBoard, newShip)
        );
    }

    @Test
    void validatePlacement_shouldThrowException_whenShipsTouchVertically() {
        ownBoard.addShip(new Ship(Set.of(
                new Coordinate(0, 0)
        )));

        Ship newShip = new Ship(Set.of(
                new Coordinate(1, 0)
        ));

        assertThrows(
                InvalidPlacementException.class,
                () -> placementValidator.validatePlacement(ownBoard, newShip)
        );
    }

    @Test
    void validatePlacement_shouldThrowException_whenShipsTouchDiagonally() {
        ownBoard.addShip(new Ship(Set.of(
                new Coordinate(0, 0)
        )));

        Ship newShip = new Ship(Set.of(
                new Coordinate(1, 1)
        ));

        assertThrows(
                InvalidPlacementException.class,
                () -> placementValidator.validatePlacement(ownBoard, newShip)
        );
    }
}
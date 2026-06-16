package ru.senla.seabattle.placement.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.placement.PlacementValidator;
import ru.senla.seabattle.placement.ShipPlacementService;
import ru.senla.seabattle.rules.FleetRules;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomFleetPlacementServiceTest {

    private RandomFleetPlacementService randomFleetPlacementService;
    private OwnBoard ownBoard;

    @BeforeEach
    void setUp() {
        PlacementValidator placementValidator = new DefaultPlacementValidator();

        ShipPlacementService shipPlacementService =
                new DefaultShipPlacementService(placementValidator);

        randomFleetPlacementService = new RandomFleetPlacementService(
                shipPlacementService,
                new Random(1)
        );

        ownBoard = new OwnBoard();
    }

    @Test
    void placeFleet_shouldPlaceExpectedNumberOfShips() {
        randomFleetPlacementService.placeFleet(ownBoard);

        assertEquals(
                FleetRules.getShipSizes().length,
                ownBoard.getShips().size()
        );
    }

    @Test
    void placeFleet_shouldPlaceShipsWithExpectedTotalDeckCount() {
        randomFleetPlacementService.placeFleet(ownBoard);

        int expectedDeckCount = 0;

        for (int shipSize : FleetRules.getShipSizes()) {
            expectedDeckCount += shipSize;
        }

        int actualDeckCount = ownBoard.getShips()
                .stream()
                .mapToInt(ship -> ship.getCoordinates().size())
                .sum();

        assertEquals(expectedDeckCount, actualDeckCount);
    }

    @Test
    void placeFleet_shouldPlaceOnlyAliveShipsInitially() {
        randomFleetPlacementService.placeFleet(ownBoard);

        assertTrue(
                ownBoard.getShips()
                        .stream()
                        .noneMatch(ship -> !ship.getHits().isEmpty())
        );
    }
}
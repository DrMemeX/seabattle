package ru.senla.seabattle.placement.impl;

import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidPlacementException;
import ru.senla.seabattle.placement.FleetPlacementService;
import ru.senla.seabattle.placement.ShipPlacementService;
import ru.senla.seabattle.rules.FleetRules;
import ru.senla.seabattle.ship.Orientation;


import java.util.Random;

public class RandomFleetPlacementService
        implements FleetPlacementService {

    private static final int MAX_ATTEMPTS_PER_SHIP = 1000;

    private final ShipPlacementService shipPlacementService;
    private final Random random;

    public RandomFleetPlacementService(ShipPlacementService shipPlacementService,
                                       Random random) {
        this.shipPlacementService = shipPlacementService;
        this.random = random;
    }

    @Override
    public void placeFleet(OwnBoard ownBoard) {
        for (int shipSize : FleetRules.getShipSizes()) {
            placeShipRandom(ownBoard, shipSize);
        }
    }

    private void placeShipRandom(OwnBoard ownBoard, int shipSize) {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS_PER_SHIP; attempt++) {
            try {
                Coordinate startCoordinate = new Coordinate(
                        random.nextInt(OwnBoard.SIZE),
                        random.nextInt(OwnBoard.SIZE)
                );

                Orientation orientation = random.nextBoolean()
                        ? Orientation.HORIZONTAL
                        : Orientation.VERTICAL;

                shipPlacementService.placeShip(
                        ownBoard,
                        startCoordinate,
                        orientation,
                        shipSize
                );

                return;
            } catch (InvalidPlacementException ignored) {
                // Пытаемся поставить корабль снова
            }
        }

        throw new InvalidPlacementException(
                    "Не удалось автоматически расставить корабль размером " + shipSize
        );
    }
}

package ru.senla.seabattle.placement.impl;

import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidPlacementException;
import ru.senla.seabattle.placement.PlacementValidator;
import ru.senla.seabattle.placement.ShipPlacementService;
import ru.senla.seabattle.rules.FleetRules;
import ru.senla.seabattle.ship.Orientation;
import ru.senla.seabattle.ship.Ship;

import java.util.HashSet;
import java.util.Set;

public class DefaultShipPlacementService implements ShipPlacementService {

    private final PlacementValidator placementValidator;

    public DefaultShipPlacementService(PlacementValidator placementValidator) {
        this.placementValidator = placementValidator;
    }

    @Override
    public void placeShip(OwnBoard ownBoard,
                          Coordinate startCoordinate,
                          Orientation orientation,
                          int size) {
        Ship ship = createShip(startCoordinate, orientation, size);

        placementValidator.validatePlacement(ownBoard, ship);

        ownBoard.addShip(ship);
    }

    @Override
    public Ship createShip(Coordinate startCoordinate, Orientation orientation, int size) {
        validateShipSize(size);

        Set<Coordinate> coordinates = new HashSet<>();

        for (int i = 0; i < size; i++) {
            int row = startCoordinate.row();
            int column = startCoordinate.column();

            if (orientation == Orientation.HORIZONTAL) {
                column += i;
            } else {
                row += i;
            }

            coordinates.add(new Coordinate(row, column));
        }

        return new Ship(coordinates);
    }

    private void validateShipSize(int size) {
        if (size < 1 || size > FleetRules.getMaxShipSize()) {
            throw new InvalidPlacementException(
                    "Размер корабля должен быть от 1 до 6"
            );
        }
    }
}
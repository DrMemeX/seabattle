package ru.senla.seabattle.placement;

import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.ship.Orientation;
import ru.senla.seabattle.ship.Ship;

public interface ShipPlacementService {

    void placeShip(OwnBoard ownBoard, Coordinate startCoordinate, Orientation orientation, int size);

    Ship createShip(Coordinate startCoordinate, Orientation orientation, int size);
}
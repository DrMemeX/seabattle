package ru.senla.seabattle.placement;

import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.ship.Ship;

public interface PlacementValidator {

    public void validatePlacement(
            OwnBoard ownBoard,
            Ship ship
    );
}

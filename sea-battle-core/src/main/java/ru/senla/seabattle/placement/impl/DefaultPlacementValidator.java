package ru.senla.seabattle.placement.impl;

import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidPlacementException;
import ru.senla.seabattle.placement.PlacementValidator;
import ru.senla.seabattle.ship.Ship;

public class DefaultPlacementValidator implements PlacementValidator {

    @Override
    public void validatePlacement(OwnBoard ownBoard, Ship ship) {
        validateBounds(ship);
        validateCollision(ownBoard, ship);
    }

    private void validateBounds(Ship ship) {
        for (Coordinate coordinate : ship.getCoordinates()) {
            if (!isInsideBoard(coordinate.row(), coordinate.column())) {
                throw new InvalidPlacementException("Корабль выходит за пределы игрового поля");
            }
        }
    }

    private void validateCollision(OwnBoard ownBoard, Ship ship) {
        for (Coordinate coordinate : ship.getCoordinates()) {
            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int neighbourRow = coordinate.row() + rowOffset;
                    int neighbourColumn = coordinate.column() + columnOffset;

                    if (!isInsideBoard(neighbourRow, neighbourColumn)) {
                        continue;
                    }

                    Coordinate neighbour = new Coordinate(neighbourRow, neighbourColumn);

                    if (ownBoard.getCell(neighbour).getState() == CellState.SHIP) {
                        throw new InvalidPlacementException(
                                "Корабли не могут пересекаться или соприкасаться"
                        );
                    }
                }
            }
        }
    }

    private boolean isInsideBoard(int row, int column) {
        return row >= 0
                && row < OwnBoard.SIZE
                && column >= 0
                && column < OwnBoard.SIZE;
    }
}
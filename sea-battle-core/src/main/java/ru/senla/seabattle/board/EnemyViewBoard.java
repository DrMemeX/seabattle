package ru.senla.seabattle.board;

import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;

public class EnemyViewBoard extends AbstractBoard {

    public void markMiss(Coordinate coordinate) {
        setCellState(coordinate, CellState.MISS);
    }

    public void markHit(Coordinate coordinate) {
        setCellState(coordinate, CellState.HIT);
    }

    public boolean isAvailableForShot(Coordinate coordinate) {
        return getCell(coordinate).getState() == CellState.EMPTY;
    }
}
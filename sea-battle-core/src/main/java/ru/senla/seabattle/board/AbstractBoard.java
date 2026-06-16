package ru.senla.seabattle.board;

import ru.senla.seabattle.board.model.Cell;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidCoordinateException;

public abstract class AbstractBoard {

    public static final int SIZE = 16;

    private final Cell[][] cells;

    protected AbstractBoard() {
        this.cells = new Cell[SIZE][SIZE];
        initializeCells();
    }

    public Cell getCell(Coordinate coordinate) {
        validateCoordinate(coordinate);
        return cells[coordinate.row()][coordinate.column()];
    }

    protected void setCellState(Coordinate coordinate, CellState state) {
        getCell(coordinate).setState(state);
    }

    protected void validateCoordinate(Coordinate coordinate) {
        if (coordinate == null) {
            throw new InvalidCoordinateException("Координата не может быть null");
        }

        if (coordinate.row() < 0 || coordinate.row() >= SIZE
                || coordinate.column() < 0 || coordinate.column() >= SIZE) {
            throw new InvalidCoordinateException("Координата находится за пределами поля");
        }
    }

    private void initializeCells() {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                cells[row][column] = new Cell();
            }
        }
    }
}
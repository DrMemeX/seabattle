package ru.senla.seabattle.board.model;

public class Cell {

    private CellState state;

    public Cell() {
        this.state = CellState.EMPTY;
    }

    public CellState getState() {
        return state;
    }

    public boolean hasState(CellState state) {
        return this.state == state;
    }

    public void setState(CellState state) {
        this.state = state;
    }
}

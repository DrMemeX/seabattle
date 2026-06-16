package ru.senla.seabattle.board.model;

public record Coordinate(
        int row,
        int column
) {
    @Override
    public String toString() {
        char rowLetter = (char) ('A' + row);

        return rowLetter + String.valueOf(column + 1);
    }
}

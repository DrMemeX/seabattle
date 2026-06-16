package ru.senla.seabattle.parser;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidCoordinateException;

import static ru.senla.seabattle.board.AbstractBoard.SIZE;

public class CoordinateParser {

    public Coordinate parse(String input) {
        if (input == null || input.isBlank()) {
            throw new InvalidCoordinateException(
                    "Координата не может быть пустой"
            );
        }

        if (input.length() < 2) {
            throw new InvalidCoordinateException(
                    "Некорректный формат координаты"
            );
        }

        input = input.trim().toUpperCase();

        char rowLetter = input.charAt(0);
        int row = rowLetter - 'A';

        int column;

        try {
            column = Integer.parseInt(input.substring(1)) - 1;
        } catch (NumberFormatException e) {
            throw new InvalidCoordinateException(
                    "Некорректный номер столбца"
            );
        }

        if (row < 0 || row >= SIZE) {
            throw new InvalidCoordinateException(
                    "Строка должна быть от A до P"
            );
        }

        if (column < 0 || column >= SIZE) {
            throw new InvalidCoordinateException(
                    "Столбец должен быть от 1 до 16"
            );
        }

        return new Coordinate(row, column);
    }
}

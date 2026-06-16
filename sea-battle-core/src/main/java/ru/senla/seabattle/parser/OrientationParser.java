package ru.senla.seabattle.parser;

import ru.senla.seabattle.exception.InvalidPlacementException;
import ru.senla.seabattle.ship.Orientation;

public class OrientationParser {

    public Orientation parse(String input) {
        if (input == null || input.isBlank()) {
            throw new InvalidPlacementException(
                    "Ориентация корабля не может быть пустой"
            );
        }

        String normalizedInput = input.trim().toUpperCase();

        return switch (normalizedInput) {
            case "H" -> Orientation.HORIZONTAL;
            case "V" -> Orientation.VERTICAL;
            default -> throw new InvalidPlacementException(
                    "Ориентация должна быть H — горизонтально или V — вертикально"
            );
        };
    }
}
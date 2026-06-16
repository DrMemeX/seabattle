package ru.senla.seabattle.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidCoordinateException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoordinateParserTest {

    private CoordinateParser coordinateParser;

    @BeforeEach
    void setUp() {
        coordinateParser = new CoordinateParser();
    }

    @Test
    void parse_shouldReturnCoordinate_whenInputIsA1() {
        Coordinate result = coordinateParser.parse("A1");

        assertEquals(new Coordinate(0, 0), result);
    }

    @Test
    void parse_shouldReturnCoordinate_whenInputIsP16() {
        Coordinate result = coordinateParser.parse("P16");

        assertEquals(new Coordinate(15, 15), result);
    }

    @Test
    void parse_shouldTrimInputAndIgnoreCase() {
        Coordinate result = coordinateParser.parse(" d2 ");

        assertEquals(new Coordinate(3, 1), result);
    }

    @Test
    void parse_shouldThrowException_whenInputIsNull() {
        assertThrows(
                InvalidCoordinateException.class,
                () -> coordinateParser.parse(null)
        );
    }

    @Test
    void parse_shouldThrowException_whenInputIsBlank() {
        assertThrows(
                InvalidCoordinateException.class,
                () -> coordinateParser.parse("   ")
        );
    }

    @Test
    void parse_shouldThrowException_whenInputIsTooShort() {
        assertThrows(
                InvalidCoordinateException.class,
                () -> coordinateParser.parse("A")
        );
    }

    @Test
    void parse_shouldThrowException_whenRowIsOutOfBounds() {
        assertThrows(
                InvalidCoordinateException.class,
                () -> coordinateParser.parse("Z1")
        );
    }

    @Test
    void parse_shouldThrowException_whenColumnIsOutOfBounds() {
        assertThrows(
                InvalidCoordinateException.class,
                () -> coordinateParser.parse("A17")
        );
    }

    @Test
    void parse_shouldThrowException_whenColumnIsNotNumber() {
        assertThrows(
                InvalidCoordinateException.class,
                () -> coordinateParser.parse("Dabc")
        );
    }
}
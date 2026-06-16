package ru.senla.seabattle.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.exception.InvalidPlacementException;
import ru.senla.seabattle.ship.Orientation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrientationParserTest {

    private OrientationParser orientationParser;

    @BeforeEach
    void setUp() {
        orientationParser = new OrientationParser();
    }

    @Test
    void parse_shouldReturnHorizontal_whenInputIsH() {
        Orientation result = orientationParser.parse("H");

        assertEquals(Orientation.HORIZONTAL, result);
    }

    @Test
    void parse_shouldReturnVertical_whenInputIsV() {
        Orientation result = orientationParser.parse("V");

        assertEquals(Orientation.VERTICAL, result);
    }

    @Test
    void parse_shouldTrimInputAndIgnoreCase() {
        Orientation result = orientationParser.parse(" h ");

        assertEquals(Orientation.HORIZONTAL, result);
    }

    @Test
    void parse_shouldThrowException_whenInputIsNull() {
        assertThrows(
                InvalidPlacementException.class,
                () -> orientationParser.parse(null)
        );
    }

    @Test
    void parse_shouldThrowException_whenInputIsBlank() {
        assertThrows(
                InvalidPlacementException.class,
                () -> orientationParser.parse("   ")
        );
    }

    @Test
    void parse_shouldThrowException_whenInputIsUnknown() {
        assertThrows(
                InvalidPlacementException.class,
                () -> orientationParser.parse("X")
        );
    }
}
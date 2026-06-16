package ru.senla.seabattle.bot;

import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.model.Coordinate;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.senla.seabattle.bot.BotTestHelper.markAllMissExcept;

class EasyBotStrategyTest {

    @Test
    void chooseShot_shouldReturnOnlyAvailableCoordinate() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();
        Coordinate availableCoordinate = new Coordinate(5, 5);

        markAllMissExcept(enemyViewBoard, Set.of(availableCoordinate));

        EasyBotStrategy strategy = new EasyBotStrategy(new Random(1));

        Coordinate result = strategy.chooseShot(enemyViewBoard);

        assertEquals(availableCoordinate, result);
    }

    @Test
    void chooseShot_shouldThrowException_whenNoAvailableCoordinatesLeft() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();

        markAllMissExcept(enemyViewBoard, Set.of());

        EasyBotStrategy strategy = new EasyBotStrategy(new Random(1));

        assertThrows(
                IllegalStateException.class,
                () -> strategy.chooseShot(enemyViewBoard)
        );
    }
}
package ru.senla.seabattle.bot;

import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.model.Coordinate;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.senla.seabattle.bot.BotTestHelper.markAllMissExcept;

class NormalBotStrategyTest {

    @Test
    void chooseShot_shouldTargetAvailableNeighbour_whenHitExists() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();

        Coordinate hitCoordinate = new Coordinate(5, 5);
        Coordinate expectedTarget = new Coordinate(5, 6);

        markAllMissExcept(enemyViewBoard, Set.of(hitCoordinate, expectedTarget));
        enemyViewBoard.markHit(hitCoordinate);

        NormalBotStrategy strategy = new NormalBotStrategy(new Random(1));

        Coordinate result = strategy.chooseShot(enemyViewBoard);

        assertEquals(expectedTarget, result);
    }

    @Test
    void chooseShot_shouldReturnOnlyAvailableCoordinate_whenNoHitExists() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();

        Coordinate availableCoordinate = new Coordinate(3, 4);

        markAllMissExcept(enemyViewBoard, Set.of(availableCoordinate));

        NormalBotStrategy strategy = new NormalBotStrategy(new Random(1));

        Coordinate result = strategy.chooseShot(enemyViewBoard);

        assertEquals(availableCoordinate, result);
    }

    @Test
    void chooseShot_shouldThrowException_whenNoAvailableCoordinatesLeft() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();

        markAllMissExcept(enemyViewBoard, Set.of());

        NormalBotStrategy strategy = new NormalBotStrategy(new Random(1));

        assertThrows(
                IllegalStateException.class,
                () -> strategy.chooseShot(enemyViewBoard)
        );
    }
}
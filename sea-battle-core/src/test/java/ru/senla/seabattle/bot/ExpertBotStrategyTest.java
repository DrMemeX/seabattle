package ru.senla.seabattle.bot;

import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.model.Coordinate;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.senla.seabattle.bot.BotTestHelper.markAllMissExcept;

class ExpertBotStrategyTest {

    @Test
    void chooseShot_shouldContinueHorizontalLine_whenTwoHitsInSameRowExist() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();

        Coordinate firstHit = new Coordinate(5, 5);
        Coordinate secondHit = new Coordinate(5, 6);
        Coordinate expectedTarget = new Coordinate(5, 7);

        markAllMissExcept(enemyViewBoard, Set.of(firstHit, secondHit, expectedTarget));
        enemyViewBoard.markHit(firstHit);
        enemyViewBoard.markHit(secondHit);

        ExpertBotStrategy strategy = new ExpertBotStrategy(new Random(1));

        Coordinate result = strategy.chooseShot(enemyViewBoard);

        assertEquals(expectedTarget, result);
    }

    @Test
    void chooseShot_shouldTargetAvailableNeighbour_whenOnlyOneHitExists() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();

        Coordinate hitCoordinate = new Coordinate(5, 5);
        Coordinate expectedTarget = new Coordinate(5, 6);

        markAllMissExcept(enemyViewBoard, Set.of(hitCoordinate, expectedTarget));
        enemyViewBoard.markHit(hitCoordinate);

        ExpertBotStrategy strategy = new ExpertBotStrategy(new Random(1));

        Coordinate result = strategy.chooseShot(enemyViewBoard);

        assertEquals(expectedTarget, result);
    }

    @Test
    void chooseShot_shouldReturnOnlyAvailableCoordinate_whenNoHitExists() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();

        Coordinate availableCoordinate = new Coordinate(4, 4);

        markAllMissExcept(enemyViewBoard, Set.of(availableCoordinate));

        ExpertBotStrategy strategy = new ExpertBotStrategy(new Random(1));

        Coordinate result = strategy.chooseShot(enemyViewBoard);

        assertEquals(availableCoordinate, result);
    }

    @Test
    void chooseShot_shouldThrowException_whenNoAvailableCoordinatesLeft() {
        EnemyViewBoard enemyViewBoard = new EnemyViewBoard();

        markAllMissExcept(enemyViewBoard, Set.of());

        ExpertBotStrategy strategy = new ExpertBotStrategy(new Random(1));

        assertThrows(
                IllegalStateException.class,
                () -> strategy.chooseShot(enemyViewBoard)
        );
    }
}
package ru.senla.seabattle.bot;

import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.model.Coordinate;

import java.util.Set;

import static ru.senla.seabattle.board.AbstractBoard.SIZE;

final class BotTestHelper {

    private BotTestHelper() {
    }

    static void markAllMissExcept(EnemyViewBoard enemyViewBoard,
                                  Set<Coordinate> excludedCoordinates) {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Coordinate coordinate = new Coordinate(row, column);

                if (!excludedCoordinates.contains(coordinate)) {
                    enemyViewBoard.markMiss(coordinate);
                }
            }
        }
    }
}
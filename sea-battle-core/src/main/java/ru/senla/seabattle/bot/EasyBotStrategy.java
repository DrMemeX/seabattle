package ru.senla.seabattle.bot;

import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.senla.seabattle.board.AbstractBoard.SIZE;

public class EasyBotStrategy implements BotStrategy {
    private final Random random;

    public EasyBotStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Coordinate chooseShot(EnemyViewBoard enemyViewBoard) {
        List<Coordinate> availableCoordinates = new ArrayList<>();

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Coordinate coordinate = new Coordinate(row, column);

                if (enemyViewBoard.isAvailableForShot(coordinate)) {
                    availableCoordinates.add(coordinate);
                }
            }
        }

        if (availableCoordinates.isEmpty()) {
            throw new IllegalStateException("У бота не осталось доступных клеток для выстрела");
        }

        return availableCoordinates.get(random.nextInt(availableCoordinates.size()));
    }
}

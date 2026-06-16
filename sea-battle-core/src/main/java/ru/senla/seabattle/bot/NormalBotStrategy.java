package ru.senla.seabattle.bot;

import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.senla.seabattle.board.AbstractBoard.SIZE;

public class NormalBotStrategy implements BotStrategy {

    private final Random random;

    public NormalBotStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Coordinate chooseShot(EnemyViewBoard enemyViewBoard) {
        List<Coordinate> hitCoordinates = findCoordinatesByState(
                enemyViewBoard,
                CellState.HIT
        );

        List<Coordinate> targetCoordinates = findTargetCoordinates(
                enemyViewBoard,
                hitCoordinates
        );

        if (!targetCoordinates.isEmpty()) {
            return targetCoordinates.get(random.nextInt(targetCoordinates.size()));
        }

        return chooseRandomAvailableCoordinate(enemyViewBoard);
    }

    private List<Coordinate> findTargetCoordinates(EnemyViewBoard enemyViewBoard,
                                                   List<Coordinate> hitCoordinates) {
        List<Coordinate> targetCoordinates = new ArrayList<>();

        for (Coordinate coordinate : hitCoordinates) {
            addIfAvailable(
                    enemyViewBoard,
                    targetCoordinates,
                    new Coordinate(coordinate.row() - 1, coordinate.column())
            );
            addIfAvailable(
                    enemyViewBoard,
                    targetCoordinates,
                    new Coordinate(coordinate.row() + 1, coordinate.column())
            );
            addIfAvailable(
                    enemyViewBoard,
                    targetCoordinates,
                    new Coordinate(coordinate.row(), coordinate.column() - 1)
            );
            addIfAvailable(
                    enemyViewBoard,
                    targetCoordinates,
                    new Coordinate(coordinate.row(), coordinate.column() + 1)
            );
        }

        return targetCoordinates;
    }

    private Coordinate chooseRandomAvailableCoordinate(EnemyViewBoard enemyViewBoard) {
        List<Coordinate> availableCoordinates = findCoordinatesByState(
                enemyViewBoard,
                CellState.EMPTY
        );

        if (availableCoordinates.isEmpty()) {
            throw new IllegalStateException("У бота не осталось доступных клеток для выстрела");
        }

        return availableCoordinates.get(random.nextInt(availableCoordinates.size()));
    }

    private List<Coordinate> findCoordinatesByState(EnemyViewBoard enemyViewBoard,
                                                    CellState state) {
        List<Coordinate> coordinates = new ArrayList<>();

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Coordinate coordinate = new Coordinate(row, column);

                if (enemyViewBoard.getCell(coordinate).hasState(state)) {
                    coordinates.add(coordinate);
                }
            }
        }

        return coordinates;
    }

    private void addIfAvailable(EnemyViewBoard enemyViewBoard,
                                List<Coordinate> targetCoordinates,
                                Coordinate coordinate) {
        if (!isInsideBoard(coordinate)) {
            return;
        }

        if (!enemyViewBoard.isAvailableForShot(coordinate)) {
            return;
        }

        if (!targetCoordinates.contains(coordinate)) {
            targetCoordinates.add(coordinate);
        }
    }

    private boolean isInsideBoard(Coordinate coordinate) {
        return coordinate.row() >= 0
                && coordinate.row() < SIZE
                && coordinate.column() >= 0
                && coordinate.column() < SIZE;
    }
}
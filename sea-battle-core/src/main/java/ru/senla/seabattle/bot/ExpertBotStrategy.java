package ru.senla.seabattle.bot;

import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.rules.FleetRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.senla.seabattle.board.AbstractBoard.SIZE;

public class ExpertBotStrategy implements BotStrategy {

    private final Random random;

    public ExpertBotStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Coordinate chooseShot(EnemyViewBoard enemyViewBoard) {
        List<Coordinate> hitCoordinates = findCoordinatesByState(
                enemyViewBoard,
                CellState.HIT
        );

        List<Coordinate> lineTargets = findLineTargets(
                enemyViewBoard,
                hitCoordinates
        );

        if (!lineTargets.isEmpty()) {
            return chooseBestByProbability(enemyViewBoard, lineTargets);
        }

        List<Coordinate> neighbourTargets = findNeighbourTargets(
                enemyViewBoard,
                hitCoordinates
        );

        if (!neighbourTargets.isEmpty()) {
            return chooseBestByProbability(enemyViewBoard, neighbourTargets);
        }

        List<Coordinate> availableCoordinates = findCoordinatesByState(
                enemyViewBoard,
                CellState.EMPTY
        );

        return chooseBestByProbability(enemyViewBoard, availableCoordinates);
    }

    private Coordinate chooseBestByProbability(EnemyViewBoard enemyViewBoard,
                                               List<Coordinate> candidates) {
        if (candidates.isEmpty()) {
            throw new IllegalStateException("У бота не осталось доступных клеток для выстрела");
        }

        int[][] probabilityMap = buildProbabilityMap(enemyViewBoard);

        int maxScore = -1;
        List<Coordinate> bestCoordinates = new ArrayList<>();

        for (Coordinate coordinate : candidates) {
            int score = probabilityMap[coordinate.row()][coordinate.column()];

            if (score > maxScore) {
                maxScore = score;
                bestCoordinates.clear();
                bestCoordinates.add(coordinate);
            } else if (score == maxScore) {
                bestCoordinates.add(coordinate);
            }
        }

        return chooseRandom(bestCoordinates);
    }

    private int[][] buildProbabilityMap(EnemyViewBoard enemyViewBoard) {
        int[][] probabilityMap = new int[SIZE][SIZE];

        for (int shipSize : FleetRules.getShipSizes()) {
            addHorizontalPlacements(
                    enemyViewBoard,
                    probabilityMap,
                    shipSize
            );

            addVerticalPlacements(
                    enemyViewBoard,
                    probabilityMap,
                    shipSize
            );
        }

        return probabilityMap;
    }

    private void addHorizontalPlacements(EnemyViewBoard enemyViewBoard,
                                         int[][] probabilityMap,
                                         int shipSize) {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column <= SIZE - shipSize; column++) {
                if (canPlaceHorizontally(enemyViewBoard, row, column, shipSize)) {
                    increaseHorizontalProbability(
                            enemyViewBoard,
                            probabilityMap,
                            row,
                            column,
                            shipSize
                    );
                }
            }
        }
    }

    private void addVerticalPlacements(EnemyViewBoard enemyViewBoard,
                                       int[][] probabilityMap,
                                       int shipSize) {
        for (int row = 0; row <= SIZE - shipSize; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (canPlaceVertically(enemyViewBoard, row, column, shipSize)) {
                    increaseVerticalProbability(
                            enemyViewBoard,
                            probabilityMap,
                            row,
                            column,
                            shipSize
                    );
                }
            }
        }
    }

    private boolean canPlaceHorizontally(EnemyViewBoard enemyViewBoard,
                                         int row,
                                         int startColumn,
                                         int shipSize) {
        for (int offset = 0; offset < shipSize; offset++) {
            Coordinate coordinate = new Coordinate(row, startColumn + offset);

            if (enemyViewBoard.getCell(coordinate).hasState(CellState.MISS)) {
                return false;
            }
        }

        return true;
    }

    private boolean canPlaceVertically(EnemyViewBoard enemyViewBoard,
                                       int startRow,
                                       int column,
                                       int shipSize) {
        for (int offset = 0; offset < shipSize; offset++) {
            Coordinate coordinate = new Coordinate(startRow + offset, column);

            if (enemyViewBoard.getCell(coordinate).hasState(CellState.MISS)) {
                return false;
            }
        }

        return true;
    }

    private void increaseHorizontalProbability(EnemyViewBoard enemyViewBoard,
                                               int[][] probabilityMap,
                                               int row,
                                               int startColumn,
                                               int shipSize) {
        for (int offset = 0; offset < shipSize; offset++) {
            Coordinate coordinate = new Coordinate(row, startColumn + offset);
            increaseProbabilityIfAvailable(enemyViewBoard, probabilityMap, coordinate);
        }
    }

    private void increaseVerticalProbability(EnemyViewBoard enemyViewBoard,
                                             int[][] probabilityMap,
                                             int startRow,
                                             int column,
                                             int shipSize) {
        for (int offset = 0; offset < shipSize; offset++) {
            Coordinate coordinate = new Coordinate(startRow + offset, column);
            increaseProbabilityIfAvailable(enemyViewBoard, probabilityMap, coordinate);
        }
    }

    private void increaseProbabilityIfAvailable(EnemyViewBoard enemyViewBoard,
                                                int[][] probabilityMap,
                                                Coordinate coordinate) {
        if (enemyViewBoard.isAvailableForShot(coordinate)) {
            probabilityMap[coordinate.row()][coordinate.column()]++;
        }
    }

    private List<Coordinate> findLineTargets(EnemyViewBoard enemyViewBoard,
                                             List<Coordinate> hitCoordinates) {
        List<Coordinate> targetCoordinates = new ArrayList<>();

        for (Coordinate firstHit : hitCoordinates) {
            for (Coordinate secondHit : hitCoordinates) {
                if (firstHit.equals(secondHit)) {
                    continue;
                }

                if (firstHit.row() == secondHit.row()) {
                    addHorizontalLineTargets(
                            enemyViewBoard,
                            targetCoordinates,
                            firstHit.row(),
                            hitCoordinates
                    );
                }

                if (firstHit.column() == secondHit.column()) {
                    addVerticalLineTargets(
                            enemyViewBoard,
                            targetCoordinates,
                            firstHit.column(),
                            hitCoordinates
                    );
                }
            }
        }

        return targetCoordinates;
    }

    private void addHorizontalLineTargets(EnemyViewBoard enemyViewBoard,
                                          List<Coordinate> targetCoordinates,
                                          int row,
                                          List<Coordinate> hitCoordinates) {
        List<Integer> hitColumns = new ArrayList<>();

        for (Coordinate coordinate : hitCoordinates) {
            if (coordinate.row() == row) {
                hitColumns.add(coordinate.column());
            }
        }

        if (hitColumns.size() < 2) {
            return;
        }

        int minColumn = findMin(hitColumns);
        int maxColumn = findMax(hitColumns);

        addIfAvailable(
                enemyViewBoard,
                targetCoordinates,
                new Coordinate(row, minColumn - 1)
        );

        addIfAvailable(
                enemyViewBoard,
                targetCoordinates,
                new Coordinate(row, maxColumn + 1)
        );
    }

    private void addVerticalLineTargets(EnemyViewBoard enemyViewBoard,
                                        List<Coordinate> targetCoordinates,
                                        int column,
                                        List<Coordinate> hitCoordinates) {
        List<Integer> hitRows = new ArrayList<>();

        for (Coordinate coordinate : hitCoordinates) {
            if (coordinate.column() == column) {
                hitRows.add(coordinate.row());
            }
        }

        if (hitRows.size() < 2) {
            return;
        }

        int minRow = findMin(hitRows);
        int maxRow = findMax(hitRows);

        addIfAvailable(
                enemyViewBoard,
                targetCoordinates,
                new Coordinate(minRow - 1, column)
        );

        addIfAvailable(
                enemyViewBoard,
                targetCoordinates,
                new Coordinate(maxRow + 1, column)
        );
    }

    private List<Coordinate> findNeighbourTargets(EnemyViewBoard enemyViewBoard,
                                                  List<Coordinate> hitCoordinates) {
        List<Coordinate> targetCoordinates = new ArrayList<>();

        for (Coordinate coordinate : hitCoordinates) {
            addNeighbourTargets(
                    enemyViewBoard,
                    targetCoordinates,
                    coordinate
            );
        }

        return targetCoordinates;
    }

    private void addNeighbourTargets(EnemyViewBoard enemyViewBoard,
                                     List<Coordinate> targetCoordinates,
                                     Coordinate coordinate) {
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

    private Coordinate chooseRandom(List<Coordinate> coordinates) {
        return coordinates.get(random.nextInt(coordinates.size()));
    }

    private int findMin(List<Integer> values) {
        int min = values.get(0);

        for (int value : values) {
            if (value < min) {
                min = value;
            }
        }

        return min;
    }

    private int findMax(List<Integer> values) {
        int max = values.get(0);

        for (int value : values) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }
}
package ru.senla.seabattle.player;

import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.Coordinate;

public interface Player {

    Coordinate chooseShot();

    String getName();

    OwnBoard getOwnBoard();

    EnemyViewBoard getEnemyViewBoard();

    int getShotCount();

    int getHitsCount();

    void incrementShotsCount();

    void incrementHitsCount();

    double getAccuracy();
}
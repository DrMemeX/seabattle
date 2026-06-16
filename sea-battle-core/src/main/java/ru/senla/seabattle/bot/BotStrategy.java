package ru.senla.seabattle.bot;

import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.model.Coordinate;

public interface BotStrategy {

    Coordinate chooseShot(EnemyViewBoard enemyViewBoard);
}
package ru.senla.seabattle.report;

import ru.senla.seabattle.player.Player;

public interface GameReportWriter {

    void startGame(Player firstPlayer, Player secondPlayer);

    void writeMove(GameMoveRecord moveRecord);

    void finishGame(Player firstPlayer, Player secondPlayer, Player winner);
}
package ru.senla.seabattle.report;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;

import java.time.LocalDateTime;

public record GameMoveRecord(
        LocalDateTime time,
        String playerName,
        Coordinate coordinate,
        ShotResult result
) {
}
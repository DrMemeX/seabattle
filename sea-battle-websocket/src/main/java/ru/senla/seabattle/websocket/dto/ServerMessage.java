package ru.senla.seabattle.websocket.dto;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;

public record ServerMessage(
        String type,
        String gameId,
        String playerId,
        String currentPlayerId,
        Coordinate coordinate,
        ShotResult shotResult,
        boolean gameOver,
        String winnerName,
        String message
) {
}
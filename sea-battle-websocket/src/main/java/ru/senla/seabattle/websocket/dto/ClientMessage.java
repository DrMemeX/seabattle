package ru.senla.seabattle.websocket.dto;

public record ClientMessage(
        String type,
        String gameId,
        String name,
        String coordinate,
        int row,
        int column
) {
}
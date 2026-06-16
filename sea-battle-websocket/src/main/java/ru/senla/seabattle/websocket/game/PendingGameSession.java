package ru.senla.seabattle.websocket.game;

import ru.senla.seabattle.player.Player;

public class PendingGameSession {

    private final String gameId;
    private final String firstPlayerId;
    private final Player firstPlayer;

    public PendingGameSession(String gameId,
                              String firstPlayerId,
                              Player firstPlayer) {
        this.gameId = gameId;
        this.firstPlayerId = firstPlayerId;
        this.firstPlayer = firstPlayer;
    }

    public String getGameId() {
        return gameId;
    }

    public String getFirstPlayerId() {
        return firstPlayerId;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }
}

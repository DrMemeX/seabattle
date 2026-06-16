package ru.senla.seabattle.websocket.game;

import ru.senla.seabattle.player.Player;
import ru.senla.seabattle.websocket.exception.GameSessionException;

public class GameSession {

    private final String gameId;
    private final String firstPlayerId;
    private final String secondPlayerId;
    private final Player firstPlayer;
    private final Player secondPlayer;

    private String currentPlayerId;

    public GameSession(String gameId,
                       String firstPlayerId,
                       String secondPlayerId,
                       Player firstPlayer,
                       Player secondPlayer) {
        this.gameId = gameId;
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.currentPlayerId = firstPlayerId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getFirstPlayerId() {
        return firstPlayerId;
    }

    public String getSecondPlayerId() {
        return secondPlayerId;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public boolean isCurrentPlayer(String playerId) {
        return currentPlayerId.equals(playerId);
    }

    public Player getPlayer(String playerId) {
        if (firstPlayerId.equals(playerId)) {
            return firstPlayer;
        }

        if (secondPlayerId.equals(playerId)) {
            return secondPlayer;
        }

        throw new GameSessionException("Игрок не найден в данной игровой сессии");
    }

    public Player getOpponent(String playerId) {
        if (firstPlayerId.equals(playerId)) {
            return secondPlayer;
        }

        if (secondPlayerId.equals(playerId)) {
            return firstPlayer;
        }

        throw new GameSessionException("Игрок не найден в данной игровой сессии");
    }

    public void switchTurn() {
        if (currentPlayerId.equals(firstPlayerId)) {
            currentPlayerId = secondPlayerId;
        } else {
            currentPlayerId = firstPlayerId;
        }
    }
}

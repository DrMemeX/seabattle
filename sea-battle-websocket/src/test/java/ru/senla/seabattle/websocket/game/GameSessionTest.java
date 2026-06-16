package ru.senla.seabattle.websocket.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.bot.EasyBotStrategy;
import ru.senla.seabattle.player.BotPlayer;
import ru.senla.seabattle.player.Player;
import ru.senla.seabattle.websocket.exception.GameSessionException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameSessionTest {

    private Player firstPlayer;
    private Player secondPlayer;
    private GameSession gameSession;

    @BeforeEach
    void setUp() {
        firstPlayer = createPlayer("Первый");
        secondPlayer = createPlayer("Второй");

        gameSession = new GameSession(
                "game-id",
                "first-id",
                "second-id",
                firstPlayer,
                secondPlayer
        );
    }

    @Test
    void constructor_shouldSetFirstPlayerAsCurrentPlayer() {
        assertEquals("first-id", gameSession.getCurrentPlayerId());
    }

    @Test
    void isCurrentPlayer_shouldReturnTrueForCurrentPlayer() {
        assertTrue(gameSession.isCurrentPlayer("first-id"));
    }

    @Test
    void isCurrentPlayer_shouldReturnFalseForAnotherPlayer() {
        assertFalse(gameSession.isCurrentPlayer("second-id"));
    }

    @Test
    void getPlayer_shouldReturnFirstPlayer() {
        assertSame(firstPlayer, gameSession.getPlayer("first-id"));
    }

    @Test
    void getPlayer_shouldReturnSecondPlayer() {
        assertSame(secondPlayer, gameSession.getPlayer("second-id"));
    }

    @Test
    void getPlayer_shouldThrowException_whenPlayerNotFound() {
        assertThrows(
                GameSessionException.class,
                () -> gameSession.getPlayer("unknown-id")
        );
    }

    @Test
    void getOpponent_shouldReturnSecondPlayerForFirstPlayerId() {
        assertSame(secondPlayer, gameSession.getOpponent("first-id"));
    }

    @Test
    void getOpponent_shouldReturnFirstPlayerForSecondPlayerId() {
        assertSame(firstPlayer, gameSession.getOpponent("second-id"));
    }

    @Test
    void getOpponent_shouldThrowException_whenPlayerNotFound() {
        assertThrows(
                GameSessionException.class,
                () -> gameSession.getOpponent("unknown-id")
        );
    }

    @Test
    void switchTurn_shouldSwitchCurrentPlayer() {
        gameSession.switchTurn();

        assertEquals("second-id", gameSession.getCurrentPlayerId());

        gameSession.switchTurn();

        assertEquals("first-id", gameSession.getCurrentPlayerId());
    }

    private Player createPlayer(String name) {
        return new BotPlayer(
                name,
                new EasyBotStrategy(new Random(1))
        );
    }
}
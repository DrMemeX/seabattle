package ru.senla.seabattle.websocket.game;

import org.junit.jupiter.api.Test;
import ru.senla.seabattle.bot.EasyBotStrategy;
import ru.senla.seabattle.player.BotPlayer;
import ru.senla.seabattle.player.Player;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class PendingGameSessionTest {

    @Test
    void constructor_shouldStoreGameIdFirstPlayerIdAndFirstPlayer() {
        Player firstPlayer = new BotPlayer(
                "Первый",
                new EasyBotStrategy(new Random(1))
        );

        PendingGameSession pendingGameSession = new PendingGameSession(
                "game-id",
                "first-player-id",
                firstPlayer
        );

        assertEquals("game-id", pendingGameSession.getGameId());
        assertEquals("first-player-id", pendingGameSession.getFirstPlayerId());
        assertSame(firstPlayer, pendingGameSession.getFirstPlayer());
    }
}
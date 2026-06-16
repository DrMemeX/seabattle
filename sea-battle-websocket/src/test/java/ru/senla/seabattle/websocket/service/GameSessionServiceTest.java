package ru.senla.seabattle.websocket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.websocket.dto.ServerMessage;
import ru.senla.seabattle.websocket.game.GameSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameSessionServiceTest {

    private GameSessionService gameSessionService;

    @BeforeEach
    void setUp() {
        gameSessionService = new GameSessionService();
    }

    @Test
    void createGame_shouldReturnGameCreatedMessage() {
        ServerMessage response = gameSessionService.createGame("Андрей");

        assertEquals("GAME_CREATED", response.type());
        assertNotNull(response.gameId());
        assertNotNull(response.playerId());
        assertEquals(response.playerId(), response.currentPlayerId());
        assertEquals("Игра создана. Ожидаем второго игрока", response.message());
    }

    @Test
    void createGame_shouldUseDefaultName_whenNameIsBlank() {
        ServerMessage createResponse = gameSessionService.createGame("   ");
        ServerMessage joinResponse = gameSessionService.joinGame(
                createResponse.gameId(),
                "Миша"
        );

        GameSession gameSession = gameSessionService.getActiveGame(
                createResponse.gameId()
        );

        assertEquals("GAME_STARTED", joinResponse.type());
        assertEquals("Игрок", gameSession.getFirstPlayer().getName());
    }

    @Test
    void joinGame_shouldStartGame_whenPendingGameExists() {
        ServerMessage createResponse = gameSessionService.createGame("Андрей");

        ServerMessage joinResponse = gameSessionService.joinGame(
                createResponse.gameId(),
                "Миша"
        );

        assertEquals("GAME_STARTED", joinResponse.type());
        assertEquals(createResponse.gameId(), joinResponse.gameId());
        assertNotNull(joinResponse.playerId());
        assertEquals(createResponse.playerId(), joinResponse.currentPlayerId());
        assertEquals("Второй игрок подключился. Игра началась", joinResponse.message());
        assertNotNull(gameSessionService.getActiveGame(createResponse.gameId()));
    }

    @Test
    void joinGame_shouldReturnError_whenGameDoesNotExist() {
        ServerMessage response = gameSessionService.joinGame(
                "unknown-game-id",
                "Миша"
        );

        assertEquals("ERROR", response.type());
        assertEquals("unknown-game-id", response.gameId());
        assertEquals("Игра не найдена или уже началась", response.message());
    }

    @Test
    void joinGame_shouldReturnError_whenGameAlreadyStarted() {
        ServerMessage createResponse = gameSessionService.createGame("Андрей");

        gameSessionService.joinGame(createResponse.gameId(), "Миша");

        ServerMessage secondJoinResponse = gameSessionService.joinGame(
                createResponse.gameId(),
                "Петя"
        );

        assertEquals("ERROR", secondJoinResponse.type());
        assertEquals("Игра не найдена или уже началась", secondJoinResponse.message());
    }

    @Test
    void makeShot_shouldReturnError_whenGameIsNotActive() {
        ServerMessage response = gameSessionService.makeShot(
                "unknown-game-id",
                "player-id",
                new Coordinate(0, 0)
        );

        assertEquals("ERROR", response.type());
        assertEquals("Активная игра не найдена", response.message());
    }

    @Test
    void makeShot_shouldReturnError_whenItIsAnotherPlayerTurn() {
        ServerMessage createResponse = gameSessionService.createGame("Андрей");
        ServerMessage joinResponse = gameSessionService.joinGame(
                createResponse.gameId(),
                "Миша"
        );

        ServerMessage response = gameSessionService.makeShot(
                createResponse.gameId(),
                joinResponse.playerId(),
                new Coordinate(0, 0)
        );

        assertEquals("ERROR", response.type());
        assertEquals("Сейчас ход другого игрока", response.message());
    }

    @Test
    void makeShot_shouldReturnShotResult_whenCurrentPlayerShoots() {
        ServerMessage createResponse = gameSessionService.createGame("Андрей");

        gameSessionService.joinGame(
                createResponse.gameId(),
                "Миша"
        );

        ServerMessage response = gameSessionService.makeShot(
                createResponse.gameId(),
                createResponse.playerId(),
                new Coordinate(0, 0)
        );

        assertEquals("SHOT_RESULT", response.type());
        assertEquals(createResponse.gameId(), response.gameId());
        assertEquals(createResponse.playerId(), response.playerId());
        assertEquals(new Coordinate(0, 0), response.coordinate());
        assertNotNull(response.shotResult());
        assertNotNull(response.currentPlayerId());
        assertNull(response.winnerName());
    }

    @Test
    void makeShot_shouldReturnRepeatedShotError_whenCurrentPlayerShootsSameCellAgain() {
        ServerMessage createResponse = gameSessionService.createGame("Андрей");

        gameSessionService.joinGame(
                createResponse.gameId(),
                "Миша"
        );

        gameSessionService.makeShot(
                createResponse.gameId(),
                createResponse.playerId(),
                new Coordinate(0, 0)
        );

        GameSession gameSession = gameSessionService.getActiveGame(
                createResponse.gameId()
        );

        if (!gameSession.isCurrentPlayer(createResponse.playerId())) {
            gameSession.switchTurn();
        }

        ServerMessage response = gameSessionService.makeShot(
                createResponse.gameId(),
                createResponse.playerId(),
                new Coordinate(0, 0)
        );

        assertEquals("ERROR", response.type());
        assertEquals("По этой клетке уже стреляли", response.message());
    }
}
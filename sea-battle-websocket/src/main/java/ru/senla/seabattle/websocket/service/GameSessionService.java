package ru.senla.seabattle.websocket.service;

import org.springframework.stereotype.Service;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;
import ru.senla.seabattle.exception.RepeatedShotException;
import ru.senla.seabattle.game.engine.ClassicGameEngine;
import ru.senla.seabattle.game.engine.GameEngine;
import ru.senla.seabattle.game.history.ShotInfo;
import ru.senla.seabattle.placement.FleetPlacementService;
import ru.senla.seabattle.placement.PlacementValidator;
import ru.senla.seabattle.placement.ShipPlacementService;
import ru.senla.seabattle.placement.impl.DefaultPlacementValidator;
import ru.senla.seabattle.placement.impl.DefaultShipPlacementService;
import ru.senla.seabattle.placement.impl.RandomFleetPlacementService;
import ru.senla.seabattle.player.Player;
import ru.senla.seabattle.websocket.dto.ServerMessage;
import ru.senla.seabattle.websocket.exception.GameSessionException;
import ru.senla.seabattle.websocket.game.GameSession;
import ru.senla.seabattle.websocket.game.PendingGameSession;
import ru.senla.seabattle.websocket.player.WebSocketPlayer;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameSessionService {

    private final Map<String, PendingGameSession> pendingGames = new ConcurrentHashMap<>();
    private final Map<String, GameSession> activeGames = new ConcurrentHashMap<>();

    private final GameEngine gameEngine;
    private final FleetPlacementService fleetPlacementService;

    public GameSessionService() {
        Random random = new Random();

        PlacementValidator placementValidator = new DefaultPlacementValidator();
        ShipPlacementService shipPlacementService =
                new DefaultShipPlacementService(placementValidator);

        this.fleetPlacementService =
                new RandomFleetPlacementService(shipPlacementService, random);

        this.gameEngine = new ClassicGameEngine();
    }

    public ServerMessage createGame(String playerName) {
        String gameId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();

        Player firstPlayer = new WebSocketPlayer(normalizeName(playerName));
        fleetPlacementService.placeFleet(firstPlayer.getOwnBoard());

        PendingGameSession pendingGameSession = new PendingGameSession(
                gameId,
                playerId,
                firstPlayer
        );

        pendingGames.put(gameId, pendingGameSession);

        return new ServerMessage(
                "GAME_CREATED",
                gameId,
                playerId,
                playerId,
                null,
                null,
                false,
                null,
                "Игра создана. Ожидаем второго игрока"
        );
    }

    public ServerMessage joinGame(String gameId, String playerName) {
        PendingGameSession pendingGameSession = pendingGames.remove(gameId);

        if (pendingGameSession == null) {
            return error(gameId, "Игра не найдена или уже началась");
        }

        String secondPlayerId = UUID.randomUUID().toString();

        Player secondPlayer = new WebSocketPlayer(normalizeName(playerName));
        fleetPlacementService.placeFleet(secondPlayer.getOwnBoard());

        GameSession gameSession = new GameSession(
                gameId,
                pendingGameSession.getFirstPlayerId(),
                secondPlayerId,
                pendingGameSession.getFirstPlayer(),
                secondPlayer
        );

        activeGames.put(gameId, gameSession);

        return new ServerMessage(
                "GAME_STARTED",
                gameId,
                secondPlayerId,
                gameSession.getCurrentPlayerId(),
                null,
                null,
                false,
                null,
                "Второй игрок подключился. Игра началась"
        );
    }

    public ServerMessage makeShot(String gameId,
                                  String playerId,
                                  Coordinate coordinate) {
        GameSession gameSession = activeGames.get(gameId);

        if (gameSession == null) {
            return error(gameId, "Активная игра не найдена");
        }

        if (!gameSession.isCurrentPlayer(playerId)) {
            return error(gameId, "Сейчас ход другого игрока");
        }

        Player attacker = gameSession.getPlayer(playerId);
        Player defender = gameSession.getOpponent(playerId);

        try {
            ShotInfo shotInfo = gameEngine.makeShot(
                    attacker,
                    defender,
                    coordinate
            );

            boolean gameOver = gameEngine.isGameOver(defender);

            if (!gameOver && shotInfo.getResult() == ShotResult.MISS) {
                gameSession.switchTurn();
            }

            return new ServerMessage(
                    "SHOT_RESULT",
                    gameId,
                    playerId,
                    gameSession.getCurrentPlayerId(),
                    shotInfo.getCoordinate(),
                    shotInfo.getResult(),
                    gameOver,
                    gameOver ? attacker.getName() : null,
                    gameOver ? "Игра окончена" : "Выстрел обработан"
            );
        } catch (RepeatedShotException | GameSessionException e) {
            return error(gameId, e.getMessage());
        }
    }

    public GameSession getActiveGame(String gameId) {
        return activeGames.get(gameId);
    }

    private ServerMessage error(String gameId, String message) {
        return new ServerMessage(
                "ERROR",
                gameId,
                null,
                null,
                null,
                null,
                false,
                null,
                message
        );
    }

    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return "Игрок";
        }

        return name.trim();
    }
}
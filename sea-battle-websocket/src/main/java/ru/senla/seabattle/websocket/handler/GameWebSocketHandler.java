package ru.senla.seabattle.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidCoordinateException;
import ru.senla.seabattle.parser.CoordinateParser;
import ru.senla.seabattle.websocket.dto.ClientMessage;
import ru.senla.seabattle.websocket.dto.ServerMessage;
import ru.senla.seabattle.websocket.service.GameSessionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final GameSessionService gameSessionService;
    private final ObjectMapper objectMapper;
    private final CoordinateParser coordinateParser;

    private final Map<WebSocketSession, String> sessionPlayerIds = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, String> sessionGameIds = new ConcurrentHashMap<>();
    private final Map<String, List<WebSocketSession>> gameSessions = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> pendingCreatorSessions = new ConcurrentHashMap<>();

    public GameWebSocketHandler(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
        this.objectMapper = new ObjectMapper();
        this.coordinateParser = new CoordinateParser();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {
        ClientMessage clientMessage = objectMapper.readValue(
                message.getPayload(),
                ClientMessage.class
        );

        switch (clientMessage.type()) {
            case "CREATE_GAME" -> handleCreateGame(session, clientMessage);
            case "JOIN_GAME" -> handleJoinGame(session, clientMessage);
            case "SHOT" -> handleShot(session, clientMessage);
            default -> sendToSession(
                    session,
                    error(null, "Неизвестный тип сообщения")
            );
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) {
        String gameId = sessionGameIds.remove(session);

        sessionPlayerIds.remove(session);

        if (gameId == null) {
            return;
        }

        List<WebSocketSession> sessions = gameSessions.get(gameId);

        if (sessions != null) {
            sessions.remove(session);
        }

        pendingCreatorSessions.remove(gameId, session);
    }

    private void handleCreateGame(WebSocketSession session,
                                  ClientMessage clientMessage) throws IOException {
        ServerMessage response = gameSessionService.createGame(clientMessage.name());

        bindSessionToGame(
                session,
                response.gameId(),
                response.playerId()
        );

        pendingCreatorSessions.put(response.gameId(), session);

        sendToSession(session, response);
    }

    private void handleJoinGame(WebSocketSession session,
                                ClientMessage clientMessage) throws IOException {
        ServerMessage response = gameSessionService.joinGame(
                clientMessage.gameId(),
                clientMessage.name()
        );

        if ("ERROR".equals(response.type())) {
            sendToSession(session, response);
            return;
        }

        bindSessionToGame(
                session,
                response.gameId(),
                response.playerId()
        );

        sendToSession(session, response);
        notifyGameCreator(response);
    }

    private void handleShot(WebSocketSession session,
                            ClientMessage clientMessage) throws IOException {
        String gameId = sessionGameIds.get(session);
        String playerId = sessionPlayerIds.get(session);

        if (gameId == null || playerId == null) {
            sendToSession(
                    session,
                    error(null, "Сессия игрока не привязана к игре")
            );
            return;
        }

        try {
            Coordinate coordinate = resolveCoordinate(clientMessage);

            ServerMessage response = gameSessionService.makeShot(
                    gameId,
                    playerId,
                    coordinate
            );

            if ("ERROR".equals(response.type())) {
                sendToSession(session, response);
                return;
            }

            broadcastToGame(gameId, response);
        } catch (InvalidCoordinateException e) {
            sendToSession(
                    session,
                    error(gameId, e.getMessage())
            );
        }
    }

    private void bindSessionToGame(WebSocketSession session,
                                   String gameId,
                                   String playerId) {
        sessionGameIds.put(session, gameId);
        sessionPlayerIds.put(session, playerId);

        gameSessions.computeIfAbsent(
                gameId,
                id -> new ArrayList<>()
        ).add(session);
    }

    private void notifyGameCreator(ServerMessage joinResponse) throws IOException {
        WebSocketSession creatorSession = pendingCreatorSessions.remove(
                joinResponse.gameId()
        );

        if (creatorSession == null || !creatorSession.isOpen()) {
            return;
        }

        ServerMessage creatorMessage = new ServerMessage(
                "GAME_STARTED",
                joinResponse.gameId(),
                sessionPlayerIds.get(creatorSession),
                joinResponse.currentPlayerId(),
                null,
                null,
                false,
                null,
                "Второй игрок подключился. Игра началась"
        );

        sendToSession(creatorSession, creatorMessage);
    }

    private void broadcastToGame(String gameId,
                                 ServerMessage message) throws IOException {
        List<WebSocketSession> sessions = gameSessions.get(gameId);

        if (sessions == null) {
            return;
        }

        for (WebSocketSession session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(WebSocketSession session,
                               ServerMessage message) throws IOException {
        if (!session.isOpen()) {
            return;
        }

        String json = objectMapper.writeValueAsString(message);
        session.sendMessage(new TextMessage(json));
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

    private Coordinate resolveCoordinate(ClientMessage clientMessage) {
        if (clientMessage.coordinate() != null
                && !clientMessage.coordinate().isBlank()) {
            return coordinateParser.parse(clientMessage.coordinate());
        }

        return new Coordinate(
                clientMessage.row(),
                clientMessage.column()
        );
    }
}
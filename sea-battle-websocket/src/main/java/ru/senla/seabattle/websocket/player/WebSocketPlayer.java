package ru.senla.seabattle.websocket.player;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.player.AbstractPlayer;

public class WebSocketPlayer extends AbstractPlayer {

    public WebSocketPlayer(String name) {
        super(name);
    }

    @Override
    public Coordinate chooseShot() {
        throw new UnsupportedOperationException(
                "Игрок не выбирает выстрел напрямую. Координата приходит от клиента через WebSocket"
        );
    }
}
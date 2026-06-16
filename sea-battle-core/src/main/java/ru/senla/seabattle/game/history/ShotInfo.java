package ru.senla.seabattle.game.history;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;

public class ShotInfo {

    private final Coordinate coordinate;
    private final ShotResult result;

    public ShotInfo(Coordinate coordinate,
                    ShotResult result) {
        this.coordinate = coordinate;
        this.result = result;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public ShotResult getResult() {
        return result;
    }
}
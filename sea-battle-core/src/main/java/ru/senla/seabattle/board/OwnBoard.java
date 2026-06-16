package ru.senla.seabattle.board;

import ru.senla.seabattle.board.model.Cell;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;
import ru.senla.seabattle.exception.InvalidCoordinateException;
import ru.senla.seabattle.exception.RepeatedShotException;
import ru.senla.seabattle.ship.Ship;

import java.util.ArrayList;
import java.util.List;

public class OwnBoard extends AbstractBoard {

    private final List<Ship> ships;

    public OwnBoard() {
        this.ships = new ArrayList<>();
    }

    public void addShip(Ship ship) {
        ships.add(ship);

        for (Coordinate coordinate : ship.getCoordinates()) {
            setCellState(coordinate, CellState.SHIP);
        }
    }

    public ShotResult shoot(Coordinate coordinate) {
        validateCoordinate(coordinate);

        Cell cell = getCell(coordinate);

        if (cell.getState() == CellState.MISS || cell.getState() == CellState.HIT) {
            throw new RepeatedShotException("По этой клетке уже стреляли");
        }

        if (cell.getState() == CellState.EMPTY) {
            cell.setState(CellState.MISS);
            return ShotResult.MISS;
        }

        Ship ship = findShipByCoordinate(coordinate);
        ship.hit(coordinate);
        cell.setState(CellState.HIT);

        return ship.isSunk() ? ShotResult.KILL : ShotResult.HIT;
    }

    public boolean allShipsDestroyed() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    public List<Ship> getShips() {
        return new ArrayList<>(ships);
    }

    private Ship findShipByCoordinate(Coordinate coordinate) {
        return ships.stream()
                .filter(ship -> ship.contains(coordinate))
                .findFirst()
                .orElseThrow(() -> new InvalidCoordinateException(
                        "Корабль по указанной координате не найден"
                ));
    }
}
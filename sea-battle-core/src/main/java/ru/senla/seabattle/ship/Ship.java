package ru.senla.seabattle.ship;

import ru.senla.seabattle.board.model.Coordinate;

import java.util.HashSet;
import java.util.Set;

public class Ship {

    private final Set<Coordinate> coordinates;
    private final Set<Coordinate> hits;

    public Ship(Set<Coordinate> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            throw new IllegalArgumentException("Корабль должен содержать хотя бы одну координату");
        }

        this.coordinates = new HashSet<>(coordinates);
        this.hits = new HashSet<>();
    }

    public boolean contains(Coordinate coordinate) {
        return coordinates.contains(coordinate);
    }

    public void hit(Coordinate coordinate) {
        if (!contains(coordinate)) {
            throw new IllegalArgumentException(
                    "Координата выстрела не принадлежит данному кораблю"
            );
        }

        hits.add(coordinate);
    }

    public boolean isSunk() {
        return hits.containsAll(coordinates);
    }

    public boolean isHit(Coordinate coordinate) {
        return hits.contains(coordinate);
    }

    public int getSize() {
        return coordinates.size();
    }

    public Set<Coordinate> getCoordinates() {
        return new HashSet<>(coordinates);
    }

    public Set<Coordinate> getHits() {
        return new HashSet<>(hits);
    }
}
package ru.senla.seabattle.player;

import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.Coordinate;

public abstract class AbstractPlayer implements Player {

    private final String name;

    private final OwnBoard ownBoard;
    private final EnemyViewBoard enemyViewBoard;

    private int shotCount;

    private int hitsCount;

    protected AbstractPlayer(String name) {
        this.name = name;
        this.ownBoard = new OwnBoard();
        this.enemyViewBoard = new EnemyViewBoard();
    }

    public abstract Coordinate chooseShot();

    public String getName() {
        return name;
    }

    public OwnBoard getOwnBoard() {
        return ownBoard;
    }

    public EnemyViewBoard getEnemyViewBoard() {
        return enemyViewBoard;
    }

    public int getShotCount() {
        return shotCount;
    }

    public int getHitsCount() {
        return hitsCount;
    }

    public void incrementShotsCount() {
        shotCount++;
    }

    public void incrementHitsCount() {
        hitsCount++;
    }

    public double getAccuracy() {
        if (shotCount == 0) {
            return 0.0;
        }

        return (double) hitsCount / shotCount * 100;
    }
}

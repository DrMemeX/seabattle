package ru.senla.seabattle.board.render;

import ru.senla.seabattle.board.AbstractBoard;
import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;

import static ru.senla.seabattle.board.AbstractBoard.SIZE;

public class BoardRenderer {

    public String renderOwnBoard(OwnBoard ownBoard) {
        return renderBoard(ownBoard, true);
    }

    public String renderEnemyBoard(EnemyViewBoard enemyViewBoard) {
        return renderBoard(enemyViewBoard, false);
    }

    private String renderBoard(AbstractBoard board, boolean showShips) {
        StringBuilder builder = new StringBuilder();

        builder.append("   ");

        for (int column = 1; column <= SIZE; column++) {
            builder.append(String.format("%3d", column));
        }

        builder.append(System.lineSeparator());

        for (int row = 0; row < SIZE; row++) {
            builder.append(String.format("%3s", (char) ('A' + row)));

            for (int column = 0; column < SIZE; column++) {
                Coordinate coordinate = new Coordinate(row, column);
                CellState state = board.getCell(coordinate).getState();

                builder.append(String.format("%3s", toSymbol(state, showShips)));
            }

            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

    private String toSymbol(CellState state, boolean showShips) {
        return switch (state) {
            case EMPTY -> " ";
            case SHIP -> showShips ? "#" : " ";
            case MISS -> ".";
            case HIT -> "X";
        };
    }
}

package ru.senla.seabattle.console.printer;

import ru.senla.seabattle.board.AbstractBoard;
import ru.senla.seabattle.board.EnemyViewBoard;
import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.CellState;
import ru.senla.seabattle.board.model.Coordinate;

import static ru.senla.seabattle.board.AbstractBoard.SIZE;

public class BoardPrinter {

    public void printBoards(OwnBoard ownBoard,
                            EnemyViewBoard enemyViewBoard) {
        System.out.println("ВАШЕ ПОЛЕ");
        printBoard(ownBoard, true);

        System.out.println();
        System.out.println("ПОЛЕ ПРОТИВНИКА");
        printBoard(enemyViewBoard, false);
    }

    public void printOwnBoard(OwnBoard ownBoard) {
        printBoard(ownBoard, true);
    }

    public void printEnemyBoard(EnemyViewBoard enemyViewBoard) {
        printBoard(enemyViewBoard, false);
    }

    private void printBoard(AbstractBoard board, boolean showShips) {
        printHeader();

        for (int row = 0; row < SIZE; row++) {
            char rowLabel = (char) ('A' + row);

            System.out.printf("%3s", rowLabel);

            for (int column = 0; column < SIZE; column++) {
                Coordinate coordinate = new Coordinate(row, column);
                CellState state = board.getCell(coordinate).getState();

                System.out.printf("%3s", toSymbol(state, showShips));
            }

            System.out.println();
        }
    }

    private void printHeader() {
        System.out.print("   ");

        for (int column = 1; column <= SIZE; column++) {
            System.out.printf("%3d", column);
        }

        System.out.println();
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

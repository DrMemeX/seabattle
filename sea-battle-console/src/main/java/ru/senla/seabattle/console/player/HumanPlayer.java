package ru.senla.seabattle.console.player;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.exception.InvalidCoordinateException;
import ru.senla.seabattle.parser.CoordinateParser;
import ru.senla.seabattle.player.AbstractPlayer;

import java.util.Scanner;

public class HumanPlayer extends AbstractPlayer {

    private final Scanner scanner;
    private final CoordinateParser coordinateParser;

    public HumanPlayer(String name,
                       Scanner scanner,
                       CoordinateParser coordinateParser) {
        super(name);
        this.scanner = scanner;
        this.coordinateParser = coordinateParser;
    }

    @Override
    public Coordinate chooseShot() {
        while (true) {
            try {
                System.out.print("Введите координату выстрела (например, D1): ");

                String input = scanner.nextLine();

                return coordinateParser.parse(input);
            } catch (InvalidCoordinateException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

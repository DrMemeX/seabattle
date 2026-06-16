package ru.senla.seabattle.console.placement;

import ru.senla.seabattle.board.OwnBoard;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.console.printer.BoardPrinter;
import ru.senla.seabattle.console.printer.ConsoleOutput;
import ru.senla.seabattle.exception.InvalidCoordinateException;
import ru.senla.seabattle.exception.InvalidPlacementException;
import ru.senla.seabattle.parser.CoordinateParser;
import ru.senla.seabattle.parser.OrientationParser;
import ru.senla.seabattle.placement.FleetPlacementService;
import ru.senla.seabattle.placement.ShipPlacementService;
import ru.senla.seabattle.rules.FleetRules;
import ru.senla.seabattle.ship.Orientation;

import java.util.Scanner;

public class ManualFleetPlacementService implements FleetPlacementService {

    private final ShipPlacementService shipPlacementService;
    private final CoordinateParser coordinateParser;
    private final OrientationParser orientationParser;
    private final BoardPrinter boardPrinter;
    private final ConsoleOutput consoleOutput;
    private final Scanner scanner;

    public ManualFleetPlacementService(ShipPlacementService shipPlacementService,
                                       CoordinateParser coordinateParser,
                                       OrientationParser orientationParser,
                                       BoardPrinter boardPrinter,
                                       ConsoleOutput consoleOutput,
                                       Scanner scanner) {
        this.shipPlacementService = shipPlacementService;
        this.coordinateParser = coordinateParser;
        this.orientationParser = orientationParser;
        this.boardPrinter = boardPrinter;
        this.consoleOutput = consoleOutput;
        this.scanner = scanner;
    }

    @Override
    public void placeFleet(OwnBoard ownBoard) {
        consoleOutput.printManualPlacementStart();

        for (int shipSize : FleetRules.getShipSizes()) {
            placeShipUntilValid(ownBoard, shipSize);
        }
    }

    private void placeShipUntilValid(OwnBoard ownBoard, int shipSize) {
        while (true) {
            try {
                boardPrinter.printOwnBoard(ownBoard);
                consoleOutput.printShipPlacementRequest(shipSize);

                consoleOutput.printCoordinatePrompt();
                Coordinate startCoordinate = coordinateParser.parse(scanner.nextLine());

                consoleOutput.printOrientationPrompt();
                Orientation orientation = orientationParser.parse(scanner.nextLine());

                shipPlacementService.placeShip(
                        ownBoard,
                        startCoordinate,
                        orientation,
                        shipSize
                );

                consoleOutput.printShipPlacedSuccessfully();
                return;
            } catch (InvalidCoordinateException | InvalidPlacementException e) {
                consoleOutput.printMessage(e.getMessage());
            }
        }
    }
}
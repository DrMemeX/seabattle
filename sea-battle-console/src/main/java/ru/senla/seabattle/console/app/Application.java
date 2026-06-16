package ru.senla.seabattle.console.app;

import ru.senla.seabattle.board.render.BoardRenderer;
import ru.senla.seabattle.bot.BotStrategy;
import ru.senla.seabattle.bot.EasyBotStrategy;
import ru.senla.seabattle.bot.ExpertBotStrategy;
import ru.senla.seabattle.bot.HardBotStrategy;
import ru.senla.seabattle.bot.NormalBotStrategy;
import ru.senla.seabattle.console.game.SeaBattleGame;
import ru.senla.seabattle.console.placement.ManualFleetPlacementService;
import ru.senla.seabattle.console.player.HumanPlayer;
import ru.senla.seabattle.console.printer.BoardPrinter;
import ru.senla.seabattle.console.printer.ConsoleOutput;
import ru.senla.seabattle.game.engine.ClassicGameEngine;
import ru.senla.seabattle.game.engine.GameEngine;
import ru.senla.seabattle.parser.CoordinateParser;
import ru.senla.seabattle.parser.OrientationParser;
import ru.senla.seabattle.placement.FleetPlacementService;
import ru.senla.seabattle.placement.PlacementValidator;
import ru.senla.seabattle.placement.ShipPlacementService;
import ru.senla.seabattle.placement.impl.DefaultPlacementValidator;
import ru.senla.seabattle.placement.impl.DefaultShipPlacementService;
import ru.senla.seabattle.placement.impl.RandomFleetPlacementService;
import ru.senla.seabattle.player.BotPlayer;
import ru.senla.seabattle.player.Player;
import ru.senla.seabattle.report.FileGameReportWriter;
import ru.senla.seabattle.report.GameReportWriter;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        CoordinateParser coordinateParser = new CoordinateParser();
        OrientationParser orientationParser = new OrientationParser();

        BoardPrinter boardPrinter = new BoardPrinter();
        ConsoleOutput consoleOutput = new ConsoleOutput();
        BoardRenderer boardRenderer = new BoardRenderer();

        PlacementValidator placementValidator = new DefaultPlacementValidator();
        ShipPlacementService shipPlacementService =
                new DefaultShipPlacementService(placementValidator);

        FleetPlacementService randomFleetPlacementService =
                new RandomFleetPlacementService(
                        shipPlacementService,
                        random
                );

        FleetPlacementService manualFleetPlacementService =
                new ManualFleetPlacementService(
                        shipPlacementService,
                        coordinateParser,
                        orientationParser,
                        boardPrinter,
                        consoleOutput,
                        scanner
                );

        GameReportWriter gameReportWriter = new FileGameReportWriter(
                createReportPath(),
                boardRenderer
        );

        String playerName = readPlayerName(scanner, consoleOutput);
        BotStrategy botStrategy = chooseBotStrategy(scanner, consoleOutput, random);
        boolean manualPlacement = chooseManualPlacement(scanner, consoleOutput);

        Player humanPlayer = new HumanPlayer(
                playerName,
                scanner,
                coordinateParser
        );

        Player botPlayer = new BotPlayer(
                "Бот",
                botStrategy
        );

        placeHumanFleet(
                humanPlayer,
                manualPlacement,
                manualFleetPlacementService,
                randomFleetPlacementService,
                consoleOutput
        );

        consoleOutput.printMessage("Выполняется расстановка флота бота");
        randomFleetPlacementService.placeFleet(botPlayer.getOwnBoard());

        GameEngine gameEngine = new ClassicGameEngine();

        SeaBattleGame game = new SeaBattleGame(
                humanPlayer,
                botPlayer,
                gameEngine,
                boardPrinter,
                consoleOutput,
                gameReportWriter
        );

        game.start();

        scanner.close();
    }

    private static String readPlayerName(Scanner scanner,
                                         ConsoleOutput consoleOutput) {
        while (true) {
            consoleOutput.printPlayerNamePrompt();

            String name = scanner.nextLine().trim();

            if (!name.isBlank()) {
                return name;
            }

            consoleOutput.printMessage("Имя не может быть пустым");
        }
    }

    private static BotStrategy chooseBotStrategy(Scanner scanner,
                                                 ConsoleOutput consoleOutput,
                                                 Random random) {
        while (true) {
            consoleOutput.printBotDifficultyMenu();

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return new EasyBotStrategy(random);
                case "2":
                    return new NormalBotStrategy(random);
                case "3":
                    return new HardBotStrategy(random);
                case "4":
                    return new ExpertBotStrategy(random);
                default:
                    consoleOutput.printInvalidMenuOption();
            }
        }
    }

    private static boolean chooseManualPlacement(Scanner scanner,
                                                 ConsoleOutput consoleOutput) {
        while (true) {
            consoleOutput.printFleetPlacementMenu();

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return false;
                case "2":
                    return true;
                default:
                    consoleOutput.printInvalidMenuOption();
            }
        }
    }

    private static void placeHumanFleet(Player humanPlayer,
                                        boolean manualPlacement,
                                        FleetPlacementService manualFleetPlacementService,
                                        FleetPlacementService randomFleetPlacementService,
                                        ConsoleOutput consoleOutput) {
        if (manualPlacement) {
            manualFleetPlacementService.placeFleet(humanPlayer.getOwnBoard());
            return;
        }

        consoleOutput.printRandomFleetPlacementStart();
        randomFleetPlacementService.placeFleet(humanPlayer.getOwnBoard());
    }

    private static Path createReportPath() {
        String fileName = "game-report-"
                + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
        )
                + ".txt";

        return Path.of("reports", fileName);
    }
}
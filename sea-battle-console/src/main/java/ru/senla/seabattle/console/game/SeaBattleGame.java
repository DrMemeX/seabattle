package ru.senla.seabattle.console.game;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;
import ru.senla.seabattle.console.printer.BoardPrinter;
import ru.senla.seabattle.console.printer.ConsoleOutput;
import ru.senla.seabattle.exception.RepeatedShotException;
import ru.senla.seabattle.game.engine.GameEngine;
import ru.senla.seabattle.game.history.ShotInfo;
import ru.senla.seabattle.player.Player;
import ru.senla.seabattle.report.GameMoveRecord;
import ru.senla.seabattle.report.GameReportWriter;

import java.time.LocalDateTime;

public class SeaBattleGame {

    private final Player firstPlayer;
    private final Player secondPlayer;
    private final GameEngine gameEngine;
    private final BoardPrinter boardPrinter;
    private final ConsoleOutput consoleOutput;
    private final GameReportWriter gameReportWriter;

    public SeaBattleGame(Player firstPlayer,
                         Player secondPlayer,
                         GameEngine gameEngine,
                         BoardPrinter boardPrinter,
                         ConsoleOutput consoleOutput,
                         GameReportWriter gameReportWriter) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.gameEngine = gameEngine;
        this.boardPrinter = boardPrinter;
        this.consoleOutput = consoleOutput;
        this.gameReportWriter = gameReportWriter;
    }

    public void start() {
        gameReportWriter.startGame(firstPlayer, secondPlayer);

        Player attacker = firstPlayer;
        Player defender = secondPlayer;

        while (!gameEngine.isGameOver(firstPlayer)
                && !gameEngine.isGameOver(secondPlayer)) {

            boardPrinter.printBoards(
                    firstPlayer.getOwnBoard(),
                    firstPlayer.getEnemyViewBoard()
            );

            consoleOutput.printTurn(attacker);

            ShotInfo shotInfo = makeShotUntilValid(attacker, defender);

            gameReportWriter.writeMove(
                    new GameMoveRecord(
                            LocalDateTime.now(),
                            attacker.getName(),
                            shotInfo.getCoordinate(),
                            shotInfo.getResult()
                    )
            );

            consoleOutput.printShotCoordinate(shotInfo.getCoordinate());
            consoleOutput.printShotResult(shotInfo.getResult());

            if (gameEngine.isGameOver(defender)) {
                gameReportWriter.finishGame(firstPlayer, secondPlayer, attacker);

                consoleOutput.printWinner(attacker);
                consoleOutput.printStatistics(firstPlayer, secondPlayer);
                return;
            }

            if (shotInfo.getResult() == ShotResult.MISS) {
                Player temp = attacker;
                attacker = defender;
                defender = temp;
            }
        }
    }

    private ShotInfo makeShotUntilValid(Player attacker,
                                        Player defender) {
        while (true) {
            try {
                Coordinate coordinate = attacker.chooseShot();

                return gameEngine.makeShot(
                        attacker,
                        defender,
                        coordinate
                );
            } catch (RepeatedShotException e) {
                consoleOutput.printMessage(e.getMessage());
            }
        }
    }
}
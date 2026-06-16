package ru.senla.seabattle.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;
import ru.senla.seabattle.board.render.BoardRenderer;
import ru.senla.seabattle.bot.EasyBotStrategy;
import ru.senla.seabattle.player.BotPlayer;
import ru.senla.seabattle.player.Player;
import ru.senla.seabattle.ship.Ship;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileGameReportWriterTest {

    @TempDir
    private Path tempDir;

    @Test
    void startGame_shouldCreateReportFileAndWritePlayers() throws IOException {
        Path reportPath = tempDir.resolve("game-report.txt");

        FileGameReportWriter reportWriter = new FileGameReportWriter(
                reportPath,
                new BoardRenderer()
        );

        Player firstPlayer = createPlayer("Игрок 1");
        Player secondPlayer = createPlayer("Игрок 2");

        reportWriter.startGame(firstPlayer, secondPlayer);

        String content = Files.readString(reportPath);

        assertTrue(content.contains("Игра началась:"));
        assertTrue(content.contains("Игрок 1: Игрок 1"));
        assertTrue(content.contains("Игрок 2: Игрок 2"));
        assertTrue(content.contains("Ходы:"));
    }

    @Test
    void writeMove_shouldAppendMoveToReportFile() throws IOException {
        Path reportPath = tempDir.resolve("game-report.txt");

        FileGameReportWriter reportWriter = new FileGameReportWriter(
                reportPath,
                new BoardRenderer()
        );

        reportWriter.writeMove(
                new GameMoveRecord(
                        LocalDateTime.of(2026, 6, 16, 12, 0),
                        "Игрок 1",
                        new Coordinate(3, 1),
                        ShotResult.MISS
                )
        );

        String content = Files.readString(reportPath);

        assertTrue(content.contains("2026-06-16T12:00"));
        assertTrue(content.contains("Игрок 1"));
        assertTrue(content.contains("D2"));
        assertTrue(content.contains("MISS"));
    }

    @Test
    void finishGame_shouldWriteWinnerStatisticsAndBoards() throws IOException {
        Path reportPath = tempDir.resolve("game-report.txt");

        FileGameReportWriter reportWriter = new FileGameReportWriter(
                reportPath,
                new BoardRenderer()
        );

        Player firstPlayer = createPlayer("Победитель");
        Player secondPlayer = createPlayer("Проигравший");

        firstPlayer.getOwnBoard().addShip(
                new Ship(Set.of(new Coordinate(0, 0)))
        );

        secondPlayer.getOwnBoard().addShip(
                new Ship(Set.of(new Coordinate(1, 1)))
        );

        reportWriter.finishGame(firstPlayer, secondPlayer, firstPlayer);

        String content = Files.readString(reportPath);

        assertTrue(content.contains("Игра окончена:"));
        assertTrue(content.contains("Победитель: Победитель"));
        assertTrue(content.contains("Победитель | выиграл"));
        assertTrue(content.contains("Проигравший | проиграл"));
        assertTrue(content.contains("Финальное поле игрока Победитель:"));
        assertTrue(content.contains("Поле противника игрока Победитель:"));
        assertTrue(content.contains("Финальное поле игрока Проигравший:"));
        assertTrue(content.contains("Поле противника игрока Проигравший:"));
    }

    @Test
    void writer_shouldCreateParentDirectories_whenTheyDoNotExist() throws IOException {
        Path reportPath = tempDir
                .resolve("reports")
                .resolve("nested")
                .resolve("game-report.txt");

        FileGameReportWriter reportWriter = new FileGameReportWriter(
                reportPath,
                new BoardRenderer()
        );

        reportWriter.writeMove(
                new GameMoveRecord(
                        LocalDateTime.of(2026, 6, 16, 12, 0),
                        "Игрок",
                        new Coordinate(0, 0),
                        ShotResult.HIT
                )
        );

        assertTrue(Files.exists(reportPath));
    }

    private Player createPlayer(String name) {
        return new BotPlayer(
                name,
                new EasyBotStrategy(new Random(1))
        );
    }
}
package ru.senla.seabattle.report;

import ru.senla.seabattle.player.Player;
import ru.senla.seabattle.board.render.BoardRenderer;
import ru.senla.seabattle.report.GameReportWriter;
import ru.senla.seabattle.ship.Ship;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileGameReportWriter implements GameReportWriter {

    private final Path reportPath;
    private final BoardRenderer boardRenderer;

    public FileGameReportWriter(Path reportPath,
                                BoardRenderer boardRenderer) {
        this.reportPath = reportPath;
        this.boardRenderer = boardRenderer;
    }

    @Override
    public void startGame(Player firstPlayer, Player secondPlayer) {
        writeLines(List.of(
                "Игра началась: " + LocalDateTime.now(),
                "Игрок 1: " + firstPlayer.getName(),
                "Игрок 2: " + secondPlayer.getName(),
                "",
                "Ходы:"
        ));
    }

    @Override
    public void writeMove(GameMoveRecord moveRecord) {
        writeLines(List.of(
                moveRecord.time()
                        + " | "
                        + moveRecord.playerName()
                        + " | "
                        + moveRecord.coordinate()
                        + " | "
                        + moveRecord.result()
        ));
    }

    @Override
    public void finishGame(Player firstPlayer, Player secondPlayer, Player winner) {
        List<String> lines = new ArrayList<>();

        lines.add("");
        lines.add("Игра окончена: " + LocalDateTime.now());
        lines.add("Победитель: " + winner.getName());
        lines.add("");
        lines.add("Результаты:");
        addPlayerResult(lines, firstPlayer, winner);
        addPlayerResult(lines, secondPlayer, winner);

        lines.add("");
        lines.add("Финальное поле игрока " + firstPlayer.getName() + ":");
        lines.add(boardRenderer.renderOwnBoard(firstPlayer.getOwnBoard()));

        lines.add("Поле противника игрока " + firstPlayer.getName() + ":");
        lines.add(boardRenderer.renderEnemyBoard(firstPlayer.getEnemyViewBoard()));

        lines.add("Финальное поле игрока " + secondPlayer.getName() + ":");
        lines.add(boardRenderer.renderOwnBoard(secondPlayer.getOwnBoard()));

        lines.add("Поле противника игрока " + secondPlayer.getName() + ":");
        lines.add(boardRenderer.renderEnemyBoard(secondPlayer.getEnemyViewBoard()));

        writeLines(lines);
    }

    private void addPlayerResult(List<String> lines, Player player, Player winner) {
        lines.add(player.getName()
                + " | "
                + (player == winner ? "выиграл" : "проиграл")
                + " | нетронутых кораблей: "
                + countUntouchedShips(player)
                + " | ходов: "
                + player.getShotCount()
                + " | попаданий: "
                + player.getHitsCount()
                + " | результативность: "
                + player.getAccuracy()
                + "%");
    }

    private long countUntouchedShips(Player player) {
        return player.getOwnBoard()
                .getShips()
                .stream()
                .filter(this::isUntouched)
                .count();
    }

    private boolean isUntouched(Ship ship) {
        return ship.getHits().isEmpty();
    }

    private void writeLines(List<String> lines) {
        try {
            Path parent = reportPath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(
                    reportPath,
                    lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Не удалось записать отчёт по игре",
                    e
            );
        }
    }
}
package ru.senla.seabattle.console.printer;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.board.model.ShotResult;
import ru.senla.seabattle.player.Player;

public class ConsoleOutput {

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printPlayerNamePrompt() {
        System.out.print("Введите ваше имя: ");
    }

    public void printBotDifficultyMenu() {
        System.out.println();
        System.out.println("Выберите сложность бота:");
        System.out.println("1 — лёгкий");
        System.out.println("2 — нормальный");
        System.out.println("3 — сложный");
        System.out.println("4 — эксперт");
        System.out.print("Ваш выбор: ");
    }

    public void printFleetPlacementMenu() {
        System.out.println();
        System.out.println("Выберите способ расстановки кораблей:");
        System.out.println("1 — случайная расстановка");
        System.out.println("2 — ручная расстановка");
        System.out.print("Ваш выбор: ");
    }

    public void printInvalidMenuOption() {
        System.out.println("Некорректный пункт меню. Попробуйте ещё раз");
    }

    public void printRandomFleetPlacementStart() {
        System.out.println();
        System.out.println("Выполняется случайная расстановка флота");
    }

    public void printManualPlacementStart() {
        System.out.println();
        System.out.println("Ручная расстановка флота");
    }

    public void printShipPlacementRequest(int shipSize) {
        System.out.println();
        System.out.println("Поставьте корабль размером " + shipSize);
    }

    public void printCoordinatePrompt() {
        System.out.print("Введите начальную координату (например, D1): ");
    }

    public void printOrientationPrompt() {
        System.out.print("Введите ориентацию корабля (H — горизонтально, V — вертикально): ");
    }

    public void printShipPlacedSuccessfully() {
        System.out.println("Корабль успешно поставлен");
    }

    public void printTurn(Player player) {
        System.out.println();
        System.out.println("Ход игрока " + player.getName());
    }

    public void printShotCoordinate(Coordinate coordinate) {
        System.out.println("Координата выстрела: " + coordinate);
    }

    public void printShotResult(ShotResult result) {
        System.out.println("Результат выстрела: " + formatShotResult(result));
    }

    public void printWinner(Player player) {
        System.out.println();
        System.out.println("Игра окончена!");
        System.out.println("Победитель: " + player.getName());
    }

    public void printStatistics(Player firstPlayer,
                                Player secondPlayer) {
        printPlayerStatistics(firstPlayer);
        printPlayerStatistics(secondPlayer);
    }

    private String formatShotResult(ShotResult result) {
        return switch (result) {
            case MISS -> "Мимо";
            case HIT -> "Ранил";
            case KILL -> "Убил";
        };
    }

    private void printPlayerStatistics(Player player) {
        System.out.println();
        System.out.println("Статистика игрока: " + player.getName());
        System.out.println("Количество выстрелов: " + player.getShotCount());
        System.out.println("Количество попаданий: " + player.getHitsCount());
        System.out.println("Точность: " + player.getAccuracy() + "%");
    }
}

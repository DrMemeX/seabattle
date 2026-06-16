package ru.senla.seabattle.player;

import ru.senla.seabattle.board.model.Coordinate;
import ru.senla.seabattle.bot.BotStrategy;

public class BotPlayer extends AbstractPlayer {

    private final BotStrategy botStrategy;

    public BotPlayer(String name, BotStrategy botStrategy) {
        super(name);
        this.botStrategy = botStrategy;
    }

    @Override
    public Coordinate chooseShot() {
        return botStrategy.chooseShot(getEnemyViewBoard());
    }
}

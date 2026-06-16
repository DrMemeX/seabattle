package ru.senla.seabattle.rules;

public final class FleetRules {

    private static final int[] SHIP_SIZES = {
            6,
            5, 5,
            4, 4, 4,
            3, 3, 3, 3,
            2, 2, 2, 2, 2,
            1, 1, 1, 1, 1, 1
    };

    private FleetRules() {
    }

    public static int[] getShipSizes() {
        return SHIP_SIZES.clone();
    }

    public static int getMaxShipSize() {
        return SHIP_SIZES[0];
    }
}
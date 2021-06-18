package com.random.captureTheFlag.player;

public enum Round {
    ONE(1), TWO(2), THREE(3), FOUR(4);

    private final int id;

    Round(int id) {
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public static Round getById(int id) {
        if (id == 1) return Round.ONE;
        if (id == 2) return Round.TWO;
        return Round.THREE;

    }


}

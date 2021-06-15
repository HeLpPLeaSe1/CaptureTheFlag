package com.random.captureTheFlag.player;

public enum Round {
    ONE(1), TWO(2), THREE(3);

    private final int id;

    Round(int id) {
        this.id = id;

    }

    public int getId() {
        return id;
    }
}

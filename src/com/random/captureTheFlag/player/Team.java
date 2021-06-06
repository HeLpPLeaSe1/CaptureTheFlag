package com.random.captureTheFlag.player;

import org.bukkit.Color;
import org.bukkit.Location;

public enum Team {

    RED('c', false, '9', "blue", Color.RED), BLUE('9', false, 'c', "red", Color.BLUE), SPECTATOR(' ', true, ' ', "", Color.GRAY), SPECTATOR_RED(' ', true, ' ', "", Color.GRAY), SPECTATOR_BLUE(' ', true, ' ', "", Color.GRAY);

    private Location spawn;
    private final char colorCode;
    private final boolean spectator;
    private final char opposingColor;
    private String opposingTeam;
    private Color color;

    Team(char colorCode, boolean spectator, char opposingColor, String opposingTeam, Color color) {
        this.colorCode = colorCode;
        this.spectator = spectator;
        this.opposingColor = opposingColor;
        this.opposingTeam = opposingTeam;
        this.color = color;
    }

    public char getColorCode() {
        return colorCode;
    }

    public boolean isSpectator() {
        return spectator;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return spawn;
    }

    public boolean getSpectator() {return spectator;}

    public char getOpposingColor() {
        return opposingColor;
    }

    public String getOpposingTeam() {
        return opposingTeam;
    }

    public Color getColor() {
        return color;
    }
}

package net.unaussprechlich.warlordsplus;


import com.google.gson.JsonObject;

public class PlayerStats {
    private int sr;
    private double kd;
    private double kda;
    private double wl;
    private int gamesPlayed;
    private int dhp;

    public PlayerStats(JsonObject playerInfo) {
        sr = 0;
    }

    public int getSr() {
        return sr;
    }

    public double getKd() {
        return kd;
    }

    public double getKda() {
        return kda;
    }

    public double getWl() {
        return wl;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getDhp() {
        return dhp;
    }
}

package net.unaussprechlich.warlordsplus;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ScoreboardManager Created by Alexander on 03.05.2020.
 * Description:
 **/
public class ScoreboardManager {

    private static ScoreboardManager instance;

    private String scoreboardTitle = "";
    private ArrayList<String> scoreboardNames = new ArrayList<>();


    private ScoreboardManager(){

    }

    public static ScoreboardManager INSTANCE() {
        if(instance == null){
            instance = new ScoreboardManager();
        }
        return instance;
    }

    public ArrayList<String> getScoreboardNames() {
        return scoreboardNames;
    }

    public String getScoreboardTitle() {
        return scoreboardTitle;
    }

    private int tick = 0;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        updateScoreboard();
    }

    private void updateScoreboard() {
        if (!scoreboardNames.isEmpty())
            scoreboardNames.clear();

        scoreboardTitle = "";

        try {
            Scoreboard scoreboard = FMLClientHandler.instance().getClient().theWorld.getScoreboard();
            ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);

            scoreboardTitle = sidebarObjective.getDisplayName();
            Collection<Score> scores = scoreboard.getSortedScores(sidebarObjective);

            for (Score score : scores) {
                ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                scoreboardNames.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
            }

        } catch (Exception e) {
            //Ignore
        }
    }
}

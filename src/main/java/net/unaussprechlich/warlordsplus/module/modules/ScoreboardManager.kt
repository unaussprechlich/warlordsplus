package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.unaussprechlich.warlordsplus.module.IModule
import java.util.*

/**
 * ScoreboardManager Created by Alexander on 03.05.2020.
 * Description:
 */
object ScoreboardManager : IModule{
    var scoreboardTitle = ""
        private set

    val scoreboardNames = ArrayList<String>()

    @SubscribeEvent
    fun onClientTick(event: ClientTickEvent?) {
        if (scoreboardNames.isNotEmpty()) scoreboardNames.clear()
        scoreboardTitle = ""
        try {
            val scoreboard = Minecraft.getMinecraft().theWorld.scoreboard
            val sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1)
            scoreboardTitle = sidebarObjective.displayName
            val scores = scoreboard.getSortedScores(sidebarObjective)
            for (score in scores) {
                val team = scoreboard.getPlayersTeam(score.playerName)
                scoreboardNames.add(ScorePlayerTeam.formatPlayerName(team, score.playerName))
            }
        } catch (e: Exception) {
            //Ignore
        }
    }
}
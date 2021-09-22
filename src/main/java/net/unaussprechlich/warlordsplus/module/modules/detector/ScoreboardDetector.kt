package net.unaussprechlich.warlordsplus.module.modules.detector

import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.util.*

/**
 * ScoreboardManager Created by Alexander on 03.05.2020.
 * Description:
 */
object ScoreboardDetector : IModule{
    var scoreboardTitle = ""
        private set

    val scoreboardFormatted = ArrayList<String>()
    val scoreboard = ArrayList<String>()

    @SubscribeEvent
    fun onClientTick(@Suppress("UNUSED_PARAMETER") event: ClientTickEvent) {
        if (scoreboardFormatted.isNotEmpty() || scoreboard.isNotEmpty()) {
            scoreboardFormatted.clear()
            scoreboard.clear()
        }

        scoreboardTitle = ""

        try {
            val scoreboard = Minecraft.getMinecraft().theWorld.scoreboard
            val sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1)

            scoreboardTitle = sidebarObjective.displayName

            for (score in scoreboard.getSortedScores(sidebarObjective)) {
                val team = scoreboard.getPlayersTeam(score.playerName)
                val value = ScorePlayerTeam.formatPlayerName(team, score.playerName)
                scoreboardFormatted.add(value)
                ScoreboardDetector.scoreboard.add(value.removeFormatting())
            }

        } catch (e: Exception) {
            //Ignore
        }
    }
}
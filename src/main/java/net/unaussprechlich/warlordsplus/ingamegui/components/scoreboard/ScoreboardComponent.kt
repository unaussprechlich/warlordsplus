package net.unaussprechlich.warlordsplus.ingamegui.components.scoreboard

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.Players
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager
import net.unaussprechlich.warlordsplus.util.TeamEnum
import org.lwjgl.util.Color


object ScoreboardComponent : AbstractRenderComponent() {

    var team = TeamEnum.NONE


    override fun render(e: RenderGameOverlayEvent.Pre) {
        try {
            renderPlayerlist()
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    fun renderPlayerlist() {

        if (thePlayer == null) return

        if (Minecraft.getMinecraft().thePlayer.displayName.formattedText.contains("\u00A7c")) {
            team = TeamEnum.RED
        } else if (Minecraft.getMinecraft().thePlayer.displayName.formattedText.contains("\u00A79")) {
            team = TeamEnum.BLUE
        } else {
            team = TeamEnum.NONE
        }

        val players = Players.getPlayersForNetworkPlayers(thePlayer!!.sendQueue.playerInfoMap)

        val teamBlue = players.filter { it.team == TeamEnum.BLUE }.sortedByDescending { it.level }
        val teamRed = players.filter { it.team == TeamEnum.RED }.sortedByDescending { it.level }

        val mostDeathsRed = teamRed.map { it.deaths }.sorted().reversed()[0]
        val mostDeathsBlue = teamBlue.map { it.deaths }.sorted().reversed()[0]

        val mostKillsRed = teamRed.map { it.kills }.sorted().reversed()[0]
        val mostKillsBlue = teamBlue.map { it.kills }.sorted().reversed()[0]

        val spacing = 40

        val w = 400

        val yStart = 20
        val xStart = xCenter - (w / 2)
        val w2 = xCenter - 10

        val xLevel = xStart + 2
        val xKills = w2
        val xDeaths = w2 + spacing

        val xDone = w2 + spacing * 2
        val xReceived = w2 + spacing * 3


        drawRect(xStart, yStart, w, 13, Color(34, 34, 39, 200))
        drawString(xLevel, yStart + 3, "Class Lvl  Name", false)
        drawString(xKills - spacing / 5, yStart + 3, "Kills", false)
        drawString(xDeaths - spacing / 4, yStart + 3, "Deaths", false)
        drawString(xDone - spacing / 5, yStart + 3, "Given", false)
        drawString(xReceived - spacing / 3, yStart + 3, "Received", false)

        var offset = 0

        drawRect(xStart, yStart + 15, w, 12 * teamBlue.size, Color(34, 34, 39, 100))
        for (p in teamBlue) {

            drawString(
                    xLevel, yStart + 17 + offset,
                    "${EnumChatFormatting.GOLD}${EnumChatFormatting.BOLD}" +
                            "${p.warlord.shortName + EnumChatFormatting.RESET}  ${if (p.prestiged) EnumChatFormatting.GOLD else ""}" +
                            "Lv${if (p.level < 10) "0${p.level}" else p.level}  " +
                            "${p.team.color}${p.name} "
            )
            drawString(
                    xKills, yStart + 17 + offset,
                    "${if (p.kills == mostKillsBlue) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}${p.kills}"
            )

            drawString(
                    xDeaths, yStart + 17 + offset,
                    "${if (p.deaths == mostDeathsBlue) EnumChatFormatting.DARK_RED else EnumChatFormatting.RESET}${p.deaths}"
            )

            //if player is on blue display only healing
            if (team == TeamEnum.BLUE) {
                drawString(
                        xDone, yStart + 17 + offset,
                        "${EnumChatFormatting.GREEN}${p.healingDone}"
                )

                drawString(
                        xReceived, yStart + 17 + offset,
                        "${EnumChatFormatting.DARK_GREEN}${p.healingReceived}"
                )
            }

            //if player is on red display only dmg
            else if (team == TeamEnum.RED) {
                drawString(
                        xDone, yStart + 17 + offset,
                        "${EnumChatFormatting.RED}${p.damageDone}"
                )
                drawString(
                        xReceived, yStart + 17 + offset,
                        "${EnumChatFormatting.DARK_RED}${p.damageReceived}"
                )
            }
            offset += 12
        }

        offset += 1

        drawRect(xStart, yStart + 15 + offset, w, 12 * teamBlue.size, Color(34, 34, 39, 100))

        for (p in teamRed) {

            drawString(
                    xLevel, yStart + 17 + offset,
                    "${EnumChatFormatting.GOLD}${EnumChatFormatting.BOLD}" +
                            "${p.warlord.shortName + EnumChatFormatting.RESET}  ${if (p.prestiged) EnumChatFormatting.GOLD else ""}" +
                            "Lv${if (p.level < 10) "0${p.level}" else p.level}  " +
                            "${p.team.color}${p.name} "
            )
            drawString(
                    xKills, yStart + 17 + offset,
                    "${if (p.kills == mostKillsRed) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}${p.kills}"
            )
            drawString(
                    xDeaths, yStart + 17 + offset,
                    "${if (p.deaths == mostDeathsRed) EnumChatFormatting.DARK_RED else EnumChatFormatting.RESET}${p.deaths}"
            )

            //if player is on red display only healing
            if (team == TeamEnum.RED) {
                drawString(
                        xDone, yStart + 17 + offset,
                        "${EnumChatFormatting.GREEN}${p.healingDone}"
                )
                drawString(
                        xReceived, yStart + 17 + offset,
                        "${EnumChatFormatting.DARK_GREEN}${p.healingReceived}"
                )
            }

            //if player is on blue display only dmg
            else if (team == TeamEnum.BLUE) {
                drawString(
                        xDone, yStart + 17 + offset,
                        "${EnumChatFormatting.RED}${p.damageDone}"
                )
                drawString(
                        xReceived, yStart + 17 + offset,
                        "${EnumChatFormatting.DARK_RED}${p.damageReceived}"
                )
            }
            offset += 12
        }
    }
}
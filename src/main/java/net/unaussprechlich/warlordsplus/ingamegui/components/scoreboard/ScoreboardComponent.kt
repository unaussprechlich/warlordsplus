package net.unaussprechlich.warlordsplus.ingamegui.components.scoreboard

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.util.TeamEnum


object ScoreboardComponent : AbstractRenderComponent() {

    override fun render(e: RenderGameOverlayEvent.Pre) {
        try {
            renderPlayerlist()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun renderPlayerlist() {

        if (thePlayer == null) return

        val players = OtherPlayers.getPlayersForNetworkPlayers(thePlayer!!.sendQueue.playerInfoMap)

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


        drawHeaderRect(xStart, yStart, w, 13)
        drawString(xLevel, yStart + 3, "Class Lvl   Name", false)
        drawString(xKills - spacing / 5, yStart + 3, "Kills", false)
        drawString(xDeaths - spacing / 4, yStart + 3, "Deaths", false)
        drawString(xDone - spacing / 5, yStart + 3, "Given", false)
        drawString(xReceived - spacing / 3, yStart + 3, "Received", false)

        var offset = 0

        drawBackgroundRect(xStart, yStart + 14, w, 10 * teamBlue.size)
        for (p in teamBlue) {


            drawString(
                    xLevel, yStart + 15 + offset,
                "${EnumChatFormatting.GOLD}${EnumChatFormatting.BOLD}" +
                        "${p.warlord.shortName + EnumChatFormatting.RESET}  ${if (p.prestiged) EnumChatFormatting.GOLD else ""}" +
                        "Lv${if (p.level < 10) "0${p.level}" else p.level}  " + (if (p.hasFlag) "${EnumChatFormatting.RED}F-" else "") +
                        "${p.team.color}${p.name} "
            )
            drawString(
                    xKills, yStart + 15 + offset,
                    "${if (p.kills == mostKillsBlue) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}${p.kills}"
            )

            drawString(
                    xDeaths, yStart + 15 + offset,
                    "${if (p.deaths == mostDeathsBlue) EnumChatFormatting.DARK_RED else EnumChatFormatting.RESET}${p.deaths}"
            )

            //if player is on blue display only healing
            if (ThePlayer.team == TeamEnum.BLUE) {
                drawString(
                        xDone, yStart + 15 + offset,
                        "${EnumChatFormatting.GREEN}${p.healingDone}"
                )

                drawString(
                        xReceived, yStart + 15 + offset,
                        "${EnumChatFormatting.DARK_GREEN}${p.healingReceived}"
                )
            }

            //if player is on red display only dmg
            else if (ThePlayer.team == TeamEnum.RED) {
                drawString(
                        xDone, yStart + 15 + offset,
                        "${EnumChatFormatting.RED}${p.damageDone}"
                )
                drawString(
                        xReceived, yStart + 15 + offset,
                        "${EnumChatFormatting.DARK_RED}${p.damageReceived}"
                )
            }
            offset += 10
        }

        offset += 1

        drawBackgroundRect(xStart, yStart + 14 + offset, w, 10 * teamBlue.size)

        for (p in teamRed) {

            drawString(
                    xLevel, yStart + 15 + offset,
                "${EnumChatFormatting.GOLD}${EnumChatFormatting.BOLD}" +
                        "${p.warlord.shortName + EnumChatFormatting.RESET}  ${if (p.prestiged) EnumChatFormatting.GOLD else ""}" +
                        "Lv${if (p.level < 10) "0${p.level}" else p.level}  " + (if (p.hasFlag) "${EnumChatFormatting.BLUE}F-" else "") +
                        "${p.team.color}${p.name} "
            )
            drawString(
                    xKills, yStart + 15 + offset,
                    "${if (p.kills == mostKillsRed) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}${p.kills}"
            )
            drawString(
                    xDeaths, yStart + 15 + offset,
                    "${if (p.deaths == mostDeathsRed) EnumChatFormatting.DARK_RED else EnumChatFormatting.RESET}${p.deaths}"
            )

            //if player is on red display only healing
            if (ThePlayer.team == TeamEnum.RED) {
                drawString(
                        xDone, yStart + 15 + offset,
                        "${EnumChatFormatting.GREEN}${p.healingDone}"
                )
                drawString(
                        xReceived, yStart + 15 + offset,
                        "${EnumChatFormatting.DARK_GREEN}${p.healingReceived}"
                )
            }

            //if player is on blue display only dmg
            else if (ThePlayer.team == TeamEnum.BLUE) {
                drawString(
                        xDone, yStart + 15 + offset,
                        "${EnumChatFormatting.RED}${p.damageDone}"
                )
                drawString(
                        xReceived, yStart + 15 + offset,
                        "${EnumChatFormatting.DARK_RED}${p.damageReceived}"
                )
            }
            offset += 10
        }
    }
}
package net.unaussprechlich.warlordsplus.ingamegui.components.scoreboard

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.Player
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

        var offset = 0
        val w = 443

        val yStart = 25
        val xStart = xCenter - (w / 2)

        val xLevel = xStart + 2
        val xName = xLevel + 53
        val xKills = xName + 100
        val xDeaths = xKills + 30
        val xDone = xDeaths + 40
        val xReceived = xDone + 60
        val xKilled = xReceived + 60

        GlStateManager.scale(.9, .9, 1.0)
        drawHeaderRect(xStart, yStart, w, 13)
        drawString(xName, yStart + 3, "Name", false)
        drawString(xKills, yStart + 3, "Kills", false)
        drawString(xDeaths, yStart + 3, "Deaths", false)
        drawString(xDone, yStart + 3, "Given", false)
        drawString(xReceived, yStart + 3, "Received", false)
        drawString(xKilled, yStart + 3, "DiedToYou/StoleKill", false)

        drawBackgroundRect(xStart, yStart + 14, w, 10 * teamBlue.size)

        val renderLine = fun(p: Player) {

            fun hasMostKills(): Boolean {
                return if (p.team == TeamEnum.BLUE)
                    p.kills == mostKillsBlue
                else
                    p.kills == mostKillsRed
            }

            fun hasMostDeaths(): Boolean {
                return if (p.team == TeamEnum.BLUE)
                    p.deaths == mostDeathsBlue
                else
                    p.deaths == mostDeathsRed
            }

            var output = ""
            fun drawFlag(): String {
                if (p.hasFlag) {
                    if (p.team == TeamEnum.BLUE)
                        EnumChatFormatting.RED
                    else
                        EnumChatFormatting.BLUE
                    output += "F-"
                }
                return output
            }
            drawString(
                xLevel, yStart + 15 + offset,
                "${EnumChatFormatting.GOLD}" +
                        "${p.warlord.shortName + EnumChatFormatting.RESET} ${if (p.prestiged) EnumChatFormatting.GOLD else ""}" +
                        "Lv${if (p.level < 10) "0${p.level}" else p.level}"
            )
            drawString(
                xName, yStart + 15 + offset,
                "${drawFlag()}${p.team.color}${p.name}"
            )
            drawString(
                xKills, yStart + 15 + offset,
                "${if (hasMostKills()) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}${p.kills}"
            )

            drawString(
                xDeaths, yStart + 15 + offset,
                "${if (hasMostDeaths()) EnumChatFormatting.DARK_RED else EnumChatFormatting.RESET}${p.deaths}"
            )

            if (ThePlayer.team == p.team) {
                drawString(
                    xDone, yStart + 15 + offset,
                    "${EnumChatFormatting.GREEN}${p.healingReceived}"
                )

                drawString(
                    xReceived, yStart + 15 + offset,
                    "${EnumChatFormatting.DARK_GREEN}${p.healingDone}"
                )
                drawString(
                    xKilled, yStart + 15 + offset,
                    "${EnumChatFormatting.RESET}${p.stoleKill}"
                )
            } else {
                drawString(
                    xDone, yStart + 15 + offset,
                    "${EnumChatFormatting.RED}${p.damageReceived}"
                )
                drawString(
                    xReceived, yStart + 15 + offset,
                    "${EnumChatFormatting.DARK_RED}${p.damageDone}"
                )
                drawString(
                    xKilled, yStart + 15 + offset,
                    "${EnumChatFormatting.RESET}${p.died}"
                )
            }

            offset += 10
        }
        teamBlue.forEach(renderLine)

        offset += 1
        drawBackgroundRect(xStart, yStart + 14 + offset, w, 10 * teamRed.size)

        teamRed.forEach(renderLine)
    }
}
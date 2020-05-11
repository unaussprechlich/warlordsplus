package net.unaussprechlich.warlordsplus.ingamegui.components.scoreboard

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.Player
import net.unaussprechlich.warlordsplus.Players
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.util.TeamEnum
import org.lwjgl.util.Color
import java.util.*


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


        var mostDeathsRed = 0
        var mostDeathsBlue = 0
        var mostKillsRed = 0
        var mostKillsBlue = 0

        val spacing = 40

        val w = 400

        val yStart = 20
        val xStart = xCenter - (w / 2)
        val w2 = xCenter - 10

        val xLevel = xStart + 2
        val xKills = w2
        val xDeaths = w2 + spacing
        //val xRatio = w2 + spacing/3
        val xDone = w2 + spacing * 2
        val xReceived = w2 + spacing * 3
        //val xHealingDone = w2 + spacing * 2
        //val xHealingReceived = w2 + spacing * 3
        //val xDamageDone =w2 + spacing * 4
        //val xDamageReceived = w2 + spacing * 5

        drawRect(xStart, yStart, w, 13, Color(34, 34, 39, 200))
        //drawCenteredString(xStart, 30, w, "Scoreboard")
        drawString(xLevel, yStart + 3, "Class Lvl  Name", false)
        drawString(xKills - spacing / 5, yStart + 3, "Kills", false)
        drawString(xDeaths - spacing / 4, yStart + 3, "Deaths", false)
        drawString(xDone - spacing / 5, yStart + 3, "Given", false)
        drawString(xReceived - spacing / 3, yStart + 3, "Received", false)


        var offset = 0



        drawRect(xStart, yStart + 15, w, 12 * teamBlue.size, Color(34, 34, 39, 100))
        for (p in teamBlue) {

            if (p.deaths > mostDeathsBlue) {
                mostDeathsBlue = p.deaths
            }
            if (p.kills > mostKillsBlue) {
                mostKillsBlue = p.kills
            }

            drawString(
                xLevel, yStart + 17 + offset,
                "${EnumChatFormatting.GOLD}${EnumChatFormatting.BOLD}" +
                        "${p.warlord.shortName + EnumChatFormatting.RESET}  ${if (p.prestiged) EnumChatFormatting.GOLD else ""}Lv${if (p.level < 10) "0${p.level}" else p.level}  " +
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

            if (p.deaths > mostDeathsRed) {
                mostDeathsRed = p.deaths
            }
            if (p.kills > mostKillsRed) {
                mostKillsRed = p.kills
            }

            drawString(
                xLevel, yStart + 17 + offset,
                "${EnumChatFormatting.GOLD}${EnumChatFormatting.BOLD}" +
                        "${p.warlord.shortName + EnumChatFormatting.RESET}  ${if (p.prestiged) EnumChatFormatting.GOLD else ""}Lv${if (p.level < 10) "0${p.level}" else p.level}  " +
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

        /*
        if (ScoreboardManager.scoreboardNames[11].contains("1000") || ScoreboardManager.scoreboardNames[12].contains("1000")) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("MOST DEATHS IN BLUE: " + mostDeathsBluePlayer.name + "-" + mostDeathsBlue)
            Minecraft.getMinecraft().thePlayer.sendChatMessage("MOST KILLS IN BLUE: " + mostKillsBluePlayer.name + "-" + mostKillsBlue)
            Minecraft.getMinecraft().thePlayer.sendChatMessage("MOST DEATHS IN RED: " + mostDeathsRedPlayer.name + "-" + mostDeathsRed)
            Minecraft.getMinecraft().thePlayer.sendChatMessage("MOST KILLS IN RED: " + mostKillsRedPlayer.name + "-" + mostKillsRed)
        }

         */

        /*

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

        if (k4 < players.size) {
            val networkplayerinfo1 = players[k4] as NetworkPlayerInfo

            if (flag) {
                mc.textureManager.bindTexture(networkplayerinfo1.locationSkin)
                drawScaledCustomSizeModalRect(j2, k2, 8.0f, 8f, 8, 8, 8, 8, 64.0f, 64.0f)
                j2 += 9
            }

            val text = if (networkplayerinfo1.displayName != null) networkplayerinfo1.displayName.formattedText
            else getPlayerName(networkplayerinfo1)

            this.mc.fontRendererObj.drawStringWithShadow(text, j2.toFloat(), k2.toFloat(), -1)
        }*/




    }


}
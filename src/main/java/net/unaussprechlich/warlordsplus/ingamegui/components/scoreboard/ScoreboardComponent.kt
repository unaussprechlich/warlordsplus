package net.unaussprechlich.warlordsplus.ingamegui.components.scoreboard

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.config.ConfigPropertyInt
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.TeamEnum

object ScoreboardComponent : AbstractRenderComponent(RenderGameOverlayEvent.ElementType.PLAYER_LIST, true) {

    @ConfigPropertyBoolean(
        category = CCategory.SCOREBOARD,
        id = "showNewScoreboard",
        comment = "Enable or disable the new scoreboard",
        def = true
    )
    var showNewScoreboard = false

    @ConfigPropertyInt(
        category = CCategory.SCOREBOARD,
        id = "setScaleDOM",
        comment = "Change the scale of the scoreboard in DOM, so all players will fit on your screen ( 90 = 90% )",
        def = 100
    )
    var setScaleDOM = 100

    @ConfigPropertyInt(
        category = CCategory.SCOREBOARD,
        id = "setScaleCTF/TDM",
        comment = "Change the scale of the scoreboard in CTF/TDM, so all players will fit on your screen ( 90 = 90% )",
        def = 100
    )
    var setScaleCTFTDM = 100

    override fun onRender(event: RenderGameOverlayEvent.Pre) {
        try {
            if (showNewScoreboard)
                renderPlayerList()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun renderPlayerList() {

        if (thePlayer == null) return

        val players = OtherPlayers.getPlayersForNetworkPlayers(thePlayer!!.sendQueue.playerInfoMap)

        val teamBlue = players.filter { it.team == TeamEnum.BLUE }.sortedByDescending { it.level }
        val teamRed = players.filter { it.team == TeamEnum.RED }.sortedByDescending { it.level }


        val mostDeathsRed = if (teamRed.isEmpty()) 0 else teamRed.map { it.deaths }.sorted().reversed()[0]
        val mostDeathsBlue = if (teamBlue.isEmpty()) 0 else teamBlue.map { it.deaths }.sorted().reversed()[0]
        val mostKillsRed = if (teamRed.isEmpty()) 0 else teamRed.map { it.kills }.sorted().reversed()[0]
        val mostKillsBlue = if (teamBlue.isEmpty()) 0 else teamBlue.map { it.kills }.sorted().reversed()[0]

        val w = 443

        val yStart = 25
        var xStart = xCenter - (w / 2)

        if (GameStateManager.isCTF || GameStateManager.isTDM) {
            GlStateManager.scale(setScaleCTFTDM.toDouble() / 100, setScaleCTFTDM.toDouble() / 100, 1.0)
            xStart = (xCenter + 50 - setScaleCTFTDM / 2 - ((w * setScaleCTFTDM.toDouble() / 100 / 2)).toInt())
        } else if (GameStateManager.isDOM) {
            GlStateManager.scale(setScaleDOM.toDouble() / 100, setScaleDOM.toDouble() / 100, 1.0)
            xStart = (xCenter + 50 - setScaleDOM / 2 - ((w * setScaleDOM.toDouble() / 100 / 2)).toInt())
        }

        val xLevel = 2.0
        val xName = 53.0
        val xKills = 100.0
        val xDeaths = 30.0
        val xDone = 40.0
        val xReceived = 60.0
        val xKilled = 60.0

        translate(xStart, yStart)

        renderRect(w, 13, Colors.DEF)

        glMatrix {
            translateY(-3)
            translateX(xLevel + xName)
            "Name".draw()
            translateX(xKills)
            "Kills".draw()
            translateX(xDeaths)
            "Deaths".draw()
            translateX(xDone)
            "Given".draw()
            translateX(xReceived)
            "Received".draw()
            translateX(xKilled)
            "DiedToYou/StoleKill".draw()
        }

        fun renderLine(p: net.unaussprechlich.warlordsplus.Player) {

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

            fun drawFlag(): String {
                if (p.hasFlag) {
                    return if (p.team == TeamEnum.BLUE)
                        "${EnumChatFormatting.RED}\u2690 "
                    else
                        "${EnumChatFormatting.BLUE}\u2690 "
                }
                return ""
            }

            fun isPrestige(): String {
                return if (p.prestiged)
                    EnumChatFormatting.GOLD.toString()
                else ""
            }

            fun level(level: Int): String {
                return if (level < 10)
                    "0${level}"
                else level.toString()
            }

            glMatrix {
                translateX(xLevel)
                "${if (p.left) "LEFT" else ""}${EnumChatFormatting.GOLD}${p.warlord.shortName}${EnumChatFormatting.RESET} ${isPrestige()}${
                    level(
                        p.level
                    )
                } ${if (p.name == Minecraft.getMinecraft().thePlayer.displayNameString) ThePlayer.spec.icon else p.spec.icon}".draw()
                translateX(xName)
                "${drawFlag()}${if (p.isDead) "${EnumChatFormatting.GRAY}${p.respawn} " else p.team.color.toString()}${p.name}".draw()
                translateX(xKills)
                "${if (hasMostKills()) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}${p.kills}".draw()
                translateX(xDeaths)
                "${if (hasMostDeaths()) EnumChatFormatting.DARK_RED else EnumChatFormatting.RESET}${p.deaths}".draw()
                if (ThePlayer.team == p.team) {
                    translateX(xDone)
                    "${EnumChatFormatting.GREEN}${p.healingReceived}".draw()
                    translateX(xReceived)
                    "${EnumChatFormatting.DARK_GREEN}${p.healingDone}".draw()
                    translateX(xKilled)
                    "${EnumChatFormatting.RESET}${p.stoleKill}".draw()
                } else {
                    translateX(xDone)
                    "${EnumChatFormatting.RED}${p.damageReceived}".draw()
                    translateX(xReceived)
                    "${EnumChatFormatting.DARK_RED}${p.damageDone}".draw()
                    translateX(xKilled)
                    "${EnumChatFormatting.RESET}${p.died}".draw()
                }
            }

            translateY(-10)
        }

        translateY(-14)
        renderRect(w, 10 * teamBlue.size + 1, Colors.DEF, 100)
        translateY(-2)
        teamBlue.forEach(::renderLine)

        translateY(-1)
        renderRect(w, 10 * teamRed.size + 1, Colors.DEF, 100)
        translateY(-2)
        teamRed.forEach(::renderLine)
    }
}
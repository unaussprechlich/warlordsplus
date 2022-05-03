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
import net.unaussprechlich.warlordsplus.config.ConfigPropertyString
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.TeamEnum

object ScoreboardComponent : AbstractRenderComponent(RenderGameOverlayEvent.ElementType.PLAYER_LIST, false) {

    override fun onRender(event: RenderGameOverlayEvent.Pre) {
        try {
            if (showNewScoreboard) {
                renderPlayerList()
                event.isCanceled = true
            }
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

        var w = 443

        if (!showDoneAndReceived) {
            showDiedToYouStoleKill = false
        }

        if (!showDiedToYouStoleKill) {
            w -= 133
        } else if (!showTopHeader) {
            w -= 100
        }
        if (!showDoneAndReceived) {
            w -= 105
        }

        val yStart = 25
        var xStart = xCenter - (w / 2)



        if (GameStateManager.isCTF || GameStateManager.isTDM) {
            GlStateManager.scale(setScaleCTFTDM.toDouble(), setScaleCTFTDM.toDouble(), 1.0)
            xStart =
                (xCenter + 50 - (setScaleCTFTDM.toDouble() * 100).toInt() / 2 - ((w * (setScaleCTFTDM.toDouble() * 100).toInt() / 100 / 2)).toInt())
        } else if (GameStateManager.isDOM) {
            GlStateManager.scale(setScaleDOM.toDouble(), setScaleDOM.toDouble(), 1.0)
            xStart =
                (xCenter + 50 - (setScaleDOM.toDouble() * 100).toInt() / 2 - ((w * (setScaleDOM.toDouble() * 100).toInt() / 100 / 2)).toInt())
        }

        xStart += moveScoreboard
        if (splitScoreBoard) {
            xStart -= 60
            if (showDoneAndReceived) {
                xStart -= 65
            }
        }

        var xLevel = 2.0
        var xName = 53.0
        var xKills = 100.0
        var xDeaths = 30.0
        var xDone = 40.0
        var xReceived = 50.0
        var xKilled = 60.0

        if (!showTopHeader) {
            xDeaths = 25.0
            xDone = 30.0
        }

        if (showTopHeader) {
            translate(xStart, yStart)
            renderRect(w, 13, Colors.DEF)
            glMatrix {
                translateY(-3)
                translateX(xLevel + xName)
                "Name".draw()
                if (!showDoneAndReceived && !showDiedToYouStoleKill) {
                    translateX(xKills)
                    "K".draw()
                    translateX(xDeaths)
                    "D".draw()
                } else {
                    translateX(xKills)
                    "Kills".draw()
                    translateX(xDeaths)
                    "Deaths".draw()
                }
                if (showDoneAndReceived) {
                    translateX(xDone)
                    "Given".draw()
                    if (showTopHeader) {
                        if (showDiedToYouStoleKill)
                            translateX(xReceived)
                        else
                            translateX(xReceived - 12.5)
                        "Received".draw()
                    }
                }
                if (showDiedToYouStoleKill) {
                    translateX(xKilled)
                    "DiedToYou/StoleKill".draw()
                }
            }
            if (splitScoreBoard) {
                glMatrix {
                    translateX(w + 5)
                    renderRect(w, 13, Colors.DEF)
                    translateY(-3)
                    translateX(xLevel + xName)
                    "Name".draw()
                    if (!showDoneAndReceived && !showDiedToYouStoleKill) {
                        translateX(xKills)
                        "K".draw()
                        translateX(xDeaths)
                        "D".draw()
                    } else {
                        translateX(xKills)
                        "Kills".draw()
                        translateX(xDeaths)
                        "Deaths".draw()
                    }
                    if (showDoneAndReceived) {
                        translateX(xDone)
                        "Given".draw()
                        if (showTopHeader) {
                            if (showDiedToYouStoleKill)
                                translateX(xReceived)
                            else
                                translateX(xReceived - 12.5)
                            "Received".draw()
                        }
                    }
                    if (showDiedToYouStoleKill) {
                        translateX(xKilled)
                        "DiedToYou/StoleKill".draw()
                    }
                }
            }
        } else {
            translate(xStart, yStart - 15)
        }

        fun renderLine(index : Int, p: net.unaussprechlich.warlordsplus.Player) {
            if (showOutline) {
                translateY(-2) {
                    renderRect(w.toDouble(), 1.25, Colors.DEF)
                    renderRect(1.25, 11.0, Colors.DEF)
                }
                translate(w - 1.25, -2.0) {
                    renderRect(1.25, 11.0, Colors.DEF)
                }
                translateY(8.75) {
                    renderRect(w.toDouble(), 1.25, Colors.DEF)
                }
            } else {
                if(index % 2 == 1){
                    translateY(-1.2){
                        renderRect(w.toDouble(), 10.75, Colors.DEF, alpha = 40)
                    }
                }
            }

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
                translate(xLevel, .5)
                "${EnumChatFormatting.GOLD}${p.warlord.shortName}${EnumChatFormatting.RESET} ${isPrestige()}${
                    level(
                        p.level
                    )
                } ${if (p.name == Minecraft.getMinecraft().thePlayer.name) ThePlayer.spec.icon else p.spec.icon}".draw()
                translateX(xName)
                "${drawFlag()}${if (p.isDead) "${EnumChatFormatting.GRAY}${if (!GameStateManager.isWarlords2) "${p.respawn} " else ""}" else p.team.color.toString()}${if (p.left) "${EnumChatFormatting.GRAY}${EnumChatFormatting.STRIKETHROUGH}" else ""}${if (p.name == Minecraft.getMinecraft().thePlayer.name) EnumChatFormatting.GREEN else ""}${p.name}".draw()
//                if (p.name == "sumSmash") {
//                    translate(xKills - 50) {
//                        renderImage(9.0, 9.0, ImageRegistry.MEME_WEIRDCHAMP)
//                    }
//                }
                translateX(xKills)
                "${if (hasMostKills()) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}${p.kills}".draw()
                translateX(xDeaths)
                "${if (hasMostDeaths()) EnumChatFormatting.DARK_RED else EnumChatFormatting.RESET}${p.deaths}".draw()
                if (ThePlayer.team == p.team) {
                    if (showDoneAndReceived) {

                        translateX(xDone)
                        "${EnumChatFormatting.GREEN}${p.healingReceived}".draw()
                        translateX(xReceived)
                        "${EnumChatFormatting.DARK_GREEN}${p.healingDone}".draw()
                    }
                    if (showDiedToYouStoleKill) {
                        translateX(xKilled)
                        "${EnumChatFormatting.RESET}${p.stoleKill}".draw()
                    }
                } else {
                    if (showDoneAndReceived) {
                        translateX(xDone)
                        "${EnumChatFormatting.RED}${p.damageReceived}".draw()
                        translateX(xReceived)
                        "${EnumChatFormatting.DARK_RED}${p.damageDone}".draw()
                    }
                    if (showDiedToYouStoleKill) {
                        translateX(xKilled)
                        "${EnumChatFormatting.RESET}${p.died}".draw()
                    }
                }
            }

            translateY(-10.75)
        }

        if (splitScoreBoard) {
            translateY(-14)
            glMatrix {
                renderRect(w.toDouble(), 10.75 * teamBlue.size + 1, Colors.DEF, 100)
                translateY(-2)
                teamBlue.forEachIndexed(::renderLine)
            }
            translateX(w + 5)
            glMatrix {
                renderRect(w.toDouble(), 10.75 * teamRed.size + 1, Colors.DEF, 100)
                translateY(-2)
                teamRed.forEachIndexed(::renderLine)
            }
        } else {
            translateY(-14)
            renderRect(w.toDouble(), 10.75 * teamBlue.size + 1, Colors.DEF, 100)
            translateY(-2)
            teamBlue.forEachIndexed(::renderLine)

            translateY(-1)
            renderRect(w.toDouble(), 10.75 * teamRed.size + 1, Colors.DEF, 100)
            translateY(-2)
            teamRed.forEachIndexed(::renderLine)
        }
    }

    @ConfigPropertyBoolean(
        category = CCategory.SCOREBOARD,
        id = "Scoreboard | Show",
        comment = "Enable or disable the new scoreboard",
        def = true
    )
    var showNewScoreboard = false

    @ConfigPropertyString(
        category = CCategory.SCOREBOARD,
        id = "Scoreboard | Scale DOM",
        comment = "Change the scale of the scoreboard in DOM, so all players will fit on your screen ( 0.9 = 90% )",
        def = "1.0"
    )
    var setScaleDOM = ".8"

    @ConfigPropertyString(
        category = CCategory.SCOREBOARD,
        id = "Scoreboard | Scale CTF/TDM",
        comment = "Change the scale of the scoreboard in CTF/TDM, so all players will fit on your screen ( 0.9 = 90% )",
        def = ".9"
    )
    var setScaleCTFTDM = "1.0"

    @ConfigPropertyInt(
        category = CCategory.SCOREBOARD,
        id = "Scoreboard | Translate Left(-) or Right(+)",
        comment = "Moves the scoreboard left or right depending on the value",
        def = 0
    )
    var moveScoreboard = 0

    @ConfigPropertyBoolean(
        category = CCategory.SCOREBOARD,
        id = "Scoreboard Header | Show",
        comment = "Shows (Kills/Deaths/Received/Given/ etc...)",
        def = true
    )
    var showTopHeader = true

    @ConfigPropertyBoolean(
        category = CCategory.SCOREBOARD,
        id = "Scoreboard | Show Box Outline",
        comment = "Shows box outline around players",
        def = false
    )
    var showOutline = false

    @ConfigPropertyBoolean(
        category = CCategory.SCOREBOARD,
        id = "Scoreboard Header | Show DiedToYou/StoleKill",
        comment = "Shows the amount of time ppl died to you or stole your kill",
        def = false
    )
    var showDiedToYouStoleKill = false

    @ConfigPropertyBoolean(
        category = CCategory.SCOREBOARD,
        id = "Scoreboard Header | Show Done/Received",
        comment = "Shows the amount dmg/heal someone has done or received to you or by you",
        def = true
    )
    var showDoneAndReceived = true

    @ConfigPropertyBoolean(
        category = CCategory.SCOREBOARD,
        id = "Scoreboard | Split",
        comment = "Splits the scoreboard horizontally instead of vertically",
        def = false
    )
    var splitScoreBoard = false
}
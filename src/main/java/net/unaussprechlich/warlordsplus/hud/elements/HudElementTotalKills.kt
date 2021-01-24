package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager.isIngame
import net.unaussprechlich.warlordsplus.module.modules.KillEvent
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import java.util.*
import kotlin.math.abs

object HudElementTotalKills : AbstractHudElement(), IUpdateConsumer, IChatConsumer {

    var blueKills: Int = 0
    var redKills: Int = 0
    private var numberOfCapsBlue: Int = 0
    private var numberOfCapsRed: Int = 0

    init {
        EventBus.register<ResetEvent> {
            redKills = 0
            blueKills = 0
            numberOfCapsRed = 0
            numberOfCapsBlue = 0
        }
        if (GameStateManager.isDOM) {
            EventBus.register<KillEvent> {
                if (OtherPlayers.getPlayerForName(it.player)!!.team == TeamEnum.BLUE)
                    blueKills++
                else if (OtherPlayers.getPlayerForName(it.player)!!.team == TeamEnum.RED)
                    redKills++;
            }
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (!GameStateManager.isDOM) {
            if (showBlueKills)
                renderStrings.add(EnumChatFormatting.BLUE.toString() + "Blue Kills: " + blueKills)
            if (showRedKills)
                renderStrings.add(EnumChatFormatting.RED.toString() + "Red Kills: " + redKills)
        }
        return renderStrings.toTypedArray()
    }

    override fun update() {

        if (GameStateManager.isCTF) {
            val blue = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardNames[12]
                    .replace(" ", "").replace("\uD83D\uDCA3", "")
            )
            val red = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardNames[11]
                    .replace(" ", "").replace("\uD83D\uDC7D", "")
            )
            blueKills =
                abs(blue.substring(blue.indexOf(":") + 1, blue.indexOf("/")).toInt() - (numberOfCapsBlue * 250)) / 5
            redKills = abs(red.substring(red.indexOf(":") + 1, red.indexOf("/")).toInt() - (numberOfCapsRed * 250)) / 5
        } else if (GameStateManager.isTDM) {
            val blue = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardNames[9]
                    .replace(" ", "").replace("\uD83D\uDCA3", "")
            )
            val red = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardNames[8]
                    .replace(" ", "").replace("\uD83D\uDC7D", "")
            )
            blueKills = blue.substring(blue.indexOf(":") + 1, blue.indexOf("/")).toInt() / 15
            redKills = red.substring(red.indexOf(":") + 1, red.indexOf("/")).toInt() / 15
        }

    }

    override fun onChat(e: ClientChatReceivedEvent) {
        if (GameStateManager.isCTF) {
            if (e.message.unformattedText.contains("captured the")) {
                if (e.message.unformattedText.contains("RED")) {
                    numberOfCapsBlue++
                } else {
                    numberOfCapsRed++
                }
            }
        }
    }

    override fun isVisible(): Boolean {
        return isIngame
    }

    override fun isEnabled(): Boolean {
        return true
    }

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "showBlueKills",
        comment = "Enable or disable the Blue Kill counter",
        def = true
    )
    var showBlueKills = false

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "showRedKills",
        comment = "Enable or disable the Red Kill counter",
        def = true
    )
    var showRedKills = false


}
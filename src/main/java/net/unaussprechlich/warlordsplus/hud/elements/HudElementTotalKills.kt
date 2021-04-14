package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager.isIngame
import net.unaussprechlich.warlordsplus.module.modules.KillEvent
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.util.*

object HudElementTotalKills : AbstractHudElement() {

    var blueKills: Int = 0
    var redKills: Int = 0
    private var numberOfCapsBlue: Int = 0
    private var numberOfCapsRed: Int = 0
    var accurateBlueKills = 0
    var accurateRedKills = 0

    init {
        EventBus.register<ResetEvent> {
            redKills = 0
            blueKills = 0
            numberOfCapsRed = 0
            numberOfCapsBlue = 0
        }

        EventBus.register<KillEvent> {
            if (GameStateManager.isCTF || GameStateManager.isDOM) {
                if (OtherPlayers.getPlayerForName(it.player)!!.team == TeamEnum.BLUE)
                    blueKills++
                else if (OtherPlayers.getPlayerForName(it.player)!!.team == TeamEnum.RED)
                    redKills++
            }

        }

        EventBus.register<ClientChatReceivedEvent> {
            if (GameStateManager.isCTF) {
                val message = it.message.unformattedText.removeFormatting()
                if (message.contains("captured the")) {
                    if (message.contains("RED")) {
                        numberOfCapsBlue++
                    } else {
                        numberOfCapsRed++
                    }
                }

            }
            if (GameStateManager.isTDM) {
                blueKills = GameStateManager.bluePoints / 15
                redKills = GameStateManager.redPoints / 15
            }
        }

        EventBus.register<TickEvent.ClientTickEvent> {
            accurateBlueKills = (GameStateManager.bluePoints - numberOfCapsBlue * 250) / 5
            accurateRedKills = (GameStateManager.redPoints - numberOfCapsRed * 250) / 5
            if (accurateBlueKills - blueKills <= 5) {
                blueKills = accurateBlueKills
            }
            if (accurateRedKills - redKills <= 5) {
                redKills = accurateRedKills
            }
        }
    }


    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (showBlueKills)
            renderStrings.add(EnumChatFormatting.BLUE.toString() + "Blue Kills: " + accurateBlueKills)
        if (showRedKills)
            renderStrings.add(EnumChatFormatting.RED.toString() + "Red Kills: " + accurateRedKills)

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return isIngame
    }

    override fun isEnabled(): Boolean {
        return true
    }

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "|| Total Kills Blue | Show",
        comment = "Enable or disable the Blue Kill counter",
        def = true
    )
    var showBlueKills = false

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "|| Total Kills Red | Show",
        comment = "Enable or disable the Red Kill counter",
        def = true
    )
    var showRedKills = false


}
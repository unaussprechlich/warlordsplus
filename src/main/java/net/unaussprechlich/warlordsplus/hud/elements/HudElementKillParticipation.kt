package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.KillEvent
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.removeFormatting
import kotlin.math.roundToInt

object HudElementKillParticipation : AbstractHudElement() {

    var numberOfTeamKills: Int = 0
    var playerKills: Int = 0
    private var numberOfCapsRed: Int = 0
    private var numberOfCapsBlue: Int = 0

    init {
        EventBus.register<ResetEvent> {
            numberOfTeamKills = 0
            playerKills = 0
            numberOfCapsRed = 0
            numberOfCapsBlue = 0
        }
        EventBus.register<ClientChatReceivedEvent> {
            if (GameStateManager.isIngame) {
                val message = it.message.formattedText.removeFormatting()
                if (GameStateManager.isCTF) {
                    if (message.contains("captured the")) {
                        if (message.contains("RED")) {
                            numberOfCapsBlue++
                        } else {
                            numberOfCapsRed++
                        }
                    }
                }
                if (message.contains("You killed") || message.contains("You assisted"))
                    playerKills++
                if (message.contains("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")) {
                    EventBus.post(
                        KPEvent(
                            (playerKills / numberOfTeamKills.toDouble() * 100).roundToInt()
                        )
                    )
                }
            }
        }
        EventBus.register<TickEvent.ClientTickEvent> {
            if (GameStateManager.isTDM) {
                if (ThePlayer.team == TeamEnum.BLUE) {
                    numberOfTeamKills = GameStateManager.bluePoints / 15
                } else if (ThePlayer.team == TeamEnum.RED) {
                    numberOfTeamKills = GameStateManager.redPoints / 15
                }
            }

        }
        EventBus.register<KillEvent> {
            if (!GameStateManager.isTDM &&
                ((OtherPlayers.getPlayerForName(it.player)!!.team == TeamEnum.BLUE && ThePlayer.team == TeamEnum.BLUE) ||
                        (OtherPlayers.getPlayerForName(it.player)!!.team == TeamEnum.RED && ThePlayer.team == TeamEnum.RED))
            )
                numberOfTeamKills++
        }

    }


    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (numberOfTeamKills > 0)
            renderStrings.add(EnumChatFormatting.YELLOW.toString() + "KP: " + (playerKills / numberOfTeamKills.toDouble() * 100).roundToInt() + "%")
        else
            renderStrings.add(EnumChatFormatting.YELLOW.toString() + "KP: NaN")


        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return GameStateManager.isIngame && !GameStateManager.isPvE
    }

    override fun isEnabled(): Boolean {
        return showKillParticipation
    }

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "||| Kill participation | Show",
        comment = "Enable or disable the Kill Participation counter",
        def = true
    )
    var showKillParticipation = true

}

data class KPEvent(
    val amount: Int
) : IEvent
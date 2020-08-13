package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager.isIngame
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import kotlin.math.abs
import kotlin.math.roundToInt

object HudElementKillParticipation : AbstractHudElement(), IUpdateConsumer, IChatConsumer {

    private var team = TeamEnum.NONE
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
            if (Minecraft.getMinecraft().thePlayer != null) {
                team = if (Minecraft.getMinecraft().thePlayer.displayName.formattedText.contains("\u00A7c")) {
                    TeamEnum.RED
                } else if (Minecraft.getMinecraft().thePlayer.displayName.formattedText.contains("\u00A79")) {
                    TeamEnum.BLUE
                } else {
                    TeamEnum.NONE
                }
            }
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (showKillParticipation) {
            if (numberOfTeamKills > 0)
                renderStrings.add(EnumChatFormatting.YELLOW.toString() + "KP: " + (playerKills / numberOfTeamKills.toDouble() * 100).roundToInt() + "%")
            else
                renderStrings.add(EnumChatFormatting.YELLOW.toString() + "KP: NaN")
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
            if (team == TeamEnum.BLUE)
                numberOfTeamKills =
                    abs(blue.substring(blue.indexOf(":") + 1, blue.indexOf("/")).toInt() - (numberOfCapsBlue * 250)) / 5
            else if (team == TeamEnum.RED)
                numberOfTeamKills =
                    abs(red.substring(red.indexOf(":") + 1, red.indexOf("/")).toInt() - (numberOfCapsRed * 250)) / 5
        } else if (GameStateManager.isTDM) {
            val blue = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardNames[9]
                    .replace(" ", "").replace("\uD83D\uDCA3", "")
            )
            val red = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardNames[8]
                    .replace(" ", "").replace("\uD83D\uDC7D", "")
            )
            if (team == TeamEnum.BLUE)
                numberOfTeamKills = blue.substring(blue.indexOf(":") + 1, blue.indexOf("/")).toInt() / 15
            else if (team == TeamEnum.RED)
                numberOfTeamKills = red.substring(red.indexOf(":") + 1, red.indexOf("/")).toInt() / 15
        }
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        if (team === TeamEnum.NONE) return
        val message = e.message.formattedText

        if (GameStateManager.isCTF) {
            if (e.message.unformattedText.contains("captured the")) {
                if (e.message.unformattedText.contains("RED")) {
                    numberOfCapsBlue++
                } else {
                    numberOfCapsRed++
                }
            }
            if (message.contains("You killed") || message.contains("You assisted"))
                playerKills++
        } else if (GameStateManager.isTDM) {
            if (message.contains("You killed") || message.contains("You assisted"))
                playerKills++
        } else if (GameStateManager.isDOM) {
            if (message.contains("was killed by")) {
                val after = message.substring(message.indexOf("was killed by"))
                if (after.contains("\u00A7c") && team === TeamEnum.RED || after.contains("\u00A79") && team === TeamEnum.BLUE) {
                    numberOfTeamKills++
                }
            } else if (message.contains("You killed")) {
                playerKills++
                numberOfTeamKills++
            } else if (message.contains("You assisted")) {
                playerKills++
            }
        }
        if (message.contains("Winner")) {
            EventBus.post(
                KPEvent(
                    Minecraft.getMinecraft().thePlayer.displayNameString,
                    (playerKills / numberOfTeamKills.toDouble() * 100).roundToInt()
                )
            )
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
        id = "showKP",
        comment = "Enable or disable the Kill Participation counter",
        def = true
    )
    var showKillParticipation = false

}

data class KPEvent(
    val player: String,
    val amount: Int
) : IEvent
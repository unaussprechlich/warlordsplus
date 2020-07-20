package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer

object HudElementRandomTarget : AbstractHudElement(), IChatConsumer {

    var playerToTarget: String = ""
    var totalPoints: Int = 0
    var streak: Int = 1

    init {
        EventBus.register<ResetEvent> {
            totalPoints = 0
            streak = 0
            pickRandomPlayer(0)
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        renderStrings.add("Target: $playerToTarget")
        renderStrings.add("Points ${streak}X: $totalPoints")

        return renderStrings.toTypedArray()
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        val message = e.message.formattedText
        if (message.contains("You killed") && message.contains(playerToTarget)) {
            totalPoints += (OtherPlayers.playersMap[playerToTarget]?.level!! + 10) * 10 * streak
            streak++
            pickRandomPlayer(0)
        } else if (message.contains("You were killed by") && message.contains(playerToTarget)) {
            totalPoints -= (OtherPlayers.playersMap[playerToTarget]?.level!! + 10) * 5 * streak
            streak = 1
            pickRandomPlayer(0)
        }
    }


    override fun isVisible(): Boolean {
        return GameStateManager.isIngame
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun pickRandomPlayer(type: Int) {
        val players = OtherPlayers.playersMap.values
        val teamBlue = players.filter { it.team == TeamEnum.BLUE }
        val teamRed = players.filter { it.team == TeamEnum.RED }
        var newTarget: String
        if (ThePlayer.team == TeamEnum.RED) {
            newTarget = teamBlue.random().name
            when (type) {
                1 ->
                    while (OtherPlayers.playersMap[newTarget]?.level!! > 50 || newTarget == playerToTarget) {
                        newTarget = teamBlue.random().name
                    }
                2 ->
                    while (OtherPlayers.playersMap[newTarget]?.level!! !in 80 downTo 50 || newTarget == playerToTarget) {
                        newTarget = teamBlue.random().name
                    }
                3 ->
                    while (OtherPlayers.playersMap[newTarget]?.level!! < 80 || newTarget == playerToTarget) {
                        newTarget = teamBlue.random().name
                    }
                0 ->
                    while (newTarget == playerToTarget) {
                        newTarget = teamBlue.random().name
                    }
            }
        } else {
            newTarget = teamRed.random().name
            when (type) {
                1 ->
                    while (OtherPlayers.playersMap[newTarget]?.level!! > 50 || newTarget == playerToTarget) {
                        newTarget = teamRed.random().name
                    }
                2 ->
                    while (OtherPlayers.playersMap[newTarget]?.level!! !in 80 downTo 50 || newTarget == playerToTarget) {
                        newTarget = teamRed.random().name
                    }
                3 ->
                    while (OtherPlayers.playersMap[newTarget]?.level!! < 74 || newTarget == playerToTarget) {
                        newTarget = teamRed.random().name
                    }
                0 ->
                    while (newTarget == playerToTarget) {
                        newTarget = teamRed.random().name
                    }
            }
        }
        playerToTarget = newTarget
    }


}
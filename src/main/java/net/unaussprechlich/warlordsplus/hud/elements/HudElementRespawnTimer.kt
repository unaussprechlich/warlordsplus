package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager.isIngame
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardNames
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer

/**
 * HudElementRespawnTimer Created by Alexander on 03.05.2020.
 * Description:
 */
class HudElementRespawnTimer : AbstractHudElement(), IUpdateConsumer {
    private var respawnTimer = 0
    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (showRespawnTimer && GameStateManager.isCTF) {
            when {
                respawnTimer - 1 < 5 ->
                    renderStrings.add(EnumChatFormatting.RED.toString() + "Respawn: " + (respawnTimer - 1))
                respawnTimer - 1 < 8 ->
                    renderStrings.add(EnumChatFormatting.YELLOW.toString() + "Respawn: " + (respawnTimer - 1))
                else ->
                    renderStrings.add(EnumChatFormatting.GREEN.toString() + "Respawn: " + (respawnTimer - 1))
            }
        }

        return renderStrings.toTypedArray()
    }

    private var tick = 0
    override fun update() {
        tick = if (tick >= 20) {
            0
        } else {
            tick++
            return
        }
        respawnTimer--
        val colon = scoreboardNames[9].lastIndexOf(":")
        val after = scoreboardNames[9].substring(colon + 1, colon + 3)
        if (respawnTimer < 0) respawnTimer = 0
        try {
            if (after.toInt() % 12 == 0) {
                respawnTimer = 12
            }
        } catch (e: Exception) {
            //respawnTimer = -1
        }
        OtherPlayers.playersMap.forEach { (_, value) ->
            if (value.isDead) {
                if (value.respawn != -1) {
                    value.respawn--
                    if (value.respawn == 0) {
                        value.isDead = false
                        value.respawn = -1
                    }
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

    companion object {
        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showRespawnTimer",
            comment = "Enable or disable the Respawn Timer",
            def = true
        )
        var showRespawnTimer = false
    }
}
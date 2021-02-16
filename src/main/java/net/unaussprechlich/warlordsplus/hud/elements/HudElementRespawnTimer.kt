package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager.isIngame
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardNames
import net.unaussprechlich.warlordsplus.module.modules.SecondEvent

/**
 * HudElementRespawnTimer Created by Alexander on 03.05.2020.
 * Description:
 */
object HudElementRespawnTimer : AbstractHudElement() {
    private var respawnTimer = 0

    init {
        EventBus.register<SecondEvent> {
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
                    if (GameStateManager.isDOM)
                        value.isDead = false
                }
            }
        }
        Renderer
    }


    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        return renderStrings.toTypedArray()
    }

    object Renderer : RenderApi.Gui<RenderGameOverlayEvent.Text>() {
        init {
            EventBus.register(::render)
        }

        override fun onRender(event: RenderGameOverlayEvent.Text) {
            glMatrix {
                translateX(xCenter + 1)
                translateY(-yCenter - 7)
                if (respawnTimer != -1)
                    respawnTimer.toString().drawCentered()
            }
        }

        override fun shouldRender(e: RenderGameOverlayEvent.Text): Boolean {
            return isIngame
        }
    }

    override fun isVisible(): Boolean {
        return isIngame
    }

    override fun isEnabled(): Boolean {
        return showRespawnTimer
    }


    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "showRespawnTimer",
        comment = "Enable or disable the Respawn Timer",
        def = true
    )
    var showRespawnTimer = false

}
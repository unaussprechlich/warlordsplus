package net.unaussprechlich.warlordsplus.module.modules.meme

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.KillEvent

object MotivationalMessages : IModule {

    var displayUntil = -1L
    var random = -1

    init {
        EventBus.register<KillEvent> {
            if (it.deathPlayer == Minecraft.getMinecraft().thePlayer.name) {
                displayUntil = System.currentTimeMillis() + 10000
                random = (Math.random() * 10).toInt()
            }
        }
        Renderer
    }

    object Renderer : RenderApi.Gui<RenderGameOverlayEvent.Text>() {
        init {
            EventBus.register(::render)
        }

        override fun onRender(event: RenderGameOverlayEvent.Text) {
            if (System.currentTimeMillis() < displayUntil) {
                glMatrix {
                    translateX(xCenter)
                    translateY(-yCenter + 15)
                    scale(5.0)
                    when (random) {
                        0 -> "YOURE SO SHIT".drawCentered()
                        1 -> "UR ADOPTED".drawCentered()
                        2 -> {
                            translateY(5)
                            "LEADERBOARD".drawCentered()
                            translateY(-8)
                            "PLAYER????????".drawCentered()
                        }
                        3 -> "????????".drawCentered()
                        4 -> "QUIT THE GAME".drawCentered()
                        5 -> "GOOD ONE".drawCentered()
                        6 -> "UR A DOGSHIT PLAYER".drawCentered()
                        7 -> "FUCKING LOSER".drawCentered()
                        8 -> "LLLLLLLLLLLLL".drawCentered()
                        9 -> "LMAO".drawCentered()

                    }
                }
            }

        }

        override fun shouldRender(e: RenderGameOverlayEvent.Text): Boolean {
            return GameStateManager.isIngame
        }
    }

}
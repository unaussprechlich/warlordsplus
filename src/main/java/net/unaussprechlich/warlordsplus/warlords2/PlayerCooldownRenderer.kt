package net.unaussprechlich.warlordsplus.warlords2

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.MinecraftForge
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.ImageRegistry

object PlayerCooldownRenderer : RenderApi.Player() {

    override fun shouldRender(e: RenderPlayerEvent.Post): Boolean {
        return showStats &&
                GameStateManager.isWarlords2 &&
                GameStateManager.isIngame &&
                e.entityPlayer != Minecraft.getMinecraft().thePlayer &&
                (GameStateManager.isPvE || !OtherPlayers.playersMap.containsKey(Minecraft.getMinecraft().thePlayer.name))
    }

    override fun onRender(event: RenderPlayerEvent.Post) {
        val player = OtherPlayers.playersMap[event.entityPlayer.name]
        if (player != null) {
            glMatrix {
                translateX(-51.5)
                translateY(75.0)
                scale(.4)

                if (player.redCooldown == 0) {
                    renderImage(75, 75, ImageRegistry.RED_ABILITY)
                } else {
                    drawCooldown(player.redCooldown)
                }
                translateX(60)
                if (player.purpleCooldown == 0) {
                    renderImage(75, 75, ImageRegistry.PURPLE_ABILITY)
                } else {
                    drawCooldown(player.purpleCooldown)
                }
                translateX(60)
                if (player.blueCooldown == 0) {
                    renderImage(75, 75, ImageRegistry.BLUE_ABILITY)
                } else {
                    drawCooldown(player.blueCooldown)
                }
                translateX(60)
                if (player.orangeCooldown == 0) {
                    renderImage(75, 75, ImageRegistry.ORANGE_ABILITY)
                } else {
                    drawCooldown(player.orangeCooldown)
                }
            }
            glMatrix {
                translateY(37)
                val width = 75
                glMatrix {
                    translateY(.5)
                    renderRectXCentered(width + 2.0, 6.0, Colors.BLACK)
                }
                translateZ(-.1)
                renderRectXCentered(width, 5, Colors.GRAY)
                translateX(-width / 2 - .5)
                translateZ(-.1)
                renderRect(((player.currentEnergy.toDouble() / player.maxEnergy) * width).toInt(), 5, Colors.GOLD)
            }
            glMatrix {
                translateY(39.5)
                translateZ(-.1)
                scale(1.5)
                player.currentEnergy.toString().drawCentered(color = Colors.GREEN)
            }
        }
    }

    fun drawCooldown(cooldown: Int) {
        glMatrix {
            scale(.9)
            translateX(5)
            translateY(-7)
            renderImage(70, 70, ImageRegistry.COOLDOWN)
            translate(30, 45)
            scale(4.2)
            val str = if (cooldown < 10) " $cooldown" else "$cooldown"
            str.draw()
        }
    }

    fun register() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @ConfigPropertyBoolean(
        category = CCategory.RENDERER,
        id = "Warlords 2 Player Stats | Show",
        comment = "Enable or disable showing player stats (cooldowns, energy, etc.) above their head in Warlords 2",
        def = true
    )
    var showStats = true
}
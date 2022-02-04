package net.unaussprechlich.warlordsplus.module.modules.qualityoflife

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.EnumParticleTypes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.removeFormatting

object SoulbindingCounter : IModule {

    private var binded: MutableList<PlayerBinded> = mutableListOf()

    init {
        EventBus.register(SoulbindingCounter::onClientTick)
        EventBus.register(SoulbindingCounter::onChat)
        Renderer
        RenderPlayer
    }

    private fun onClientTick(e: TickEvent.ClientTickEvent) {
        binded.removeIf { it.validUntil < System.currentTimeMillis() }
        if (showParticle) {
            binded.forEach { binded ->
                val players = Minecraft.getMinecraft().theWorld.playerEntities
                players.filter {
                    it.name == binded.name
                }.forEach { player ->
                    repeat(20) {
                        Minecraft.getMinecraft().theWorld.spawnParticle(
                            EnumParticleTypes.SLIME,
                            player.posX + Math.random() * 2 - 1,
                            player.posY + 1.5 + Math.random() * 2 - 1,
                            player.posZ + Math.random() * 2 - 1,
                            0.0,
                            0.0,
                            0.0
                        )
                    }
                }
            }
        }
    }

    private fun onChat(e: ClientChatReceivedEvent) {
        val message = e.message.unformattedText.removeFormatting()
        if (message.contains("Your Soulbinding Weapon has bound")) {
            binded.add(PlayerBinded(message.substring(message.indexOf("bound") + 6, message.indexOf("!"))))
        }
    }

    data class PlayerBinded(
        val name: String,
        val validUntil: Long = System.currentTimeMillis() + 2000
    )

    object Renderer : RenderApi.Gui<RenderGameOverlayEvent.Text>() {
        init {
            EventBus.register(::render)
        }

        override fun onRender(event: RenderGameOverlayEvent.Text) {
            glMatrix {
                translateX(xCenter + 1)
                translateY(-yCenter + 13)
                if (binded.size > 0) {
                    "${EnumChatFormatting.LIGHT_PURPLE}${binded.size}".drawCentered()
                }
            }
        }

        override fun shouldRender(e: RenderGameOverlayEvent.Text): Boolean {
            return GameStateManager.isIngame && showCounter
        }
    }

    object RenderPlayer : RenderApi.World() {
        override fun onRender(event: RenderWorldLastEvent) {
            binded.forEach { binded ->
                val players = Minecraft.getMinecraft().theWorld.playerEntities
                players.filter {
                    it.name == binded.name
                }.forEach {
                    glMatrix {
                        translateToPos(it.posX, it.posY + 4.75, it.posZ)
                        autoRotate()
                        scaleForWorldRendering()
                        scale(10.0)
                        "V".drawCentered()
                    }
                }
            }
        }
    }

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "SoulBinding Counter | Enable",
        comment = "Enable or disable the soulbinding counter above crosshair",
        def = true
    )
    var showCounter = true

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "SoulBinding Particles | Enable",
        comment = "Enable or disable the soulbinding particles",
        def = true
    )
    var showParticle = true
}
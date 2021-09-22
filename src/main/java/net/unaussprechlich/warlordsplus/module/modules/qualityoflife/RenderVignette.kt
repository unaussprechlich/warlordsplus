package net.unaussprechlich.warlordsplus.module.modules.qualityoflife

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.SOMEBODY_DID
import net.unaussprechlich.warlordsplus.module.modules.YOU_DID
import net.unaussprechlich.warlordsplus.util.ImageRegistry
import net.unaussprechlich.warlordsplus.util.contain
import net.unaussprechlich.warlordsplus.util.removeFormatting

object RenderVignette : IModule {

    private var veners: MutableMap<String, IntervenedBy> = mutableMapOf()

    data class IntervenedBy(
        val name: String,
        val validUntil: Long = System.currentTimeMillis() + 6000
    )

    data class IntervenedExpire(
        val name: String
    )

    init {
        EventBus.register<RenderGameOverlayEvent.Pre> {
            if (it.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
                if (veners.isNotEmpty()) {
                    renderTint(ScaledResolution(Minecraft.getMinecraft()), it.partialTicks)
                }
            }
        }
        EventBus.register<TickEvent.ClientTickEvent> {
            veners.values.filter { it.validUntil < System.currentTimeMillis() }.map { it.name }
                .forEach { veners.remove(it) }
        }
        EventBus.register<IntervenedBy> {
            veners[it.name] = it
        }
        EventBus.register<IntervenedExpire> {
            veners.remove(it.name)
        }

        EventBus.register(RenderVignette::onChat)
    }

    private fun onChat(e: ClientChatReceivedEvent) {
        val message = e.message.unformattedText.removeFormatting()
        if (e.message.unformattedText contain YOU_DID) {
            if (intervene) {
                if (message.contains("is shielding you with their Intervene!")) {
                    EventBus.post(IntervenedBy(message.substring(2, message.indexOf("is") - 1)))
                }
            }
        } else if (e.message.unformattedText contain SOMEBODY_DID) {
            if (intervene) {
                if (message.contains("Intervene has expired!")) {
                    EventBus.post(IntervenedExpire(message.substring(2, message.indexOf("'s"))))
                }
            }
        }
    }


    private fun renderTint(res: ScaledResolution, partialTicks: Float) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.depthMask(false)
        GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0)
        GlStateManager.color(1f - 25 / 255f, 1f - 25 / 255f, 1f - 200 / 255f, .5f)
        Minecraft.getMinecraft().textureManager.bindTexture(ImageRegistry.VIGNETTE.getResourceLocation())
        val tes = Tessellator.getInstance()
        val wr = tes.worldRenderer
        wr.begin(7, DefaultVertexFormats.POSITION_TEX)
        wr.pos(0.0, res.scaledHeight_double, -90.0).tex(0.0, 1.0).endVertex()
        wr.pos(res.scaledWidth_double, res.scaledHeight_double, -90.0).tex(1.0, 1.0).endVertex()
        wr.pos(res.scaledWidth_double, 0.0, -90.0).tex(1.0, 0.0).endVertex()
        wr.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex()
        tes.draw()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.depthMask(true)
        GlStateManager.enableDepth()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "Vignette | Intervene",
        comment = "Enable or disable intervene vignette",
        def = false
    )
    var intervene = false
}
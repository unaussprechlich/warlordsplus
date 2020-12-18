package net.unaussprechlich.renderapi

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.unaussprechlich.renderapi.util.MinecraftOpenGlStuff
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.removeFormatting

abstract class RenderBasics : MinecraftOpenGlStuff() {

    companion object {

        fun scaleForText() =
            GlStateManager.scale(-1.45 * 0.016666668f, -1.45 * 0.016666668f, 1.45 * 0.016666668f)

        fun renderRect(width: Double, height: Double, color: Colors, alpha: Int = 255, z: Double = 0.0) {
            if (alpha < 255) GlStateManager.depthMask(false)
            worldrenderer.begin(
                7,
                DefaultVertexFormats.POSITION_COLOR
            )
            worldrenderer.pos(0.0, 0.0, z).color(color, alpha).endVertex()
            worldrenderer.pos(0.0, height, z).color(color, alpha).endVertex()
            worldrenderer.pos(width, height, z).color(color, alpha).endVertex()
            worldrenderer.pos(width, 0.0, z).color(color, alpha).endVertex()
            tessellator.draw()
            if (alpha < 255) GlStateManager.depthMask(true)
        }

        fun renderRectXCentered(width: Double, height: Double, color: Colors, alpha: Int = 255, z: Double = 0.0) {
            if (alpha < 255) GlStateManager.depthMask(false)
            val w2 = width / 2
            worldrenderer.begin(
                7,
                DefaultVertexFormats.POSITION_COLOR
            )
            worldrenderer.pos(-w2, 0.0, z).color(color, alpha).endVertex()
            worldrenderer.pos(-w2, height, z).color(color, alpha).endVertex()
            worldrenderer.pos(w2, height, z).color(color, alpha).endVertex()
            worldrenderer.pos(w2, 0.0, z).color(color, alpha).endVertex()
            tessellator.draw()
            if (alpha < 255) GlStateManager.depthMask(true)
        }

        fun WorldRenderer.color(color: Colors, alpha: Int = 255): WorldRenderer {
            return this.color(color.red, color.green, color.blue, alpha)
        }

        fun String.height(): Double = 8.0

        fun String.width(): Int =
            fontRenderer.getStringWidth(this)

        fun String.unformattedWidth(): Int =
            fontRenderer.getStringWidth(this.removeFormatting())

        fun String.draw(seeThruBlocks: Boolean = false) {
            GlStateManager.enableTexture2D()
            if (seeThruBlocks) {
                GlStateManager.depthMask(false)
                GlStateManager.disableDepth()
                fontRenderer.drawString(this, 0, 0, 553648127)
                GlStateManager.enableDepth()
                GlStateManager.depthMask(true)
            }
            translateZ(1.0)
            fontRenderer.drawString(this, 0, 0, -1)
            translateZ(-1.0)
            GlStateManager.disableTexture2D()
        }

        fun String.drawCentered(seeThruBlocks: Boolean = false) {
            GlStateManager.enableTexture2D()
            if (seeThruBlocks) {
                GlStateManager.depthMask(false)
                GlStateManager.disableDepth()
                fontRenderer.drawString(this, -this.width() / 2, 0, 553648127)
                GlStateManager.enableDepth()
                GlStateManager.depthMask(true)
            }
            translateZ(1.0)
            fontRenderer.drawString(this, -this.width() / 2, 0, -1)
            translateZ(-1.0)
            GlStateManager.disableTexture2D()
        }

        fun String.drawWithBackground(backgroundColor: Colors, alpha: Int = 255, padding: Int = 1) {
            renderRect(this.width() + padding * 2.0, 7.0 + padding * 2, backgroundColor, alpha = alpha)
            translate(padding.toDouble(), padding.toDouble(), 0.0)
            draw()
            translate(-padding.toDouble(), -padding.toDouble(), 0.0)
        }

        fun String.drawCenteredWithBackground(backgroundColor: Colors) {
            val width = this.width() + 2.0
            translateX(-width / 2)
            renderRect(width, 9.0, backgroundColor)
            translate(1.0 + width / 2, 1.0, 0.0)
            draw()
            translate(-1.0, -1.0, 0.0)
        }
    }
}
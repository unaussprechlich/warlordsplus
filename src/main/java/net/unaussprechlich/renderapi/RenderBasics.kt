package net.unaussprechlich.renderapi

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.unaussprechlich.renderapi.util.IResourceLocationProvider
import net.unaussprechlich.renderapi.util.MinecraftOpenGlStuff
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.removeFormatting
import org.lwjgl.opengl.GL11

abstract class RenderBasics : MinecraftOpenGlStuff() {

    companion object {

        /**
         * Scale the renderer to be able to draw inside the world
         */
        fun scaleForWorldRendering() =
            GlStateManager.scale(-1.45 * 0.016666668f, -1.45 * 0.016666668f, 1.45 * 0.016666668f)

        fun renderImage(
            width: Int,
            height: Int,
            resourceLocationProvider: IResourceLocationProvider,
            alpha: Int = 255,
            z: Double = 0.0
        ) = renderImage(width.toDouble(), height.toDouble(), resourceLocationProvider.getResourceLocation(), alpha, z)

        fun renderImage(
            width: Double,
            height: Double,
            resourceLocationProvider: IResourceLocationProvider,
            alpha: Int = 255,
            z: Double = 0.0
        ) = renderImage(width, height, resourceLocationProvider.getResourceLocation(), alpha, z)


        fun renderImage(
            width: Double,
            height: Double,
            resourceLocation: ResourceLocation,
            alpha: Int = 255,
            z: Double = 0.0,
            red: Int = 255,
            green: Int = 255,
            blue: Int = 255
        ) {
            if (alpha < 255) GlStateManager.depthMask(false)

            Minecraft.getMinecraft().textureManager.bindTexture(resourceLocation)

            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 0, 1)
            GlStateManager.color(red / 1f, green / 1f, blue / 1f, alpha / 255f)
            GlStateManager.enableBlend()
            GlStateManager.enableTexture2D()

            //Used to scale the image
            val f = (1.0f / height).toFloat()
            val f1 = (1.0f / width).toFloat()

            worldrenderer.begin(
                7,
                DefaultVertexFormats.POSITION_TEX
            )
            worldrenderer.pos(0.0, 0.0, z).tex(0.0 * f1, 0.0 * f).endVertex()
            worldrenderer.pos(0.0, height, z).tex(0.0 * f1, height * f).endVertex()
            worldrenderer.pos(width, height, z).tex(width * f1, height * f).endVertex()
            worldrenderer.pos(width, 0.0, z).tex(width * f1, 0.0 * f).endVertex()
            tessellator.draw()

            GlStateManager.disableTexture2D()
            GlStateManager.disableBlend()

            if (alpha < 255) GlStateManager.depthMask(true)
        }

        /**
         * Draws a rectangle
         */
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

        fun renderRect(width: Int, height: Int, color: Colors, alpha: Int = 255, z: Double = 0.0) =
            renderRect(width.toDouble(), height.toDouble(), color, alpha, z)

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

        fun renderRectXCentered(width: Int, height: Int, color: Colors, alpha: Int = 255, z: Double = 0.0) =
            renderRectXCentered(width.toDouble(), height.toDouble(), color, alpha, z)

        fun WorldRenderer.color(color: Colors, alpha: Int = 255): WorldRenderer {
            return this.color(color.red, color.green, color.blue, alpha)
        }

        fun String.height(): Double = 8.0

        fun String.width(): Int =
            fontRenderer.getStringWidth(this)

        fun String.unformattedWidth(): Int =
            fontRenderer.getStringWidth(this.removeFormatting())

        fun String.draw(seeThruBlocks: Boolean = false, shadow: Boolean = false) {
            GlStateManager.enableTexture2D()
            if (seeThruBlocks) {
                GlStateManager.depthMask(false)
                GlStateManager.disableDepth()
                fontRenderer.drawString(this, 0, 0, 553648127)
                GlStateManager.enableDepth()
                GlStateManager.depthMask(true)
            }
            translateZ(1.0)

            if (shadow) {
                fontRenderer.drawStringWithShadow(this, 0f, 0f, -1)
            } else {
                fontRenderer.drawString(this, 0, 0, -1)
            }

            translateZ(-1.0)
            GlStateManager.disableTexture2D()
        }

        fun String.drawCentered(seeThruBlocks: Boolean = false, shadow: Boolean = false) {
            GlStateManager.enableTexture2D()
            if (seeThruBlocks) {
                GlStateManager.depthMask(false)
                GlStateManager.disableDepth()
                fontRenderer.drawString(this, -this.width() / 2, 0, 553648127)
                GlStateManager.enableDepth()
                GlStateManager.depthMask(true)
            }
            translateZ(1.0)

            if (shadow) {
                fontRenderer.drawStringWithShadow(this, -this.width() / 2f, 0f, -1)
            } else {
                fontRenderer.drawString(this, -this.width() / 2, 0, -1)
            }

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
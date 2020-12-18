package net.unaussprechlich.renderapi.util

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats


abstract class MinecraftOpenGlStuff {

    companion object {

        val tessellator: Tessellator
            get() = Tessellator.getInstance()

        val worldrenderer: WorldRenderer
            get() = tessellator.worldRenderer

        val mc: Minecraft
            get() = Minecraft.getMinecraft()

        val thePlayer: EntityPlayerSP?
            get() = mc.thePlayer

        val renderManager: RenderManager
            get() = mc.renderManager

        val fontRenderer: FontRenderer
            get() = mc.fontRendererObj

        fun begin() = worldrenderer.begin(
            7,
            DefaultVertexFormats.POSITION_COLOR
        )

        inline fun depthMaskTrue(fn: () -> Unit) {
            GlStateManager.depthMask(true)
            fn()
            GlStateManager.depthMask(false)
        }

        inline fun depthMaskFalse(fn: () -> Unit) {
            GlStateManager.depthMask(false)
            fn()
            GlStateManager.depthMask(true)
        }

        inline fun depth(fn: () -> Unit) {
            GlStateManager.enableDepth()
            fn()
            GlStateManager.disableDepth()
        }

        inline fun noDepth(fn: () -> Unit) {
            GlStateManager.disableDepth()
            fn()
            GlStateManager.enableDepth()
        }

        inline fun blend(fn: () -> Unit) {
            GlStateManager.enableBlend()
            fn()
            GlStateManager.disableBlend()
        }

        inline fun noBlend(fn: () -> Unit) {
            GlStateManager.disableBlend()
            fn()
            GlStateManager.enableBlend()
        }

        inline fun useTextures(fn: () -> Unit) {
            GlStateManager.enableTexture2D()
            fn()
            GlStateManager.disableTexture2D()
        }

        inline fun glMatrix(fn: () -> Unit) {
            GlStateManager.pushMatrix()
            fn()
            GlStateManager.popMatrix()
        }

        fun rotate(angle: Float, x: Float, y: Float, z: Float) =
            GlStateManager.rotate(angle, x, y, z)

        fun rotateX(angle: Float) =
            GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f)

        fun rotateY(angle: Float) =
            GlStateManager.rotate(angle, 0.0f, 1.0f, 0.0f)

        fun rotateZ(angle: Float) =
            GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f)

        fun translate(x: Double, y: Double, z: Double) =
            GlStateManager.translate(x, y, z)

        fun translateX(x: Double) =
            GlStateManager.translate(x, 0.0, 0.0)

        fun translateY(y: Double) =
            GlStateManager.translate(0.0, -y, 0.0)

        fun translateZ(z: Double) =
            GlStateManager.translate(0.0, 0.0, z)

        fun scale(amount: Double) =
            GlStateManager.scale(amount, amount, -amount)

    }
}
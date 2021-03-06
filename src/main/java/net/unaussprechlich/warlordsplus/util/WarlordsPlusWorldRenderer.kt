package net.unaussprechlich.warlordsplus.util

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11

abstract class WarlordsPlusWorldRenderer(
    val autoRotate: Boolean = true,
    val seeTextThruBlocks: Boolean = false
) {

    protected var event: RenderPlayerEvent? = null
        private set

    val tessellator: Tessellator
        get() = Tessellator.getInstance()

    val worldrenderer: WorldRenderer
        get() = tessellator.worldRenderer

    protected abstract fun onRender(event: RenderPlayerEvent.Post)

    fun render(e: RenderPlayerEvent.Post) {
        event = e

        val x = e.x
        val y = e.y
        val z = e.z

        GlStateManager.alphaFunc(516, 0.1f)

        GlStateManager.pushMatrix()
        GlStateManager.translate(
            x,
            y + e.entityPlayer.height + 0.5f,
            z
        )
        GL11.glNormal3f(0f, 1f, 0f)

        if (autoRotate) {
            GlStateManager.rotate(
                -e.renderer.renderManager.playerViewY,
                0.0f,
                1.0f,
                0.0f
            )
            //GlStateManager.rotate(e.renderer.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        }

        val scale = 1.45 * 0.016666668f
        GlStateManager.scale(-scale, -scale, scale)

        GlStateManager.disableLighting()
        GlStateManager.depthMask(true)
        GlStateManager.enableDepth()
        GlStateManager.enableBlend()

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)

        GlStateManager.disableTexture2D()

        onRender(e)

        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.enableLighting()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()

        event = null

    }

    protected inline fun depthMaskTrue(fn: () -> Unit) {
        GlStateManager.depthMask(true)
        fn()
        GlStateManager.depthMask(false)
    }

    protected inline fun depthMaskFalse(fn: () -> Unit) {
        GlStateManager.depthMask(false)
        fn()
        GlStateManager.depthMask(true)
    }

    protected inline fun depth(fn: () -> Unit) {
        GlStateManager.enableDepth()
        fn()
        GlStateManager.disableDepth()
    }

    protected inline fun noDepth(fn: () -> Unit) {
        GlStateManager.disableDepth()
        fn()
        GlStateManager.enableDepth()
    }

    protected inline fun blend(fn: () -> Unit) {
        GlStateManager.enableBlend()
        fn()
        GlStateManager.disableBlend()
    }

    protected inline fun noBlend(fn: () -> Unit) {
        GlStateManager.disableBlend()
        fn()
        GlStateManager.enableBlend()
    }

    protected inline fun useTextures(fn: () -> Unit) {
        GlStateManager.enableTexture2D()
        fn()
        GlStateManager.disableTexture2D()
    }

    protected inline fun glMatrix(fn: () -> Unit) {
        GlStateManager.pushMatrix()
        fn()
        GlStateManager.popMatrix()
    }

    protected fun rotate(angle: Float, x: Float, y: Float, z: Float) =
        GlStateManager.rotate(angle, x, y, z)

    protected fun rotateX(angle: Float) =
        GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f)

    protected fun rotateY(angle: Float) =
        GlStateManager.rotate(angle, 0.0f, 1.0f, 0.0f)

    protected fun rotateZ(angle: Float) =
        GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f)

    protected fun translate(x: Double, y: Double, z: Double) =
        GlStateManager.translate(x, y, z)

    protected fun translateX(x: Double) =
        GlStateManager.translate(x, 0.0, 0.0)

    protected fun translateY(y: Double) =
        GlStateManager.translate(0.0, -y, 0.0)

    protected fun translateZ(z: Double) =
        GlStateManager.translate(0.0, 0.0, z)

    protected fun scale(amount: Double) =
        GlStateManager.scale(amount, amount, -amount)

    protected fun begin() = worldrenderer.begin(
        7,
        DefaultVertexFormats.POSITION_COLOR
    )

    protected fun pos(x: Double, y: Double, z: Double) = worldrenderer.pos(x, y, z)
    protected fun draw() = tessellator.draw()

    protected fun renderRect(width: Double, height: Double, z: Double, color: Colors, alpha: Int) {
        worldrenderer.begin(
            7,
            DefaultVertexFormats.POSITION_COLOR
        )
        worldrenderer.pos(0.0, 0.0, z).color(color, alpha).endVertex()
        worldrenderer.pos(0.0, height, z).color(color, alpha).endVertex()
        worldrenderer.pos(width, height, z).color(color, alpha).endVertex()
        worldrenderer.pos(width, 0.0, z).color(color, alpha).endVertex()
        tessellator.draw()
    }

    protected fun renderRectXCentered(width: Double, height: Double, z: Double, color: Colors, alpha: Int) {
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
    }

    protected fun String.draw() {
        GlStateManager.enableTexture2D()
        if (seeTextThruBlocks) {
            GlStateManager.depthMask(false)
            GlStateManager.disableDepth()
            event!!.renderer.fontRendererFromRenderManager.drawString(this, 0, 0, 553648127)
            GlStateManager.enableDepth()
            GlStateManager.depthMask(true)
        }
        translateZ(1.0)
        event!!.renderer.fontRendererFromRenderManager.drawString(this, 0, 0, -1)
        translateZ(-1.0)
        GlStateManager.disableTexture2D()
    }

    protected fun String.drawCentered() {
        GlStateManager.enableTexture2D()
        if (seeTextThruBlocks) {
            GlStateManager.depthMask(false)
            GlStateManager.disableDepth()
            event!!.renderer.fontRendererFromRenderManager.drawString(this, -this.width() / 2, 0, 553648127)
            GlStateManager.enableDepth()
            GlStateManager.depthMask(true)
        }

        translateZ(1.0)
        event!!.renderer.fontRendererFromRenderManager.drawString(this, -this.width() / 2, 0, -1)
        translateZ(-1.0)
        GlStateManager.disableTexture2D()
    }


    protected fun String.height(): Double = 8.0

    protected fun String.width(): Int =
        Minecraft.getMinecraft().fontRendererObj.getStringWidth(this)

    protected fun WorldRenderer.color(color: Colors, alpha: Int = 255): WorldRenderer {
        return this.color(color.red, color.green, color.blue, alpha)
    }
}
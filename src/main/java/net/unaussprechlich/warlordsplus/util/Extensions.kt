package net.unaussprechlich.warlordsplus.util

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.opengl.GL11

val REGEX_FILTER = Regex("[^A-Za-z0-9/.,&_!:>+)(y<\\-\\]\\[\\u00AB\\u00BB\\s]")

infix fun Int.fdiv(i: Int): Float = this / i.toFloat();
infix fun Int.ddiv(i: Int): Double = this / i.toDouble();

fun String.removeSpaces(): String = this.replace(" ", "")
fun String.removeFormatting(): String {
    return EnumChatFormatting.getTextWithoutFormattingCodes(this).replace(REGEX_FILTER, "")
}

fun TickEvent.ClientTickEvent.checkPreConditions(): Boolean {
    return this.phase != TickEvent.Phase.END
            && !Minecraft.getMinecraft().isGamePaused
            && Minecraft.getMinecraft().thePlayer != null
}

infix fun String.contain(other: String): Boolean = this.contains(other)
infix fun String.notContain(other: String): Boolean = !(this.contains(other))

abstract class WarlordsPlusWorldRenderer(
    val autoRotate: Boolean = true
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

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y + e.entityPlayer.height + 0.5f, z)
        GL11.glNormal3f(0f, 1f, 0f)

        if (autoRotate) {
            GlStateManager.rotate(-e.renderer.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
            //GlStateManager.rotate(e.renderer.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        }

        val scale = 1.45 * 0.016666668f
        GlStateManager.scale(-scale, -scale, scale)

        GlStateManager.disableLighting()
        GlStateManager.enableBlend()
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.disableTexture2D()

        onRender(e)

        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.enableLighting()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()

        event = null

    }

    inline fun useTextures(fn: () -> Unit) {
        GlStateManager.enableTexture2D()
        fn()
        GlStateManager.disableTexture2D()
    }

    inline fun text(fn: () -> Unit) {
        GlStateManager.enableTexture2D()
        GlStateManager.disableDepth()
        GlStateManager.depthMask(false)

        fn()

        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.disableTexture2D()
    }

    inline fun glMatrix(fn: () -> Unit) {
        GlStateManager.pushMatrix()
        fn()
        GlStateManager.popMatrix()
    }

    inline fun rotate(angle: Float, x: Float, y: Float, z: Float) = GlStateManager.rotate(angle, x, y, z)
    inline fun rotateX(angle: Float) = GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f)
    inline fun rotateY(angle: Float) = GlStateManager.rotate(angle, 0.0f, 1.0f, 0.0f)
    inline fun rotateZ(angle: Float) = GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f)

    inline fun translate(x: Double, y: Double, z: Double) = GlStateManager.translate(x, y, z)
    inline fun translateX(x: Double) = GlStateManager.translate(x, 0.0, 0.0)
    inline fun translateY(y: Double) = GlStateManager.translate(0.0, -y, 0.0)
    inline fun translateZ(z: Double) = GlStateManager.translate(0.0, 0.0, z)

    inline fun scale(amount: Double) = GlStateManager.scale(amount, amount, -amount)

    inline fun begin() = worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
    inline fun pos(x: Double, y: Double, z: Double) = worldrenderer.pos(x, y, z)
    inline fun draw() = tessellator.draw()

    fun renderRect(width: Double, height: Double, z: Double, color: Colors, alpha: Int) {
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos(0.0, 0.0, z).color(color, alpha).endVertex()
        worldrenderer.pos(0.0, height, z).color(color, alpha).endVertex()
        worldrenderer.pos(width, height, z).color(color, alpha).endVertex()
        worldrenderer.pos(width, 0.0, z).color(color, alpha).endVertex()
        tessellator.draw()
    }

    fun renderRectXCentered(width: Double, height: Double, z: Double, color: Colors, alpha: Int) {
        val w2 = width / 2
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos(-w2, 0.0, z).color(color, alpha).endVertex()
        worldrenderer.pos(-w2, height, z).color(color, alpha).endVertex()
        worldrenderer.pos(w2, height, z).color(color, alpha).endVertex()
        worldrenderer.pos(w2, 0.0, z).color(color, alpha).endVertex()
        tessellator.draw()
    }

    fun String.draw() =
        event!!.renderer.fontRendererFromRenderManager.drawString(this, 0, 0, -1)


    fun String.drawCentered() =
        event!!.renderer.fontRendererFromRenderManager.drawString(this, -this.width() / 2, 0, -1)

    fun String.height(): Double = 8.0

    fun String.width(): Int =
        Minecraft.getMinecraft().fontRendererObj.getStringWidth(this)

    fun WorldRenderer.color(color: Colors, alpha: Int = 255): WorldRenderer {
        return this.color(color.red, color.green, color.blue, alpha)
    }
}






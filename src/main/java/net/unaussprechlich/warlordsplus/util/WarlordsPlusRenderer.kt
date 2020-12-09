package net.unaussprechlich.warlordsplus.util

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.mixin.AccessorRenderManager
import org.lwjgl.opengl.GL11

abstract class WarlordsPlusRenderer<E : Event>(
    val seeTextThruBlocks: Boolean = false
) {


    protected var event: E? = null

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
    }

    abstract class World(
        seeTextThruBlocks: Boolean = false
    ) : WarlordsPlusRenderer<RenderEntitiesEvent>(seeTextThruBlocks) {

        companion object {
            val renderPosX: Double
                get() = (renderManager as AccessorRenderManager).renderPosX
            val renderPosY: Double
                get() = (renderManager as AccessorRenderManager).renderPosY
            val renderPosZ: Double
                get() = (renderManager as AccessorRenderManager).renderPosZ
        }

        init {
            EventBus.register(::render)
        }

        /**
         * Translates the renderer to a given position
         *
         * icamera.setPosition(d0, d1, d2);
         */
        protected fun translateToPos(x: Double, y: Double, z: Double) {
            val entity = event!!.renderViewEntity
            val partialTicks = (event!!.partialTicks).toDouble()
            val d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
            val d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
            val d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
            translate(x - d0, y - d1, z - d2)
        }

        protected fun scaleToNormalTextSize() {
            val f1 = 0.016666668f * 1.6f
            GlStateManager.scale(-f1, -f1, f1)
        }

        protected fun autoRotate() {
            GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
            GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        }

        override fun render(e: RenderEntitiesEvent) {
            event = e

            GlStateManager.alphaFunc(516, 0.1f)
            GlStateManager.pushAttrib()
            GlStateManager.pushMatrix()

            GL11.glNormal3f(0f, 1f, 0f)


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
            GlStateManager.popAttrib()

            event = null
        }
    }

    abstract class Gui<GuiE : RenderGameOverlayEvent>() : WarlordsPlusRenderer<GuiE>() {

        override fun render(e: GuiE) {
            GlStateManager.pushAttrib()
            GlStateManager.pushMatrix()
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
            GlStateManager.popAttrib()
        }

        companion object {
            val renderItem: RenderItem
                get() = mc.renderItem

            val mcScale: Int
                get() {
                    val scale = mc.gameSettings.guiScale
                    return if (scale == 0) ScaledResolution(mc).scaleFactor
                    else scale
                }

            val width: Int
                get() = scaledMcWidth
            val height: Int
                get() = scaledMcHeight

            val displayWith: Int
                get() = mc.displayWidth

            val displayHeight: Int
                get() = mc.displayHeight

            val scaledMcWidth: Int
                get() = mc.displayWidth / mcScale

            val scaledMcHeight: Int
                get() = mc.displayHeight / mcScale

            val xCenter: Int
                get() = mc.displayWidth / 2 / mcScale

            val yCenter: Int
                get() = mc.displayHeight / 2 / mcScale

            val xLeft: Int
                get() = 0

            val xRight: Int
                get() = mc.displayWidth / mcScale

            val yBottom: Int
                get() = mc.displayHeight / mcScale

            val yTop: Int
                get() = 0
        }

        protected fun drawItemStackWithText(id: Int, meta: Int, overlay: String?) {
            GlStateManager.enableBlend()
            RenderHelper.enableStandardItemLighting()
            GlStateManager.color(0.0f, 0.0f, 32.0f)

            val iStack = ItemStack(Item.getItemById(id))
            if (meta > 0) iStack.itemDamage = meta

            renderItem.renderItemAndEffectIntoGUI(iStack, 0, 0)
            if (overlay != null)
                renderItem.renderItemOverlayIntoGUI(fontRenderer, iStack, 0, 0, overlay)

            RenderHelper.disableStandardItemLighting()
            GlStateManager.disableBlend()
        }
    }

    abstract class Player(
        val autoRotate: Boolean = true,
        seeTextThruBlocks: Boolean = false
    ) : WarlordsPlusRenderer<RenderPlayerEvent.Post>(seeTextThruBlocks) {

        override fun render(e: RenderPlayerEvent.Post) {
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
    }

    protected abstract fun onRender(event: E)


    protected open fun mapRenderManager(event: E): RenderManager {
        return Minecraft.getMinecraft().renderManager
    }

    open fun render(e: E) {
        event = e

        GlStateManager.alphaFunc(516, 0.1f)

        GlStateManager.pushMatrix()

        GL11.glNormal3f(0f, 1f, 0f)

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

    protected fun scaleForText() =
        GlStateManager.scale(-1.45 * 0.016666668f, -1.45 * 0.016666668f, 1.45 * 0.016666668f)

    protected fun begin() = worldrenderer.begin(
        7,
        DefaultVertexFormats.POSITION_COLOR
    )

    protected fun pos(x: Double, y: Double, z: Double) = worldrenderer.pos(x, y, z)
    protected fun draw() = tessellator.draw()

    protected fun renderRect(width: Double, height: Double, color: Colors, alpha: Int = 255, z: Double = 0.0) {
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

    protected fun renderRectXCentered(width: Double, height: Double, color: Colors, alpha: Int = 255, z: Double = 0.0) {
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

    protected fun String.drawWithBackground(backgroundColor: Colors, alpha: Int = 255, padding: Int = 1) {
        renderRect(this.width() + padding * 2.0, 7.0 + padding * 2, backgroundColor, alpha = alpha)
        translate(padding.toDouble(), padding.toDouble(), 0.0)
        this.draw()
        translate(-padding.toDouble(), -padding.toDouble(), 0.0)
    }

    protected fun String.drawCenteredWithBackground(backgroundColor: Colors) {
        val width = this.width() + 2.0
        translateX(-width / 2)
        renderRect(width, 9.0, backgroundColor)
        translate(1.0 + width / 2, 1.0, 0.0)
        this.draw()
        translate(-1.0, -1.0, 0.0)
    }

    protected fun String.draw() {
        GlStateManager.enableTexture2D()
        if (seeTextThruBlocks) {
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

    protected fun String.drawCentered() {
        GlStateManager.enableTexture2D()
        if (seeTextThruBlocks) {
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


    protected fun String.height(): Double = 8.0

    protected fun String.width(): Int =
        fontRenderer.getStringWidth(this)

    protected fun String.unformattedWidth(): Int =
        fontRenderer.getStringWidth(this.removeFormatting())

    protected fun WorldRenderer.color(color: Colors, alpha: Int = 255): WorldRenderer {
        return this.color(color.red, color.green, color.blue, alpha)
    }
}
package net.unaussprechlich.renderapi.renderer

import net.minecraft.client.renderer.GlStateManager
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.mixin.AccessorRenderManager
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.util.RenderEntitiesEvent
import org.lwjgl.opengl.GL11


abstract class RenderApiWorld : RenderApi<RenderEntitiesEvent>() {

    companion object {
        val renderPosX: Double
            get() = (renderManager as AccessorRenderManager).renderPosX
        val renderPosY: Double
            get() = (renderManager as AccessorRenderManager).renderPosY
        val renderPosZ: Double
            get() = (renderManager as AccessorRenderManager).renderPosZ
    }

    init {
        EventBus.register<RenderEntitiesEvent> {
            if (shouldRender(it)) render(it)
        }
    }

    open fun shouldRender(event: RenderEntitiesEvent): Boolean {
        return true
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

    protected fun autoRotateX() {
        GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
    }

    inline fun autoRotate(fn: () -> Unit) {
        GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        fn()
        GlStateManager.rotate(renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
    }

    inline fun autoRotateX(fn: () -> Unit) {
        GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        fn()
        GlStateManager.rotate(renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
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
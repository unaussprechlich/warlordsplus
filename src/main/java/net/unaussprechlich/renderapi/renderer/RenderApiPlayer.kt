package net.unaussprechlich.renderapi.renderer

import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderPlayerEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import org.lwjgl.opengl.GL11

abstract class RenderApiPlayer(
    val autoRotate: Boolean = true
) : RenderApi<RenderPlayerEvent.Post>() {

    init {
        EventBus.register<RenderPlayerEvent.Post> {
            if (shouldRender(it)) render(it)
        }
    }

    open fun shouldRender(event: RenderPlayerEvent.Post): Boolean {
        return true
    }

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
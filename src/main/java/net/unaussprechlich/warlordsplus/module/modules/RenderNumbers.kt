package net.unaussprechlich.warlordsplus.module.modules


import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RendererLivingEntity
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import org.lwjgl.opengl.GL11

object RenderNumbers : IModule {
    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "enableRenderer",
        comment = "Enable or disable the renderer",
        def = true
    )
    var showRender: Boolean = true

    val players: ArrayList<PlayerStat> = arrayListOf()

    init {
        EventBus.register<DamageHealingEvent> {
            players.add(
                PlayerStat(it)
            )
        }
    }

    /**
     * Called when a player is rendered
     * Frames * Player
     *
     */
    @SubscribeEvent
    fun onRenderPlayer(e: RenderPlayerEvent.Pre) {
        if (GameStateManager.notIngame && !showRender) return

        for (player in players) {
            if (player.event.player == e.entityPlayer.displayNameString) {
                renderName(
                    e.renderer,
                    getRenderString(
                        player
                    ),
                    e.entityPlayer,
                    e.x + player.position,
                    e.y - 1.2 + player.positionY,
                    e.z + player.position
                )
            }
        }
    }

    fun getRenderString(p: PlayerStat): String {
        var output: String = ""
        output += when {
            (p.event.absorbed) -> return "" + EnumChatFormatting.RED + "Absorbed!"
            (p.event.damage) -> "" + EnumChatFormatting.RED + "-" + p.event.amount
            else -> "" + EnumChatFormatting.GREEN + "+" + p.event.amount
        }
        if (p.event.crit)
            output += "!"
        return output
    }

    @SubscribeEvent
    fun onTickEvent(event: TickEvent.ClientTickEvent) {
        players.removeIf {
            it.time < System.currentTimeMillis()
        }
    }

    class PlayerStat(val event: DamageHealingEvent) {
        val time: Long = System.currentTimeMillis() + 1750
        val position = fun(): Double {
            val thing = Math.random() - 0.5
            return if (thing >= 0) thing + .65 else thing - .65
        }()
        val positionY = fun(): Double {
            val thing = Math.random() - 0.3
            return if (thing >= 0) thing + .3 else thing - .3
        }()
    }
}


/**
 * renders a string above a player copied from the original mc namerenderer
 *
 * @param renderer the renderer
 * @param str      the string to render
 * @param entityIn the entity to render above
 * @param x        x-cord
 * @param y        y-cord
 * @param z        z-cord
 */

fun renderName(
    renderer: RendererLivingEntity<*>,
    str: String,
    entityIn: EntityPlayer,
    x: Double,
    y: Double,
    z: Double
) {
    val fontrenderer = renderer.fontRendererFromRenderManager
    val f = 1.6f
    val f1 = 0.016666668f * f
    pushMatrix()
    translate(x.toFloat() + 0.0f, y.toFloat() + entityIn.height + 0.5f, z.toFloat())
    GL11.glNormal3f(0.0f, 1.0f, 0.0f)
    rotate(-renderer.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
    rotate(renderer.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
    scale(-f1, -f1, f1)
    disableLighting()
    depthMask(false)
    disableDepth()
    enableBlend()
    tryBlendFuncSeparate(770, 771, 1, 0)
    val tessellator = Tessellator.getInstance()
    val worldrenderer = tessellator.worldRenderer
    val i = 0
    val j = fontrenderer.getStringWidth(str) / 2
    disableTexture2D()
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
    worldrenderer.pos((-j - 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f)
        .endVertex()
    worldrenderer.pos((-j - 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f)
        .endVertex()
    worldrenderer.pos((j + 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f)
        .endVertex()
    worldrenderer.pos((j + 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f)
        .endVertex()
    tessellator.draw()
    enableTexture2D()
    fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127)
    enableDepth()
    depthMask(true)
    fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1)
    enableLighting()
    disableBlend()
    color(1.0f, 1.0f, 1.0f, 1.0f)
    popMatrix()
}

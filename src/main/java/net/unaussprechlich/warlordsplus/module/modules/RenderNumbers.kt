package net.unaussprechlich.warlordsplus.module.modules


import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import org.lwjgl.opengl.GL11
import kotlin.math.abs
import kotlin.math.exp

object RenderNumbers : IModule {
    @ConfigPropertyBoolean(
        category = CCategory.RENDERER,
        id = "showDamageHealingNumbersAroundPlayers",
        comment = "Enable or disable the Damage/Healing numbers when hitting/healing player",
        def = true
    )
    var showRender: Boolean = true

    val players: MutableMap<String, ArrayList<RenderObject>> = mutableMapOf()

    private fun addPlayerEvent(event: AbstractDamageHealEvent) {
        if (!players.contains(event.player))
            players[event.player] = arrayListOf(RenderObject(event))
        else players[event.player]?.add(RenderObject(event))
    }

    init {
        EventBus.register<DamageDoneEvent>(::addPlayerEvent)
        EventBus.register<HealingGivenEvent>(::addPlayerEvent)
        EventBus.register<DamageAbsorbedEvent>(::addPlayerEvent)
        EventBus.register<EnergyStolenEvent>(::addPlayerEvent)
        EventBus.register<EnergyGivenEvent>(::addPlayerEvent)
    }

    /**
     * Called when a player is rendered
     * Frames * Player
     *
     */
    @SubscribeEvent
    fun onRenderPlayer(e: RenderPlayerEvent.Pre) {
        if (GameStateManager.notIngame || !showRender) return
        if (!players.containsKey(e.entityPlayer.displayNameString)) return

        players[e.entityPlayer.displayNameString]?.forEach { it.render(e) }
    }


    @SubscribeEvent
    fun onTickEvent(event: TickEvent.ClientTickEvent) {
        players.values.forEach {
            it.removeIf { renderObject ->
                renderObject.time < System.currentTimeMillis()
            }
        }
    }

    class RenderObject(val event: AbstractDamageHealEvent) {
        val time: Long = System.currentTimeMillis() + 1750
        val zOffset = kotlin.math.sin(Math.random() * 365f) * 1.1
        val xOffset = kotlin.math.cos(Math.random() * 365f) * 1.1
        val yOffset = Math.random() * 0.5f

        fun getRenderString(): String {
            return when {
                (event.isAbsorbed) ->
                    "${EnumChatFormatting.RED} Absorbed!"
                (event is DamageDoneEvent) ->
                    "${if (event.isCrit) EnumChatFormatting.DARK_RED else EnumChatFormatting.RED}-${event.amount}${if (event.isCrit) "!" else ""}"
                (event is HealingGivenEvent) ->
                    "${if (event.isCrit) EnumChatFormatting.DARK_GREEN else EnumChatFormatting.GREEN}+${event.amount}${if (event.isCrit) "!" else ""}"
                (event is EnergyStolenEvent) ->
                    "${EnumChatFormatting.YELLOW}-${event.amount}"
                (event is EnergyGivenEvent) ->
                    "${EnumChatFormatting.YELLOW}+${event.amount}"
                else -> "UNDEFINED"
            }
        }

        /**
         * renders a string above a player copied from the original mc namerenderer
         */
        fun render(e: RenderPlayerEvent.Pre) {

            val animate = abs(time - 1750 - System.currentTimeMillis())

            val x = e.x + xOffset
            val y = e.y - 1 + yOffset - (exp(animate / 100f) * 0.000001)
            val z = e.z + zOffset

            val f1 = 0.016666668f * 1.6f

            val str = getRenderString()
            val strWidth = e.renderer.fontRendererFromRenderManager.getStringWidth(str)

            pushMatrix()
            translate(x.toFloat() + 0.0f, y.toFloat() + e.entityPlayer.height + 0.5f, z.toFloat())
            GL11.glNormal3f(0.0f, 1.0f, 0.0f)
            rotate(-e.renderer.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
            rotate(e.renderer.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
            scale(-f1, -f1, f1)

            disableLighting()
            depthMask(false)
            disableDepth()
            enableBlend()

            tryBlendFuncSeparate(770, 771, 1, 0)
            val tessellator = Tessellator.getInstance()

            e.renderer.fontRendererFromRenderManager.drawString(str, -strWidth / 2, 0, 553648127)
            enableDepth()
            depthMask(true)
            e.renderer.fontRendererFromRenderManager.drawString(str, -strWidth / 2, 0, -1)
            enableLighting()
            disableBlend()
            color(1.0f, 1.0f, 1.0f, 1.0f)
            popMatrix()
        }
    }
}




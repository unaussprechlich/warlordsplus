package net.unaussprechlich.warlordsplus.module.modules.stats

import kotlinx.serialization.UnstableDefault
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.config.ConfigPropertyInt
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.WarlordsPlusWorldRenderer
import net.unaussprechlich.warlordsplus.util.checkPreConditions

@UnstableDefault
object StatsDisplayRenderer : IModule {

    @ConfigPropertyBoolean(
        category = CCategory.STATS,
        id = "showStats",
        comment = "Enable or disable the StatsDisplay",
        def = true
    )
    var showStats = false

    @ConfigPropertyBoolean(
        category = CCategory.STATS,
        id = "autoShowStats",
        comment = "Switch between automatically showing the stats and sneaking to show.",
        def = false
    )
    var autoShowStats = false

    @ConfigPropertyInt(
        category = CCategory.STATS,
        id = "displayTime",
        comment = "The duration in ms displaying the StatsDisplay.",
        def = 20000
    )
    var displayTime = 20000

    val renderObjects: MutableMap<String, StatsRenderObject> = mutableMapOf()

    init {
        EventBus.register<ResetEvent> {
            if (autoShowStats) renderObjects.clear()
        }
    }

    @SubscribeEvent
    fun onRenderPlayer(e: RenderPlayerEvent.Post) {
        if (!GameStateManager.isWarlords /*|| TODO GameStateManager.isIngame*/) return
        if (!renderObjects.containsKey(e.entityPlayer.displayNameString)) return
        renderObjects[e.entityPlayer.displayNameString]?.render(e)
    }

    @SubscribeEvent
    fun onClientTick(e: TickEvent.ClientTickEvent) {
        if (e.checkPreConditions() || !showStats) return

        if (!autoShowStats)
            renderObjects.entries.removeIf { it.value.expires <= System.currentTimeMillis() }

        /*TODO if(GameStateManager.isIngame) return */

        val mc = Minecraft.getMinecraft()

        if (mc.thePlayer == null || !mc.thePlayer.isSneaking) return
        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) {
            if (mc.objectMouseOver.entityHit is EntityOtherPlayerMP) {
                if (renderObjects.containsKey(mc.objectMouseOver.entityHit.name)) return
                StatsLoader.loadPlayerWithCallback(
                    mc.objectMouseOver.entityHit.name,
                    StatsRenderObject(mc.objectMouseOver.entityHit.name)::callback
                )
            }
        }
    }

    class StatsRenderObject(val name: String) : WarlordsPlusWorldRenderer() {

        var expires = System.currentTimeMillis() + displayTime
        lateinit var data: WarlordsSrApiData
        var isLoading = true
        var error = false

        init {
            renderObjects[name] = this
        }

        fun callback(player: StatsLoader.PlayerCacheEntry) {
            isLoading = false
            expires = System.currentTimeMillis() + displayTime
            if (player.data == null) {
                error = true
                return
            }
            data = player.data
        }

        override fun onRender(event: RenderPlayerEvent.Post) {
            if (isLoading) {
                renderLoading(event)
                return
            } else if (error) {
                renderError(event)
                return
            }
            renderBasicStats(event)
            //renderPaladinStats(e)
            //renderWarriorStats(e)
            //renderMageStats(e)
            //renderShamanStats(e)
        }

        private fun renderLoading(e: RenderPlayerEvent.Post) {
            translateY(0.5)
            text {
                "Loading ...".drawCentered()
            }
        }

        private fun renderError(e: RenderPlayerEvent.Post) {
            translateY(0.5)
            text {
                "${EnumChatFormatting.RED}ERROR | maybe nicked?".drawCentered()
            }
        }

        fun renderBasicStats(e: RenderPlayerEvent.Post) {
            if (data.warlordsSr == null || data.warlordsHypixel == null) return

            val width = 150.0

            translateY(-30.0)
            translateX(80.0)

            scale(0.5)

            renderRectXCentered(width, 10.0, -0.5, Colors.DEF, 255)

            translateY(-1.0)

            text {
                "GeneralStats".drawCentered()
            }

            translateY(-9.0)
            renderRectXCentered(width, 30.0, -0.5, Colors.DEF, 100)

            text {
                translateX(-width / 2)
                translateY(-1.0)
                "SR: ${data.warlordsSr?.sR}".draw()
                translateY(-8.0)
                "WL: ${data.warlordsSr?.wL}".draw()
            }
        }

        fun renderPaladinStats(e: RenderPlayerEvent.Pre) {

            val paladinInfo = ArrayList<String>()

            paladinInfo.add("" + EnumChatFormatting.YELLOW + "Paladin")
            if (data?.warlordsSr?.paladin?.SR != null)
                paladinInfo.add("SR: " + (data.warlordsSr?.paladin?.SR))
            if (data?.warlordsSr?.paladin?.WL != null)
                paladinInfo.add("WL: " + data.warlordsSr?.paladin?.WL)
            if (data?.warlordsSr?.paladin?.DHP != null)
                paladinInfo.add("DHP: " + data.warlordsSr?.paladin?.DHP)
            var yOffset = .5

        }

        fun renderWarriorStats(e: RenderPlayerEvent.Pre) {

            val warriorInfo = ArrayList<String>()

            warriorInfo.add("" + EnumChatFormatting.BLACK + "Warrior")
            if (data?.warlordsSr?.warrior?.SR != null)
                warriorInfo.add("SR: " + (data.warlordsSr?.warrior?.SR))
            if (data?.warlordsSr?.warrior?.WL != null)
                warriorInfo.add("WL: " + data.warlordsSr?.warrior?.WL)
            if (data?.warlordsSr?.warrior?.DHP != null)
                warriorInfo.add("DHP: " + data.warlordsSr?.warrior?.DHP)
            var yOffset = .5

        }

        fun renderMageStats(e: RenderPlayerEvent.Pre) {

            val mageInfo = ArrayList<String>()

            mageInfo.add("" + EnumChatFormatting.BLUE + "Mage")
            if (data?.warlordsSr?.mage?.SR != null)
                mageInfo.add("SR: " + (data.warlordsSr?.mage?.SR))
            if (data?.warlordsSr?.mage?.WL != null)
                mageInfo.add("WL: " + data.warlordsSr?.mage?.WL)
            if (data?.warlordsSr?.mage?.DHP != null)
                mageInfo.add("DHP: " + data.warlordsSr?.mage?.DHP)
            var yOffset = .5

        }

        fun renderShamanStats(e: RenderPlayerEvent.Pre) {

            val shamanInfo = ArrayList<String>()

            shamanInfo.add("" + EnumChatFormatting.GRAY + "Shaman")
            if (data.warlordsSr?.shaman?.SR != null)
                shamanInfo.add("SR: " + (data.warlordsSr?.shaman?.SR))
            if (data.warlordsSr?.shaman?.WL != null)
                shamanInfo.add("WL: " + data.warlordsSr?.shaman?.WL)
            if (data.warlordsSr?.shaman?.DHP != null)
                shamanInfo.add("DHP: " + data.warlordsSr?.shaman?.DHP)
            var yOffset = .5

        }
    }
}



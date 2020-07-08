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
        if (!GameStateManager.isWarlords || GameStateManager.isIngame) return
        if (!renderObjects.containsKey(e.entityPlayer.displayNameString)) {
            if (autoShowStats && StatsLoader.containsPlayer(e.entityPlayer.displayNameString)) {
                val data = StatsLoader.getPlayer(e.entityPlayer.displayNameString)?.data
                if (data != null) {
                    val renderObject = StatsRenderObject(e.entityPlayer.displayNameString)
                    renderObject.data = data
                    renderObject.isLoading = false
                }
            }
        }
        renderObjects[e.entityPlayer.displayNameString]?.render(e)
    }

    @SubscribeEvent
    fun onClientTick(e: TickEvent.ClientTickEvent) {
        if (e.checkPreConditions() || !showStats) return

        if (autoShowStats) return

        renderObjects.entries.removeIf { it.value.expires <= System.currentTimeMillis() }

        if (GameStateManager.isIngame || !GameStateManager.isWarlords) return

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
            renderBasicStats()
            renderPaladinStats()
            renderWarriorStats()
            renderMageStats()
            renderShamanStats()
        }

        private fun renderLoading(e: RenderPlayerEvent.Post) {
            translateY(0.5)
            "Loading ...".drawCentered()
        }

        private fun renderError(e: RenderPlayerEvent.Post) {
            translateY(0.5)
            "${EnumChatFormatting.RED}ERROR | maybe nicked?".drawCentered()
        }

        fun renderBasicStats() {
            if (data.warlordsSr == null || data.warlordsHypixel == null) return

            glMatrix {
                val width = 150.0
                translateZ(-5.0)
                translateY(40.0)
                renderRectXCentered(width, 10.0, -0.5, Colors.DEF, 255)
                translateY(-1.0)
                glMatrix {
                    scale(.8)
                    translateY(-1.0)
                    "GeneralStats".drawCentered()
                }
                translateY(-9.0)
                renderRectXCentered(width, 20.0, -0.5, Colors.DEF, 100)
                scale(.6)
                translateX(-width * .825)
                translateY(-1.0)
                "SR: ${data.warlordsSr?.sR}".draw()
                translateY(-8.0)
                "Plays: ${data.warlordsSr?.plays}".draw()
                translateY(-8.0)
                "Blue Wins: ${data.warlordsHypixel?.winsBlu}".draw()
                translateY(-8.0)
                "Red Wins: ${data.warlordsHypixel?.winsRed}".draw()

                translateX(92.5)
                translateY(25.0)

                translateY(-1.0)
                "W/L: ${data.warlordsSr?.wL}".draw()
                translateY(-8.0)
                "Wins: ${data.warlordsHypixel?.wins}".draw()
                translateY(-8.0)
                "Losses: ${data.warlordsHypixel?.losses}".draw()
                translateY(-8.0)
                "AFKs: ${data.warlordsHypixel?.penalty}".draw()

                translateX(85.0)
                translateY(25.0)

                translateY(-1.0)
                "K/D: ${data.warlordsSr?.kD}".draw()
                translateY(-8.0)
                "KA/D: ${data.warlordsSr?.kDA}".draw()
                translateY(-8.0)
                "Kills: ${data.warlordsHypixel?.kills}".draw()
                translateY(-8.0)
                "Deaths: ${data.warlordsHypixel?.deaths}".draw()

            }
        }

        fun renderPaladinStats() {
            if (data.warlordsSr == null || data.warlordsHypixel == null) return

            glMatrix {
                val width = 80.0
                translateZ(-10.0)
                translateY(-12.0)
                translateX(-110.0)
                renderRectXCentered(width / 1.8, 10.0, -0.5, Colors.DEF, 255)
                translateY(-2.0)
                glMatrix {
                    scale(.6)
                    translateY(-1.0)

                    "Paladin${if (data.ranking?.paladin?.overall != null) " #${data.ranking?.paladin?.overall}" else ""}".drawCentered()

                }
                translateY(-8.0)
                renderRectXCentered(width / 1.8, 20.0, -0.5, Colors.DEF, 100)

                scale(.7)
                translateX(-width * .37)
                translateY(-2.0)
                "SR: ${data.warlordsSr?.paladin?.SR}".draw()
                translateY(-8.0)
                "WL: ${data.warlordsSr?.paladin?.WL}".draw()
                translateY(-8.0)
                "DHP: ${data.warlordsSr?.paladin?.DHP}".draw()

            }
        }

        fun renderWarriorStats() {
            if (data.warlordsSr == null || data.warlordsHypixel == null) return

            glMatrix {
                val width = 80.0
                translateZ(-10.0)
                translateY(-12.0)
                translateX(-60.0)
                renderRectXCentered(width / 1.8, 10.0, -0.5, Colors.DEF, 255)
                translateY(-3.0)
                glMatrix {
                    scale(.6)

                    "Warrior${if (data.ranking?.warrior?.overall != null) " #${data.ranking?.warrior?.overall}" else ""}".drawCentered()

                }
                translateY(-8.0)
                renderRectXCentered(width / 1.8, 20.0, -0.5, Colors.DEF, 100)

                scale(.7)
                translateX(-width * .36)
                translateY(-1.0)
                "SR: ${data.warlordsSr?.warrior?.SR}".draw()
                translateY(-8.0)
                "WL: ${data.warlordsSr?.warrior?.WL}".draw()
                translateY(-8.0)
                "DHP: ${data.warlordsSr?.warrior?.DHP}".draw()

            }
        }

        fun renderMageStats() {
            if (data.warlordsSr == null || data.warlordsHypixel == null) return

            glMatrix {
                val width = 80.0
                translateZ(-10.0)
                translateY(-12.0)
                translateX(60.0)
                renderRectXCentered(width / 1.8, 10.0, -0.5, Colors.DEF, 255)
                translateY(-2.0)
                glMatrix {
                    scale(.6)

                    "Mage${if (data.ranking?.mage?.overall != null) " #${data.ranking?.mage?.overall}" else ""}".drawCentered()

                }
                translateY(-8.0)
                renderRectXCentered(width / 1.8, 20.0, -0.5, Colors.DEF, 100)

                scale(.7)
                translateX(-width * .36)
                translateY(-2.0)
                "SR: ${data.warlordsSr?.mage?.SR}".draw()
                translateY(-8.0)
                "WL: ${data.warlordsSr?.mage?.WL}".draw()
                translateY(-8.0)
                "DHP: ${data.warlordsSr?.mage?.DHP}".draw()

            }
        }

        fun renderShamanStats() {
            if (data.warlordsSr == null || data.warlordsHypixel == null) return

            glMatrix {
                val width = 80.0
                translateZ(-10.0)
                translateY(-12.0)
                translateX(110.0)
                renderRectXCentered(width / 1.8, 10.0, -0.5, Colors.DEF, 255)
                translateY(-2.0)
                glMatrix {
                    scale(.6)

                    "Shaman${if (data.ranking?.shaman?.overall != null) " #${data.ranking?.shaman?.overall}" else ""}".drawCentered()

                }
                translateY(-8.0)
                renderRectXCentered(width / 1.8, 20.0, -0.5, Colors.DEF, 100)

                scale(.7)
                translateX(-width * .36)
                translateY(-2.0)
                "SR: ${data.warlordsSr?.shaman?.SR}".draw()
                translateY(-8.0)
                "WL: ${data.warlordsSr?.shaman?.WL}".draw()
                translateY(-8.0)
                "DHP: ${data.warlordsSr?.shaman?.DHP}".draw()

            }
        }
    }
}



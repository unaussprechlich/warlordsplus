package net.unaussprechlich.warlordsplus.module.modules.stats

import kotlinx.serialization.UnstableDefault
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.config.ConfigPropertyInt
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.Colors

@UnstableDefault
object StatsDisplayRenderer : IModule, RenderApi.Player() {

    val renderObjects: MutableMap<String, StatsRenderObject> = mutableMapOf()

    init {
        EventBus.register<ResetEvent> {
            if (autoShowStats) renderObjects.clear()
        }

        EventBus.register(::onClientTick)
    }

    override fun shouldRender(event: RenderPlayerEvent.Post): Boolean {
        return GameStateManager.isWarlords && !GameStateManager.isIngame
    }

    private fun onClientTick(e: TickEvent.ClientTickEvent) {
        if (!showStats) return

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

    override fun onRender(event: RenderPlayerEvent.Post) {
        if (!renderObjects.containsKey(event.entityPlayer.displayNameString)) {
            if (autoShowStats && StatsLoader.containsPlayer(event.entityPlayer.displayNameString)) {
                val data = StatsLoader.getPlayer(event.entityPlayer.displayNameString)?.data
                if (data != null) {
                    val renderObject = StatsRenderObject(event.entityPlayer.displayNameString)
                    renderObject.data = data
                    renderObject.isLoading = false
                }
            }
        }

        renderObjects[event.entityPlayer.displayNameString]?.onRender(event)
    }

    class StatsRenderObject(val name: String) {

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

        fun onRender(event: RenderPlayerEvent.Post) {
            if (isLoading) {
                renderLoading(event)
                return
            } else if (error) {
                renderError(event)
                return
            }

            if (showStats) {
                if (showBasicStats)
                    renderBasicStats()
                if (showClassStats) {
                    translateX(-110.0)
                    renderClassStats(
                        "Paladin",
                        data.warlordsSr?.paladin?.SR,
                        data.warlordsSr?.paladin?.WL,
                        data.warlordsSr?.paladin?.DHP,
                        data.warlordsSr?.paladin?.LEVEL
                    )
                    translateX(50.0)
                    renderClassStats(
                        "Warrior",
                        data.warlordsSr?.warrior?.SR,
                        data.warlordsSr?.warrior?.WL,
                        data.warlordsSr?.warrior?.DHP,
                        data.warlordsSr?.warrior?.LEVEL
                    )
                    translateX(120.0)
                    renderClassStats(
                        "Mage",
                        data.warlordsSr?.mage?.SR,
                        data.warlordsSr?.mage?.WL,
                        data.warlordsSr?.mage?.DHP,
                        data.warlordsSr?.mage?.LEVEL
                    )
                    translateX(50.0)
                    renderClassStats(
                        "Shaman",
                        data.warlordsSr?.shaman?.SR,
                        data.warlordsSr?.shaman?.WL,
                        data.warlordsSr?.shaman?.DHP,
                        data.warlordsSr?.shaman?.LEVEL
                    )
                }
            }
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
                renderRectXCentered(width, 10.0, Colors.DEF, 255, -0.5)
                translateY(-1.0)
                glMatrix {
                    scale(.8)
                    translateY(-1.0)
                    "GeneralStats".drawCentered()
                }
                translateY(-9.0)
                renderRectXCentered(width, 20.0, Colors.DEF, 100, -0.5)
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

        fun renderClassStats(name: String, sr: Int?, wl: Double?, dhp: Int?, rank: Int?) {
            if (data.warlordsSr == null || data.warlordsHypixel == null) return

            glMatrix {
                val width = 80.0
                translateZ(-10.0)
                translateY(-12.0)
                renderRectXCentered(width / 1.8, 10.0, Colors.DEF, 255, -0.5)
                translateY(-2.0)
                glMatrix {
                    scale(.6)
                    translateY(-1.0)

                    "$name${if (rank != null) " Lv$rank" else ""}".drawCentered()

                }
                translateY(-8.0)
                renderRectXCentered(width / 1.8, 20.0, Colors.DEF, 100, -0.5)

                scale(.7)
                translateX(-width * .37)
                translateY(-2.0)
                "SR: $sr".draw()
                translateY(-8.0)
                "WL: $wl".draw()
                translateY(-8.0)
                "DHP: $dhp".draw()

            }
        }
    }

    @ConfigPropertyBoolean(
        category = CCategory.STATS,
        id = "Stats | Show",
        comment = "Enable or disable the StatsDisplay",
        def = true
    )
    var showStats = false

    @ConfigPropertyBoolean(
        category = CCategory.STATS,
        id = "Stats | Show General",
        comment = "Enable or disable the Basic Stats Above Heads",
        def = true
    )
    var showBasicStats = false

    @ConfigPropertyBoolean(
        category = CCategory.STATS,
        id = "Stats | Show Class",
        comment = "Enable or disable Showing Stats of Each Class",
        def = true
    )
    var showClassStats = false

    @ConfigPropertyBoolean(
        category = CCategory.STATS,
        id = "Auto Show Stats",
        comment = "Switch between automatically showing the stats and sneaking to show.",
        def = true
    )
    var autoShowStats = true

    @ConfigPropertyInt(
        category = CCategory.STATS,
        id = "Stats | Display Time",
        comment = "The duration in ms displaying the StatsDisplay.",
        def = 60000
    )
    var displayTime = 60000
}



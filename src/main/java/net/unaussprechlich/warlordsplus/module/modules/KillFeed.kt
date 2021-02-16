package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.OtherPlayers.playersMap
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.SpecsEnumSuper
import net.unaussprechlich.warlordsplus.util.TeamEnum

object KillFeed : RenderApi.Gui<RenderGameOverlayEvent.Chat>(), IModule {

    private val kills: ArrayList<KillEvent> = arrayListOf()

    init {
        EventBus.register<KillEvent> {
            kills.add(it)
        }
        EventBus.register<TickEvent.ClientTickEvent> {
            if (kills.isNotEmpty()) {
                kills.removeIf { it.sysTime + 10000 < System.currentTimeMillis() }
            }
        }
        EventBus.register(::render)
    }

    override fun shouldRender(e: RenderGameOverlayEvent.Chat): Boolean {
        return GameStateManager.isIngame
    }

    override fun onRender(event: RenderGameOverlayEvent.Chat) {
        if (!show || kills.isEmpty()) return

        glMatrix {
            translate(xRight)
            kills.forEach {
                translate(-5.0, 7.0) {
                    val kill = StringBuilder()
                    try {
                        kill.append(getSpec(it.player))
                        if (playersMap[it.player]!!.team == TeamEnum.BLUE) {
                            kill.append("${EnumChatFormatting.BLUE}${it.player} ${EnumChatFormatting.WHITE}-> ")
                        } else {
                            kill.append("${EnumChatFormatting.RED}${it.player} ${EnumChatFormatting.WHITE}-> ")
                        }
                        kill.append(getSpec(it.deathPlayer))
                        if (playersMap[it.deathPlayer]!!.team == TeamEnum.BLUE) {
                            kill.append("${EnumChatFormatting.BLUE}${it.deathPlayer}")
                        } else {
                            kill.append("${EnumChatFormatting.RED}${it.deathPlayer}")
                        }
                        kill.toString().drawLeft()
                        translateY(-11)
                    } catch (e: Exception) {

                    }

                }
            }

        }
    }

    private fun getSpec(player: String): String {
        return when (playersMap[player]!!.superSpec) {
            SpecsEnumSuper.DAMAGE -> {
                "${EnumChatFormatting.DARK_RED}銌"
            }
            SpecsEnumSuper.TANK -> {
                "${EnumChatFormatting.GOLD}鉰"
            }
            SpecsEnumSuper.HEALER -> {
                "${EnumChatFormatting.DARK_GREEN}銀"
            }
            else -> {
                ""
            }

        }
    }

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "showKillFeedTopRight",
        comment = "Enable or disable the kill feed",
        def = true
    )
    var show = true
}


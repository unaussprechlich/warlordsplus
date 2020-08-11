package net.unaussprechlich.warlordsplus.module

import kotlinx.serialization.UnstableDefault
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.EasyConfigHandler
import net.unaussprechlich.warlordsplus.hud.HudManager
import net.unaussprechlich.warlordsplus.ingamegui.IngameGuiManager
import net.unaussprechlich.warlordsplus.module.modules.*
import net.unaussprechlich.warlordsplus.module.modules.stats.StatsDisplayRenderer
import net.unaussprechlich.warlordsplus.module.modules.stats.StatsLoader
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer

@UnstableDefault
object ModuleManager {

    private val modules = ArrayList<IModule>()

    init {
        with(modules) {
            add(EasyConfigHandler)
            add(ScoreboardManager)
            add(GameStateManager)
            //add(Meme)
            add(DamageAndHealParser)
            add(KillAssistParser)
            add(FlagTakenDetector)
            add(ThePlayer)
            add(HudManager.INSTANCE())
            add(IngameGuiManager)
            add(ChatDetector)
            add(RenderNumbers)
            add(StatsLoader)
            add(StatsDisplayRenderer)
            add(RenderStatsInGame)
            //add(testRender)
        }
    }

    fun register() {
        MinecraftForge.EVENT_BUS.register(this)
        modules.forEach(MinecraftForge.EVENT_BUS::register)
    }
}


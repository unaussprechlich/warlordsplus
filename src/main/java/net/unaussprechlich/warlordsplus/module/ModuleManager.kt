package net.unaussprechlich.warlordsplus.module

import kotlinx.serialization.UnstableDefault
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.EasyConfigHandler
import net.unaussprechlich.warlordsplus.hud.HudManager
import net.unaussprechlich.warlordsplus.ingamegui.IngameGuiManager
import net.unaussprechlich.warlordsplus.module.modules.*
import net.unaussprechlich.warlordsplus.stats.StatsRenderer
import net.unaussprechlich.warlordsplus.stats.WarlordsSrApi

@UnstableDefault
object ModuleManager {

    private val modules = ArrayList<IModule>()

    init {
        with(modules){
            add(EasyConfigHandler)
            add(ScoreboardManager)
            add(GameStateManager)
            add(Meme)
            add(DamageAndHealParser)
            add(KillAssistParser)
            add(FlagTakenDetector)
            add(ThePlayer)
            add(HudManager.INSTANCE())
            add(IngameGuiManager)
            add(ChatDetector)
            add(RenderNumbers)
            add(WarlordsSrApi)
            add(StatsRenderer)
        }
    }

    fun register(){
        MinecraftForge.EVENT_BUS.register(this)
        modules.forEach(MinecraftForge.EVENT_BUS::register)
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if(GameStateManager.notIngame) return;
        try {

            if (event.message.formattedText == "§r§eThe gates will fall in §r§c5 §r§eseconds!§r") {
                EventBus.post(ResetEvent())
            }

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}

data class ResetEvent(val time: Long = System.currentTimeMillis()) : IEvent
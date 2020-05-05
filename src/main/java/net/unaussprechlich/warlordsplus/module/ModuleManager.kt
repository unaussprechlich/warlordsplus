package net.unaussprechlich.warlordsplus.module

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.warlordsplus.config.EasyConfigHandler
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager
import net.unaussprechlich.warlordsplus.hud.HudManager
import net.unaussprechlich.warlordsplus.ingamegui.IngameGuiManager
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.Meme
import net.unaussprechlich.warlordsplus.util.consumers.IResetConsumer


object ModuleManager {

    private val modules = ArrayList<IModule>()

    init {
        with(modules){
            add(EasyConfigHandler)
            add(ScoreboardManager)
            add(GameStateManager)
            add(Meme)
            add(HudManager.INSTANCE())
            add(IngameGuiManager)
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
                modules.filter { it is IResetConsumer }.forEach {
                    (it as IResetConsumer).reset()
                }
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}
package net.unaussprechlich.warlordsplus.ingamegui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.*
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.warlordsplus.ingamegui.components.ChatComponent
import net.unaussprechlich.warlordsplus.ingamegui.components.EnergyComponent
import net.unaussprechlich.warlordsplus.ingamegui.components.HealthComponent
import net.unaussprechlich.warlordsplus.ingamegui.components.WhoIsWinningComponent
import net.unaussprechlich.warlordsplus.ingamegui.components.scoreboard.ScoreboardComponent
import net.unaussprechlich.warlordsplus.ingamegui.components.skills.*
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer


object IngameGuiManager : IModule{

    private val components = ArrayList<AbstractRenderComponent>()

    init {

        with(components){
            add(HealthComponent)
            add(EnergyComponent)
            add(RedThingyComponent)
            add(BlueThingyComponent)
            add(PurpleThingyComponent)
            add(YellowThingyComponent)
            add(HorseComponent)
            add(WhoIsWinningComponent)
            add(ScoreboardComponent)
            add(ChatComponent)
        }

    }

    @SubscribeEvent
    fun onTick(@Suppress("UNUSED_PARAMETER") e : TickEvent.ClientTickEvent){
        if(GameStateManager.isIngame){
            components.filter { it is IUpdateConsumer }.forEach{
                (it as IUpdateConsumer).update()
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    fun render(e : RenderGameOverlayEvent.Pre){

        if(GameStateManager.notIngame) return
        if(Minecraft.getMinecraft().currentScreen is GuiChat) return

        when(e.type){
            PLAYER_LIST -> {
                ScoreboardComponent.render(e)
                e.isCanceled = true
            }
            BOSSHEALTH -> {
                //e.isCanceled = true
            }
            ARMOR -> {
                //e.isCanceled = true
            }
            HEALTH -> {
                //e.isCanceled = true
            }
            FOOD -> {
                //e.isCanceled = true
            }
            HOTBAR -> {
                //RedThingyComponent.render(e)
                //PurpleThingyComponent.render(e)
                //BlueThingyComponent.render(e)
                //YellowThingyComponent.render(e)
                //e.isCanceled = true
            }
            EXPERIENCE -> {
                //EnergyComponent.render(e)
                //e.isCanceled = true
            }
            TEXT -> {

                //e.isCanceled = true
            }
            HEALTHMOUNT -> {
                //e.isCanceled = true
            }
            JUMPBAR -> {
                //e.isCanceled = true
            }
            CHAT -> {
                ChatComponent.render(e)
                //e.isCanceled = true
            }

            else -> return
        }

        return
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if(GameStateManager.notIngame) return;
        try {

            components.filter { it is IChatConsumer }.forEach{
                (it as IChatConsumer).onChat(event)
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}
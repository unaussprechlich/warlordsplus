package net.unaussprechlich.warlordsplus.ingamegui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.warlordsplus.WarlordsPlus
import net.unaussprechlich.warlordsplus.ingamegui.components.EnergyComponent
import net.unaussprechlich.warlordsplus.ingamegui.components.HealthComponent
import net.unaussprechlich.warlordsplus.ingamegui.components.WhoIsWinningComponent
import net.unaussprechlich.warlordsplus.ingamegui.components.skills.*
import net.unaussprechlich.warlordsplus.ingamegui.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.ingamegui.consumers.IResetConsumer
import net.unaussprechlich.warlordsplus.ingamegui.consumers.IUpdateConsumer


object IngameGuiManager {

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
        }

    }

    @SubscribeEvent
    fun onTick(e : TickEvent.ClientTickEvent){
        if(WarlordsPlus.isIngame()){

            components.filter { it is IUpdateConsumer }.forEach{
                (it as IUpdateConsumer).update()
            }

            GuiIngameForge.renderHelmet = false
            GuiIngameForge.renderPortal = false
            GuiIngameForge.renderHotbar = false
            GuiIngameForge.renderBossHealth = false
            GuiIngameForge.renderHealth = false
            GuiIngameForge.renderArmor = false
            GuiIngameForge.renderFood = false
            GuiIngameForge.renderHealthMount = false
            GuiIngameForge.renderExperiance = false
            GuiIngameForge.renderJumpBar = false
            GuiIngameForge.renderObjective = false

        } else {
            GuiIngameForge.renderHelmet = true
            GuiIngameForge.renderPortal = true
            GuiIngameForge.renderHotbar = true
            GuiIngameForge.renderBossHealth = true
            GuiIngameForge.renderHealth = true
            GuiIngameForge.renderArmor = true
            GuiIngameForge.renderFood = true
            GuiIngameForge.renderHealthMount = true
            GuiIngameForge.renderExperiance = true
            GuiIngameForge.renderJumpBar = true
            GuiIngameForge.renderObjective = true
        }
    }


    @SubscribeEvent(priority = EventPriority.NORMAL)
    fun render(e : RenderGameOverlayEvent.Post) : Boolean{
        if(!WarlordsPlus.isIngame()) return false
        if(Minecraft.getMinecraft().currentScreen is GuiChat) return false

        if(e.type == RenderGameOverlayEvent.ElementType.ALL)
            components.forEach{
                it.render(e)
            }

        return false
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if(!WarlordsPlus.isIngame()) return;
        try {

            components.filter { it is IChatConsumer }.forEach{
                (it as IChatConsumer).onChat(event)
            }

            components.filter { it is IResetConsumer }.forEach {
                (it as IResetConsumer).reset()
            }

            if (event.message.formattedText == "§r§eThe gates will fall in §r§c5 §r§eseconds!§r") {
                components.filter { it is IResetConsumer }.forEach{
                    (it as IResetConsumer).reset()
                }
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }



}
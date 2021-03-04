package net.unaussprechlich.warlordsplus.module.modules.notificationmodule

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.renderapi.RenderBasics
import net.unaussprechlich.warlordsplus.module.IModule

object NotificationManager : RenderApi.Gui<RenderGameOverlayEvent.Chat>(), IModule {

    private val notifications: ArrayList<AbstractNotification> = arrayListOf()

    init {
        EventBus.register(::render)
        EventBus.register<TickEvent.ClientTickEvent> {
            if (notifications.isNotEmpty()) {
                notifications.removeIf { it.validUntil <= System.currentTimeMillis() }
            }
        }
    }

    override fun shouldRender(e: RenderGameOverlayEvent.Chat): Boolean {
        if (Minecraft.getMinecraft().currentScreen is GuiChat) return false
        if(notifications.isEmpty()) return false
        return super.shouldRender(e)
    }

    override fun onRender(event: RenderGameOverlayEvent.Chat) {
        glMatrix {
            translate(xRight - 5.0, yTop + 5.0)
            notifications.forEach {
                GlStateManager.pushMatrix()
                val height = it.onRender()
                GlStateManager.popMatrix()
                translateY(- (height + 3.0))
            }
        }
    }

    fun add(notification : AbstractNotification){
        notifications.add(notification)
    }

}

abstract class AbstractNotification : RenderBasics(){
    /**
     * The notification will be displayed until the value is <= currentMillis
     */
    abstract val validUntil : Long

    /**
     * The render methode of the notification
     */
    abstract fun onRender() : Int
}

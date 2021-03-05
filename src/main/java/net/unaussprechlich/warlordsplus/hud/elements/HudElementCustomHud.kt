package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import java.util.*

object HudElementCustomHud : AbstractHudElement(), IChatConsumer {

    var counter: Int = 0

    init {
        EventBus.register<ResetEvent> {
            counter = 0
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        //if (chatCheck.isNotEmpty())
        //    renderStrings.add(EnumChatFormatting.WHITE.toString() + chatCheckName + ": " + counter)

        return renderStrings.toTypedArray()
    }

    override fun onChat(e: ClientChatReceivedEvent) {
//        val message = e.message.formattedText
//        val toCheck = chatCheck.split(",")
//        if (toCheck.contains(chatCheck))
//            counter++
    }

    override fun isVisible(): Boolean {
        return GameStateManager.isIngame
    }

    override fun isEnabled(): Boolean {
        return GameStateManager.isWarlords
    }

//    @ConfigPropertyString(
//        category = CCategory.HUD,
//        id = "chatCheck",
//        comment = "Message to check in chat to update the custom counter",
//        def = ""
//    )
//    var chatCheck = ""
//
//    @ConfigPropertyString(
//        category = CCategory.HUD,
//        id = "chatCheckName",
//        comment = "Custom Counter Name",
//        def = ""
//    )
//    var chatCheckName = ""
}
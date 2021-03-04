package net.unaussprechlich.warlordsplus.module.modules.notificationmodule.notifications

import net.minecraft.util.EnumChatFormatting
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.config.ConfigPropertyInt
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.ChatDetector
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.AbstractNotification
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.NotificationManager
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.SoundsRegistry

object ChatNotifications : IModule {

    init {
        EventBus.register<ChatDetector.ChatMessageEvent> {
            if (isEnabled && !it.isThePlayer()) {
                NotificationManager.add(ChatNotification(it))
            }
        }

        EventBus.register<ChatDetector.ChatMessageEvent> {
            if (isPlaySound && !it.isThePlayer())
                SoundsRegistry.MEME_GRINDR_NOTIFICATION.play()
        }
    }

    private class ChatNotification(
        val chatMessage : ChatDetector.ChatMessageEvent
    ) : AbstractNotification() {

        override val validUntil: Long = System.currentTimeMillis() + duration

        override fun onRender(): Int {
            translateX(-width)
            renderRect(width, 11, Colors.DEF, 200)
            translate(5.0, 2.0) {
                "${chatMessage.type.color.enumColor}${chatMessage.type.chatName} ${EnumChatFormatting.RESET}${chatMessage.playerNameFormatted}".draw()
            }

            val renderStrings = fontRenderer.listFormattedStringToWidth(
                "${EnumChatFormatting.RESET}${chatMessage.message}",
                width - 10
            )

            renderRect(2, 12 + renderStrings.size * 9, chatMessage.type.color, 200)
            translateY(-11)
            renderRect(width, renderStrings.size * 9 + 1, Colors.DEF, 100)

            translateY(-1)
            translateX(5) {
                renderStrings.forEach { s ->
                    s.draw()
                    translateY(-9)
                }
            }
            return 12 + renderStrings.size * 9
        }

    }

    //Config ###########################################################################################################

    @ConfigPropertyBoolean(
        category = CCategory.CHAT,
        id = "enabled",
        comment = "Enable the chat notification component",
        def = true
    )
    var isEnabled = true

    @ConfigPropertyBoolean(
        category = CCategory.CHAT,
        id = "notificationsound",
        comment = "Plays a notification sound if set to true.",
        def = true
    )
    var isPlaySound = true

    @ConfigPropertyInt(
        category = CCategory.CHAT,
        id = "duration",
        comment = "The duartion in ms for showing the chat notification",
        def = 10000
    )
    var duration = 10000

    @ConfigPropertyInt(
        category = CCategory.CHAT,
        id = "width",
        comment = "The width of the chat notification",
        def = 250
    )
    var width = 250


}
package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.config.ConfigPropertyInt
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.Colors

object ChatNotifications : RenderApi.Gui<RenderGameOverlayEvent.Chat>(), IModule {

    private val messages: ArrayList<ChatDetector.ChatMessageEvent> = arrayListOf()

    init {
        EventBus.register<ChatDetector.ChatMessageEvent> {
            if (!it.isThePlayer()) {
                messages.add(it)
            }
        }
        EventBus.register<TickEvent.ClientTickEvent> {
            if (messages.isNotEmpty() && isEnabled) {
                messages.removeIf { it.time + duration < System.currentTimeMillis() }
            }
        }
        EventBus.register(::render)
    }

    override fun shouldRender(e: RenderGameOverlayEvent.Chat): Boolean {
        if (Minecraft.getMinecraft().currentScreen is GuiChat) return false
        return super.shouldRender(e)
    }

    override fun onRender(event: RenderGameOverlayEvent.Chat) {
        if (messages.isEmpty() || !isEnabled) return

        glMatrix {
            translate(xRight - width - 5.0, yTop + 5.0)
            messages.forEach {
                renderRect(width, 11, Colors.DEF, 200)
                translate(5.0, 2.0) {
                    "${it.type.color.enumColor}${it.type.chatName} ${EnumChatFormatting.RESET}${it.playerNameFormatted}".draw()
                }

                val renderStrings = fontRenderer.listFormattedStringToWidth(
                    "${EnumChatFormatting.RESET}${it.message}",
                    width - 10
                )

                renderRect(2, 12 + renderStrings.size * 9, it.type.color, 200)
                translateY(-11)
                renderRect(width, renderStrings.size * 9 + 1, Colors.DEF, 100)

                translateY(-1)
                translateX(5) {
                    renderStrings.forEach { s ->
                        s.draw()
                        translateY(-9)
                    }
                }
                translateY(-3)
            }
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
package net.unaussprechlich.warlordsplus.ingamegui.components

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.config.ConfigPropertyInt
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.module.modules.ChatDetector
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer


object ChatComponent : AbstractRenderComponent(), IUpdateConsumer {

    private val messages: ArrayList<ChatDetector.ChatMessageEvent> = arrayListOf()

    init {
        EventBus.register<ChatDetector.ChatMessageEvent> {
            if (GameStateManager.isIngame) messages.add(it)
        }
    }

    override fun render(e: RenderGameOverlayEvent.Pre) {
        if (messages.isEmpty() && isEnabled) return

        val xStart = xRight - width - 5
        val yStart = yTop + 5

        var offset = 0

        messages.forEach {

            drawRect(xStart, yStart + offset, width, 11, Colors.DEF.HEADER)
            drawString(xStart + 5, yStart + offset + 2, it.playerNameFormatted, false)

            offset += 11

            val renderStrings = fontRenderer.listFormattedStringToWidth(
                "${it.type.color.enumColor}${it.type.chatName} > ${EnumChatFormatting.RESET}${it.message}",
                width - 6
            )

            drawBackgroundRect(xStart, yStart + offset, width, renderStrings.size * 9 + 1)

            renderStrings.forEachIndexed { index, s ->
                drawString(xStart + 5, yStart + offset + index * 9 + 1, s, true)
            }

            drawRect(xStart, yStart + offset - 11, 2, 12 + renderStrings.size * 9, it.type.color.HEADER)

            offset += renderStrings.size * 9 + 3
        }
    }


    override fun update() {
        if (messages.isEmpty() || !isEnabled) return
        messages.removeIf { it.time + duration < System.currentTimeMillis() }
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
        def = 2000
    )
    var duration = 2000

    @ConfigPropertyInt(
        category = CCategory.CHAT,
        id = "width",
        comment = "The width of the chat notification",
        def = 250
    )
    var width = 250

}
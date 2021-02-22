package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardNames
import net.unaussprechlich.warlordsplus.util.removeFormatting

object AutoMapFinder : IModule {

    var future = -1L

    init {
        EventBus.register<TickEvent.ClientTickEvent> {
            if (GameStateManager.inLobby && enabled) {
                try {
                    if (scoreboardNames[7].removeFormatting().isNotEmpty() && (scoreboardNames[7].removeFormatting()
                            .substring(12, 17) == "00:27" || scoreboardNames[7].removeFormatting()
                            .substring(12, 17) == "00:12") && future < System.currentTimeMillis()
                    ) {
                        when (scoreboardNames[10].removeFormatting().substring(5)) {
                            "Warsong" -> if (!warsong) rejoin()
                            "Crossfire" -> if (!crossfire) rejoin()
                            "Gorge" -> if (!gorge) rejoin()
                            "Atherrough Valley" -> if (!valley) rejoin()
                            "The Rift" -> if (!rift) rejoin()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        EventBus.register<ClientChatReceivedEvent> {
            if (GameStateManager.inLobby && enabled) {
                if (it.message.unformattedText.removeFormatting() == "The game starts in 30 seconds!") {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}YOU HAVE AUTO REJOIN FOR PRIVATE GAMES ENABLED"))
                    val chatComponent = ChatComponentText("${EnumChatFormatting.GOLD}CLICK HERE TO DISABLE")
                    chatComponent.chatStyle.chatHoverEvent =
                        HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText("Disable"))
                    chatComponent.chatStyle.chatClickEvent =
                        ClickEvent(ClickEvent.Action.RUN_COMMAND, "/disableautojoin")
                    Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent)
                }
            }
        }
    }

    private fun rejoin() {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/play warlords_ctf_mini")
        future = System.currentTimeMillis() + 2000
    }


    @ConfigPropertyBoolean(
        category = CCategory.PRIVATEGAME,
        id = "AutoGameFinder",
        comment = "",
        def = true
    )
    var enabled = true

    @ConfigPropertyBoolean(
        category = CCategory.PRIVATEGAME,
        id = "Warsong",
        comment = "",
        def = true
    )
    var warsong = true

    @ConfigPropertyBoolean(
        category = CCategory.PRIVATEGAME,
        id = "Crossfire",
        comment = "",
        def = true
    )
    var crossfire = true

    @ConfigPropertyBoolean(
        category = CCategory.PRIVATEGAME,
        id = "Gorge",
        comment = "",
        def = true
    )
    var gorge = true

    @ConfigPropertyBoolean(
        category = CCategory.PRIVATEGAME,
        id = "Valley",
        comment = "",
        def = true
    )
    var valley = true

    @ConfigPropertyBoolean(
        category = CCategory.PRIVATEGAME,
        id = "Rift",
        comment = "",
        def = true
    )
    var rift = true
}
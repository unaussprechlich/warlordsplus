package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.removeFormatting

object RevenantArmyTimer : IModule {

    private var armyTime: MutableList<Int> = mutableListOf()

    init {
        EventBus.register<ResetEvent> {
            armyTime.clear()
        }
        EventBus.register<ClientChatReceivedEvent> {
            val message = it.message.unformattedText.removeFormatting()
            if (message.contains("Undying Army protects you for")) {
                armyTime.add(10)
            } else if (message.startsWith("Winner")) {
                armyTime.clear()
            }
        }
        EventBus.register<SecondEvent> {
            armyTime.forEach {
                if (it < 1) {
                    armyTime.remove(it)
                }
            }
            for ((index, value) in armyTime.withIndex()) {
                if (show)
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}TIME LEFT UNTIL UNDYING HEAL ${EnumChatFormatting.GREEN}$value"))
                armyTime[index] = value - 1
            }
        }
    }

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "showRevenantArmyHealTimer",
        comment = "Enable or disable the revenant heal timer",
        def = true
    )
    var show = true

}
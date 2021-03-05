package net.unaussprechlich.warlordsplus.commands

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.ModConfigGui

class OpenConfigCommand : AbstractCommand() {
    override fun getCommandName(): String {
        return "warlordsplus"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "opens warlordsplus config"
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        try {
            openConfigInNextTick = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var openConfigInNextTick = false

    init {
        EventBus.register<TickEvent.ClientTickEvent> {
            if (openConfigInNextTick) {
                Minecraft.getMinecraft().displayGuiScreen(ModConfigGui(null))
                openConfigInNextTick = false
            }
        }
    }
}
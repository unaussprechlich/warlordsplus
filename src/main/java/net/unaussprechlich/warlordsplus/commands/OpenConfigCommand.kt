package net.unaussprechlich.warlordsplus.commands

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
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
        Minecraft.getMinecraft().displayGuiScreen(ModConfigGui(null))
    }
}
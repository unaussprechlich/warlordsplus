package net.unaussprechlich.warlordsplus.commands

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

class TestCommand : AbstractCommand() {
    override fun getCommandName(): String {
        return "wlplustest"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "sends 1 to 3000"
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        for (i in 0..5000) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("" + i)
        }
    }
}
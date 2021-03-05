package net.unaussprechlich.warlordsplus.commands

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.unaussprechlich.warlordsplus.hud.elements.HudElementSessionStats

class SetKillDeathAssistCommand : AbstractCommand() {
    override fun getCommandName(): String {
        return "setkda"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "sets kill death assist"
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        if (Minecraft.getMinecraft().thePlayer.displayNameString == "sumSmash" || Minecraft.getMinecraft().thePlayer.displayNameString == "unaussprechlich") {
            HudElementSessionStats.totalPlayerKills = parameters[0].toInt()
            HudElementSessionStats.totalPlayerDeaths = parameters[1].toInt()
            HudElementSessionStats.totalPlayerAssists = parameters[2].toInt()
        } else {
            val message: IChatComponent = ChatComponentText("Loser")
            Minecraft.getMinecraft().thePlayer.addChatMessage(message)
        }

    }
}
package net.unaussprechlich.warlordsplus.util.commands

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.unaussprechlich.warlordsplus.hud.elements.HudElementSessionStats
import java.util.*

class SetKillDeathAssistCommand : ICommand {
    override fun getCommandName(): String {
        return "setkda"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "sets kill death assist"
    }

    override fun getCommandAliases(): List<String> {
        val commandAliases: MutableList<String> = ArrayList()
        return commandAliases
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

    override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
        return true
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender,
        args: Array<String>,
        pos: BlockPos
    ): List<String>? {
        return null
    }

    override fun isUsernameIndex(args: Array<String>, index: Int): Boolean {
        return false
    }

    override fun compareTo(o: ICommand): Int {
        return 0
    }
}
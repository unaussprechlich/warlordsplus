package net.unaussprechlich.warlordsplus.util.commands

import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.unaussprechlich.warlordsplus.module.modules.AutoMapFinder
import java.util.*

class DisableAutoJoin : ICommand {
    override fun compareTo(other: ICommand?): Int {
        return 0
    }

    override fun getCommandName(): String {
        return "disableautojoin"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "disables auto join feature for private game"
    }

    override fun getCommandAliases(): MutableList<String> {
        val commandAliases: MutableList<String> = ArrayList()
        return commandAliases
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        AutoMapFinder.enabled = false
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender,
        args: Array<String>,
        pos: BlockPos
    ): List<String>? {
        return null
    }


    override fun isUsernameIndex(args: Array<out String>?, index: Int): Boolean {
        return false
    }

}
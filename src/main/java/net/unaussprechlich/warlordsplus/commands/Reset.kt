package net.unaussprechlich.warlordsplus.commands

import net.minecraft.command.CommandException
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.SpecsEnum
import java.util.*

class Reset : ICommand {
    override fun getCommandName(): String {
        return "reset"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "reset"
    }

    override fun getCommandAliases(): List<String> {
        val commandAliases: MutableList<String> = ArrayList()
        return commandAliases
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        if (parameters.isEmpty()) {
            EventBus.post(ResetEvent())
        } else {
            when (parameters[0].toInt()) {
                0 -> SpecWinCommand.specs = SpecsEnum.values()
            }
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
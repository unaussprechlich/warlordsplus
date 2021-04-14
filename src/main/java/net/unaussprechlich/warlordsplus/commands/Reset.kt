package net.unaussprechlich.warlordsplus.commands

import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.SpecsEnum

class Reset : AbstractCommand() {
    override fun getCommandName(): String {
        return "reset"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "reset"
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
}
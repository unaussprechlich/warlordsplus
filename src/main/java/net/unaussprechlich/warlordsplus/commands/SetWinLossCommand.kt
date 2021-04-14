package net.unaussprechlich.warlordsplus.commands

import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.unaussprechlich.warlordsplus.hud.elements.HudElementSessionStats

class SetWinLossCommand : AbstractCommand() {
    override fun getCommandName(): String {
        return "setwinloss"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "sets win loss"
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        HudElementSessionStats.totalWins = parameters[0].toInt()
        HudElementSessionStats.totalLosses = parameters[1].toInt()
        HudElementSessionStats.streak = parameters[2].toInt()
    }

}
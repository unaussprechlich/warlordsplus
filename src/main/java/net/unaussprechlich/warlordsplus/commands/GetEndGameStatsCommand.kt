package net.unaussprechlich.warlordsplus.commands

import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.unaussprechlich.warlordsplus.module.modules.StatsDisplayAfterGame
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.util.*


class GetEndGameStatsCommand : ICommand {
    override fun compareTo(other: ICommand?): Int {
        return 0
    }

    override fun getCommandName(): String {
        return "getendgamestats"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "copies end games stats in tab"
    }

    override fun getCommandAliases(): MutableList<String> {
        val commandAliases: MutableList<String> = ArrayList()
        return commandAliases
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        val selection = StatsDisplayAfterGame.lastGameStats
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender?,
        args: Array<out String>?,
        pos: BlockPos?
    ): MutableList<String> {
        TODO("Not yet implemented")
    }

    override fun isUsernameIndex(args: Array<out String>?, index: Int): Boolean {
        return false
    }

}
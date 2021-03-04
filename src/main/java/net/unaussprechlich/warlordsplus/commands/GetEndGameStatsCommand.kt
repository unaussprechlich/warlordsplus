package net.unaussprechlich.warlordsplus.commands

import net.minecraft.command.ICommandSender
import net.unaussprechlich.warlordsplus.module.modules.StatsDisplayAfterGame
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.util.*


class GetEndGameStatsCommand : AbstractCommand() {

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

}
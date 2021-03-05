package net.unaussprechlich.warlordsplus.commands

import net.minecraft.command.ICommandSender
import net.unaussprechlich.warlordsplus.module.modules.AutoMapFinder

class DisableAutoJoin : AbstractCommand() {
    override fun getCommandName(): String {
        return "disableautojoin"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "disables auto join feature for private game"
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        AutoMapFinder.enabled = false
    }

}
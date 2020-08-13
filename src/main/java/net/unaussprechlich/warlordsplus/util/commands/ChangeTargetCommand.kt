package net.unaussprechlich.warlordsplus.util.commands

import net.minecraft.command.CommandException
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.unaussprechlich.warlordsplus.hud.elements.HudElementRandomTarget
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import java.util.*

class ChangeTargetCommand : ICommand {
    override fun getCommandName(): String {
        return "changetarget"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "changes target"
    }

    override fun getCommandAliases(): List<String> {
        val commandAliases: MutableList<String> = ArrayList()
        return commandAliases
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        if (GameStateManager.isIngame) {
            if (parameters.isNotEmpty()) {
                when {
                    parameters[0] == "hard" ->
                        HudElementRandomTarget.pickRandomPlayer(3)
                    parameters[0] == "medium" ->
                        HudElementRandomTarget.pickRandomPlayer(2)
                    parameters[0] == "easy" ->
                        HudElementRandomTarget.pickRandomPlayer(1)
                }
            } else {
                HudElementRandomTarget.pickRandomPlayer(0)
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
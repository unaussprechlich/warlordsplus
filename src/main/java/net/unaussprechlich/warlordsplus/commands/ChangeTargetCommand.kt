package net.unaussprechlich.warlordsplus.commands

import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.unaussprechlich.warlordsplus.hud.elements.HudElementRandomTarget
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager

class ChangeTargetCommand : AbstractCommand() {
    override fun getCommandName(): String {
        return "changetarget"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "changes target"
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

}
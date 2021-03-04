package net.unaussprechlich.warlordsplus.commands

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.fml.client.FMLClientHandler
import java.util.*

class RemoveSpec : ICommand {
    override fun getCommandName(): String {
        return "removespec"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "removes spec from list"
    }

    override fun getCommandAliases(): List<String> {
        val commandAliases: MutableList<String> = ArrayList()
        return commandAliases
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        if (parameters.isNotEmpty()) {
            println(SpecWinCommand.specsString)
            SpecWinCommand.specsString = SpecWinCommand.specsString.replace("${parameters[0]}, ", "")
            ClientCommandHandler.instance.executeCommand(FMLClientHandler.instance().clientPlayerEntity, "/specwin")
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
                ChatComponentText(
                    "Next Spec - ${
                        SpecWinCommand.specsString.split(
                            ", "
                        ).shuffled().take(1)[0]
                    }"
                )
            )
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
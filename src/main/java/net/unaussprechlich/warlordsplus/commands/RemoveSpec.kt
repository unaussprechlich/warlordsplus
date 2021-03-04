package net.unaussprechlich.warlordsplus.commands

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.fml.client.FMLClientHandler

class RemoveSpec : AbstractCommand() {
    override fun getCommandName(): String {
        return "removespec"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "removes spec from list"
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

}
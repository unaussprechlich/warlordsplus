package net.unaussprechlich.warlordsplus.util.commands

import kotlinx.serialization.UnstableDefault
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.unaussprechlich.warlordsplus.module.modules.stats.StatsLoader
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import java.util.*

class GetPlayerStatsCommand : ICommand, IUpdateConsumer {

    var counter = 0

    override fun getCommandName(): String {
        return "playerstat"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "get stats of player"
    }

    override fun getCommandAliases(): List<String> {
        val commandAliases: MutableList<String> = ArrayList()
        return commandAliases
    }

    @UnstableDefault
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        if (parameters.isNotEmpty()) {
            val player: String = parameters[0]
            counter++
            if (counter < 5) {
                if (counter == 1)
                    StatsLoader.loadPlayer(player)
                processCommand(sender, parameters)
            } else {
                counter = 0
                val data = StatsLoader.getPlayer(player)?.data
                val names = StringBuilder()
                data?.nameHistory?.forEach { names.append(it.name + ",") }
                val nameHistory: IChatComponent = ChatComponentText(
                    "${EnumChatFormatting.GOLD}Names: ${EnumChatFormatting.WHITE}${names}"
                )
                val stats: IChatComponent = ChatComponentText(
                    "${EnumChatFormatting.GOLD}Names: ${EnumChatFormatting.WHITE}"
                )
                Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                Minecraft.getMinecraft().thePlayer.addChatMessage(nameHistory)
                Minecraft.getMinecraft().thePlayer.addChatMessage(stats)
                Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
            }

        }
    }

    override fun update() {
        TODO("Not yet implemented")
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
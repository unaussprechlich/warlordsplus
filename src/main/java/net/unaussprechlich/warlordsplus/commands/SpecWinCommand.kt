package net.unaussprechlich.warlordsplus.commands

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.util.SpecsEnum
import net.unaussprechlich.warlordsplus.util.removeSpaces
import java.util.*


object SpecWinCommand : ICommand {

    var specs = SpecsEnum.values()
    var specsString = specs.joinToString { it.classname }

    init {
        //EventBus.register(::onChat)
    }

    private fun onChat(e: ClientChatReceivedEvent) {
        if (e.message.unformattedText.removeSpaces().startsWith("Winner")) {
            if (specsString.contains(ThePlayer.spec.toString())) {
                ClientCommandHandler.instance.executeCommand(
                    FMLClientHandler.instance().clientPlayerEntity,
                    "/removespec ${ThePlayer.spec.classname}"
                )
            }
        }
    }


    override fun getCommandName(): String {
        return "specwin"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "shows how many wins you need with each spec is left"
    }

    override fun getCommandAliases(): List<String> {
        val commandAliases: MutableList<String> = ArrayList()
        return commandAliases
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        println(specsString)
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(ChatComponentText("${EnumChatFormatting.GOLD}--- Wins Left ---"))
        var specStringTrimmed = specsString.substring(0, specsString.length - 4)
        specStringTrimmed.split(", ").forEach { sendMessage(it, "Remove $it") }

    }

    fun sendMessage(message: String, hover: String?) {
        val chatComponent = ChatComponentText(message)
        println(message)
        if (hover != null && hover.isNotEmpty()) {
            chatComponent.chatStyle.chatHoverEvent = HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                ChatComponentText(
                    hover
                )
            )
        }
        if (hover != null) {
            chatComponent.chatStyle.chatClickEvent =
                ClickEvent(ClickEvent.Action.RUN_COMMAND, "/removespec ${hover.replace("Remove ", "")}")
        }

        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(chatComponent)
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
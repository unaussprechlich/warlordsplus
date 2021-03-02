package net.unaussprechlich.warlordsplus.util.commands

import kotlinx.serialization.UnstableDefault
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.util.*


class GetPartyMembers : ICommand {

    var partyMembers = mutableListOf<String>()
    var occurrences = -1
    var checkParty = false

    init {
        EventBus.register<ClientChatReceivedEvent> {
            var message = it.message.unformattedText.removeFormatting()
            if (checkParty) {
                if (occurrences >= 0) {
                    message = message.replace(Regex("\\[[^\\]]*\\]"), "")
                    when {
                        message.startsWith("Party Leader:") -> {
                            partyMembers.add(message.substring(14).replace(Regex("[^a-zA-Z0-9]"), ""))
                        }
                        message.startsWith("Party Moderators:") -> {
                            var players = message.substring(19).split("   ")
                            players.forEach {
                                partyMembers.add(it)
                            }

                        }
                        message.startsWith("Party Members:") -> {
                            var players = message.substring(16).split("   ")
                            players.forEach {
                                partyMembers.add(it)
                            }
                        }
                    }
                }
                if (message == "-----------------------------") {
                    occurrences++
                }
                if (occurrences == 2) {
                    checkParty = false
                    occurrences = -1
                }

                var players = StringBuilder()
                var prefix = ""
                partyMembers.forEach {
                    players.append(prefix)
                    prefix = ","
                    players.append(it.replace(" ", ""))
                }
                players.trim()
                val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(StringSelection(players.toString()), StringSelection(players.toString()))
                println(partyMembers)
            }
        }
    }

    override fun getCommandName(): String {
        return "getparty"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "gets party members"
    }

    override fun getCommandAliases(): List<String> {
        return ArrayList()
    }

    @UnstableDefault
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        occurrences = 0
        partyMembers = arrayListOf()
        checkParty = true
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/pl")
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
        return true
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender,
        args: Array<String>,
        pos: BlockPos
    ): List<String>? {
        TODO("Not yet implemented")
    }

    override fun isUsernameIndex(args: Array<String>, index: Int): Boolean {
        return false
    }

    override fun compareTo(o: ICommand): Int {
        return 0
    }

}
package net.unaussprechlich.warlordsplus.commands

import kotlinx.serialization.UnstableDefault
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection


class GetPartyMembers : AbstractCommand() {

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

    @UnstableDefault
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        occurrences = 0
        partyMembers = arrayListOf()
        checkParty = true
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/pl")
    }

}
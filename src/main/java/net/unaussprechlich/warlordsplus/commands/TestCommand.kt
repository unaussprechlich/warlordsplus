package net.unaussprechlich.warlordsplus.commands

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.detector.ScoreboardDetector.scoreboard
import net.unaussprechlich.warlordsplus.module.modules.qualityoflife.PowerUpTimer
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.warlords2.PlayerCooldownRenderer

class TestCommand : AbstractCommand() {
    override fun getCommandName(): String {
        return "wlplustest"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "sends 1 to 3000"
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
//        for (i in 0..5000) {
//            Minecraft.getMinecraft().thePlayer.sendChatMessage("" + i)
//        }
//        for (value in PowerUpTimer.powerUps.values) {
//            when {
//                value.color == Colors.GREEN || value.color == Colors.ORANGE -> {
//                    println("new PowerupOption(loc.addXYZ(${value.x}, ${value.y}, ${value.z}), PowerupType.HEALING)")
//                }
//                value.color == Colors.RED -> {
//                    println("new PowerupOption(loc.addXYZ(${value.x}, ${value.y}, ${value.z}), PowerupType.DAMAGE)")
//                }
//                value.color == Colors.YELLOW -> {
//                    println("new PowerupOption(loc.addXYZ(${value.x}, ${value.y}, ${value.z}), PowerupType.SPEED)")
//                }
//            }
//        }
//
//        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("isWarlords2 = " + GameStateManager.isWarlords2))
//        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("isPvE = " + GameStateManager.isPvE))
//        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("isIngame = " + GameStateManager.isIngame))
//        //Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("entityPlayer = " + PlayerCooldownRenderer.entityPlayer))
//        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("playersMap = " + !OtherPlayers.playersMap.containsKey
//            (Minecraft.getMinecraft().thePlayer.name)))
//        val size = scoreboard.size
//        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("score - 5 = " + scoreboard[size - 5]))
//        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("score - 4 = " + scoreboard[size - 4]))
//        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("score - 3 = " + scoreboard[size - 3]))

    }
}
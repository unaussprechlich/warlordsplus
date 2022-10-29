package net.unaussprechlich.warlordsplus.commands

import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.unaussprechlich.warlordsplus.module.modules.qualityoflife.PowerUpTimer
import net.unaussprechlich.warlordsplus.util.Colors

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
        for (value in PowerUpTimer.powerUps.values) {
            when {
                value.color == Colors.GREEN || value.color == Colors.ORANGE -> {
                    println("new PowerupOption(loc.addXYZ(${value.x}, ${value.y}, ${value.z}), PowerupType.HEALING)")
                }
                value.color == Colors.RED -> {
                    println("new PowerupOption(loc.addXYZ(${value.x}, ${value.y}, ${value.z}), PowerupType.DAMAGE)")
                }
                value.color == Colors.YELLOW -> {
                    println("new PowerupOption(loc.addXYZ(${value.x}, ${value.y}, ${value.z}), PowerupType.SPEED)")
                }
            }
        }
    }
}
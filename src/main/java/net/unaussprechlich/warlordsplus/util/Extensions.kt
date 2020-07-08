package net.unaussprechlich.warlordsplus.util

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.fml.common.gameevent.TickEvent

val REGEX_FILTER = Regex("[^A-Za-z0-9/.,&_!:>+')(y<\\-\\]\\[\\u00AB\\u00BB\\s]")

infix fun Int.fdiv(i: Int): Float = this / i.toFloat();
infix fun Int.ddiv(i: Int): Double = this / i.toDouble();

fun String.removeSpaces(): String = this.replace(" ", "")
fun String.removeFormatting(): String {
    return EnumChatFormatting.getTextWithoutFormattingCodes(this).replace(REGEX_FILTER, "")
}

fun TickEvent.ClientTickEvent.checkPreConditions(): Boolean {
    return this.phase != TickEvent.Phase.END
            && !Minecraft.getMinecraft().isGamePaused
            && Minecraft.getMinecraft().thePlayer != null
}

infix fun String.contain(other: String): Boolean = this.contains(other)
infix fun String.notContain(other: String): Boolean = !(this.contains(other))






package net.unaussprechlich.warlordsplus.util

import net.minecraft.util.EnumChatFormatting


infix fun Int.fdiv(i: Int): Float = this / i.toFloat();
infix fun Int.ddiv(i: Int): Double = this / i.toDouble();

fun String.removeSpaces(): String = this.replace(" ", "")
fun String.removeFormatting(): String = EnumChatFormatting.getTextWithoutFormattingCodes(this)
infix fun String.has(other: String) : Boolean = this.contains(other)
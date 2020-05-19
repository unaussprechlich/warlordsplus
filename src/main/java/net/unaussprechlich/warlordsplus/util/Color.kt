package net.unaussprechlich.warlordsplus.util

import net.minecraft.util.EnumChatFormatting
import org.lwjgl.util.Color

fun Color.convertToArgb(): Int {
    return (this.alpha shl 24) or
            (this.red shl 16) or
            (this.green shl 8) or
            (this.blue)
}

enum class Colors(val red: Int, val green: Int, val blue: Int, val enumColor: EnumChatFormatting) {
    DEF(34, 34, 39, EnumChatFormatting.OBFUSCATED),
    BLACK(0, 0, 0, EnumChatFormatting.BLACK),
    DARK_BLUE(0, 0, 170, EnumChatFormatting.DARK_BLUE),
    DARK_GREEN(0, 170, 0, EnumChatFormatting.DARK_GREEN),
    DARK_AQUA(0, 170, 170, EnumChatFormatting.DARK_AQUA),
    DARK_RED(170, 0, 0, EnumChatFormatting.DARK_RED),
    DARK_PURPLE(170, 0, 170, EnumChatFormatting.DARK_PURPLE),
    DARK_GRAY(85, 85, 85, EnumChatFormatting.DARK_GRAY),
    GOLD(255, 170, 0, EnumChatFormatting.GOLD),
    GRAY(170, 170, 170, EnumChatFormatting.GRAY),
    BLUE(85, 85, 255, EnumChatFormatting.BLUE),
    GREEN(85, 255, 85, EnumChatFormatting.GREEN),
    AQUA(85, 255, 255, EnumChatFormatting.AQUA),
    RED(255, 85, 85, EnumChatFormatting.RED),
    LIGHT_PURPLE(255, 85, 255, EnumChatFormatting.LIGHT_PURPLE),
    YELLOW(255, 255, 85, EnumChatFormatting.YELLOW),
    WHITE(255, 255, 255, EnumChatFormatting.WHITE);

    val FULL: Int = Color(red.toByte(), green.toByte(), blue.toByte(), 255.toByte()).convertToArgb()
    val BACKGROUND: Int = Color(red.toByte(), green.toByte(), blue.toByte(), 100.toByte()).convertToArgb()
    val HEADER: Int = Color(red.toByte(), green.toByte(), blue.toByte(), 200.toByte()).convertToArgb()
}


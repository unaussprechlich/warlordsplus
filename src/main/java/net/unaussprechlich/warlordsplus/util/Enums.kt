package net.unaussprechlich.warlordsplus.util

import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.util.EnumChatFormatting


enum class TeamEnum(val teamName: String, val color: EnumChatFormatting?) {
    BLUE("Blue", EnumChatFormatting.BLUE), RED("Red", EnumChatFormatting.RED), NONE("NONE", null)
}

enum class WarlordsEnum(val classname: String, val shortName: String) {
    WARRIOR("Warrior", "WAR"), SHAMAN("Shaman", "SHA"),
    MAGE("Mage", "MAG"), PALADIN("Paladin", "PAL"),
    NONE("NONE", "NONE")
}

enum class SpecsEnum(val classname: String) {
    AVENGER("Avenger"), CRUSADER("Crusader"), PROTECTOR("Protector"),
    BERSERKER("Berserker"), DEFENDER("Defender"), REVENANT("Revenant"),
    PYROMANCER("Pyromancer"), CRYOMANCER("Cryomancer"), AQUAMANCER("Aquamancer"),
    THUNDERLORD("Thunderlord"), SPIRITGUARD("Spiritguard"), EARTHWARDEN("Earthwarden"),
    NONE("NONE")
}

enum class HypixelRank(val rankName: String, rankNameFormatted: String, color: ChatFormatting) {
    DEFAULT(
        "DEFAULT",
        "",
        ChatFormatting.GRAY
    ),
    VIP(
        "[VIP]",
        ChatFormatting.GREEN.toString() + "[VIP]",
        ChatFormatting.GREEN
    ),
    VIP_PLUS(
        "[VIP+]",
        ChatFormatting.GREEN.toString() + "[VIP" + ChatFormatting.GOLD + "+" + ChatFormatting.GREEN + "]",
        ChatFormatting.GREEN
    ),
    MVP(
        "[MVP]",
        ChatFormatting.AQUA.toString() + "[MVP]",
        ChatFormatting.AQUA
    ),
    MVP_PLUS(
        "[MVP+]",
        ChatFormatting.AQUA.toString() + "[MVP" + ChatFormatting.GOLD + "+" + ChatFormatting.AQUA + "]",
        ChatFormatting.AQUA
    ),
    MVP_PLUS_PLUS(
        "[MVP++]",
        ChatFormatting.AQUA.toString() + "[MVP" + ChatFormatting.GOLD + "+" + ChatFormatting.GOLD + "+" + ChatFormatting.AQUA + "]",
        ChatFormatting.AQUA
    ),
    HELPER(
        "[HELPER]",
        ChatFormatting.BLUE.toString() + "[HELPER]",
        ChatFormatting.BLUE
    ),
    MOD(
        "[MOD]",
        ChatFormatting.DARK_GREEN.toString() + "[MOD]",
        ChatFormatting.DARK_GREEN
    ),
    YT(
        "[YT]",
        ChatFormatting.GOLD.toString() + "[YT]",
        ChatFormatting.GOLD
    ),
    ADMIN(
        "[ADMIN]",
        ChatFormatting.RED.toString() + "[ADMIN]",
        ChatFormatting.RED
    );

}

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

enum class SpecsEnum(val classname: String, val weapon: String, val icon: String) {
    AVENGER("Avenger", "Avenger", "${EnumChatFormatting.RED}銌"), CRUSADER(
        "Crusader",
        "Crusader",
        "${EnumChatFormatting.YELLOW}鉰"
    ),
    PROTECTOR("Protector", "Protector", "${EnumChatFormatting.GREEN}銀"),
    BERSERKER("Berserker", "35%", "${EnumChatFormatting.RED}銌"), DEFENDER(
        "Defender",
        "25%",
        "${EnumChatFormatting.YELLOW}鉰"
    ),
    REVENANT("Revenant", "crippling", "${EnumChatFormatting.GREEN}銀"),
    PYROMANCER("Pyromancer", "Fireball", "${EnumChatFormatting.RED}銌"), CRYOMANCER(
        "Cryomancer",
        "Frostbolt",
        "${EnumChatFormatting.YELLOW}鉰"
    ),
    AQUAMANCER("Aquamancer", "Water", "${EnumChatFormatting.GREEN}銀"),
    THUNDERLORD("Thunderlord", "Lightning", "${EnumChatFormatting.RED}銌"), SPIRITGUARD(
        "Spiritguard",
        "Fallen",
        "${EnumChatFormatting.YELLOW}鉰"
    ),
    EARTHWARDEN("Earthwarden", "Earthen", "${EnumChatFormatting.GREEN}銀"),
    NONE("NONE", "NONE", "")
}

enum class SpecsEnumSuper() {
    DAMAGE, TANK, HEALER, NONE
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

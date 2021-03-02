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

enum class SpecsEnum(
    val classname: String,
    val red: String,
    val purple: String,
    val blue: String,
    val orange: String,
    val weapon: String,
    val icon: String
) {
    AVENGER(
        "Avenger",
        "Consecrate",
        "Light Infusion",
        "Holy Radiance",
        "Avenger's Wrath",
        "Avenger",
        "${EnumChatFormatting.RED}銌"
    ),
    CRUSADER(
        "Crusader",
        "Consecrate",
        "Light Infusion",
        "Holy Radiance",
        "Inspiring Presence",
        "Crusader",
        "${EnumChatFormatting.YELLOW}鉰"
    ),
    PROTECTOR(
        "Protector",
        "Consecrate",
        "Light Infusion",
        "Holy Radiance",
        "Hammer of Light",
        "Protector",
        "${EnumChatFormatting.GREEN}銀"
    ),
    BERSERKER("Berserker", "Seismic Wave", "Ground Slam", "Blood Lust", "Berserk", "35%", "${EnumChatFormatting.RED}銌"),
    DEFENDER(
        "Defender",
        "Seismic Wave",
        "Ground Slam",
        "Intervene",
        "Last Stand",
        "25%",
        "${EnumChatFormatting.YELLOW}鉰"
    ),
    REVENANT(
        "Revenant",
        "Reckless Charge",
        "Ground Slam",
        "Orbs of Light",
        "Undying Army",
        "crippling",
        "${EnumChatFormatting.GREEN}銀"
    ),
    PYROMANCER(
        "Pyromancer",
        "Flame Burst",
        "Time Warp",
        "Arcane Shield",
        "Inferno",
        "Fireball",
        "${EnumChatFormatting.RED}銌"
    ),
    CRYOMANCER(
        "Cryomancer",
        "Freezing Breath",
        "Time Warp",
        "Arcane Shield",
        "Ice Barrier",
        "Frostbolt",
        "${EnumChatFormatting.YELLOW}鉰"
    ),
    AQUAMANCER(
        "Aquamancer",
        "Water Breath",
        "Time Warp",
        "Arcane Shield",
        "Healing Rain",
        "Water",
        "${EnumChatFormatting.GREEN}銀"
    ),
    THUNDERLORD(
        "Thunderlord",
        "Chain Lightning",
        "Windfury Weapon",
        "Lightning Rod",
        "Capacitor Totem",
        "Lightning",
        "${EnumChatFormatting.RED}銌"
    ),
    SPIRITGUARD(
        "Spiritguard",
        "Spirit Link",
        "Soulbinding Weapon",
        "Repentance",
        "Death's Debt",
        "Fallen",
        "${EnumChatFormatting.YELLOW}鉰"
    ),
    EARTHWARDEN(
        "Earthwarden",
        "Boulder",
        "Earthliving Weapon",
        "Chain Heal",
        "Healing Totem",
        "Earthen",
        "${EnumChatFormatting.GREEN}銀"
    ),
    NONE("NONE", "", "", "", "", "NONE", "")
}

enum class SpecsEnumSuper(val specName: String) {
    DAMAGE("damage"), TANK("defense"), HEALER("healer"), NONE("NONE")
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
//
//enum class GameState(val lobbyName: String) {
//    INLOBBY("In Lobby"), INCTF("Capture the Flag"), INDOM("Domination"), INTDM("Team Deathmatch")
//}

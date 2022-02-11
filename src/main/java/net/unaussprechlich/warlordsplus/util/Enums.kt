package net.unaussprechlich.warlordsplus.util

import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.util.EnumChatFormatting


enum class TeamEnum(val teamName: String, val color: EnumChatFormatting?) {
    BLUE("Blue", EnumChatFormatting.BLUE),
    RED("Red", EnumChatFormatting.RED),
    NONE("NONE", null)
}

enum class WarlordsEnum(val classname: String, val shortName: String) {
    WARRIOR("Warrior", "WAR"),
    SHAMAN("Shaman", "SHA"),
    MAGE("Mage", "MAG"),
    PALADIN("Paladin", "PAL"),
    ROGUE("Rogue", "ROG"),
    NONE("NONE", "NONE")
}

enum class SpecsEnum(
    val classname: String,
    val red: String,
    val purple: String,
    val blue: String,
    val orange: String,
    val weapon: String,
    val type : SpecTypeEnum
) {
    AVENGER(
        "Avenger",
        "Consecrate",
        "Light Infusion",
        "Holy Radiance",
        "Avenger's Wrath",
        "Avenger",
        SpecTypeEnum.DAMAGE

    ),
    CRUSADER(
        "Crusader",
        "Consecrate",
        "Light Infusion",
        "Holy Radiance",
        "Inspiring Presence",
        "Crusader",
        SpecTypeEnum.TANK
    ),
    PROTECTOR(
        "Protector",
        "Consecrate",
        "Light Infusion",
        "Holy Radiance",
        "Hammer of Light",
        "Protector",
        SpecTypeEnum.HEALER
    ),
    BERSERKER(
        "Berserker",
        "Seismic Wave",
        "Ground Slam",
        "Blood Lust",
        "Berserk",
        "receives 35 less",
        SpecTypeEnum.DAMAGE
    ),
    DEFENDER(
        "Defender",
        "Seismic Wave",
        "Ground Slam",
        "Intervene",
        "Last Stand",
        "receives 25 less",
        SpecTypeEnum.TANK
    ),
    REVENANT(
        "Revenant",
        "Reckless Charge",
        "Ground Slam",
        "Orbs of Life",
        "Undying Army",
        "crippling",
        SpecTypeEnum.HEALER
    ),
    PYROMANCER(
        "Pyromancer",
        "Flame Burst",
        "Time Warp",
        "Arcane Shield",
        "Inferno",
        "Fireball",
        SpecTypeEnum.DAMAGE
    ),
    CRYOMANCER(
        "Cryomancer",
        "Freezing Breath",
        "Time Warp",
        "Arcane Shield",
        "Ice Barrier",
        "Frostbolt",
        SpecTypeEnum.TANK
    ),
    AQUAMANCER(
        "Aquamancer",
        "Water Breath",
        "Time Warp",
        "Arcane Shield",
        "Healing Rain",
        "Water",
        SpecTypeEnum.HEALER
    ),
    THUNDERLORD(
        "Thunderlord",
        "Chain Lightning",
        "Windfury Weapon",
        "Lightning Rod",
        "Capacitor Totem",
        "Lightning",
        SpecTypeEnum.DAMAGE
    ),
    SPIRITGUARD(
        "Spiritguard",
        "Spirit Link",
        "Soulbinding Weapon",
        "Repentance",
        "Death's Debt",
        "Fallen",
        SpecTypeEnum.TANK
    ),
    EARTHWARDEN(
        "Earthwarden",
        "Boulder",
        "Earthliving Weapon",
        "Chain Heal",
        "Healing Totem",
        "Earthen",
        SpecTypeEnum.HEALER
    ),
    ASSASSIN(
        "Assassin",
        "Incendiary Curse",
        "Blinding Assault",
        "Soul Switch",
        "Order Of Eviscerate",
        "Judgement",
        SpecTypeEnum.DAMAGE
    ),
    VINDICATOR(
        "Vindicator",
        "Soul Shackle",
        "Heart To Heart",
        "Wide Guard",
        "Vindicate",
        "Righteous",
        SpecTypeEnum.TANK
    ),
    APOTHECARY(
        "Apothecary",
        "Soothing Puddle",
        "Acupressure",
        "Remedic Chains",
        "Draining Miasma",
        "Impaling",
        SpecTypeEnum.HEALER
    ),

    NONE("NONE", "", "", "", "", "NONE", SpecTypeEnum.NONE);

    val icon
        get() = type.coloredSymbol
}

enum class SpecTypeEnum(val specName: String, val symbol : String, val color: EnumChatFormatting) {
    DAMAGE("damage", "銌", EnumChatFormatting.RED),
    TANK("defense", "鉰", EnumChatFormatting.YELLOW),
    HEALER("healer", "銀", EnumChatFormatting.GREEN),
    NONE("NONE", "", EnumChatFormatting.OBFUSCATED);

    val coloredSymbol = "$color$symbol"
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

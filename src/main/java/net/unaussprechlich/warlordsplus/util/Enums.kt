package net.unaussprechlich.warlordsplus.util

import net.minecraft.util.EnumChatFormatting


enum class TeamEnum(val teamName : String, val color : EnumChatFormatting?) {
    BLUE("Blue", EnumChatFormatting.BLUE), RED("Red", EnumChatFormatting.RED), NONE("NONE", null)
}

enum class WarlordsEnum(val classname: String, val shortName : String){
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
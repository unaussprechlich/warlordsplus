package net.unaussprechlich.warlordsplus.config;

import net.minecraft.util.EnumChatFormatting;

/**
 * Little HelperEnum to avoid spelling mistakes :P
 * Please add a new category as enum and as static final (const) :)
 */
public enum CCategory {

    //ADD A NEW CATEGORY HERE >>
    ENUM_UNKNOWN("Unknown", EnumChatFormatting.BLACK),
    ENUM_GENERAL("General", EnumChatFormatting.WHITE),
    ENUM_HUD("Hud", EnumChatFormatting.WHITE),
    ENUM_STATS("Stats", EnumChatFormatting.WHITE),
    ENUM_CHAT("Chat", EnumChatFormatting.WHITE),
    ENUM_RENDERER("Renderer", EnumChatFormatting.WHITE),
    ENUM_SCOREBOARD("Scoreboard", EnumChatFormatting.WHITE),
    ENUM_PRIVATEGAME("Private Game", EnumChatFormatting.WHITE),
    ENUM_MODULES("Modules", EnumChatFormatting.WHITE);

    //CAN'T CAST ENUMS IN @ConfigProperty<T> SO HERE ARE SOME STATIC FINALS, WE ALL LOVE STATIC FINALS!!!
    //ALSO ADD HERE >>

    public static final String UNKNOWN = "Unknown";
    public static final String GENERAL = "General";
    public static final String HUD = "Hud";
    public static final String CHAT = "Chat";
    public static final String STATS = "Stats";
    public static final String RENDERER = "Renderer";
    public static final String SCOREBOARD = "Scoreboard";
    public static final String PRIVATEGAME = "Private Game";
    public static final String MODULES = "Modules";

    private final String name;
    private final EnumChatFormatting enumChatFormatting;

    CCategory(String name, EnumChatFormatting enumChatFormatting) {
        this.name = name;
        this.enumChatFormatting = enumChatFormatting;
    }

    public static CCategory getCategoryByName(String name) {
        for (CCategory cCategory : CCategory.values())
            if (cCategory.name.equalsIgnoreCase(name))
                return cCategory;
        return CCategory.ENUM_UNKNOWN;
    }

    public String getName() {
        return name;
    }

    public EnumChatFormatting getEnumChatFormatting() {
        return enumChatFormatting;
    }
}

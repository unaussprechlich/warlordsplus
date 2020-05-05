package net.unaussprechlich.warlordsplus.config


object GeneralConfigSettings {
    @ConfigPropertyBoolean(CCategory.GENERAL, "hudDisabled", "HUD Disabled", false)
    @JvmStatic var hudDisabled: Boolean = false

    @ConfigPropertyBoolean(CCategory.GENERAL, "hudDisplayVersion", "HUD DisplayVersion", true)
    @JvmStatic var hudDisplayVersion: Boolean = true

    @ConfigPropertyInt(CCategory.GENERAL, "displayXOffset", "HUD offset (X)", 5)
    @JvmStatic var displayXOffset: Int = 5

    @ConfigPropertyInt(CCategory.GENERAL, "displayYOffset", "HUD offset (Y)", 5)
    @JvmStatic var displayYOffset: Int = 5

    @ConfigPropertyBoolean(CCategory.GENERAL, "hudBackground", "Should the HUD have a background?", true)
    @JvmStatic var hudBackground: Boolean = true

    @ConfigPropertyInt(CCategory.GENERAL, "hudRed", "HUD Red", 0)
    @JvmStatic var hudRed: Int = 0

    @ConfigPropertyInt(CCategory.GENERAL, "hudGreen", "HUD green", 0)
    @JvmStatic var hudGreen: Int = 0

    @ConfigPropertyInt(CCategory.GENERAL, "hudAlpha", "HUD alpha", 150)
    @JvmStatic var hudAlpha: Int = 150

}
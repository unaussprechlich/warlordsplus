package net.unaussprechlich.warlordsplus.module.modules.notificationmodule.notifications


import net.minecraft.util.EnumChatFormatting
import net.unaussprechlich.warlordsplus.WarlordsPlus
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.AbstractNotification
import net.unaussprechlich.warlordsplus.util.ChatMessageComposer
import net.unaussprechlich.warlordsplus.util.Colors



class UpdateModNotification (private val version : String, private val url : String) : AbstractNotification() {

    private val UPDATE_NOTIFICATION_WIDTH = 200

    override val validUntil: Long = System.currentTimeMillis() + 60000

    init {
        ChatMessageComposer.printSeparationMessage(EnumChatFormatting.DARK_RED)
        ChatMessageComposer("You are currently running version ${WarlordsPlus.getModVersion()} of ${WarlordsPlus.MOD_NAME_BRANDING}${EnumChatFormatting.RESET}, which is no longer supported by the developers. Please update to version $version by downloading and installing the .jar file from the link below. \n").send()
        ChatMessageComposer(url).addFormatting(EnumChatFormatting.GRAY).makeLink(url).send()
        ChatMessageComposer.printSeparationMessage(EnumChatFormatting.DARK_RED)
    }

    override fun onRender(): Int {
        translateX(- UPDATE_NOTIFICATION_WIDTH)
        renderRect(UPDATE_NOTIFICATION_WIDTH.toDouble(), 15.0, Colors.DEF, alpha = 255)
        translateY(-4)
        translateX(5){
            "${EnumChatFormatting.DARK_RED}[Update] ${EnumChatFormatting.GOLD}Warlords${EnumChatFormatting.RED}Plus".draw()
        }
        translateY(-11)

        val messageHeight = "WarlordsPlus has been updated to version ${EnumChatFormatting.BOLD}${version}${EnumChatFormatting.RESET}. Please click the link in chat to download the latest version."
            .drawWithBackgroundAndWidth(Colors.DEF, UPDATE_NOTIFICATION_WIDTH, alpha = 100, padding = 5)

        return messageHeight + 11
    }

}


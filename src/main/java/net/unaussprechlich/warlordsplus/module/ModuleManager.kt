package net.unaussprechlich.warlordsplus.module

import kotlinx.serialization.UnstableDefault
import net.minecraftforge.common.MinecraftForge
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.EasyConfigHandler
import net.unaussprechlich.warlordsplus.hud.HudManager
import net.unaussprechlich.warlordsplus.ingamegui.IngameGuiManager
import net.unaussprechlich.warlordsplus.module.modules.*
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.NotificationManager
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.notifications.ChatNotifications
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.notifications.KillFeedNotifications
import net.unaussprechlich.warlordsplus.module.modules.stats.StatsDisplayRenderer
import net.unaussprechlich.warlordsplus.module.modules.stats.StatsLoader

@UnstableDefault
object ModuleManager {

    private val modules = ArrayList<IModule>()

    init {
        with(modules) {
            add(EasyConfigHandler)
            add(ScoreboardManager)
            add(GameStateManager)
            add(DamageAndHealParser)
            add(KillAssistParser)
            add(FlagTakenDetector)
            add(ThePlayer)
            add(HudManager)
            add(IngameGuiManager)
            add(ChatDetector)
            add(RenderNumbers)
            add(StatsLoader)
            add(StatsDisplayRenderer)
            add(RenderStatsInGame)
            add(StatsDisplayAfterGame)
            add(PowerUpTimer)
            add(StatsInLobby)
            add(NotificationManager)
            add(ChatNotifications)
            add(KillFeedNotifications)
            //add(Meme)
            add(CancelJumpBarOnHorse)
            add(SoulbindingCounter)
            add(SkillDetector)
            add(RenderVignette)
            add(AutoMapFinder)
            //add(MotivationalMessages)
            add(AutoTBag)
            add(RevenantArmyTimer)
            //add(Excuses)
            //add(ImageRendering)
        }
    }

    fun register() {
        MinecraftForge.EVENT_BUS.register(this)
        modules.forEach(MinecraftForge.EVENT_BUS::register)
    }
}


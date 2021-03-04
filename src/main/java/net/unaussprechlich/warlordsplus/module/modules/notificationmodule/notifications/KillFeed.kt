package net.unaussprechlich.warlordsplus.module.modules.notificationmodule.notifications

import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.OtherPlayers.colorForPlayer
import net.unaussprechlich.warlordsplus.OtherPlayers.playersMap
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.KillEvent
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.AbstractNotification
import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.NotificationManager
import net.unaussprechlich.warlordsplus.util.Colors


object KillFeedNotifications : IModule {

    init {
        EventBus.register<KillEvent> {
            NotificationManager.add(KillFeedNotification(it))
        }
    }

    private class KillFeedNotification(killEvent: KillEvent) : AbstractNotification() {

        private val killerWidth : Int
        private val deathPlayerWidth : Int
        private val killerColor : Colors
        private val deathPlayerColor : Colors
        private val killerFormatted : String
        private val deathPlayerFormatted : String
        override val validUntil: Long = System.currentTimeMillis() + 10000

        init {
            killerColor = colorForPlayer(killEvent.player)
            deathPlayerColor = colorForPlayer(killEvent.deathPlayer)

            val killerSpec = playersMap[killEvent.player]?.spec?.type?.symbol
            val deathPlayerSpec = playersMap[killEvent.deathPlayer]?.spec?.type?.symbol
            killerFormatted = "${killerSpec?: ""}${killEvent.player}"
            deathPlayerFormatted = "${deathPlayerSpec?: ""}${killEvent.deathPlayer}"

            killerWidth = killerFormatted.width()
            deathPlayerWidth = deathPlayerFormatted.width()
        }

        override fun onRender(): Int {
            translateX(- (killerWidth + deathPlayerWidth + 20 ))
            renderRect(killerWidth + 4.0, 11.0, killerColor, alpha = 255)
            translateX(2)
            translateY(2){
                killerFormatted.draw()
            }
            translateX(killerWidth + 2)
            renderRect(12.0, 11.0, Colors.DEF)
            translate(4, 2){
                ">".draw()
            }

            translateX(12)
            renderRect(deathPlayerWidth + 4.0, 11.0, deathPlayerColor, alpha = 255)
            translateX(2)
            translateY(2){
                deathPlayerFormatted.draw()
            }
            return 11
        }
    }

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "showKillFeedTopRight",
        comment = "Enable or disable the kill feed",
        def = true
    )
    var show = true
}


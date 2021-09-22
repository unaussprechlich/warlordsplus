package net.unaussprechlich.warlordsplus.module.modules.notificationmodule.notifications


import net.unaussprechlich.warlordsplus.module.modules.notificationmodule.AbstractNotification
import net.unaussprechlich.warlordsplus.util.Colors


class ErrorNotification(name: String, message: String) : AbstractErrorNotification(name, message, Colors.DARK_RED)

abstract class AbstractErrorNotification (private val name : String, private val message : String,
                                          private val errorColor : Colors,
                                          ) : AbstractNotification() {

    val ERROR_NOTIFICATION_WIDTH = 200

    override val validUntil: Long = System.currentTimeMillis() + 100000

    override fun onRender(): Int {
        translateX(- ERROR_NOTIFICATION_WIDTH)
        renderRect(ERROR_NOTIFICATION_WIDTH.toDouble(), 11.0, errorColor, alpha = 255)
        translateY(-2)
        translateX(5){
            "[Error] $name".draw()
        }
        translateY(-9)

        val messageHeight = message
            .drawWithBackgroundAndWidth(errorColor, ERROR_NOTIFICATION_WIDTH, alpha = 100, padding = 5)

        return messageHeight + 11
    }

}


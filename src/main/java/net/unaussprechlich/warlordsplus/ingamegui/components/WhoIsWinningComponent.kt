package net.unaussprechlich.warlordsplus.ingamegui.components

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager
import net.unaussprechlich.warlordsplus.util.fdiv
import net.unaussprechlich.warlordsplus.util.removeFormatting
import net.unaussprechlich.warlordsplus.util.removeSpaces


object WhoIsWinningComponent : AbstractRenderComponent(RenderGameOverlayEvent.ElementType.TEXT) {

    var pointsToWin = 1000
    var bluePoints = 0
    var redPoints = 0
    var timeToWin = "00:00"

    var redFlag = ""
    var blueFlag = ""

    init {
        EventBus.register(::update)
    }

    override fun onRender(event: RenderGameOverlayEvent.Pre) {

        val vsWidth = timeToWin.unformattedWidth() + 12
        val pWidth = pointsToWin.toString().unformattedWidth() + 20
        val y = yTop + 10
        val bpWith = (pWidth * (bluePoints fdiv pointsToWin)).toInt()
        val rpWidth = (pWidth * (redPoints fdiv pointsToWin)).toInt()

        /*TODO convert
        drawCenteredStringWithHeaderBox(xCenter - (vsWidth / 2), y, vsWidth, timeToWin)

        drawRect(xCenter - (vsWidth / 2) - bpWith, y, bpWith, 13, Color(0, 0, 255, 150).convertToArgb())
        drawCenteredStringWithBox(xCenter - (vsWidth / 2) - pWidth, y, pWidth, bluePoints.toString(), Color(0, 0, 255, 50).convertToArgb())

        if (blueFlag != "Safe")
            drawStringWithBox(xCenter - (vsWidth / 2) - getTextWidth(blueFlag), y + 13, blueFlag, Color(34, 34, 39, 200).convertToArgb())


        drawRect(xCenter + (vsWidth / 2), y, rpWidth, 13, Color(255, 0, 0, 150).convertToArgb())
        drawCenteredStringWithBox(xCenter + (vsWidth / 2), y, pWidth, redPoints.toString(), Color(255, 0, 0, 50).convertToArgb())

        if (redFlag != "Safe")
            drawStringWithBox(xCenter + (vsWidth / 2), y + 13, redFlag, Color(34, 34, 39, 200).convertToArgb())
         */


    }

    fun update(e: TickEvent.ClientTickEvent) {
        try {
            if (ScoreboardManager.scoreboardFormatted.size != 15) return

            val blue = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardFormatted[12]
                    .replace(" ", "").replace("\uD83D\uDCA3", "")
            )
            val red = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardFormatted[11]
                    .replace(" ", "").replace("\uD83D\uDC7D", "")
            )
            val time = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardFormatted[9]
                    .replace(" ", "").replace("\uD83D\uDC0D", ""))

            timeToWin = time.substring(time.indexOf(":") + 1)
            pointsToWin = blue.substring(blue.indexOf("/") + 1).toInt()
            bluePoints = blue.substring(blue.indexOf(":") + 1, blue.indexOf("/")).toInt()
            redPoints = red.substring(red.indexOf(":") + 1,red.indexOf("/")).toInt()

            if(ScoreboardManager.scoreboardFormatted[6].contains("Flag")){
                val bFlag = ScoreboardManager.scoreboardFormatted[6].removeFormatting().removeSpaces()
                val rFlag = ScoreboardManager.scoreboardFormatted[7].removeFormatting().removeSpaces()

                redFlag = rFlag.substring(rFlag.indexOf(":") + 1)
                blueFlag = bFlag.substring(bFlag.indexOf(":") + 1)
            }

        } catch (e : Exception){

        }


    }


}

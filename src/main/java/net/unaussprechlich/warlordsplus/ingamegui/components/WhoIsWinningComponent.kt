package net.unaussprechlich.warlordsplus.ingamegui.components

import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ITickable
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.ScoreboardManager
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.ingamegui.consumers.IUpdateConsumer
import net.unaussprechlich.warlordsplus.util.fdiv
import org.lwjgl.util.Color


object WhoIsWinningComponent : AbstractRenderComponent(), IUpdateConsumer {

    var pointsToWin = 1000
    var bluePoints = 0
    var redPoints = 0
    var timeToWin = "00:00"

    override fun render(e: RenderGameOverlayEvent.Post){

        val vsWidth = getTextWidth(timeToWin) + 12
        val pWidth  = getTextWidth(pointsToWin.toString()) + 20
        val y = yTop + 10
        val bpWith  = (pWidth * (bluePoints fdiv pointsToWin)).toInt()
        val rpWidth = (pWidth * (redPoints fdiv pointsToWin)).toInt()

        drawCenteredStringWithBox(xCenter - (vsWidth / 2),y , vsWidth, timeToWin, Color(34,34,39, 200))

        drawRect(xCenter - (vsWidth / 2) - bpWith, y, bpWith, 13, Color(0, 0, 255, 150))
        drawCenteredStringWithBox(xCenter - (vsWidth / 2) - pWidth, y, pWidth, bluePoints.toString(), Color(0, 0, 255, 50))

        drawRect(xCenter + (vsWidth / 2), y, rpWidth, 13, Color(255, 0, 0, 150))
        drawCenteredStringWithBox(xCenter + (vsWidth / 2), y, pWidth, redPoints.toString(), Color(255, 0, 0, 50))
    }

    override fun update() {
        try{

            if(ScoreboardManager.scoreboardNames.size != 15) return

            val blue = EnumChatFormatting.getTextWithoutFormattingCodes(ScoreboardManager.scoreboardNames[12]
                    .replace(" ", "").replace("\uD83D\uDCA3", ""))
            val red = EnumChatFormatting.getTextWithoutFormattingCodes(ScoreboardManager.scoreboardNames[11]
                    .replace(" ", "").replace("\uD83D\uDC7D", ""))
            val time = EnumChatFormatting.getTextWithoutFormattingCodes(ScoreboardManager.scoreboardNames[9]
                    .replace(" ", "").replace("\uD83D\uDC0D", ""))

            timeToWin = time.substring(time.indexOf(":") + 1)
            pointsToWin = blue.substring(blue.indexOf("/") + 1).toInt()
            bluePoints = blue.substring(blue.indexOf(":") + 1, blue.indexOf("/")).toInt()
            redPoints = red.substring(red.indexOf(":") + 1,red.indexOf("/")).toInt()

        } catch (e : Exception){
            println(e)
        }


    }

}
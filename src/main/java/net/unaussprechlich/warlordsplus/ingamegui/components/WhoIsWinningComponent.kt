package net.unaussprechlich.warlordsplus.ingamegui.components

import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ITickable
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.ScoreboardManager
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import org.lwjgl.util.Color


object WhoIsWinningComponent : AbstractRenderComponent(), ITickable {

    var pointsToWin = 1000
    var bluePoints = 0
    var redPoints = 0

    override fun render(e: RenderGameOverlayEvent.Post){

        drawStringWithBox(200, 100, "PointsToWin: $pointsToWin", Color(0, 0, 0, 100))
        drawStringWithBox(200, 112, "BLU: $bluePoints", Color(0, 0, 255, 100))
        drawStringWithBox(200, 124, "RED: $redPoints", Color(255, 0, 0, 100))
    }

    override fun update() {
        try{

            if(ScoreboardManager.scoreboardNames.size != 15) return


            val blue = EnumChatFormatting.getTextWithoutFormattingCodes(ScoreboardManager.scoreboardNames[12]
                    .replace(" ", "").replace("\uD83D\uDCA3", ""))
            val red = EnumChatFormatting.getTextWithoutFormattingCodes(ScoreboardManager.scoreboardNames[11]
                    .replace(" ", "").replace("\uD83D\uDC7D", ""))

            println("BLU: $blue")
            println("RED: $red")

            pointsToWin = blue.substring(blue.indexOf("/") + 1).toInt()
            bluePoints = blue.substring(blue.indexOf(":") + 1, blue.indexOf("/")).toInt()
            redPoints = red.substring(red.indexOf(":") + 1,red.indexOf("/")).toInt()
        } catch (e : Exception){
            println(e)
        }


    }

}
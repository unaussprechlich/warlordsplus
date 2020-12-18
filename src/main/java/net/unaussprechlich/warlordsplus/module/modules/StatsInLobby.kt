package net.unaussprechlich.warlordsplus.module.modules

import kotlinx.serialization.UnstableDefault
import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.EnumChatFormatting
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.stats.StatsLoader
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.RenderEntitiesEvent
import net.unaussprechlich.warlordsplus.util.drawSr
import java.text.NumberFormat
import java.util.*


object StatsInLobby : IModule, RenderApi.World() {

    @UnstableDefault
    override fun onRender(event: RenderEntitiesEvent) {
        if (!GameStateManager.isWarlords) return
        if (GameStateManager.isIngame) return

        val player = Minecraft.getMinecraft().thePlayer.displayNameString
        val data = StatsLoader.getPlayer(player)?.data ?: return

        if (data.warlordsSr == null || data.warlordsHypixel == null) return


        glMatrix {
            translateToPos(-2518.1, 59.0, 744.5)
            rotateY(90.0f)
            scaleForText()
            scale(.95)
            renderRectXCentered(130.0, 300.0, Colors.DEF, 255, -0.5)
            translateX(-61.0)
            translateY(-4.0)
            drawSr(data.warlordsSr.sR)
            translateY(-11.5)
            "Plays: ${data.warlordsSr.plays}".draw()
            translateY(-8.0)
            "W/L: ${data.warlordsSr.wL}".draw()
            translateY(-8.0)
            "Wins: ${data.warlordsHypixel.wins}".draw()
            translateY(-8.0)
            "Blue Wins: ${data.warlordsHypixel.winsBlu}".draw()
            translateY(-8.0)
            "Red Wins: ${data.warlordsHypixel.winsRed}".draw()
            translateY(-8.0)
            "Losses: ${data.warlordsHypixel.losses}".draw()
            translateY(-8.0)
            "Left/AFKed: ${data.warlordsHypixel.penalty}".draw()
            translateY(-8.0)
            "DHP: ${data.warlordsSr.dHP}".draw()
            translateY(-8.0)
            "DMG Dealt: ${data.warlordsHypixel.damage}".draw()
            translateY(-8.0)
            "DMG Taken: ${data.warlordsHypixel.damageTaken}".draw()
            translateY(-8.0)
            "Healing Dealt: ${data.warlordsHypixel.heal}".draw()
            translateY(-8.0)
            "Healing Leeched: ${data.warlordsHypixel.lifeLeeched}".draw()
            translateY(-8.0)
            "Prevented: ${data.warlordsHypixel.damagePrevented}".draw()
            translateY(-8.0)
            "K/D: ${data.warlordsSr.kD}".draw()
            translateY(-8.0)
            "KA/D: ${data.warlordsSr.kDA}".draw()
            translateY(-8.0)
            "Kills: ${data.warlordsHypixel.kills}".draw()
            translateY(-8.0)
            "Deaths: ${data.warlordsHypixel.deaths}".draw()
            translateY(-8.0)
            "Assists: ${data.warlordsHypixel.assists}".draw()
        }

        fun EntityArmorStand.renderClassStats(sr: Int?, wl: Double?, dhp: Int?, rank: Int?) {
            glMatrix {
                translateToPos(this.posX, this.posY + 4, this.posZ)
                scaleForText()
                rotateY(-90.0f)
                scale(1.025)
                renderRectXCentered(70.0, 56.0, Colors.DEF, 255, -0.5)
                translateY(-5.0)
                translateX(-25.0)
                drawSr(sr)
                translateX(25.0)
                translateY(-15.0)
                "W/L: $wl".drawCentered()
                translateY(-9.0)
                if (dhp == null)
                    "DHP: 0".drawCentered()
                else
                    "DHP: ${NumberFormat.getNumberInstance(Locale.US).format(dhp)}".drawCentered()
                translateY(-15.0)
                if (rank != null)
                    "#$rank".drawCentered()
                else
                    "unranked".drawCentered()
            }

        }

        Minecraft.getMinecraft().theWorld.getLoadedEntityList().filter {
            it is EntityArmorStand && it.customNameTag.contains("Shaman") || it.customNameTag.contains("Mage") || it.customNameTag.contains(
                "Warrior"
            ) || it.customNameTag.contains("Paladin") || (it.customNameTag.contains("Capture the Flag") && it.posX != -2540.5) || (it.customNameTag.contains(
                "Domination"
            ) && it.posX != -2540.5) || (it.customNameTag.contains("Team Deathmatch") && it.posX != -2540.5) || it.customNameTag.contains(
                "Queue"
            )
        }.map {
            it as EntityArmorStand
        }.forEach {
            when {
                it.customNameTag.contains("Shaman") -> {
                    it.renderClassStats(
                        data.warlordsSr.shaman?.SR,
                        data.warlordsSr.shaman?.WL,
                        data.warlordsSr.shaman?.DHP,
                        data.ranking?.shaman?.overall
                    )
                }
                it.customNameTag.contains("Mage") -> {
                    it.renderClassStats(
                        data.warlordsSr.mage?.SR,
                        data.warlordsSr.mage?.WL,
                        data.warlordsSr.mage?.DHP,
                        data.ranking?.mage?.overall
                    )
                }
                it.customNameTag.contains("Warrior") -> {
                    it.renderClassStats(
                        data.warlordsSr.warrior?.SR,
                        data.warlordsSr.warrior?.WL,
                        data.warlordsSr.warrior?.DHP,
                        data.ranking?.warrior?.overall
                    )
                }
                it.customNameTag.contains("Paladin") -> {
                    it.renderClassStats(
                        data.warlordsSr.paladin?.SR,
                        data.warlordsSr.paladin?.WL,
                        data.warlordsSr.paladin?.DHP,
                        data.ranking?.paladin?.overall
                    )
                }
                it.customNameTag.contains("Capture the Flag") -> {
                    glMatrix {
                        translateToPos(it.posX, it.posY + 1.2, it.posZ)
                        autoRotateX()
                        translate(3.0, 0.0, 0.0)
                        scaleForText()
                        scale(.95)
                        renderRect(90.0, 55.0, Colors.DEF, 255, -0.5)
                        translate(4.0, 4.0, 0.0)
                        "Wins: ${data.warlordsHypixel.winsCapturetheflag}".draw()
                        translate(-4.0, 10.0, 0.0)
                        renderRect(140.0, 2.0, Colors.WHITE, 255, -0.0)
                        translate(4.0, 6.0, 0.0)
                        "Blue Wins: ${EnumChatFormatting.BLUE}${data.warlordsHypixel.winsCapturetheflagBlu}".draw()
                        translateY(-8.0)
                        "Red Wins: ${EnumChatFormatting.RED}${data.warlordsHypixel.winsCapturetheflagBlu}".draw()
                        translateY(-8.0)
                        "Team Caps: ${data.warlordsHypixel.flagConquerTeam}".draw()
                        translateY(-8.0)
                        "Your Caps: ${data.warlordsHypixel.flagConquerSelf}".draw()
                    }
                }
                it.customNameTag.contains("Domination") -> {
                    glMatrix {
                        translateToPos(it.posX - .25, it.posY + 1.25, it.posZ - 1.75)
                        autoRotate()
                        scaleForText()
                        scale(.95)
                        renderRectXCentered(75.0, 30.0, Colors.DEF, 100, -0.5)
                        translateX(-34.0)
                        translateY(-4.0)
                        "Wins: ${data.warlordsHypixel.winsDomination}".draw()
                        translateY(-8.0)
                        "Blue Wins: ${data.warlordsHypixel.winsDominationBlu}".draw()
                        translateY(-8.0)
                        "Red Wins: ${data.warlordsHypixel.winsDominationRed}".draw()
                    }
                }
                it.customNameTag.contains("Team Deathmatch") -> {
                    glMatrix {
                        translateToPos(it.posX - .25, it.posY + 1.25, it.posZ - 1.75)
                        autoRotate()
                        scaleForText()
                        scale(.95)
                        renderRectXCentered(82.0, 30.0, Colors.DEF, 100, -0.5)
                        translateX(-37.0)
                        translateY(-4.0)
                        "Wins: ${data.warlordsHypixel.winsTeamdeathmatch}".draw()
                        translateY(-8.0)
                        "Blue Wins: ${data.warlordsHypixel.winsTeamdeathmatchBlu}".draw()
                        translateY(-8.0)
                        "Red Wins: ${data.warlordsHypixel.winsTeamdeathmatchRed}".draw()
                    }
                }
                it.customNameTag.contains("Queue") -> {
                    glMatrix {
                        translateToPos(it.posX, it.posY + 2, it.posZ)
                        autoRotate()
                        scaleForText()
                        scale(5.0)
                        translateY(18.5)
                        //renderRectXCentered(10.0, 15.0, Colors.DEF, 100, -0.5)
                        val queue = it.customNameTag.substring(2, it.customNameTag.indexOf("in") - 1)
                        queue.drawCentered()
                    }
                }
            }
        }
    }


}

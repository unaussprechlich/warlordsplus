package net.unaussprechlich.warlordsplus.module.modules

import kotlinx.serialization.UnstableDefault
import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityArmorStand
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.stats.StatsLoader
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.RenderEntitiesEvent
import net.unaussprechlich.warlordsplus.util.WarlordsPlusRenderer

object StatsInLobby : IModule, WarlordsPlusRenderer.World(seeTextThruBlocks = true) {
    @UnstableDefault
    override fun onRender(event: RenderEntitiesEvent) {
        if (!GameStateManager.isWarlords) return
        if (GameStateManager.isIngame) return

        val player = Minecraft.getMinecraft().thePlayer.displayNameString
        val data = StatsLoader.getPlayer(player)?.data ?: return

        if (data.warlordsSr == null || data.warlordsHypixel == null) return


        glMatrix {
            translateToPos(-2529.0, 55.0, 744.5)
            autoRotate()
            scaleForText()
            scale(.95)
            renderRectXCentered(320.0, 75.0, Colors.DEF, 100, -0.5)
            translateY(-4.0)
            "${data.warlordsSr.sR}".drawCentered()
            translateY(-11.5)
            translateX(-152.0)
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

            translateX(84.0)
            translateY(48.0)

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

            translateX(140.5)
            translateY(41.0)

            translateY(-1.0)
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
                    glMatrix {
                        translateToPos(-2520.5, 59.0, 740.5)
                        autoRotate()
                        scaleForText()
                        scale(1.025)
                        renderRectXCentered(80.0, 75.0, Colors.DEF, 100, -0.5)
                        translateY(-5.0)
                        "SR: ${data.warlordsSr.shaman?.SR}".drawCentered()
                        translateY(-8.0)
                        "W/L: ${data.warlordsSr.shaman?.WL}".drawCentered()
                        translateY(-8.0)
                        "Wins: ${data.warlordsHypixel.winsShaman}".drawCentered()
                        translateY(-8.0)
                        "Losses: ${data.warlordsHypixel.lossesShaman}".drawCentered()
                        translateY(-8.0)
                        "DHP: ${data.warlordsSr.shaman?.DHP}".drawCentered()
                        translateY(-8.0)
                        "D: ${data.warlordsHypixel.damageShaman}".drawCentered()
                        translateY(-8.0)
                        "H: ${data.warlordsHypixel.healShaman}".drawCentered()
                        translateY(-8.0)
                        "P: ${data.warlordsHypixel.damagePreventedShaman}".drawCentered()
                    }
                }
                it.customNameTag.contains("Mage") -> {
                    glMatrix {
                        translateToPos(-2520.5, 59.0, 742.5)
                        autoRotate()
                        scaleForText()
                        scale(1.025)
                        renderRectXCentered(80.0, 75.0, Colors.DEF, 100, -0.5)
                        translateY(-5.0)
                        "SR: ${data.warlordsSr.mage?.SR}".drawCentered()
                        translateY(-8.0)
                        "W/L: ${data.warlordsSr.mage?.WL}".drawCentered()
                        translateY(-8.0)
                        "Wins: ${data.warlordsHypixel.winsMage}".drawCentered()
                        translateY(-8.0)
                        "Losses: ${data.warlordsHypixel.lossesMage}".drawCentered()
                        translateY(-8.0)
                        "DHP: ${data.warlordsSr.mage?.DHP}".drawCentered()
                        translateY(-8.0)
                        "D: ${data.warlordsHypixel.damageMage}".drawCentered()
                        translateY(-8.0)
                        "H: ${data.warlordsHypixel.healMage}".drawCentered()
                        translateY(-8.0)
                        "P: ${data.warlordsHypixel.damagePreventedMage}".drawCentered()
                    }
                }
                it.customNameTag.contains("Warrior") -> {
                    glMatrix {
                        translateToPos(-2520.5, 59.0, 746.5)
                        autoRotate()
                        scaleForText()
                        scale(1.025)
                        renderRectXCentered(80.0, 75.0, Colors.DEF, 100, -0.5)
                        translateY(-5.0)
                        "SR: ${data.warlordsSr.warrior?.SR}".drawCentered()
                        translateY(-8.0)
                        "W/L: ${data.warlordsSr.warrior?.WL}".drawCentered()
                        translateY(-8.0)
                        "Wins: ${data.warlordsHypixel.winsWarrior}".drawCentered()
                        translateY(-8.0)
                        "Losses: ${data.warlordsHypixel.lossesWarrior}".drawCentered()
                        translateY(-8.0)
                        "DHP: ${data.warlordsSr.warrior?.DHP}".drawCentered()
                        translateY(-8.0)
                        "D: ${data.warlordsHypixel.damageWarrior}".drawCentered()
                        translateY(-8.0)
                        "H: ${data.warlordsHypixel.healWarrior}".drawCentered()
                        translateY(-8.0)
                        "P: ${data.warlordsHypixel.damagePreventedWarrior}".drawCentered()
                    }
                }
                it.customNameTag.contains("Paladin") -> {
                    glMatrix {
                        translateToPos(-2520.5, 59.0, 748.5)
                        autoRotate()
                        scaleForText()
                        scale(1.025)
                        renderRectXCentered(80.0, 75.0, Colors.DEF, 100, -0.5)
                        translateY(-5.0)
                        "SR: ${data.warlordsSr.paladin?.SR}".drawCentered()
                        translateY(-8.0)
                        "W/L: ${data.warlordsSr.paladin?.WL}".drawCentered()
                        translateY(-8.0)
                        "Wins: ${data.warlordsHypixel.winsPaladin}".drawCentered()
                        translateY(-8.0)
                        "Losses: ${data.warlordsHypixel.lossesPaladin}".drawCentered()
                        translateY(-8.0)
                        "DHP: ${data.warlordsSr.paladin?.DHP}".drawCentered()
                        translateY(-8.0)
                        "D: ${data.warlordsHypixel.damagePaladin}".drawCentered()
                        translateY(-8.0)
                        "H: ${data.warlordsHypixel.healPaladin}".drawCentered()
                        translateY(-8.0)
                        "P: ${data.warlordsHypixel.damagePreventedPaladin}".drawCentered()
                    }
                }
                it.customNameTag.contains("Capture the Flag") -> {
                    glMatrix {
                        translateToPos(it.posX - .25, it.posY + 1.25, it.posZ - 1.75)
                        autoRotate()
                        scaleForText()
                        scale(.95)
                        renderRectXCentered(95.0, 47.0, Colors.DEF, 100, -0.5)
                        translateX(-43.0)
                        translateY(-4.0)
                        "Wins: ${data.warlordsHypixel.winsCapturetheflag}".draw()
                        translateY(-8.0)
                        "Blue Wins: ${data.warlordsHypixel.winsCapturetheflagBlu}".draw()
                        translateY(-8.0)
                        "Red Wins: ${data.warlordsHypixel.winsCapturetheflagBlu}".draw()
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

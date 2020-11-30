package net.unaussprechlich.warlordsplus.util.commands

import kotlinx.serialization.UnstableDefault
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.unaussprechlich.warlordsplus.module.modules.stats.StatsLoader
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

class GetPlayerStatsCommand : ICommand, IUpdateConsumer {

    var counter = 0

    override fun getCommandName(): String {
        return "playerstat"
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "get stats of player"
    }

    override fun getCommandAliases(): List<String> {
        return ArrayList()
    }

    @UnstableDefault
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, parameters: Array<String>) {
        if (parameters.isNotEmpty()) {
            val player: String = parameters[0]
            StatsLoader.loadPlayerWithCallback(player) {
                val data = it.data
                if (data == null) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}LOADING PLAYER - TRY AGAIN IN 3-7 SECONDS"))
                } else {
                    val names = StringBuilder()
                    data.nameHistory?.forEach { names.append(it.name + ", ") }
                    val nameHistory: IChatComponent = ChatComponentText(
                        "${EnumChatFormatting.GOLD}Names: ${EnumChatFormatting.WHITE}${names}\n"
                    )
                    val stats: IChatComponent = ChatComponentText(
                        "${EnumChatFormatting.GOLD}General Stats: \n" +
                                (if (data.playername == "sumSmash") "  D=====8\n" else "") +
                                "${EnumChatFormatting.GRAY}  SR: ${EnumChatFormatting.WHITE}${data.warlordsSr?.sR}\n" +
                                "${EnumChatFormatting.GRAY}  Plays: ${EnumChatFormatting.WHITE}${data.warlordsSr?.plays}\n" +
                                "${EnumChatFormatting.GRAY}  Afks/Leaves: ${EnumChatFormatting.WHITE}${data.warlordsHypixel?.penalty}\n" +
                                "${EnumChatFormatting.GRAY}  W/L: ${EnumChatFormatting.WHITE}${data.warlordsSr?.wL}\n" +
                                "${EnumChatFormatting.GRAY}  Wins: ${EnumChatFormatting.WHITE}${data.warlordsHypixel?.wins}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.BLUE}${data.warlordsHypixel?.winsBlu}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.RED}${data.warlordsHypixel?.winsRed}\n" +
                                "${EnumChatFormatting.GRAY}  Losses: ${EnumChatFormatting.WHITE}${data.warlordsHypixel?.losses}\n" +
                                "${EnumChatFormatting.GRAY}  K/D: ${EnumChatFormatting.WHITE}${data.warlordsSr?.kD}\n" +
                                "${EnumChatFormatting.GRAY}  KA/D: ${EnumChatFormatting.WHITE}${data.warlordsSr?.kDA}\n" +
                                "${EnumChatFormatting.GRAY}  Kills: ${EnumChatFormatting.WHITE}${data.warlordsHypixel?.kills}\n" +
                                "${EnumChatFormatting.GRAY}  Deaths: ${EnumChatFormatting.WHITE}${data.warlordsHypixel?.deaths}\n" +
                                "${EnumChatFormatting.GRAY}  Assists: ${EnumChatFormatting.WHITE}${data.warlordsHypixel?.assists}\n" +
                                "\n" +
                                "${EnumChatFormatting.GOLD}Class Stats: \n" +
                                "${EnumChatFormatting.GOLD}  Paladin: ${EnumChatFormatting.WHITE}${data.warlordsSr?.paladin?.LEVEL}\n" +
                                "${EnumChatFormatting.GRAY}    SR: ${EnumChatFormatting.WHITE}${data.warlordsSr?.paladin?.SR}\n" +
                                "${EnumChatFormatting.GRAY}    W/L: ${EnumChatFormatting.WHITE}${data.warlordsSr?.paladin?.WL}\n" +
                                "${EnumChatFormatting.GRAY}    DHP: ${EnumChatFormatting.WHITE}${data.warlordsSr?.paladin?.DHP}\n" +
                                "${EnumChatFormatting.GRAY}    Plays: ${EnumChatFormatting.WHITE}${data.warlordsHypixel?.paladinPlays}\n" +
                                "${EnumChatFormatting.GOLD}  Warrior: ${EnumChatFormatting.WHITE}${data.warlordsSr?.warrior?.LEVEL}\n" +
                                "${EnumChatFormatting.GRAY}    SR: ${EnumChatFormatting.WHITE}${data.warlordsSr?.warrior?.SR}\n" +
                                "${EnumChatFormatting.GRAY}    W/L: ${EnumChatFormatting.WHITE}${data.warlordsSr?.warrior?.WL}\n" +
                                "${EnumChatFormatting.GRAY}    DHP: ${EnumChatFormatting.WHITE}${data.warlordsSr?.warrior?.DHP}\n" +
                                "${EnumChatFormatting.GRAY}    Plays: ${EnumChatFormatting.WHITE}${data.warlordsHypixel?.warriorPlays}\n" +
                                "${EnumChatFormatting.GOLD}  Mage: ${EnumChatFormatting.WHITE}${data.warlordsSr?.mage?.LEVEL}\n" +
                                "${EnumChatFormatting.GRAY}    SR: ${EnumChatFormatting.WHITE}${data.warlordsSr?.mage?.SR}\n" +
                                "${EnumChatFormatting.GRAY}    W/L: ${EnumChatFormatting.WHITE}${data.warlordsSr?.mage?.WL}\n" +
                                "${EnumChatFormatting.GRAY}    DHP: ${EnumChatFormatting.WHITE}${data.warlordsSr?.mage?.DHP}\n" +
                                "${EnumChatFormatting.GRAY}    Plays: ${EnumChatFormatting.WHITE}${data.warlordsHypixel?.magePlays}\n" +
                                "${EnumChatFormatting.GOLD}  Shaman: ${EnumChatFormatting.WHITE}${data.warlordsSr?.shaman?.LEVEL}\n" +
                                "${EnumChatFormatting.GRAY}    SR: ${EnumChatFormatting.WHITE}${data.warlordsSr?.shaman?.SR}\n" +
                                "${EnumChatFormatting.GRAY}    W/L: ${EnumChatFormatting.WHITE}${data.warlordsSr?.shaman?.WL}\n" +
                                "${EnumChatFormatting.GRAY}    DHP: ${EnumChatFormatting.WHITE}${data.warlordsSr?.shaman?.DHP}\n" +
                                "${EnumChatFormatting.GRAY}    Plays: ${EnumChatFormatting.WHITE}${data.warlordsHypixel?.shamanPlays}\n"
                    )

                    //boosted meter
                    var winRatioWeightBasic = .4
                    var classWinRatioWeightBasic = .6
                    if (data.warlordsSr!!.plays!! > 5000) {
                        winRatioWeightBasic = .7
                        classWinRatioWeightBasic = .3
                    }
                    //blue vs red wins || >1.6x = party booster
                    val winRatio =
                        if ((data.warlordsHypixel?.winsBlu?.toDouble())?.div((data.warlordsHypixel.winsRed!!))
                                ?.times(50)
                                ?.div(1.5)!! < 0
                        ) 0 else ((data.warlordsHypixel.winsBlu.toDouble()).div((data.warlordsHypixel.winsRed!!))
                            .times(50)
                            .div(1.5)).roundToInt()
                    //level to win ration || all 90s = 3000 plays
                    val classWinRatio =
                        (3000 - (data.warlordsSr.paladin?.LEVEL!! + data.warlordsSr.warrior?.LEVEL!! +
                                data.warlordsSr.mage?.LEVEL!! + data.warlordsSr.shaman?.LEVEL!!) * 3000 / 360) / 10


                    val boostedNum =
                        (winRatio * winRatioWeightBasic + classWinRatio * classWinRatioWeightBasic).roundToInt()
                    val boostedMeter: IChatComponent = ChatComponentText(
                        "${EnumChatFormatting.AQUA}SMASH BOOSTED METER/100 V1: " +
                                "${
                                    when {
                                        boostedNum > 80 -> EnumChatFormatting.DARK_RED
                                        boostedNum > 60 -> EnumChatFormatting.RED
                                        boostedNum > 40 -> EnumChatFormatting.YELLOW
                                        boostedNum > 20 -> EnumChatFormatting.DARK_GREEN
                                        else -> EnumChatFormatting.GREEN
                                    }
                                }$boostedNum"
                    )

                    //garbage meter
                    //5000/50 = 0 --- 0/0 = 100/
                    val sr = data.warlordsSr.sR
                    // > 70% = capper one trick - punish = *.85
                    val capToWinRatio =
                        data.warlordsHypixel.flagConquerSelf!!.toDouble() / data.warlordsHypixel.winsCapturetheflag!!
                    // > 50% = cryo one trick - punish = *.70
                    val cryoPlayRatio = data.warlordsHypixel.cryomancerPlays!!.toDouble() / data.warlordsSr.plays!!

                    //sr = 5000 = 0 garbage
                    //sr = 0 = 100 garbage
                    var garbageNum = (abs(5000 - sr!!) / 100) * 2.0
                    if (capToWinRatio > .75)
                        garbageNum += 25
                    if (cryoPlayRatio > .5)
                        garbageNum += 20

                    val garbageMeter: IChatComponent = ChatComponentText(
                        "${EnumChatFormatting.DARK_AQUA}SMASH GARBAGE METER/100 V1: " +
                                "${
                                    when {
                                        garbageNum > 80 -> EnumChatFormatting.DARK_RED
                                        garbageNum > 60 -> EnumChatFormatting.RED
                                        garbageNum > 40 -> EnumChatFormatting.YELLOW
                                        garbageNum > 20 -> EnumChatFormatting.DARK_GREEN
                                        else -> EnumChatFormatting.GREEN
                                    }
                                }${garbageNum.toInt()}"
                    )

                    var boostedWeight = .5
                    var garbageWeight = 1.5
                    if (data.warlordsSr.plays > 5000) {
                        boostedWeight = 1.5
                        garbageWeight = .5
                    }

                    val shitMeter: IChatComponent = ChatComponentText(
                        "OVERALL SHITNESS: " +
                                "${
                                    when {
                                        (boostedNum.toDouble() * boostedWeight + garbageNum * garbageWeight) / 2 > 100 -> EnumChatFormatting.BLACK
                                        (boostedNum.toDouble() * boostedWeight + garbageNum * garbageWeight) / 2 > 80 -> EnumChatFormatting.DARK_RED
                                        (boostedNum.toDouble() * boostedWeight + garbageNum * garbageWeight) / 2 > 60 -> EnumChatFormatting.RED
                                        (boostedNum.toDouble() * boostedWeight + garbageNum * garbageWeight) / 2 > 40 -> EnumChatFormatting.YELLOW
                                        (boostedNum.toDouble() * boostedWeight + garbageNum * garbageWeight) / 2 > 20 -> EnumChatFormatting.DARK_GREEN
                                        else -> EnumChatFormatting.GREEN
                                    }
                                }${
                                    when {
                                        (boostedNum.toDouble() * boostedWeight + garbageNum * garbageWeight) / 2 > 100 -> "SUPER SHIT"
                                        (boostedNum.toDouble() * boostedWeight + garbageNum * garbageWeight) / 2 > 80 -> "PRETTY SHIT"
                                        (boostedNum.toDouble() * boostedWeight + garbageNum * garbageWeight) / 2 > 60 -> "SHIT"
                                        (boostedNum.toDouble() * boostedWeight + garbageNum * garbageWeight) / 2 > 40 -> "KINDA SHIT"
                                        (boostedNum.toDouble() * boostedWeight + garbageNum * garbageWeight) / 2 > 20 -> "BARELY SHIT"
                                        else -> "NOT REALLY SHIT"
                                    }
                                }"
                    )
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                    Minecraft.getMinecraft().thePlayer.addChatMessage(nameHistory)
                    Minecraft.getMinecraft().thePlayer.addChatMessage(stats)
                    Minecraft.getMinecraft().thePlayer.addChatMessage(boostedMeter)
                    Minecraft.getMinecraft().thePlayer.addChatMessage(garbageMeter)
                    Minecraft.getMinecraft().thePlayer.addChatMessage(shitMeter)
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                }
            }
        }
    }

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
        return true
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender,
        args: Array<String>,
        pos: BlockPos
    ): List<String>? {
        return null
    }

    override fun isUsernameIndex(args: Array<String>, index: Int): Boolean {
        return false
    }

    override fun compareTo(o: ICommand): Int {
        return 0
    }

}
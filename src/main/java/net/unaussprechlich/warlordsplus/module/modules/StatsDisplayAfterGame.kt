package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.util.MinecraftOpenGlStuff
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.elements.HudElementHitCounter
import net.unaussprechlich.warlordsplus.hud.elements.HudElementKillParticipation
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.DefaultFontInfo
import net.unaussprechlich.warlordsplus.util.SpecsEnum
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.text.DecimalFormat
import kotlin.math.roundToInt

object StatsDisplayAfterGame : IModule {

    var showStats = true
    var counter = 0
    var lastGameStats = StringSelection("")
    var sendDHP = false

    init {
        EventBus.register<ResetEvent> {
            showStats = true
            counter = 0
        }
        EventBus.register(::onChat)
    }

    fun onChat(e: ClientChatReceivedEvent) {
        if (GameStateManager.notIngame) return

        val message = e.message.formattedText
        if (message.removeFormatting().contains("Winner - ")) {
            addStatsToClipboard(message)
        }
        if (GameStateManager.isIngame) {
            if (sendDHP) {
                sendCenteredMessage("       DHP: ${EnumChatFormatting.GOLD}${addCommas(DamageHealingAbsorbedEndOfGame.totalDHP)}")
                sendDHP = false
            }
            if (message.removeFormatting().trimStart().startsWith("Damage:")) {
                sendDHP = true
            }
            if (message.contains("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")) {
                counter++
                if (counter == 2) {
                    if (showStats) {
                        if (printGeneralStats) {
                            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                            displayStats()
                        }
                        if (printScoreboard) {
                            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                            displayScoreboard()
                        }
                        if (printGeneralStats || printScoreboard)
                            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                        //displayMinuteStats()
                        showStats = false
                    }
                    counter = 0
                }
            }
        }
    }

    fun addCommas(number: Int): String {
        if (number == 0) return "0"
        val formatter = DecimalFormat("#,###.00")
        return formatter.format(number.toDouble()).toString().dropLast(3)
    }

    fun addZero(number: Int): String {
        if (number < 10)
            return "0$number"
        return number.toString()
    }

    private fun displayClassStats() {
        when (ThePlayer.spec) {
            SpecsEnum.AVENGER -> {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "Avenger's Strike - " + ThePlayer.strikeCounter
                    )
                )
            }
            SpecsEnum.CRUSADER -> {

            }
            SpecsEnum.PROTECTOR -> {

            }
            SpecsEnum.BERSERKER -> {

            }
            SpecsEnum.DEFENDER -> {

            }
            SpecsEnum.REVENANT -> {

            }
            SpecsEnum.PYROMANCER -> {

            }
            SpecsEnum.CRYOMANCER -> {

            }
            SpecsEnum.AQUAMANCER -> {

            }
            SpecsEnum.THUNDERLORD -> {

            }
            SpecsEnum.SPIRITGUARD -> {

            }
            SpecsEnum.EARTHWARDEN -> {

            }
        }
    }

    private fun displayStats() {
        val stats: IChatComponent = ChatComponentText(
            "${EnumChatFormatting.GRAY}Hits: ${EnumChatFormatting.WHITE}${HudElementHitCounter.totalHits}${EnumChatFormatting.GOLD} Energy Given: ${EnumChatFormatting.WHITE}${ThePlayer.energyGivenCounter}${EnumChatFormatting.GOLD} Energy Received: ${EnumChatFormatting.WHITE}${ThePlayer.energyReceivedCounter}${EnumChatFormatting.GOLD} Energy Stolen: ${EnumChatFormatting.WHITE}${ThePlayer.energyStolenCounter}${EnumChatFormatting.GOLD} Energy Lost: ${EnumChatFormatting.WHITE}${ThePlayer.energyLostCounter}${"\n"}" +
                    "${EnumChatFormatting.GOLD}KP: ${EnumChatFormatting.WHITE}${if (HudElementKillParticipation.numberOfTeamKills > 0) (HudElementKillParticipation.playerKills / HudElementKillParticipation.numberOfTeamKills.toDouble() * 100).roundToInt() else 0}%${EnumChatFormatting.BLUE} Blue Kills: ${EnumChatFormatting.WHITE}${
                        getTeamKills(
                            true
                        )
                    }${EnumChatFormatting.RED} Red Kills: ${EnumChatFormatting.WHITE}${getTeamKills(false)}${"\n"}" +
                    "${EnumChatFormatting.DARK_GREEN}Healing Received: ${EnumChatFormatting.WHITE}${addCommas(ThePlayer.healingReceivedCounter)}${EnumChatFormatting.DARK_RED} Damage Received: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            ThePlayer.damageTakenCounter
                        )
                    }${"\n\n"}" +
                    "${EnumChatFormatting.DARK_RED}Highest Damage Per Min: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.highestDamage
                        )
                    }${EnumChatFormatting.DARK_RED} At Minute ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.highestDamageMin
                        )
                    }${"\n"}" +
                    "${EnumChatFormatting.RED}Lowest Damage Per Min: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.lowestDamage
                        )
                    }${EnumChatFormatting.RED} At Minute ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.lowestDamageMin
                        )
                    }${"\n"}" +
                    "${EnumChatFormatting.DARK_GREEN}Highest Healing Per Min: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.highestHealing
                        )
                    }${EnumChatFormatting.DARK_GREEN} At Minute ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.highestHealingMin
                        )
                    }${"\n"}" +
                    "${EnumChatFormatting.GREEN}Lowest Healing Per Min: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.lowestHealing
                        )
                    }${EnumChatFormatting.GREEN} At Minute ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.lowestHealingMin
                        )
                    }${"\n"}" +
                    "${EnumChatFormatting.GOLD}Highest Absorbed Per Min: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.highestAbsorbed
                        )
                    }${EnumChatFormatting.GOLD} At Minute ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.highestAbsorbedMin
                        )
                    }${"\n"}" +
                    "${EnumChatFormatting.YELLOW}Lowest Absorbed Per Min: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.lowestAbsorbed
                        )
                    }${EnumChatFormatting.YELLOW} At Minute ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.lowestAbsorbedMin
                        )
                    }${"\n"}" +
                    "${EnumChatFormatting.DARK_PURPLE}Highest DHP Per Min: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.highestDHP
                        )
                    }${EnumChatFormatting.DARK_PURPLE} At Minute ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.highestDHPMin
                        )
                    }${"\n"}" +
                    "${EnumChatFormatting.LIGHT_PURPLE}Lowest DHP Per Min: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.lowestDHP
                        )
                    }${EnumChatFormatting.LIGHT_PURPLE} At Minute ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.lowestDHPMin
                        )
                    }${"\n\n"}" +
                    "${EnumChatFormatting.DARK_RED}Average DPM: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.averageDamagePerMin
                        )
                    }${EnumChatFormatting.DARK_GREEN} Average HPM: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.averageHealingPerMin
                        )
                    }${EnumChatFormatting.GOLD} Average APM: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.averageAbsorbedPerMin
                        )
                    }${EnumChatFormatting.DARK_PURPLE} Average DHP: ${EnumChatFormatting.WHITE}${
                        addCommas(
                            DamageHealingAbsorbedEndOfGame.averageDHPPerMin
                        )
                    }"

        )
        Minecraft.getMinecraft().thePlayer.addChatMessage(stats)
    }

    private fun getTeamKills(blueTeam: Boolean): Int {
        val players = OtherPlayers.getPlayersForNetworkPlayers(MinecraftOpenGlStuff.thePlayer!!.sendQueue.playerInfoMap)
        val teamBlue = players.filter { it.team == TeamEnum.BLUE }.sortedByDescending { it.level }
        val teamRed = players.filter { it.team == TeamEnum.RED }.sortedByDescending { it.level }
        return if (blueTeam) {
            teamBlue.sumBy { it.kills }
        } else {
            teamRed.sumBy { it.kills }
        }
    }

    private fun displayScoreboard() {
        val players =
            OtherPlayers.getPlayersForNetworkPlayers(Minecraft.getMinecraft().thePlayer!!.sendQueue.playerInfoMap)
        val teamBlue = players.filter { it.team == TeamEnum.BLUE }.sortedByDescending { it.level }
        val teamRed = players.filter { it.team == TeamEnum.RED }.sortedByDescending { it.level }

        for (player in teamBlue) {
            if (ThePlayer.team == TeamEnum.BLUE) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.DARK_GRAY}[${EnumChatFormatting.WHITE}${player.warlord.shortName}${EnumChatFormatting.DARK_GRAY}][${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}${
                            addZero(
                                player.level
                            )
                        }${EnumChatFormatting.DARK_GRAY}][${player.spec.icon}${EnumChatFormatting.DARK_GRAY}] ${EnumChatFormatting.BLUE}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.healingReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_GREEN}${player.healingDone}${if (player.picks > 0) "${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GOLD}Picks: ${EnumChatFormatting.GREEN}${player.picks}" else ""}${if (player.caps > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Caps: ${EnumChatFormatting.RED}${player.caps}" else ""}${if (player.returns > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Returns: ${EnumChatFormatting.BLUE}${player.returns}" else ""}"
                    )
                )
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.DARK_GRAY}[${EnumChatFormatting.WHITE}${player.warlord.shortName}${EnumChatFormatting.DARK_GRAY}][${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}${
                            addZero(
                                player.level
                            )
                        }${EnumChatFormatting.DARK_GRAY}][${player.spec.icon}${EnumChatFormatting.DARK_GRAY}] ${EnumChatFormatting.BLUE}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.RED}${player.damageReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_RED}${player.damageDone}${if (player.picks > 0) "${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GOLD}Picks: ${EnumChatFormatting.GREEN}${player.picks}" else ""}${if (player.caps > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Caps: ${EnumChatFormatting.RED}${player.caps}" else ""}${if (player.returns > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Returns: ${EnumChatFormatting.BLUE}${player.returns}" else ""}"
                    )
                )
            }

        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
        for (player in teamRed) {
            if (ThePlayer.team == TeamEnum.RED) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.DARK_GRAY}[${EnumChatFormatting.WHITE}${player.warlord.shortName}${EnumChatFormatting.DARK_GRAY}][${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}${
                            addZero(
                                player.level
                            )
                        }${EnumChatFormatting.DARK_GRAY}][${player.spec.icon}${EnumChatFormatting.DARK_GRAY}] ${EnumChatFormatting.RED}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.healingReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_GREEN}${if (player.picks > 0) "${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GOLD}Picks: ${EnumChatFormatting.GREEN}${player.picks}" else ""}${if (player.caps > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Caps: ${EnumChatFormatting.BLUE}${player.caps}" else ""}${if (player.returns > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Returns: ${EnumChatFormatting.RED}${player.returns}" else ""}"
                    )
                )
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.DARK_GRAY}[${EnumChatFormatting.WHITE}${player.warlord.shortName}${EnumChatFormatting.DARK_GRAY}][${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}${
                            addZero(
                                player.level
                            )
                        }${EnumChatFormatting.DARK_GRAY}][${player.spec.icon}${EnumChatFormatting.DARK_GRAY}] ${EnumChatFormatting.RED}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.RED}${player.damageReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_RED}${player.damageDone}${if (player.picks > 0) "${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GOLD}Picks: ${EnumChatFormatting.GREEN}${player.picks}" else ""}${if (player.caps > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Caps: ${EnumChatFormatting.BLUE}${player.caps}" else ""}${if (player.returns > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Returns: ${EnumChatFormatting.RED}${player.returns}" else ""}"
                    )
                )
            }
        }
        //PAL[90]sumSmash - 32:3 - 3798:7893
    }

    fun displayMinuteStats() {
        for (minuteStat in ThePlayer.minuteStat.indices) {
            //Minute 1 -  Kills:123 - Deaths:123 - Hits:123 - Damage:123 - Healing: 123
            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("Minute $minuteStat - Kills:${ThePlayer.minuteStat[minuteStat][0]} - Deaths:${ThePlayer.minuteStat[minuteStat][1]} - Hits:${ThePlayer.minuteStat[minuteStat][2]} - Damage:${ThePlayer.minuteStat[minuteStat][3]} - Healing:${ThePlayer.minuteStat[minuteStat][4]}"))

        }

    }

    private fun addStatsToClipboard(winner: String) {
        val players =
            OtherPlayers.getPlayersForNetworkPlayers(Minecraft.getMinecraft().thePlayer!!.sendQueue.playerInfoMap)
        val teamBlue = players.filter { it.team == TeamEnum.BLUE }.sortedByDescending { it.level }
        val teamRed = players.filter { it.team == TeamEnum.RED }.sortedByDescending { it.level }
        var endGameStats = "Winners:"
        if (winner.contains("BLU")) {
            teamBlue.forEach {
                endGameStats += "${it.uuid.toString().replace("-", "")}[${it.kills}:${it.deaths}],"
            }
            endGameStats = endGameStats.substring(0, endGameStats.length - 1)
            endGameStats += "Losers:"
            teamRed.forEach {
                endGameStats += "${it.uuid.toString().replace("-", "")}[${it.kills}:${it.deaths}],"
            }
        } else {
            teamRed.forEach {
                endGameStats += "${it.uuid.toString().replace("-", "")}[${it.kills}:${it.deaths}],"
            }
            endGameStats = endGameStats.substring(0, endGameStats.length - 1)
            endGameStats += "Losers:"
            teamBlue.forEach {
                endGameStats += "${it.uuid.toString().replace("-", "")}[${it.kills}:${it.deaths}],"
            }
        }
        endGameStats = endGameStats.substring(0, endGameStats.length - 1)
        println(endGameStats)
        val selection = StringSelection(endGameStats)
        lastGameStats = selection
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        if (addToClipboard)
            clipboard.setContents(selection, selection)
    }

    private const val CENTER_PX = 154

    fun sendCenteredMessage(message: String?) {
        var messagePxSize = 0
        var previousCode = false
        var isBold = false
        if (message != null) {
            for (c in message.toCharArray()) {
                if (c == '§') {
                    previousCode = true
                } else if (previousCode) {
                    previousCode = false
                    isBold = if (c == 'l' || c == 'L') {
                        true
                    } else false
                } else {
                    val dFI: DefaultFontInfo = DefaultFontInfo.getDefaultFontInfo(c)
                    messagePxSize += if (isBold) dFI.boldLength else dFI.length
                    messagePxSize++
                }
            }
        }
        val halvedMessageSize = messagePxSize / 2
        val toCompensate = CENTER_PX - halvedMessageSize
        val spaceLength: Int = DefaultFontInfo.SPACE.length + 1
        var compensated = 0
        val sb = StringBuilder()
        while (compensated < toCompensate) {
            sb.append(" ")
            compensated += spaceLength
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(sb.toString() + message))
    }

    @ConfigPropertyBoolean(
        category = CCategory.CHAT,
        id = "| End Game Stats | Print General Stats",
        comment = "Enable or disable showing general stats like total kills,dmg/heal received,etc at the end of the game in chat",
        def = true
    )
    var printGeneralStats = false

    @ConfigPropertyBoolean(
        category = CCategory.CHAT,
        id = "| End Game Stats | Print Scoreboard",
        comment = "Enable or disable showing the scoreboard at the end of the game in chat",
        def = true
    )
    var printScoreboard = false

    @ConfigPropertyBoolean(
        category = CCategory.CHAT,
        id = "| End Game Stats | Add Scoreboard To Clipboard",
        comment = "Enable or disable add the scoreboard to the clipboard - USED FOR COMPS",
        def = false
    )
    var addToClipboard = false

}
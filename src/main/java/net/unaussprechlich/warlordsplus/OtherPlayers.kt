package net.unaussprechlich.warlordsplus

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.*
import net.unaussprechlich.warlordsplus.util.*
import java.util.*
import java.util.regex.Pattern


open class Player(val name: String, val uuid: UUID) {

    var kills: Int = 0
    var deaths: Int = 0
    var damageDone: Int = 0
    var damageReceived: Int = 0
    var healingDone: Int = 0
    var healingReceived: Int = 0
    var warlord = WarlordsEnum.NONE
    var spec = SpecsEnum.NONE
    var team = TeamEnum.NONE
    var level = 0
    var prestiged: Boolean = false
    var hasFlag: Boolean = false
    var died: Int = 0
    var stoleKill: Int = 0
    var picks: Int = 0
    var returns: Int = 0
    var caps: Int = 0
    var left: Boolean = false
    var isDead: Boolean = false
    var respawn: Int = -1
}

private val numberPattern = Pattern.compile("[0-9]{2}")
private var brokenTab = -1L

object OtherPlayers : IModule {

    val playersMap: MutableMap<String, Player> = mutableMapOf()
    val tempPlayersMap: MutableMap<String, Player> = mutableMapOf()

    init {
        EventBus.register<ResetEvent> {
            playersMap.clear()
            tempPlayersMap.clear()

            val players = getPlayersForNetworkPlayers(Minecraft.getMinecraft().thePlayer!!.sendQueue.playerInfoMap)
            println("[WarlordsPlus|OtherPlayers] found ${players.size} players!")
        }

        EventBus.register<TenSecondEvent> {
            tempPlayersMap.clear()
            val players = getPlayersForNetworkPlayers2(Minecraft.getMinecraft().thePlayer!!.sendQueue.playerInfoMap)
            //compare playermap with temp - whoever is missing left
            playersMap.forEach {
                it.value.left = !it.value.isDead && !tempPlayersMap.containsKey(it.key)
            }
        }

        EventBus.register<SecondEvent> {
            Minecraft.getMinecraft().theWorld.playerEntities.filterIsInstance<EntityOtherPlayerMP>().forEach {
                it.refreshDisplayName()
            }
        }

        EventBus.register<KillEvent> {
            if (it.deathPlayer in playersMap) {
                playersMap[it.deathPlayer]!!.deaths++
                playersMap[it.deathPlayer]!!.isDead = true
                playersMap[it.deathPlayer]!!.respawn = it.respawn
            }
            if (it.player in playersMap)
                playersMap[it.player]!!.kills++
        }

        EventBus.register<HealingReceivedEvent> {
            if (it.player in playersMap)
                playersMap[it.player]!!.healingReceived += it.amount
        }

        EventBus.register<HealingGivenEvent> {
            if (it.player in playersMap)
                playersMap[it.player]!!.healingDone += it.amount
        }

        EventBus.register<DamageTakenEvent> {
            if (it.player in playersMap)
                playersMap[it.player]!!.damageReceived += it.amount
        }

        EventBus.register<DamageDoneEvent> {
            if (it.player in playersMap)
                playersMap[it.player]!!.damageDone += it.amount
        }

        EventBus.register<FlagTakenEvent> {
            playersMap[it.playerWithFlag]!!.hasFlag = it.playerWithFlag in playersMap && it.hasFlag
        }

        EventBus.register<KillRatioEvent> {
            playersMap[it.otherPlayer]!!.died += 1
        }

        EventBus.register<KillStealEvent> {
            playersMap[it.otherPlayer]!!.stoleKill += 1
        }

        EventBus.register<FlagPickedEvent> {
            playersMap[it.playerThatPicked]!!.picks += 1
        }

        EventBus.register<FlagCapturedEvent> {
            playersMap[it.playerThatCaptured]!!.caps += 1
        }

        EventBus.register<FlagReturnedEvent> {
            playersMap[it.playerThatReturned]!!.returns += 1
        }

        /* TODO
        EventBus.register<PlayerLeaveEvent> {
            playersMap[it.player]!!.left = it.left
        }*/


        EventBus.register<TickEvent.ClientTickEvent> {
            if (GameStateManager.inLobby) {
                try {
                    Minecraft.getMinecraft().theWorld.playerEntities.filterIsInstance<EntityOtherPlayerMP>().forEach {
                        val inventory = it.inventory.mainInventory[0]
                        //todo fix not refreshing if u cant get the spec from hand

                        if (inventory != null && (!inventory.tagCompound.toString().removeFormatting()
                                .contains("Menu") || !inventory.tagCompound.toString().removeFormatting()
                                .contains("Mount") || !inventory.tagCompound.toString().removeFormatting()
                                .contains("Flag"))
                        ) {
                            it.refreshDisplayName()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getPlayersForNetworkPlayers(networkPlayers: Collection<NetworkPlayerInfo>): Collection<Player> {

        //Filter out if we already have the Player stored
        networkPlayers.filter {

            !playersMap.containsKey(it.gameProfile.name)

            //Filter out any strange "Players" appearing in the Scoreboard, by assuming they must have a class
        }.filter { player ->

            try {
                WarlordsEnum.values().any {
                    player.playerTeam.colorPrefix contain it.shortName
                }
            } catch (e: Exception) {
                if (brokenTab < System.currentTimeMillis()) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}LOOKS LIKE YOUR TAB IS BUGGED PLEASE REJOIN THE LOBBY - /hub - /rejoin"))
                    brokenTab = System.currentTimeMillis() + 2000
                }

                false
            }

            //Create the Player
        }.map {

            val player = Player(it.gameProfile.name, it.gameProfile.id)

            //getting player class
            player.warlord = WarlordsEnum.values().first { w -> it.playerTeam.colorPrefix contain w.shortName }

            //getting player level
            val m = numberPattern.matcher(it.playerTeam.colorSuffix.removeFormatting())
            player.level = if (!m.find()) 0 else {
                m.group().toInt()
            }

            //getting player team
            player.team = TeamEnum.values().first { t -> it.playerTeam.colorPrefix.contains(t.color.toString()) }

            //checking if player is prestige
            player.prestiged = it.playerTeam.colorSuffix.contains("${EnumChatFormatting.GOLD}")
            return@map player

        }.forEach { playersMap[it.name] = it }

        playersMap.filter {
            it.value.spec == SpecsEnum.NONE
        }.forEach { player ->
            val players = Minecraft.getMinecraft().theWorld.playerEntities
            players.filter {
                it is EntityOtherPlayerMP
                it.name == player.value.name
            }.filter {
                it.inventory.firstEmptyStack == 1
            }.map {
                if (it.inventory.mainInventory[0].tagCompound.toString().contains("LEFT-CLICK")) {
                    if (player.value.warlord == WarlordsEnum.WARRIOR) {
                        player.value.spec = SpecsEnum.values()
                            .firstOrNull { w ->
                                it.inventory.mainInventory[0].tagCompound.toString().removeFormatting() contain w.weapon
                            }
                            ?: SpecsEnum.NONE
                    } else {
                        player.value.spec = SpecsEnum.values()
                            .firstOrNull { w -> it.inventory.mainInventory[0].chatComponent.formattedText contain w.weapon }
                            ?: SpecsEnum.NONE
                    }
                } else if (it.inventory.mainInventory[0].tagCompound.toString().contains("RIGHT-CLICK")) {
                    player.value.spec = SpecsEnum.values()
                        .firstOrNull { w -> it.inventory.mainInventory[0].chatComponent.formattedText contain w.classname }
                        ?: SpecsEnum.NONE
                } else if (it.inventory.mainInventory[0].tagCompound.toString()
                        .contains("Cooldown") && !it.inventory.mainInventory[0].tagCompound.toString().contains("Mount")
                ) {
                    val warlord = player.value.warlord
                    when (it.inventory.mainInventory[0].metadata) {
                        1 -> if (warlord != WarlordsEnum.PALADIN && warlord != WarlordsEnum.WARRIOR)
                            player.value.spec = SpecsEnum.values()
                                .firstOrNull { w -> it.inventory.mainInventory[0].tagCompound.toString() contain w.red }
                                ?: SpecsEnum.NONE
                        0 -> if (warlord != WarlordsEnum.PALADIN && warlord != WarlordsEnum.WARRIOR && warlord != WarlordsEnum.MAGE)
                            player.value.spec = SpecsEnum.values()
                                .firstOrNull { w -> it.inventory.mainInventory[0].tagCompound.toString() contain w.purple }
                                ?: SpecsEnum.NONE
                        10 -> if (warlord != WarlordsEnum.PALADIN && warlord != WarlordsEnum.MAGE)
                            player.value.spec = SpecsEnum.values()
                                .firstOrNull { w -> it.inventory.mainInventory[0].tagCompound.toString() contain w.blue }
                                ?: SpecsEnum.NONE
                        14 -> player.value.spec = SpecsEnum.values()
                            .firstOrNull { w -> it.inventory.mainInventory[0].tagCompound.toString() contain w.orange }
                            ?: SpecsEnum.NONE
                    }
                }
            }
        }

        //Return the players :)
        return playersMap.values
    }

    fun getPlayersForNetworkPlayers2(networkPlayers: Collection<NetworkPlayerInfo>): Collection<Player> {

        //Filter out if we already have the Player stored
        networkPlayers.filter {

            !tempPlayersMap.containsKey(it.gameProfile.name)

            //Filter out any strange "Players" appearing in the Scoreboard, by assuming they must have a class
        }.filter { player ->

            try {
                WarlordsEnum.values().any {
                    player.playerTeam.colorPrefix contain it.shortName
                }
            } catch (e: Exception) {
                false
            }

            //Create the Player
        }.map {

            val player = Player(it.gameProfile.name, it.gameProfile.id)

            return@map player

        }.forEach { tempPlayersMap[it.name] = it }

        return tempPlayersMap.values
    }

    @SubscribeEvent
    fun onPlayerName(e: PlayerEvent.NameFormat) {
        when {
            GameStateManager.isIngame -> {
                playersMap.filter {
                    it.value.spec != SpecsEnum.NONE
                }.filter {
                    it.value.name == e.username
                }.forEach {
                    e.displayname =
                        "${EnumChatFormatting.DARK_GRAY}[${it.value.spec.type.coloredSymbol}${EnumChatFormatting.DARK_GRAY}] ${if (it.value.team == TeamEnum.BLUE) EnumChatFormatting.BLUE else if (it.value.team == TeamEnum.RED) EnumChatFormatting.RED else ""}${e.displayname}${EnumChatFormatting.RESET}"
                }
            }
            GameStateManager.inLobby -> {
                try {
                    val playerInv = e.entityPlayer.inventory
                    if (playerInv.mainInventory[0] != null) {
                        var spec = SpecsEnum.NONE
                        if (playerInv.mainInventory[0].tagCompound.toString().contains("LEFT-CLICK")) {
                            spec = SpecsEnum.values()
                                .firstOrNull { w -> playerInv.mainInventory[0].tagCompound.toString() contain w.weapon }
                                ?: SpecsEnum.NONE
                        } else if (playerInv.mainInventory[0].tagCompound.toString().contains("Cooldown")) {
                            when (playerInv.mainInventory[0].metadata) {
                                1 -> spec = SpecsEnum.values()
                                    .firstOrNull { w -> playerInv.mainInventory[0].tagCompound.toString() contain w.red }
                                    ?: SpecsEnum.NONE
                                0 -> spec = SpecsEnum.values()
                                    .firstOrNull { w -> playerInv.mainInventory[0].tagCompound.toString() contain w.purple }
                                    ?: SpecsEnum.NONE
                                10 -> spec = SpecsEnum.values()
                                    .firstOrNull { w -> playerInv.mainInventory[0].tagCompound.toString() contain w.blue }
                                    ?: SpecsEnum.NONE
                                14 -> spec = SpecsEnum.values()
                                    .firstOrNull { w -> playerInv.mainInventory[0].tagCompound.toString() contain w.orange }
                                    ?: SpecsEnum.NONE
                            }
                        }
                        e.displayname =
                            "${EnumChatFormatting.DARK_GRAY}[${spec.type.coloredSymbol}${EnumChatFormatting.DARK_GRAY}]${EnumChatFormatting.RESET}${e.displayname}"
                    }
                } catch (err: Exception) {
                    println(e)
                    println(e.entityPlayer)
                    println(err.printStackTrace())
                }
            }
            else -> {
                e.displayname = e.username
            }
        }
    }

    fun getPlayerForName(name: String): Player? {
        return playersMap[name]
    }

    fun colorForPlayer(name: String): Colors {
        return when (playersMap[name]!!.team) {
            TeamEnum.BLUE -> {
                Colors.DARK_BLUE
            }
            TeamEnum.RED -> {
                Colors.DARK_RED
            }
            else -> {
                Colors.BLACK
            }
        }
    }
}
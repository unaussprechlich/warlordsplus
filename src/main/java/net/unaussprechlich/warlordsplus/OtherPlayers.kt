package net.unaussprechlich.warlordsplus

import net.minecraft.client.Minecraft
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.EnumChatFormatting
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.hud.elements.KPEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.*
import net.unaussprechlich.warlordsplus.util.*
import java.util.*
import java.util.regex.Pattern


//TODO [ ] find out/think about what spec they are playing :) Just write a comment with some ideas
open class Player(val name: String, val uuid : UUID) {

    var kills: Int = 0
    var deaths: Int = 0
    var killParticipation: Int = 0
    var damageDone : Int = 0
    var damageReceived : Int = 0
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
    var left: Boolean = false
    var isDead: Boolean = false
    var respawn: Int = -1
}

private val numberPattern = Pattern.compile("[0-9]{2}")

object OtherPlayers : IModule {

    val playersMap: MutableMap<String, Player> = mutableMapOf()

    init {
        EventBus.register<ResetEvent> {
            playersMap.clear()

            val players = getPlayersForNetworkPlayers(Minecraft.getMinecraft().thePlayer!!.sendQueue.playerInfoMap)
            println("[WarlordsPlus|OtherPlayers] found ${players.size} players!")
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

        EventBus.register<KPEvent> {
            if (it.player in playersMap)
                playersMap[it.player]!!.killParticipation = it.amount
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

        /* TODO
        EventBus.register<PlayerLeaveEvent> {
            playersMap[it.player]!!.left = it.left
        }*/

    }

    fun getPlayersForNetworkPlayers(networkPlayers : Collection<NetworkPlayerInfo>): Collection<Player> {

        //Filter out if we already have the Player stored
        networkPlayers.filter {

            !playersMap.containsKey(it.gameProfile.name)

            //Filter out any strange "Players" appearing in the Scoreboard, by assuming they must have a class
        }.filter { player ->

            WarlordsEnum.values().any {
                player.playerTeam.colorPrefix contain it.shortName
            }

        //Create the Player
        }.map{

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

            val newMap: MutableMap<String, Player> = mutableMapOf()
            newMap.putAll(playersMap)
            for (networkPlayer in networkPlayers) {
                if (newMap.containsKey(networkPlayer.gameProfile.name)) {
                    newMap.remove(networkPlayer.gameProfile.name)
                }
            }
            for (mutableEntry in newMap) {
                if (mutableEntry.value.name == player.name)
                    player.left = true
            }

            return@map player

        }.forEach { playersMap[it.name] = it }

        //Return the players :)
        return playersMap.values
    }

    fun getPlayerForName(name : String) : Player?{
        return playersMap[name]
    }
}
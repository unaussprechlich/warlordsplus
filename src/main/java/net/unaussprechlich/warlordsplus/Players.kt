package net.unaussprechlich.warlordsplus

import net.minecraft.client.network.NetworkPlayerInfo
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.ResetEvent
import net.unaussprechlich.warlordsplus.module.modules.*
import net.unaussprechlich.warlordsplus.util.*
import java.util.*
import java.util.regex.Pattern

//TODO [ ] find out/think about what spec they are playing :) Just write a comment with some ideas
open class Player(val name: String, val uuid : UUID) {

    var kills: Int = 0
    var deaths : Int = 0

    var damageDone : Int = 0
    var damageReceived : Int = 0
    var healingDone : Int = 0
    var healingReceived : Int = 0

    var warlord = WarlordsEnum.NONE

    var spec = SpecsEnum.NONE

    var team = TeamEnum.NONE

    var level = 0

}

private val numberPattern = Pattern.compile("[0-9]{2}")


object Players : IModule{

    private val playersMap : MutableMap<String, Player> = mutableMapOf()

    init{
        EventBus.register<ResetEvent> {
            playersMap.clear()
        }

        EventBus.register<KillEvent> {
            if(playersMap.contains(it.deathPlayer))
                playersMap[it.deathPlayer]!!.deaths++
            if(playersMap.contains(it.player))
                playersMap[it.player]!!.kills++
        }

        EventBus.register<HealingReceivedEvent> {
            if(playersMap.contains(it.player))
                playersMap[it.player]!!.healingDone += it.amount
        }

        EventBus.register<HealingGivenEvent> {
            if(playersMap.contains(it.player))
                playersMap[it.player]!!.healingReceived += it.amount
        }

        EventBus.register<DamageTakenEvent> {
            if(playersMap.contains(it.player))
                playersMap[it.player]!!.damageDone += it.amount
        }

        EventBus.register<DamageDoneEvent> {
            if(playersMap.contains(it.player))
                playersMap[it.player]!!.damageReceived += it.amount
        }
    }

    fun getPlayersForNetworkPlayers(networkPlayers : Collection<NetworkPlayerInfo>): Collection<Player> {

        val names = networkPlayers.map { it.gameProfile.name }

        //Filter out if we already have the Player stored
        networkPlayers.filter {

            !playersMap.containsKey(it.gameProfile.name)

        //Filter out any strange "Players" appearing in the Scoreboard, by assuming they must have a Spec
        }.filter { player ->

            WarlordsEnum.values().any {
                    player.playerTeam.colorPrefix has it.shortName
            }

        //Create the Player
        }.map{

            val player = Player(it.gameProfile.name, it.gameProfile.id)

            player.warlord = WarlordsEnum.values().first { w -> it.playerTeam.colorPrefix has w.shortName }

            val m = numberPattern.matcher(it.playerTeam.colorSuffix.removeFormatting())
            player.level = if (!m.find()) 0 else m.group().toInt()

            player.team = TeamEnum.values().first{ t -> it.playerTeam.colorPrefix.contains(t.color.toString())}

            return@map player

        }.forEach { playersMap[it.name] = it }

        //Return the players :)
        return playersMap.values
    }

    fun getPlayerForName(name : String) : Player?{
        return playersMap[name]
    }
}
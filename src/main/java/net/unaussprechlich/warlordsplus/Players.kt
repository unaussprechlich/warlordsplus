package net.unaussprechlich.warlordsplus

import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.ResetEvent


class OtherPlayer(val name: String) {

    /*
    TODO
     [ ] add a bunch of fields like
        - kills
        - deaths
        - healingProvided
        - damageDoneToYou
        - energyGiven
        - killedYouXTimes
        - ...
     [ ] make a enum to determine their Team and store it in a field
     */

    //TODO(BONUS) [ ] find out/think about what spec they are playing :)

    init{
        /*
    TODO
     [ ] register the KillEvent and check it against this Player
     [ ] register the Damage/Healing/... Event and check it against this Player
     */

    }


}

object Players : IModule{

    private val playersMap : MutableMap<String, OtherPlayer> = mutableMapOf()

    init{
        //TODO think about a way to reset the Map once a game starts
    }

    fun getPlayerForName(name : String) : OtherPlayer?{
        return playersMap[name]
    }
}
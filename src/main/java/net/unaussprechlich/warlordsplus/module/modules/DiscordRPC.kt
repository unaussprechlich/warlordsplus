package net.unaussprechlich.warlordsplus.module.modules


import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.entities.pipe.PipeStatus
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.ForgeEventProcessor
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.util.TeamEnum
import java.time.OffsetDateTime


object DiscordRPC : IPCListener {
    private val builder: RichPresence.Builder = RichPresence.Builder()
    val client: IPCClient = IPCClient(811200780673220649)

    init {
        EventBus.register<WarlordsLeaveAndJoinEvent> {
            if(it.isWarlords){
                if(client.status != PipeStatus.CONNECTED && enabled){
                    try {

                        client.setListener(object : IPCListener {
                            override fun onReady(client: IPCClient) {
                                client.sendRichPresence(builder.build())
                            }
                        })
                        client.connect()
                        client.sendRichPresence(builder.build())

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                if (client.status == PipeStatus.CONNECTED && !enabled) {
                    try {
                        client.close()
                    } catch (e : Exception){
                        e.printStackTrace()
                    }

                }
            }
        }

        EventBus.register<ForgeEventProcessor.EverySecond> {
            try {
                val builder = RichPresence.Builder()
                builder.setDetails(lobbyFormatted())
                if (GameStateManager.inLobby) {
                    builder.setLargeImage("warlordsicon")
                    builder.setState(GameStateManager.getPlayersInLobby())
                    builder.setStartTimestamp(OffsetDateTime.now())
                    builder.setEndTimestamp(OffsetDateTime.now().plusSeconds(GameStateManager.getTimeLeftLobby()))
                } else if (GameStateManager.isIngame) {
                    builder.setSmallImage(ThePlayer.superSpec.specName)
                    builder.setLargeImage(ThePlayer.warlord.classname.toLowerCase())
                    builder.setState(pointsFormatted())
                    builder.setStartTimestamp(OffsetDateTime.now())
                    builder.setEndTimestamp(OffsetDateTime.now().plusSeconds(GameStateManager.getTimeLeftGame()))
                }
                client.sendRichPresence(builder.build())
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    private fun lobbyFormatted(): String {
        when {
            GameStateManager.inLobby -> {
                return if (ScoreboardManager.scoreboard[10].contains("Map:"))
                    "In ${ScoreboardManager.scoreboard[10].substring(5)} Lobby"
                else
                    "In ${ScoreboardManager.scoreboard[9].substring(5)} Lobby"
            }
            GameStateManager.isCTF -> {
                return "Playing Capture the Flag"
            }
            GameStateManager.isTDM -> {
                return "Playing Team Deathmatch"
            }
            GameStateManager.isDOM -> {
                return "Playing Domination"
            }
        }
        return "In Lobby"
    }

    private fun pointsFormatted(): String {
        when {
            GameStateManager.bluePoints == GameStateManager.redPoints -> return "Tied"
            ThePlayer.team == TeamEnum.BLUE -> {
                return if (GameStateManager.bluePoints > GameStateManager.redPoints) {
                    "Winning by ${GameStateManager.bluePoints - GameStateManager.redPoints} points"
                } else {
                    "Losing by ${GameStateManager.redPoints - GameStateManager.bluePoints} points"
                }
            }
            ThePlayer.team == TeamEnum.RED -> {
                return if (GameStateManager.redPoints > GameStateManager.bluePoints) {
                    "Winning by ${GameStateManager.redPoints - GameStateManager.bluePoints} points"
                } else {
                    "Losing by ${GameStateManager.bluePoints - GameStateManager.redPoints} points"
                }
            }
            else -> return ""
        }
    }

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "Discord RPC | Enable",
        comment = "Enable or disable rich presence on discord",
        def = true
    )
    var enabled = true
}
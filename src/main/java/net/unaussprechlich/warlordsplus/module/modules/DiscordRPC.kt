package net.unaussprechlich.warlordsplus.module.modules

import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.exceptions.NoDiscordClientException
import java.io.FileNotFoundException


class DiscordRPC : IPCListener {
    private val builder: RichPresence.Builder = RichPresence.Builder()
    val client: IPCClient = IPCClient(811200780673220649)

    fun start() {
        try {
            //if (enabled) {
                client.setListener(object : IPCListener {
                    override fun onReady(client: IPCClient) {
                        client.sendRichPresence(builder.build())
                    }
                })
                client.connect()
                client.sendRichPresence(builder.build())
            //}
        } catch (e: Exception) {
            if (e is RuntimeException || e is FileNotFoundException) return
            e.printStackTrace()
        }
    }

    fun stop() {
        try {
            client.close()
        } catch (e: Exception) {
            if (e is NoDiscordClientException || e is RuntimeException) return
            e.printStackTrace()
        }
    }

    fun setPresence(firstLine: String?) {
        builder.setDetails(firstLine)
        client.sendRichPresence(builder.build())
    }

    fun setPresence(firstLine: String?, secondLine: String?) {
        builder.setDetails(firstLine)
            .setState(secondLine)
        client.sendRichPresence(builder.build())
    }

    fun setPresence(firstLine: String?, secondLine: String?, largeImage: String?) {
        builder.setDetails(firstLine)
            .setState(secondLine)
            .setLargeImage(largeImage)
        client.sendRichPresence(builder.build())
    }

    fun setPresence(firstLine: String?, secondLine: String?, largeImage: String?, smallImage: String?) {
        builder.setDetails(firstLine)
            .setState(secondLine)
            .setLargeImage(largeImage)
            .setSmallImage(smallImage)
        client.sendRichPresence(builder.build())
    }

    companion object {
        var INSTANCE = DiscordRPC()
    }

//    @ConfigPropertyBoolean(
//        category = CCategory.MODULES,
//        id = "enableDiscordRPC",
//        comment = "Enable or disable rich presence on discord",
//        def = true
//    )
//    var enabled = true
}
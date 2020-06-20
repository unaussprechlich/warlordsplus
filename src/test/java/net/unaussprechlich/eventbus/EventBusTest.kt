package net.unaussprechlich.eventbus

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.unaussprechlich.warlordsplus.PlayerStats
import org.junit.Test
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import kotlin.test.assertEquals


class EventBusTest {

    data class TestEvent(val bool : Boolean) : IEvent
    data class MyThing(val myString: String) : IEvent

    @Test
    fun test(){
        var test = false

        EventBus.register<TestEvent> {
            test = it.bool
        }

        EventBus.register<MyThing> {
            it
        }

        EventBus.post(TestEvent(true))
        EventBus.post(MyThing("Hello"))

        assertEquals(true, test)
    }

    @Test
    fun testest() {

        //call name ASYNC + parse json
        //loop through players
        //response
        //draw shit
        //ASYNC

        var response: Json

        var playerStats: PlayerStats
        suspend fun getStatsForName(name: String): PlayerStats {
            var url = URL("https://warlordssr.unaussprechlich.net/api/" + name)
            val request: URLConnection = url.openConnection()
            request.connect()
            val jp = JsonParser() //from gson
            val root: JsonElement =
                jp.parse(InputStreamReader(request.content as InputStream)) //Convert the input stream to a json element
            return PlayerStats(root.asJsonObject)
        }

        runBlocking {
            playerStats = getStatsForName("sumSmash")
        }

        GlobalScope.async {

        }

        GlobalScope.launch {
        }

        //assertEquals(true,true)
        //assertEquals(0, playerStats.sr)
    }

}
package net.unaussprechlich.eventbus

import org.junit.Test
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

    /*
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
    }*/

}
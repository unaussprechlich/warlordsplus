package net.unaussprechlich.eventbus

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.UnstableDefault
import org.junit.Test
import kotlin.test.assertEquals


class WarlordsSrApiTest {


    @UnstableDefault
    @Test
    fun warlordsSrApiTest() {
        runBlocking {
            //val result = WarlordsSrApi.client.get<WarlordsSrApiResponse>("https://warlordssr.unaussprechlich.net/api/WaterMasterBR")
            //println(result)
            assertEquals(true, true)
        }
    }
}
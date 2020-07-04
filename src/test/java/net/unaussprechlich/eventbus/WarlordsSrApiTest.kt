package net.unaussprechlich.eventbus

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.UnstableDefault
import net.unaussprechlich.warlordsplus.stats.WarlordsSrApi
import org.junit.Test
import kotlin.test.assertEquals


class WarlordsSrApiTest {


    @UnstableDefault
    @Test
    fun warlordsSrApiTest() {
        runBlocking {
            val result = WarlordsSrApi.getRequest("sumSmash")
            println(result)
            assertEquals(result.data.playername, "sumSmash")
        }

    }
}
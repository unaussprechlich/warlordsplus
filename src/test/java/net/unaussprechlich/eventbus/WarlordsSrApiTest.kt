//package net.unaussprechlich.eventbus
//
//import kotlinx.coroutines.runBlocking
//import net.unaussprechlich.http.HttpModule
//import org.junit.Test
//import kotlin.test.assertEquals
//
//
//class WarlordsSrApiTest {
//
//    @Test
//    fun warlordsSrApiTest() {
//        runBlocking {
//            val result = HttpModule.WarlordsSrApi.getWarlordsSrResponse("unaussprechlich")
//            println(result)
//            assertEquals(true, true)
//        }
//    }
//}
package net.unaussprechlich.eventbus


import org.junit.Test
import kotlin.test.assertEquals


class EventBusTest {

    data class TestEvent(val bool: Boolean) : IEvent
    data class MyThing(val myString: String) : IEvent

    @Test
    fun testEventBus() {
        var test = false

        EventBus.register<TestEvent> {
            test = it.bool
        }

        EventBus.register<MyThing> {
            it
        }

        EventBus.post(TestEvent(true))
        EventBus.post(MyThing("Hello"))

        val textMessage = "You assisted unaussprechlich in killing sumTrash"
        println(textMessage.substring(textMessage.indexOf("You assisted ") + 13, textMessage.indexOf("in ") - 1))

        assertEquals(true, test)
    }
}
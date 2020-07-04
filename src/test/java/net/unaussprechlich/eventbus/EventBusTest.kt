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

        assertEquals(true, test)
    }
}
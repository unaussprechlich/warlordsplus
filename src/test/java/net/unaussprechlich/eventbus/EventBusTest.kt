package net.unaussprechlich.eventbus

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


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
            println(it)
        }

        //EventBus.post(TestEvent(true))
        //EventBus.post(MyThing("Hello"))

        assertEquals(true, test)
    }

}
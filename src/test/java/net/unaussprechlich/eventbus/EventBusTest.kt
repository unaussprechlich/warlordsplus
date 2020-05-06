package net.unaussprechlich.eventbus

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class EventBusTest {

    data class TestEvent(val bool : Boolean) : IEvent

    @Test
    fun test(){
        var test = false

        EventBus.register<TestEvent> {
            test = it.bool
        }

        EventBus.post(TestEvent(true))

        assertEquals(true  , test)
    }

}
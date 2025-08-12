package io.github.pandier.snowball.impl.event

import io.github.pandier.snowball.event.Event
import io.github.pandier.snowball.event.Listener
import kotlin.test.*

class EventManagerImplTest {

    interface UnrelatedInterface
    interface ParentTestEventInterface : Event
    open class ParentTestEvent : Event, ParentTestEventInterface
    interface TestEventInterface : Event
    class TestEvent : ParentTestEvent(), TestEventInterface

    @Suppress("unused")
    class TestListeners {
        var parentTestEventInterfaceReceived = false
        var parentTestEventReceived = false
        var testEventInterfaceReceived = false
        var testEventReceiveCount = 0
        val testEventReceived get() = testEventReceiveCount > 0

        @Listener
        fun onParentTestEventInterface(event: ParentTestEventInterface) {
            parentTestEventInterfaceReceived = true
        }

        @Listener
        fun onParentTestEvent(event: ParentTestEvent) {
            parentTestEventReceived = true
        }

        @Listener
        fun onTestEventInterface(event: TestEventInterface) {
            testEventInterfaceReceived = true
        }

        @Listener
        fun onTestEvent(event: TestEvent) {
            testEventReceiveCount++
        }
    }

    @Suppress("unused")
    class IllegalTestListeners {

        @Listener
        fun onUnrelatedInterface(event: UnrelatedInterface) {
        }
    }


    @Test
    fun listenerReceivesDispatchedEvent() {
        val eventManager = EventManagerImpl()
        val testListeners = TestListeners()
        eventManager.register(testListeners)
        eventManager.dispatch(ParentTestEvent())
        assertTrue(testListeners.parentTestEventReceived)
        eventManager.dispatch(TestEvent())
        assertTrue(testListeners.testEventReceived)
    }

    @Test
    fun listenerReceivesAllDispatchedEventTypes() {
        val eventManager = EventManagerImpl()
        val testListeners = TestListeners()
        eventManager.register(testListeners)
        eventManager.dispatch(TestEvent())
        assertTrue(testListeners.testEventReceived, "Test event should be received")
        assertTrue(testListeners.testEventInterfaceReceived, "Interface that the test event implements should be received")
        assertTrue(testListeners.parentTestEventReceived, "Parent event should be received")
        assertTrue(testListeners.parentTestEventInterfaceReceived, "Interface that the parent event implements should be received")
    }

    @Test
    fun listenerDoesNotReceiveChildEvent() {
        val eventManager = EventManagerImpl()
        val testListeners = TestListeners()
        eventManager.register(testListeners)
        eventManager.dispatch(ParentTestEvent())
        assertTrue(testListeners.parentTestEventReceived, "Test event should be received")
        assertFalse(testListeners.testEventReceived, "Child event shouldn't be received")
        assertFalse(testListeners.testEventInterfaceReceived, "Interface that the child event implements shouldn't be received")
    }

    @Test
    fun listenerDoesNotReceiveEventAfterUnregistering() {
        val eventManager = EventManagerImpl()
        val testListeners = TestListeners()
        eventManager.register(testListeners)
        eventManager.unregister(testListeners)
        eventManager.dispatch(TestEvent())
        assertFalse(testListeners.testEventReceived)
        assertFalse(testListeners.testEventInterfaceReceived)
        assertFalse(testListeners.parentTestEventReceived)
        assertFalse(testListeners.parentTestEventInterfaceReceived)
    }

    @Test
    fun registeringContainerWithIllegalListenerThrowsException() {
        val eventManager = EventManagerImpl()
        assertFailsWith<IllegalStateException> {
            eventManager.register(IllegalTestListeners())
        }
    }

    @Test
    fun registeringAlreadyRegisteredContainerDoesNothing() {
        val eventManager = EventManagerImpl()
        val container = TestListeners()
        eventManager.register(container)
        eventManager.register(container)
        eventManager.dispatch(TestEvent())
        assertEquals(1, container.testEventReceiveCount, "Event shouldn't be received multiple times")
    }
}
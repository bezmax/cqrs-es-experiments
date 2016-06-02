package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.CanHandle
import com.bezmax.cqrscourse.cooking.CanStart
import com.bezmax.cqrscourse.cooking.HasMessageStats
import com.bezmax.cqrscourse.cooking.messages.MessageBase

class ThreadedDispatcher<M extends MessageBase, H extends CanHandle<M>> implements CanHandle<M>, CanStart, HasMessageStats {
    private CanHandle mainDispatcher
    private QueuedDispatcher beforeQueue
    private List<QueuedDispatcher> queuedDispatchers

    def name = "ThreadedDispatcher"

    public ThreadedDispatcher(List<H> handlers) {
        queuedDispatchers = handlers.collect {
            new QueuedDispatcher(name: "mainQueue", orderHandler: it)
        }

        mainDispatcher =
            new FairQueueDispatcher(
                orderHandlers: queuedDispatchers as Queue,
                name: "FairDispatcher"
            )

        beforeQueue = new QueuedDispatcher(name: "BeforeQueue")
        beforeQueue.forwardTo = mainDispatcher
    }

    void handle(M msg) {
        beforeQueue.handle(msg)
    }

    def start() {
        beforeQueue.start()
        queuedDispatchers.each {it.start()}
    }

    List<MessageStats> getMessageStats() {
        [] + beforeQueue.getMessageStats() + queuedDispatchers.sum {it.getMessageStats()}
    }

    String toString() {
        name
    }
}

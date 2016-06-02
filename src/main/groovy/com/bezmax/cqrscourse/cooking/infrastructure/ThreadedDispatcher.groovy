package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.CanStart
import com.bezmax.cqrscourse.cooking.HasQueueStats
import com.bezmax.cqrscourse.cooking.messages.MessageBase

class ThreadedDispatcher<M extends MessageBase, H extends Handles<M>> implements Handles<M>, CanStart, HasQueueStats {
    private Handles mainDispatcher
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

    List<MessageStats> getQueueStats() {
        [] + beforeQueue.queueStats + queuedDispatchers.sum {it.queueStats}
    }

    String toString() {
        def subNames = queuedDispatchers.collect {it.toString()}.join(", ")
        "$name -> $subNames"
    }
}

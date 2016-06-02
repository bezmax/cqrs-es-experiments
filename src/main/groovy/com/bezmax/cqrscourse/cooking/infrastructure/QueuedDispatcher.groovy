package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.CanStart

import com.bezmax.cqrscourse.cooking.HasQueueStats
import com.bezmax.cqrscourse.cooking.messages.MessageBase
import org.slf4j.LoggerFactory

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class QueuedDispatcher<M extends MessageBase> implements Handles<M>, CanStart, HasQueueStats {
    static LOGGER = LoggerFactory.getLogger(QueuedDispatcher)

    def name = "QueuedDispatcher"
    private BlockingQueue<M> messages = new LinkedBlockingQueue<>()

    private Handles<M> orderHandler

    void handle(M msg) {
        LOGGER.debug("Queued: $msg")
        messages << msg
    }

    def start() {
        Thread.start {
            def order
            while (order = messages.take()) {
                LOGGER.trace("Process: $order")
                orderHandler.handle(order)
            }
        }
    }

    void setForwardTo(Handles<M> handler) {
        orderHandler = handler
    }

    String toString() {
        "$name(${orderHandler.toString()})"
    }

    int getCount() {
        messages.size()
    }

    List<MessageStats> getQueueStats() {
        [] + new MessageStats(name: toString(), count: getCount())
    }
}

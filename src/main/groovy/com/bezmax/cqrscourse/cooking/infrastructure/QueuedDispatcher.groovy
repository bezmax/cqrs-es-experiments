package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.CanHandle
import com.bezmax.cqrscourse.cooking.CanStart
import com.bezmax.cqrscourse.cooking.CanForward
import com.bezmax.cqrscourse.cooking.HasMessageStats
import com.bezmax.cqrscourse.cooking.messages.MessageBase
import org.slf4j.LoggerFactory

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class QueuedDispatcher<M extends MessageBase> implements CanHandle<M>, CanStart, CanForward<M>, HasMessageStats {
    static LOGGER = LoggerFactory.getLogger(QueuedDispatcher)

    def name = "QueuedDispatcher"
    private BlockingQueue<M> messages = new LinkedBlockingQueue<>()

    private CanHandle<M> orderHandler

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

    void setForwardTo(CanHandle<M> handler) {
        orderHandler = handler
    }

    String toString() {
        "$name(${orderHandler.toString()})"
    }

    int getCount() {
        messages.size()
    }

    List<MessageStats> getMessageStats() {
        [] + new MessageStats(name: toString(), count: getCount())
    }
}

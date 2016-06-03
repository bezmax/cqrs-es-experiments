package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.CanStart

import com.bezmax.cqrscourse.cooking.infrastructure.stats.HasQueueStats
import com.bezmax.cqrscourse.cooking.infrastructure.stats.MessageStats
import org.slf4j.LoggerFactory

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class QueuedDispatcher<M> implements Handles<M>, CanStart, HasQueueStats {
    static LOGGER = LoggerFactory.getLogger(QueuedDispatcher)

    def name = "QueuedDispatcher"
    private BlockingQueue<Exchange<M>> exchanges = new LinkedBlockingQueue<>()

    private Handles<M> orderHandler

    void handle(Exchange<M> exchange, M msg) {
        LOGGER.debug("Queued: $exchange")
        exchanges << exchange
    }

    def start() {
        Thread.start {
            def exchange
            while (exchange = exchanges.take()) {
                LOGGER.trace("Process: $exchange")
                orderHandler.handle(exchange, exchange.message.body)
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
        exchanges.size()
    }

    List<MessageStats> getQueueStats() {
        [] + new MessageStats(name: toString(), count: getCount())
    }
}

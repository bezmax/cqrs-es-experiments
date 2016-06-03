package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.infrastructure.Exchange
import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.infrastructure.stats.HasQueueStats
import com.bezmax.cqrscourse.cooking.infrastructure.stats.MessageStats
import com.bezmax.cqrscourse.cooking.messages.events.OrderCompleted
import com.bezmax.cqrscourse.cooking.messages.events.OrderPaid
import org.slf4j.LoggerFactory

import java.util.concurrent.atomic.AtomicInteger


class Printer implements Handles<OrderCompleted>, HasQueueStats {
    static LOGGER = LoggerFactory.getLogger(Printer)

    AtomicInteger counter = new AtomicInteger(0)

    void handle(Exchange<OrderCompleted> exchange, OrderCompleted msg) {
        LOGGER.debug("Printer received $exchange")
        counter.incrementAndGet()
        LOGGER.info(msg.order.serialize())
    }

    List<MessageStats> getQueueStats() {
        [] + new MessageStats(name: "ResultPrinter", count: counter.get())
    }


    @Override
    public String toString() {
        "Printer"
    }
}

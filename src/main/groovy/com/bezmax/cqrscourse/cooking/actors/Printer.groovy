package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.HasQueueStats
import com.bezmax.cqrscourse.cooking.infrastructure.MessageStats
import com.bezmax.cqrscourse.cooking.messages.events.OrderPaid
import org.slf4j.LoggerFactory

import java.util.concurrent.atomic.AtomicInteger


class Printer implements Handles<OrderPaid>, HasQueueStats {
    static LOGGER = LoggerFactory.getLogger(Printer)

    AtomicInteger counter = new AtomicInteger(0)

    void handle(OrderPaid msg) {
        LOGGER.debug("Printer received $msg")
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

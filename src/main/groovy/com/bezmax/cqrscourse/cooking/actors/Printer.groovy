package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.CanHandle
import com.bezmax.cqrscourse.cooking.HasMessageStats
import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.infrastructure.MessageStats
import com.bezmax.cqrscourse.cooking.messages.OrderPaid
import org.slf4j.LoggerFactory

import java.util.concurrent.atomic.AtomicInteger


class Printer implements CanHandle<OrderPaid>, HasMessageStats {
    static LOGGER = LoggerFactory.getLogger(Printer)

    AtomicInteger counter = new AtomicInteger(0)

    void handle(OrderPaid msg) {
        LOGGER.debug("Printer received $msg")
        counter.incrementAndGet()
        LOGGER.info(msg.order.serialize())
    }

    List<MessageStats> getMessageStats() {
        [] + new MessageStats(name: "ResultPrinter", count: counter.get())
    }
}

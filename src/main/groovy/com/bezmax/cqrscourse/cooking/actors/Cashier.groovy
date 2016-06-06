package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.infrastructure.Exchange
import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.messages.commands.CollectPayment
import com.bezmax.cqrscourse.cooking.messages.events.OrderPaid
import org.slf4j.LoggerFactory


class Cashier implements Handles<CollectPayment> {
    static LOGGER = LoggerFactory.getLogger(Cashier)

    def name

    void handle(Exchange<CollectPayment> exchange, CollectPayment msg) {
        LOGGER.info("Cashier: {}", exchange)

        Order o = msg.order
        o.paid = true
        Thread.sleep(500)

        exchange.respond(new OrderPaid(order: o))
    }

    @Override
    public String toString() {
        "Cashier[$name]"
    }
}

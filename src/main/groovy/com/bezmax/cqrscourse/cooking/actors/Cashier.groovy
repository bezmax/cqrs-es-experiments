package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.CanHandle
import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.Publisher

import com.bezmax.cqrscourse.cooking.messages.OrderPaid
import com.bezmax.cqrscourse.cooking.messages.OrderPriced
import org.slf4j.LoggerFactory


class Cashier implements CanHandle<OrderPriced> {
    static LOGGER = LoggerFactory.getLogger(Cashier)

    def name
    Publisher pub

    void handle(OrderPriced msg) {
        LOGGER.debug("Cashier: {}", msg)

        Order o = msg.order
        o.paid = true
        Thread.sleep(500)
        pub.publish(OrderPaid.toString(), new OrderPaid(order: o))
    }

    @Override
    public String toString() {
        "Cashier[$name]"
    }
}

package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.infrastructure.Exchange
import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.messages.commands.PriceOrder
import com.bezmax.cqrscourse.cooking.messages.events.OrderPriced
import org.slf4j.LoggerFactory


class AssistantManager implements Handles<PriceOrder> {
    static LOGGER = LoggerFactory.getLogger(AssistantManager)

    def pricebook = [
            "razorblade icecream": 10.0,
            "baked mushroom": 15.0
    ]

    def name

    @Override
    public String toString() {
        "Assistant[$name]"
    }

    void handle(Exchange<PriceOrder> exchange, PriceOrder msg) {
        LOGGER.debug("Assistant Manager: {}", exchange)

        Order o = msg.order
        o.total = 0
        o.items.each {
            it.price = pricebook[it.item]
            o.total += (it.price * it.quantity)
        }
        o.tax = 0.21
        Thread.sleep(500)

        exchange.respond(new OrderPriced(order: o))
    }
}

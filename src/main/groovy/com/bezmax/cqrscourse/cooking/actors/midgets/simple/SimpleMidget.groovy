package com.bezmax.cqrscourse.cooking.actors.midgets.simple

import com.bezmax.cqrscourse.cooking.infrastructure.Exchange
import com.bezmax.cqrscourse.cooking.actors.midgets.Midget
import com.bezmax.cqrscourse.cooking.messages.commands.CollectPayment
import com.bezmax.cqrscourse.cooking.messages.commands.CookFood
import com.bezmax.cqrscourse.cooking.messages.commands.PriceOrder
import com.bezmax.cqrscourse.cooking.messages.events.FoodCooked
import com.bezmax.cqrscourse.cooking.messages.events.OrderCompleted
import com.bezmax.cqrscourse.cooking.messages.events.OrderPaid
import com.bezmax.cqrscourse.cooking.messages.events.OrderPlaced
import com.bezmax.cqrscourse.cooking.messages.events.OrderPriced
import org.slf4j.LoggerFactory

class SimpleMidget implements Midget {
    static LOGGER = LoggerFactory.getLogger(SimpleMidget)

    void handle(Exchange<OrderPlaced> exchange, OrderPlaced msg) {
        LOGGER.debug("Placed")
        exchange.respond(new CookFood(order: msg.order))
    }

    void handle(Exchange<FoodCooked> exchange, FoodCooked msg) {
        LOGGER.debug("Cooked")
        exchange.respond(new PriceOrder(order: msg.order))
    }

    void handle(Exchange<OrderPriced> exchange, OrderPriced msg) {
        LOGGER.debug("Priced")
        exchange.respond(new CollectPayment(order: msg.order))
    }

    void handle(Exchange<OrderPaid> exchange, OrderPaid msg) {
        LOGGER.debug("Paid and completed")
        exchange.respond(new OrderCompleted(order: msg.order))
    }

    void handle(Exchange<Object> exchange, Object msg) {
        LOGGER.debug("Other")
    }
}

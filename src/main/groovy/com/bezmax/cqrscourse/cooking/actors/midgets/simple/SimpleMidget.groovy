package com.bezmax.cqrscourse.cooking.actors.midgets.simple

import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.infrastructure.Exchange
import com.bezmax.cqrscourse.cooking.actors.midgets.Midget
import com.bezmax.cqrscourse.cooking.messages.commands.CollectPayment
import com.bezmax.cqrscourse.cooking.messages.commands.CookFood
import com.bezmax.cqrscourse.cooking.messages.commands.DelayedMessage
import com.bezmax.cqrscourse.cooking.messages.commands.PriceOrder
import com.bezmax.cqrscourse.cooking.messages.commands.RetryCooking
import com.bezmax.cqrscourse.cooking.messages.events.FoodCooked
import com.bezmax.cqrscourse.cooking.messages.events.OrderCompleted
import com.bezmax.cqrscourse.cooking.messages.events.OrderPaid
import com.bezmax.cqrscourse.cooking.messages.events.OrderPlaced
import com.bezmax.cqrscourse.cooking.messages.events.OrderPriced
import org.slf4j.LoggerFactory

import java.time.LocalDateTime

class SimpleMidget implements Midget {
    static LOGGER = LoggerFactory.getLogger(SimpleMidget)

    private boolean cookingComplete = false

    void handle(Exchange<OrderPlaced> exchange, OrderPlaced msg) {
        LOGGER.debug("Placed")
        requestFoodCooking(exchange, msg.order)
    }

    private void requestFoodCooking(Exchange<?> exchange, Order order) {
        exchange.respond(new CookFood(order: order))
        exchange.respond(new DelayedMessage(
                when: LocalDateTime.now().plusSeconds(5),
                target: exchange.corrId,
                body: new RetryCooking(order: order)
        ))
    }

    void handle(Exchange<RetryCooking> exchange, RetryCooking msg) {
        LOGGER.debug("Retry cooking")
        if (!cookingComplete) {
            LOGGER.info("Retrying cooking!")
            requestFoodCooking(exchange, msg.order)
        }
    }

    void handle(Exchange<FoodCooked> exchange, FoodCooked msg) {
        LOGGER.debug("Cooked")
        cookingComplete = true
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

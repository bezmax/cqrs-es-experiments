package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.CanHandle
import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.Publisher

import com.bezmax.cqrscourse.cooking.messages.FoodCooked
import com.bezmax.cqrscourse.cooking.messages.OrderPriced
import org.slf4j.LoggerFactory


class AssistantManager implements CanHandle<FoodCooked> {
    static LOGGER = LoggerFactory.getLogger(AssistantManager)

    def pricebook = [
            "razorblade icecream": 10.0,
            "baked mushroom": 15.0
    ]

    def name
    Publisher pub

    @Override
    public String toString() {
        "Assistant[$name]"
    }

    void handle(FoodCooked msg) {
        LOGGER.debug("Assistant Manager: {}", msg)

        Order o = msg.order
        o.total = 0
        o.items.each {
            it.price = pricebook[it.item]
            o.total += (it.price * it.quantity)
        }
        o.tax = 0.21
        Thread.sleep(500)
        pub.publish(OrderPriced.toString(), new OrderPriced(order: o))
    }
}

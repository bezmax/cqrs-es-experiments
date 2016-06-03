package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.infrastructure.Publisher
import com.bezmax.cqrscourse.cooking.messages.events.OrderPlaced
import org.slf4j.LoggerFactory


class Waiter {
    static LOGGER = LoggerFactory.getLogger(Waiter)

    def name

    Publisher pub

    UUID createOrder() {
        LOGGER.debug("Waiter creates new order")
        def order = new Order()
        order.tableNumber = 23
        order.items = [
            new Order.Item(
                    item: "razorblade icecream",
                    quantity: 2
            ),
            new Order.Item(
                    item: "baked mushroom",
                    quantity: 5
            )
        ]
        LOGGER.debug("Waiter: {}", order)

        if (Math.random() < 0.2) {
            order.shady = true
        }
        pub.publish(new OrderPlaced(order: order), order.id.toString())
        return order.id
    }

    @Override
    public String toString() {
        "Waiter[$name]"
    }
}

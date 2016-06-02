package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.Publisher

import com.bezmax.cqrscourse.cooking.messages.OrderPlaced
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

        pub.publish(new OrderPlaced(order: order))
        return order.id
    }

    @Override
    public String toString() {
        "Waiter[$name]"
    }
}

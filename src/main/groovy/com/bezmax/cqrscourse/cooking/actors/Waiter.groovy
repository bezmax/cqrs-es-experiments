package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.CanHandleOrder
import com.bezmax.cqrscourse.cooking.Order


class Waiter {
    CanHandleOrder orderHandler

    Waiter(CanHandleOrder orderHandler) {
        this.orderHandler = orderHandler
    }

    UUID createOrder() {
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
        orderHandler.handle(order)
        return order.id
    }
}

package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.CanHandleOrder
import com.bezmax.cqrscourse.cooking.Order


class AssistentManager implements CanHandleOrder {
    def pricebook = [
            "razorblade icecream": 10.0,
            "baked mushroom": 15.0
    ]

    CanHandleOrder orderHandler

    AssistentManager(CanHandleOrder orderHandler) {
        this.orderHandler = orderHandler
    }

    void handle(Order o) {
        o.total = 0
        o.items.each {
            it.price = pricebook[it.item]
            o.total += (it.price * it.quantity)
        }
        o.tax = 0.21
        orderHandler.handle(o)
    }
}

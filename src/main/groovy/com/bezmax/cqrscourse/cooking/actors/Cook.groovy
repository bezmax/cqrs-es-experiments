package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.CanHandleOrder
import com.bezmax.cqrscourse.cooking.Order


class Cook implements CanHandleOrder {
    def cookbook = [
        "razorblade icecream": "razor blades, ice cream",
        "baked mushroom": "mushroom"
    ]

    CanHandleOrder orderHandler

    Cook(CanHandleOrder orderHandler) {
        this.orderHandler = orderHandler
    }

    void handle(Order o) {
        o.ingredients = o.items.collect {cookbook[it.item]}.join(", ")
        Thread.sleep(3000)
        orderHandler.handle(o)
    }
}

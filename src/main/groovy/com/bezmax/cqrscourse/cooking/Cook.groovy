package com.bezmax.cqrscourse.cooking


class Cook implements CanHandleOrder {
    CanHandleOrder orderHandler

    Cook(CanHandleOrder orderHandler) {
        this.orderHandler = orderHandler
    }

    void handle(Order o) {
        o.ingredients = "razor blades, ice cream"
        Thread.sleep(3000)
        orderHandler.handle(o)
    }
}

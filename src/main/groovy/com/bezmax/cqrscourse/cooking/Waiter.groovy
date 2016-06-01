package com.bezmax.cqrscourse.cooking


class Waiter {
    CanHandleOrder orderHandler

    Waiter(CanHandleOrder orderHandler) {
        this.orderHandler = orderHandler
    }

    UUID createOrder() {
        def order = new Order()
        order.tableNumber = 23
        orderHandler.handle(order)
        return order.id
    }
}

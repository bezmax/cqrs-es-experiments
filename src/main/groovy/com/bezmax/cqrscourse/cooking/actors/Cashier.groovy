package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.CanHandleOrder
import com.bezmax.cqrscourse.cooking.Order


class Cashier implements CanHandleOrder {
    CanHandleOrder orderHandler

    Cashier(CanHandleOrder orderHandler) {
        this.orderHandler = orderHandler
    }

    void handle(Order o) {
        o.paid = true
        orderHandler.handle(o)
    }
}

package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.messages.Message


class Multiplexer<M extends Message> implements Handles<M> {
    List<Handles<M>> orderHandlers

    void handle(Exchange<M> exchange, M msg) {
        orderHandlers.each {it.handle(exchange, msg)}
    }
}

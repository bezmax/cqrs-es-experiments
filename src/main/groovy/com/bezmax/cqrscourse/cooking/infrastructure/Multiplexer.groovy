package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.messages.MessageBase


class Multiplexer<M extends MessageBase> implements Handles<M> {
    List<Handles<M>> orderHandlers

    void handle(M msg) {
        orderHandlers.each {it.handle(msg)}
    }
}

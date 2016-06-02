package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.CanHandle
import com.bezmax.cqrscourse.cooking.messages.MessageBase


class Multiplexer<M extends MessageBase> implements CanHandle<M> {
    List<CanHandle<M>> orderHandlers

    void handle(M msg) {
        orderHandlers.each {it.handle(msg)}
    }
}

package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.messages.Message
import org.slf4j.LoggerFactory


class RoundRobinDispatcher<M extends Message> implements Handles<M> {
    static LOGGER = LoggerFactory.getLogger(RoundRobinDispatcher)
    Queue<Handles<M>> orderHandlers

    void handle(Exchange<M> exchange, M msg) {
        LOGGER.debug("${exchange}")
        orderHandlers.peek().handle(exchange, msg)
        orderHandlers << orderHandlers.remove()
    }
}

package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.CanHandle
import com.bezmax.cqrscourse.cooking.messages.MessageBase
import org.slf4j.LoggerFactory


class RoundRobinDispatcher<M extends MessageBase> implements CanHandle<M> {
    static LOGGER = LoggerFactory.getLogger(RoundRobinDispatcher)
    Queue<CanHandle<M>> orderHandlers

    void handle(M msg) {
        LOGGER.debug("${msg}")
        orderHandlers.peek().handle(msg)
        orderHandlers << orderHandlers.remove()
    }
}

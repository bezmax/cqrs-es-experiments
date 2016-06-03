package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import org.slf4j.LoggerFactory


class FairQueueDispatcher<M> implements Handles<M> {
    static LOGGER = LoggerFactory.getLogger(FairQueueDispatcher)

    def name = "FairDispatcher"
    def threshold = 5
    List<QueuedDispatcher<M>> orderHandlers

    void handle(Exchange<M> exchange, M msg) {
        LOGGER.debug("${exchange}")

        QueuedDispatcher<M> target
        while (null == (target = orderHandlers.find { it.count < threshold })) {
            Thread.sleep(500)
        }

        target.handle(exchange, msg)
    }

    String toString() {
        name
    }
}
